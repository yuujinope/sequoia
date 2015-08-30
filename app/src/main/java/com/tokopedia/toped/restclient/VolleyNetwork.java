package com.tokopedia.toped.restclient;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tkpd_Eka on 8/29/2015.
 */
@SuppressWarnings("unused")
public abstract class VolleyNetwork {
    public class PostStringRequest extends StringRequest {

        Map<String, String> param = new HashMap<>();
        Map<String, String> header = new HashMap<>();

        public PostStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public void setParam(Map<String, String> param) {
            this.param = param;
        }

        public void setHeader(Map<String, String> header) {
            this.header = header;
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return param;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            headers.put("Accept", "application/json");
            return headers;
        }

        @Override
        public String getBodyContentType() {
            return null;
        }
    }

    public static final int DEFAULT_TIMEOUT = 10000;
    public static final int DEFAULT_RETRY_COUNT = 2;

    public static final int METHOD_GET = Request.Method.GET;
    public static final int METHOD_POST = Request.Method.POST;
    public static final int METHOD_PUT = Request.Method.PUT;

    protected Context context;
    protected String url;
    protected Map<String, String> param = new HashMap<>();
    protected Map<String, String> header = new HashMap<>();
    private PostStringRequest request;

    private int retryTimeout = DEFAULT_TIMEOUT;
    private int retryMaxCount = DEFAULT_RETRY_COUNT;
    private int method = METHOD_GET;

    public VolleyNetwork(Context context, String url) {
        this.url = url;
        this.context = context;
    }

    public abstract void onRequestResponse(String response);

    /**
     * Override this class to add event on network retry
     */
    @SuppressWarnings("unused")
    protected void onNetworkRetrying(VolleyError volleyError) throws VolleyError {
    }

    /**
     * Override this class to add event on network stop retrying
     */

    @SuppressWarnings("unused")
    protected void onNetworkRetryingStop(VolleyError volleyNetwork) throws VolleyError {
    }

    public void commit() {
        request = new PostStringRequest(method, url, onRequestListener(), onRequestErrorListener());
        request.setHeader(header);
        request.setParam(param);
        request.setRetryPolicy(getRetryPolicy());
        VolleyNetworkRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public final void killConnection() {
        request.cancel();
    }

    public final void restartRequest() {
        VolleyNetworkRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public final void addParam(String key, String value) {
        param.put(key, value);
    }

    public final void addHeader(String key, String value) {
        header.put(key, value);
    }

    public final void setRetryPolicy(int timeout, int maxCount) {
        retryTimeout = timeout;
        retryMaxCount = maxCount;
    }

    public final void setMethod(int method) {
        this.method = method;
    }

    private Response.Listener<String> onRequestListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onRequestResponse(s);
            }
        };
    }

    protected RetryPolicy getRetryPolicy() {
        return new RetryPolicy() {

            int retry;

            @Override
            public int getCurrentTimeout() {
                return retryTimeout;
            }

            @Override
            public int getCurrentRetryCount() {
                return retry;
            }

            @Override
            public void retry(VolleyError volleyError) throws VolleyError {
                onNetworkRetrying(volleyError);
                retry++;
                if (retry >= retryMaxCount) {
                    onNetworkRetryingStop(volleyError);
                    throw volleyError;
                }
            }
        };
    }

    protected Response.ErrorListener onRequestErrorListener() {

        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) { // TODO Handle error
                volleyError.printStackTrace();
            }
        };
    }

}