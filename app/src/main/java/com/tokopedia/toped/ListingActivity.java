package com.tokopedia.toped;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.toped.base.MainActivity;
import com.tokopedia.toped.fragment.FragmentListing;
import com.tokopedia.toped.fragment.FragmentOnMyWay;
import com.tokopedia.toped.restclient.NetworkClient;
import com.tokopedia.toped.utils.GCMHandler;
import com.tokopedia.toped.utils.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
public class ListingActivity extends MainActivity{

    private class ListingPagerAdapter extends FragmentPagerAdapter{

        public ListingPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position];
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    View mainView;
    TabLayout tabLayout;
    ViewPager pager;
    String[] CONTENT = {"LISTING", "ON MY WAY"};
    ArrayList<Fragment> fragments;
    ListingPagerAdapter adapter;

    @Override
    protected void inflateMainView(int mainViewId) {
        createView(mainViewId);
        tabLayout = (TabLayout)mainView.findViewById(R.id.tab);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        pager = (ViewPager)mainView.findViewById(R.id.pager);
        fragments = new ArrayList<>();
        fragments.add(new FragmentListing());
        fragments.add(new FragmentOnMyWay());
        adapter = new ListingPagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        loadUserDatas();
    }

    private void createView(int mainViewId){
        FrameLayout frame = (FrameLayout)findViewById(mainViewId);
        mainView = LayoutInflater.from(this).inflate(R.layout.activity_listing, null, false);
        frame.addView(mainView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(getBaseContext(), PostListingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadUserDatas(){
        NetworkClient network = new NetworkClient(this, "http://128.199.227.169:8000/user");
        network.setMethod(network.METHOD_GET);
        network.setListener(new NetworkClient.NetworkClientSuccess() {
            @Override
            public void onSuccess(String s) {
                saveUserDatas(s);
            }
        });
        network.commit();
    }


    private void saveUserDatas(String s){
        try {
            JSONArray users = new JSONArray(s);
            JSONObject user;
            UserData udata = UserData.getInstance(this);
            for(int i = 0; i< users.length() ; i++){
                user = users.getJSONObject(i);
                udata.addData(user.getString("id"), user.getString("user"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
