package com.example.h_sed.test2.manager;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;
import com.example.h_sed.test2.ApiApplication;
import com.example.h_sed.test2.R;
import com.example.h_sed.test2.mapping.Navigation;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by h_sed on 4/9/2016.
 */

public class ApiUtils  {
    static List<Navigation> navigations;
    public static final String UTF8 = "UTF-8";
    private final static RequestQueue myMainRequestQueue = Volley
            .newRequestQueue(ApiApplication.getAppContext());



    public static void getData(final Response.Listener<List<Navigation>> listener, Response.ErrorListener errorListener) {
        String url = ApiApplication.getAppContext().getString(R.string.base_url);
        Request<List<Navigation>> request = new Request<List<Navigation>>(
                Request.Method.GET, url, errorListener) {

            @Override
            protected Response parseNetworkResponse(NetworkResponse networkResponse) {

                try {
                    String jsonString = new String(networkResponse.data, UTF8);
                    JSONArray jsonArray = new JSONArray(jsonString);

                    //Parse response in a different class
                    //When we have multiple API calls we send them to this class to parse
                    navigations = APIParser.parseNavigation(jsonArray);
                    //Data fetching is done, lets back to the UI
                    //Data binding principals in progress
                    return Response.success(navigations,
                            HttpHeaderParser.parseCacheHeaders(networkResponse));
                } catch (JSONException e) {
                    Log.e(ApiUtils.class
                            .getSimpleName(), "Failed communication with API.  [" + e + "]");
                } catch (UnsupportedEncodingException e) {
                    Log.e(ApiUtils.class.getSimpleName(), "A valid json string" +
                            "was not received from the server [" + e + "]");

                }
                //An error occured
                listener.onResponse(null);
                return null;
            }

            @Override
            protected void deliverResponse(List<Navigation> response) {
                listener.onResponse(navigations);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        //This queue will manage future API calls when we have multiple calls
        myMainRequestQueue.add(request);
    }
}