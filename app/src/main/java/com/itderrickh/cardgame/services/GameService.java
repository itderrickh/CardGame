package com.itderrickh.cardgame.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.AuthFailureError;
import com.itderrickh.cardgame.helpers.VolleyCallback;

import org.json.JSONObject;
import java.util.Map;
import java.util.HashMap;

public class GameService {

    private static String SERVICE_URL = "http://webdev.cs.uwosh.edu/students/heined50/CardsBackend/service.php";
    private static GameService me;
    public GameService() {}

    public static GameService getInstance() {
        if(me == null) {
            me = new GameService();
        }

        return me;
    }

    public void service(Context context, final String token, final VolleyCallback callback) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SERVICE_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = super.getHeaders();
                if(params == null || params.isEmpty()) {
                    params = new HashMap<>();
                }

                params.put("Authorize", token);
                return params;
            }
        };

        requestQueue.add(jsObjRequest);
    }
}
