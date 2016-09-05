package ansteph.com.beecab.view.registration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class RegistrationFragment extends Fragment {

    public final static String TAG = RegistrationFragment.class.getSimpleName();
    //Volley RequestQueue
    private RequestQueue requestQueue;
    EditText txtFullName;
    EditText txtEmail;
    EditText txtMobile;
    EditText txtPassword;

    String fullName ="", email="", mobile="",pwd="";

    public RegistrationFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_registration, container, false);

        txtFullName = (EditText) rootView.findViewById(R.id.input_name);
        txtEmail = (EditText) rootView.findViewById(R.id.input_email);
        txtMobile = (EditText) rootView.findViewById(R.id.input_mobile);
        txtPassword = (EditText) rootView.findViewById(R.id.input_password);

        requestQueue = Volley.newRequestQueue(getActivity());
        Button btnCreateAcc = (Button) rootView.findViewById(R.id.btn_signup);
    btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Do registration

                try {
                    registerClient();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

       /* btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SetRoute.class);
                startActivity(i);
            }
        });*/

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Registration");
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    private void setTitle(String title)
    {


        // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle();

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(title);
    }


    //this method will register the user
    private void registerClient() throws JSONException {

        //Displaying a progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Registering", "Please wait... you will soon be in our awesome network",false,false);

        //Getting user data
        fullName = txtFullName.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        mobile = txtMobile.getText().toString().trim();
        pwd = txtPassword.getText().toString().trim();


        //create the string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.REGISTER_URL,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();

                        try{
                            //creating the Json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            String serverMsg = jsonResponse.getString("message");
                            //if it is success
                            if(!error)
                            {
                                //asking user to confirm OTP
                                confirmOtp();
                            }else{ //check for message already existing user
                                Toast.makeText(getActivity(), "Welcome back to our network, we happy to have you back", Toast.LENGTH_LONG).show();

                            }


                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding the parameters to the request
                params.put(Config.KEY_NAME, fullName);
                params.put(Config.KEY_EMAIL, email);
                params.put(Config.KEY_MOBILE, mobile);
                params.put(Config.KEY_PASSWORD, pwd);

                return params;
            }
        };
        //Adding request the queue
        requestQueue.add(stringRequest);
    }


    private void confirmOtp(){
        //switch fragment to await confirmation of the otp
        Fragment fragment = new CheckOTPFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()

                .addToBackStack(CheckOTPFragment.class.getSimpleName());

        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        setTitle("Activate User");
    }

}
