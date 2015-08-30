package com.tokopedia.toped;

import android.content.BroadcastReceiver;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tokopedia.toped.base.MainActivity;
import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.utils.LocationHandler;
import com.tokopedia.toped.utils.PlaceJSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tkpd_Eka on 8/30/2015.
 */
public class PostListingActivity extends MainActivity{
    View mainView;
    TextInputLayout nameLayout;
    TextInputLayout fromLayout;
    TextInputLayout toLayout;
    TextInputLayout timeLayout;
    EditText name;
    AutoCompleteTextView from;
    AutoCompleteTextView to;
    EditText time;

    AutoCompleteTextView selectedTV;

    LocationHandler locationHandler;

    GoogleApiClient mGoogleApiClient;

    String geoLoc;

    ParserTask parserTask;
    PlacesTask placesTask;

    @Override
    protected void inflateMainView(int mainViewId) {

        FrameLayout frame = (FrameLayout)findViewById(mainViewId);
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_post_listing, null, false);
        frame.addView(mainView);
        nameLayout = (TextInputLayout)mainView.findViewById(R.id.name);
        fromLayout = (TextInputLayout)mainView.findViewById(R.id.from);
        toLayout = (TextInputLayout)mainView.findViewById(R.id.to);
        timeLayout = (TextInputLayout)mainView.findViewById(R.id.time);
        name = (EditText)mainView.findViewById(R.id.name_text);
        from = (AutoCompleteTextView)mainView.findViewById(R.id.from_text);
        to = (AutoCompleteTextView)mainView.findViewById(R.id.to_text);
        time = (EditText)mainView.findViewById(R.id.time_text);
        nameLayout.setHint("Listing name");
        timeLayout.setHint("Maximum time");
        fromLayout.setHint("From");
        toLayout.setHint("To");

        locationHandler = new LocationHandler();
        locationHandler.init(this);
        locationHandler.setListener(new LocationHandler.LocationHandlerListener() {
            @Override
            public void onLocationUpdate(double latitude, double longitude) {
                geoLoc = latitude + "," + longitude;
                locationHandler.removeLocationUpdate();
            }
        });
        locationHandler.registerLocationUpdates();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("Working nah " + charSequence);
                placesTask = new PlacesTask();
                placesTask.execute(charSequence.toString());
                selectedTV = from;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        to.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                System.out.println("Working nah " + charSequence);
                placesTask = new PlacesTask();
                placesTask.execute(charSequence.toString());
                selectedTV = to;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                    }
                })
                .build();

        mGoogleApiClient.connect();
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

//    / Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBdtegmHYkktm8NUD3Ns9S7tgJbwWJN7HE";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println("Got mah data " + result);
            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(final List<HashMap<String, String>> result) {

            String[] fromx = new String[] { "description"};
            int[] tox = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            final SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, fromx, tox);

            // Setting the adapter
//            atvPlaces.setAdapter(adapter); TODO
            selectedTV.setAdapter(adapter);
            selectedTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedTV.setText(result.get(i).get("description"));
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.send){
            NetworkClient network = new NetworkClient(this, "http://128.199.227.169:8000/list/new");
            network.setMethod(network.METHOD_POST);
            network.addHeader("Content-Type", "application/x-www-form-urlencoded");
            network.addParam("title", name.getText().toString());
            network.addParam("from", from.getText().toString());
            network.addParam("to", to.getText().toString());
            network.addParam("geo", geoLoc);
            network.addParam("userid", "123123");// TODO
            network.setListener(new NetworkClient.NetworkClientSuccess() {
                @Override
                public void onSuccess(String s) {
                    System.out.println("KIRISAME oke~ " + s);
                    Toast.makeText(getBaseContext(), "Success add listing", Toast.LENGTH_SHORT);
                    finish();
                }
            });
            network.commit();
        }
        else if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
