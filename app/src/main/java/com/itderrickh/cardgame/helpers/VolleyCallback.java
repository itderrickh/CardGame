package com.itderrickh.cardgame.helpers;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback{
    void onSuccess(JSONObject string);
    void onError(VolleyError string);
}
