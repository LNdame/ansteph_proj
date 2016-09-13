package ansteph.com.beecab.view.registration;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckOTPFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckOTPFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = CheckOTPFragment.class.getSimpleName();
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //inputOtp
    EditText txtOTP;
    //Volley RequestQueue
    private RequestQueue requestQueue;
    String otp="";

    public CheckOTPFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckOTPFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckOTPFragment newInstance(String param1, String param2) {
        CheckOTPFragment fragment = new CheckOTPFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView= inflater.inflate(R.layout.fragment_check_otp, container, false);
        requestQueue = Volley.newRequestQueue(getActivity());

        txtOTP = (EditText) rootView.findViewById(R.id.inputOtp);

        Button btnSubmit = (Button) rootView.findViewById(R.id.btn_verify_otp);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (  txtOTP.getText().toString().trim().length()!=0 ){
                    try {
                        confirmOTP();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(), "The code is still empty",Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Activate User");
    }

    public void gotoLogin()
    {
       /* Fragment fragment = new RegSuccessFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()

                .addToBackStack(RegSuccessFragment.TAG);

        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();
        setTitle("Edit profile");*/
        Intent i = new Intent (getActivity(),Login.class);
        startActivity(i);
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

    private void confirmOTP() throws JSONException{
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Registering","Please wait... you will soon be in our awesome network",false,false);
        otp = txtOTP.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.URL_VERIFY_OTP,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        try{
                            //creating the Json object from the response
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            String serverMsg = jsonResponse.getString("message");

                            if(!error)
                            {
                                //go to back to login page
                                gotoLogin();
                            }else{
                                Toast.makeText(getActivity(), "Wrong code entered, please try again",Toast.LENGTH_LONG).show();
                                txtOTP.setText("");
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_OTP,otp);

                return params;
            }
        };

        //Adding request the queue
        requestQueue.add(stringRequest);
    }
}
