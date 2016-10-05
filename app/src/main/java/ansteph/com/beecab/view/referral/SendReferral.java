package ansteph.com.beecab.view.referral;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
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

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.view.callacab.CabCaller;

public class SendReferral extends AppCompatActivity {

    RadioButton radCell, radEmail;
    EditText editEmail, editCellphone;

    TextInputLayout txtInCell, txtInEmail;
    GlobalRetainer mGlobalRetainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_referral);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer =(GlobalRetainer) getApplicationContext();

        radCell = (RadioButton) findViewById(R.id.radioCellphone);
        radEmail = (RadioButton) findViewById(R.id.radioEmail);

        editEmail= (EditText) findViewById(R.id.input_email);
        editCellphone= (EditText) findViewById(R.id.input_cell);

        txtInCell =(TextInputLayout) findViewById(R.id.input_layout_cell);
        txtInEmail=(TextInputLayout) findViewById(R.id.input_layout_email);

        if(radCell.isChecked())
        {
            txtInCell.setVisibility(View.VISIBLE);
            txtInEmail.setVisibility(View.INVISIBLE);

            editCellphone.setVisibility(View.VISIBLE);
            editEmail.setVisibility(View.INVISIBLE);
        }else{
            txtInCell.setVisibility(View.INVISIBLE);
            txtInEmail.setVisibility(View.VISIBLE);

            editCellphone.setVisibility(View.INVISIBLE);
            editEmail.setVisibility(View.VISIBLE);
        }





        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void  onRadBtnClicked (View view)
    {
              //has the button being checked
              boolean checked =((RadioButton) view).isChecked();

              //check which one was fired
              switch (view.getId())
              {
                  case R.id.radioCellphone:
                      if(checked)
                      {
                          txtInCell.setVisibility(View.VISIBLE);
                          txtInEmail.setVisibility(View.INVISIBLE);

                          editCellphone.setVisibility(View.VISIBLE);
                          editEmail.setVisibility(View.INVISIBLE);
                      }
                      break;
                  case R.id.radioEmail:
                      if(checked)
                      {

                          txtInCell.setVisibility(View.INVISIBLE);
                          txtInEmail.setVisibility(View.VISIBLE);

                          editCellphone.setVisibility(View.INVISIBLE);
                          editEmail.setVisibility(View.VISIBLE);
                      }
                      break;
              }

          }



   public void  SendReferral (View view)
   {
       String contact ="";
       if( txtInCell.getVisibility() == View.VISIBLE){
           contact = editCellphone.getText().toString().trim();
       }else{
           contact = editEmail.getText().toString().trim();
       }
        //send the referral to server
       try {
           createReferral(contact);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }


    private boolean isEmailOk(){
        if(!editEmail.getText().toString().isEmpty())
        {
            return Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString().trim()).matches();
        }else{
            return false;
        }
    }

    private boolean isCellphoneOk(){
        if(!editCellphone.getText().toString().isEmpty())
        {
            return editCellphone.getText().toString().trim().length()==10;

        }else{
            return false;
        }
    }



    public void createReferral (final String contact) throws JSONException {
        final ProgressDialog loading = ProgressDialog.show(this, "Telling your friend","Sending the message", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_CLIENT_REFERRAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);

                            String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            //if it is success
                            if(!error)
                            {
                                //Move back to cabcaller
                                //if success redirect to cabcaller lander
                               // Intent i = new Intent(getActivity(), CabCaller.class);
                                //startActivity(i);
                                Toast.makeText(getApplication(), serverMsg, Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplication(), serverMsg, Toast.LENGTH_LONG).show();

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Log.e("ref error",error.getMessage());
                        Toast.makeText(getApplication(), "Something wrong happen",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params= new HashMap<>();
                //adding params
                params.put("id", mGlobalRetainer.get_grClient().getId());
                params.put("contact", contact);

                // params.put(Config.KEY_JOB_SHARED,mJourneyRequest.isShared()==true?"1":"0");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
