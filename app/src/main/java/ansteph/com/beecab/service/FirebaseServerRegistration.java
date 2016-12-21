package ansteph.com.beecab.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.model.Client;
import ansteph.com.beecab.view.callacab.CabCaller;

/**
 * Created by loicStephan on 10/08/16.
 */
public class FirebaseServerRegistration {

    private static final String TAG = FirebaseServerRegistration.class.getSimpleName();

    private Context context;

    private Client client;

    private String token;
    private RequestQueue requestQueue;


    public FirebaseServerRegistration(Context context, Client client, String token) {
        this.context = context;
        this.client = client;
        this.token = token;
    }





    public  void registerFBToken()
    {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);

        if(client!= null)
        {
            requestQueue = Volley.newRequestQueue(context);

            final String mobile = client.getMobile();
            final String id =  client.getId();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_FB,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                                String serverMsg = jsonResponse.getString("message");

                                if(error){
                                //    Toast.makeText(context, serverMsg,Toast.LENGTH_LONG).show();
                                }else{
                                  //  Toast.makeText(getContext(), serverMsg,Toast.LENGTH_LONG).show(); //context as oppose to getcontext maybe.
                                }

                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.getMessage(),Toast.LENGTH_LONG).show();
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






    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



}
