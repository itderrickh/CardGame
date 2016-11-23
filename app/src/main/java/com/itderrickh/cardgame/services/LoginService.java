package com.itderrickh.cardgame.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itderrickh.cardgame.helpers.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginService {
    private static String LOGIN_URL = "http://webdev.cs.uwosh.edu/students/heined50/CardsBackend/login.php";
    private static LoginService me;
    public LoginService() {}

    public static LoginService getInstance() {
        if(me == null) {
            me = new LoginService();
        }

        return me;
    }

    public void login(Context context, String username, String password, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, LOGIN_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                });

        requestQueue.add(jsObjRequest);
    }
}
