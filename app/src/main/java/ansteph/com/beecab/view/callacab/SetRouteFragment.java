package ansteph.com.beecab.view.callacab;

import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.model.JourneyRequest;
import ansteph.com.beecab.service.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetRouteFragment extends Fragment {


    //Volley RequestQueue
    private RequestQueue requestQueue;

    public SetRouteFragment() {
    }

    EditText edtDestination, edtPickUp , edtFare, edtTime;
    TimePicker picker;
    public static  String TAG = SetRouteFragment.class.getSimpleName();

    private JourneyRequest mJourneyRequest;

    GlobalRetainer mGlobalRetainer;
    public static SetRouteFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SetRouteFragment fragment = new SetRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_set_route, container, false);

        mJourneyRequest = new JourneyRequest();
        edtDestination = (EditText) rootView.findViewById(R.id.edtDestination) ;
        edtPickUp = (EditText) rootView.findViewById(R.id.edtPickUp) ;

        edtFare = (EditText) rootView.findViewById(R.id.edtFare) ;

        mGlobalRetainer = (GlobalRetainer)getActivity().getApplicationContext();


        //set the time of the pisker to now
        Calendar c = Calendar.getInstance();
        c.setTime(c.getTime());

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        edtTime = (EditText) rootView.findViewById(R.id.edtTime);

        edtTime.setText(String.valueOf(hour) + " : "+String.valueOf(minute));

        //picker = (TimePicker) rootView.findViewById(R.id.timePicker);
        //picker.setIs24HourView(Boolean.TRUE);
        //picker.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        //picker.setCurrentMinute(c.get(Calendar.MINUTE));


        ImageButton btnDestShowMap = (ImageButton) rootView.findViewById(R.id.imgbtnDest);
        btnDestShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt(Constants.ADDRESS, Constants.DESTINATION_GRAB);

                Fragment fragment = new AddressMapFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .addToBackStack(AddressMapFragment.TAG);
                fragmentTransaction.replace(R.id.container_body, fragment,AddressMapFragment.TAG);
                fragmentTransaction.commit();
            }
        });


        ImageButton btnPickUpShowMap = (ImageButton) rootView.findViewById(R.id.imgbtnPick);
        btnPickUpShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle args = new Bundle();
                args.putInt(Constants.ADDRESS, Constants.PICKUP_GRAB);

                Fragment fragment = new AddressMapFragment();
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .addToBackStack(AddressMapFragment.TAG);
                fragmentTransaction.replace(R.id.container_body, fragment,AddressMapFragment.TAG);
                fragmentTransaction.commit();
            }
        });

        ImageButton btntimepick = (ImageButton) rootView.findViewById(R.id.imgbtntime);
        btntimepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment nf = new TimePickerFragment();
                nf.show(getActivity().getSupportFragmentManager(),"TimePicker");
            }
        });


        Button btnHailSubmit = (Button) rootView.findViewById(R.id.btnHailSubmit);
        btnHailSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(isFormComplete())
               {
                   mJourneyRequest.setPickupAddr(edtPickUp.getText().toString().trim());
                   mJourneyRequest.setDestinationAddr(edtDestination.getText().toString().trim());

                   if(edtFare.getText().toString().trim().isEmpty())
                   {
                       mJourneyRequest.setProposedFare("00.00");
                   }else{
                       mJourneyRequest.setProposedFare(edtFare.getText().toString().trim());
                   }



                   mJourneyRequest.setPickupTime(edtTime.getText().toString().trim());
                   mJourneyRequest.setShared(false);
                   mJourneyRequest.setCallAllowed(false);
                   mJourneyRequest.setClientID(mGlobalRetainer.get_grClient().getId());
                   mGlobalRetainer.addPendingJob(mJourneyRequest);


                   //send request to server
                   try {
                       CreateRequest ();
                   } catch (JSONException e) {
                       e.printStackTrace();
                   }



               }else{
                   Toast.makeText(getActivity(), "Oops! we couldn't read one of the addresses, please check", Toast.LENGTH_LONG).show();
               }


            }
        });

        requestQueue = Volley.newRequestQueue(getActivity());

        return rootView;
    }


    public boolean isFormComplete(){

        if(edtDestination.getText().toString().trim().isEmpty()){return false;}
        if(edtPickUp.getText().toString().trim().isEmpty()){return false;}
        return true;
    }


    public void CreateRequest () throws JSONException{
      final ProgressDialog loading = ProgressDialog.show(getActivity(), "Sending Job","whistling at cabs", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.CREATE_JOB_URL,
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
                                Intent i = new Intent(getActivity(), CabCaller.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(getActivity(), serverMsg, Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getActivity(), "Something wrong happen",Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String>params= new HashMap<>();
                //adding params
                params.put("pickupAddr", mJourneyRequest.getPickupAddr());
                params.put("destAddr", mJourneyRequest.getDestinationAddr());
                params.put("pickupTime", mJourneyRequest.getPickupTime());
                params.put("proposedFare", mJourneyRequest.getProposedFare());
                params.put("callAllowed",mJourneyRequest.isCallAllowed()== true? "1":"0");
                params.put("pickupCoord", mJourneyRequest.getPickupCoord());
                params.put("destCoord", mJourneyRequest.getDestinationCoord());
                params.put("city", mJourneyRequest.getCity());
                params.put("tcID", mJourneyRequest.getClientID());
                params.put(Config.KEY_JOB_SHARED,mJourneyRequest.isShared()==true?"1":"0");
               // params.put(Config.KEY_JOB_SHARED,mJourneyRequest.isShared()==true?"1":"0");
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        String des =( (SetRoute)getActivity()).getDestinationLocation();
        Location desLoc = ( (SetRoute)getActivity()).getDestLocationCoor();
       // Toast.makeText(getActivity(),( (SetRoute)getActivity()).getCity(), Toast.LENGTH_LONG).show();

        if(des!=null && !des.isEmpty()){
            edtDestination.setText(des);
            mJourneyRequest.setDestinationAddr(des);
            mJourneyRequest.setDestinationCoord(desLoc.getLatitude()+", "+desLoc.getLongitude());

        }


        String pickup = ( (SetRoute)getActivity()).getPickupLocation();
        Location pickupLoc =( (SetRoute)getActivity()).getPickupLocationCoor();

        if (pickup!=null && !pickup.isEmpty())
        {
            edtPickUp.setText(pickup);
            mJourneyRequest.setPickupAddr(pickup);
            mJourneyRequest.setPickupCoord(pickupLoc.getLatitude()+", "+pickupLoc.getLongitude());

            mJourneyRequest.setCity(((SetRoute)getActivity()).getCity());
        }

    }


    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(getActivity(),"I just resumed", Toast.LENGTH_SHORT).show();
    }
}
