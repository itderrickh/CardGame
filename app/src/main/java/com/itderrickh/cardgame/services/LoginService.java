package com.itderrickh.cardgame.services;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.itderrickh.cardgame.helpers.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    public void login(Context context, String username, String password, boolean isRegister, final VolleyCallback callback) {
        JSONObject sendVars = new JSONObject();

        try {
            sendVars.put("email", username);
            sendVars.put("password", password);
            sendVars.put("isRegister", isRegister);
        } catch (Exception ex) {

        }

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, LOGIN_URL, sendVars, new Response.Listener<JSONObject>() {
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
