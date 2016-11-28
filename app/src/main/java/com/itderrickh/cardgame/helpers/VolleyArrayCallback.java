package com.itderrickh.cardgame.helpers;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface VolleyArrayCallback{
    void onSuccess(JSONArray result);
    void onError(VolleyError string);
}
