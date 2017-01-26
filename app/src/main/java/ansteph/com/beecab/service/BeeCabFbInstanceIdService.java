package ansteph.com.beecab.service;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;

/**
 * Created by loicStephan on 04/08/16.
 */
public class BeeCabFbInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG =BeeCabFbInstanceIdService.class.getSimpleName();
            GlobalRetainer mGlobalRetainer;
    private RequestQueue requestQueue;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token= FirebaseInstanceId.getInstance().getToken();
       // Log.d("token", token);
        // Saving reg id to shared preferences
        storeRegIdInPref(token);

        registerFBToken( token);

        // sending reg id to your server
        sendRegistrationToServer(token);


        //Notify CabCaller that the token registration is complete
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
      //  Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void registerFBToken(final String token)
    {
        mGlobalRetainer = (GlobalRetainer) getApplicationContext();

        if(mGlobalRetainer.get_grClient()!= null)
        {
            requestQueue = Volley.newRequestQueue(getApplicationContext());

            final String mobile = mGlobalRetainer.get_grClient().getMobile();
            final String id =  mGlobalRetainer.get_grClient().getId();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_FB,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                                String serverMsg = jsonResponse.getString("message");

                                if(error){
                                    Toast.makeText(getApplicationContext(), serverMsg,Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getApplicationContext(), serverMsg,Toast.LENGTH_LONG).show();
                                }

                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }


            ){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("token",token);
                    params.put("mobile",mobile);
                    params.put("id",id);
                    params.put("flag","0");
                    return params;
                }
            };



            requestQueue.add(stringRequest);


        }



    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.putString("regoldId", "");
        editor.commit();
    }
}
