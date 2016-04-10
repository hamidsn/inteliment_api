package com.example.h_sed.test2.manager;

import com.example.h_sed.test2.mapping.Navigation;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h_sed on 4/9/2016.
 */
public class APIParser {
    public static List<Navigation> parseNavigation(JSONArray jsonArray) throws JSONException {

        List<Navigation> navigations = new ArrayList<Navigation>();
        for (int i = 0; i < jsonArray.length(); i++) {
            navigations.add(new Gson()
                    .fromJson(jsonArray.getJSONObject(i).toString(), Navigation.class));
        }
        return navigations;
    }
}