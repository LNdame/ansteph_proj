package ansteph.com.beecab.view.registration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;

public class LostPassword extends AppCompatActivity {


    EditText edtCellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtCellphone = (EditText) findViewById(R.id.input_cell);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    public void sendTempPwd (View view)
    {
        String cellphone = edtCellphone.getText().toString().trim();

        try {
            updatePassword(cellphone);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void updatePassword(String cellphone) throws JSONException {

        final ProgressDialog loading = ProgressDialog.show(this, "Sending","SMS on his way", false, false);

        String url = ""+String.format(Config.LOST_PWD_URL,cellphone);

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            if(!error){

                                Toast.makeText(getApplicationContext(), serverMsg, Toast.LENGTH_SHORT).show();

                                //go back to landing
                                gotoLogin();
                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
            }
        }){};
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



    public void gotoLogin()
    {
        onBackPressed();
    }

}
