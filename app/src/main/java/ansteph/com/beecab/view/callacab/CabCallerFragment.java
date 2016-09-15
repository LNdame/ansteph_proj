package ansteph.com.beecab.view.callacab;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ansteph.com.beecab.R;
import ansteph.com.beecab.adapter.JobListViewAdapter;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.model.JourneyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CabCallerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CabCallerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = CabCallerFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    JobListViewAdapter adapter;
    JobListViewAdapter asAdapter;
    private ListView listViewPending, listViewAssigned;
    GlobalRetainer mGlobalRetainer;

    LinearLayout llAssigned, llPending;
    public CabCallerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CabCallerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CabCallerFragment newInstance(String param1, String param2) {
        CabCallerFragment fragment = new CabCallerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_cab_caller, container, false);

        mGlobalRetainer =(GlobalRetainer) getActivity().getApplicationContext();

        Button btnCaller = (Button) rootView.findViewById(R.id.btnCaller);
        btnCaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SetRoute.class);
                startActivity(i);
            }
        });

        llAssigned = (LinearLayout) rootView.findViewById(R.id.llAssignedJob);
        llPending = (LinearLayout) rootView.findViewById(R.id.llPendingJob);

        final ViewAnimator viewAnimator = (ViewAnimator) rootView.findViewById(R.id.viewAnimator);

        Button btnAssigned = (Button) rootView.findViewById(R.id.btnAssigned);
        Button btnPending = (Button) rootView.findViewById(R.id.btnPending);

        final Animation inAmin = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_in_left);
        final Animation outAmin = AnimationUtils.loadAnimation(getActivity(),android.R.anim.slide_out_right);

        viewAnimator.setInAnimation(inAmin);
        viewAnimator.setOutAnimation(outAmin);

        viewAnimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAnimator.showNext();
            }
        });



        btnAssigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               llAssigned.setVisibility(View.VISIBLE);
                llPending.setVisibility(View.GONE);
            }
        });


        btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llAssigned.setVisibility(View.GONE);
                llPending.setVisibility(View.VISIBLE);
            }
        });


        listViewPending =(ListView) rootView.findViewById(R.id. listViewPending);
        listViewAssigned =(ListView) rootView.findViewById(R.id. listViewAssigned);


        if(mGlobalRetainer.get_grPendingJobs()!=null ){

        adapter = new JobListViewAdapter(getActivity(), R.layout.job_listview_item, mGlobalRetainer.get_grPendingJobs());
        listViewPending.setAdapter(adapter);
        }

        if(mGlobalRetainer.get_grAssignedJobs()!=null)
        {
            asAdapter = new JobListViewAdapter(getActivity(), R.layout.job_listview_item, mGlobalRetainer.get_grAssignedJobs());
            listViewAssigned.setAdapter(asAdapter);
        }




        try {
            retrievePendingJobs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            retrieveAssignedJobs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;

    }

    private void UpdatePendingJobList(JSONArray jobArray)
    {
        mGlobalRetainer.get_grPendingJobs().clear();

        for (int i=0; i<jobArray.length(); i++)
        {
            try {
                JSONObject job = jobArray.getJSONObject(i);
             JourneyRequest j=  new JourneyRequest(job.getInt("id"), job.getString("jr_pickup_add"),job.getString("jr_destination_add"),job.getString("jr_pickup_time"),String.valueOf(job.getInt("jr_proposed_fare"))
                        ,job.getString("jr_pickup_coord"),job.getString("jr_destination_coord"),job.getString("jr_tc_id"));

                SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT);

                try{
                    Date mDate = sdf.parse(job.getString("jr_time_created"));
                    j.setTimeCreated(mDate);
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }

                mGlobalRetainer.get_grPendingJobs().add(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        adapter.notifyDataSetChanged();
    }


    private void UpdateAssignJobList(JSONArray jobArray)
    {
        mGlobalRetainer.get_grAssignedJobs().clear();
        for (int i=0; i<jobArray.length(); i++)
        {
            try {
                JSONObject job = jobArray.getJSONObject(i);
                JourneyRequest j=  new JourneyRequest(job.getInt("id"), job.getString("jr_pickup_add"),job.getString("jr_destination_add"),job.getString("jr_pickup_time"),String.valueOf(job.getInt("jr_proposed_fare"))
                        ,job.getString("jr_pickup_coord"),job.getString("jr_destination_coord"),job.getString("jr_tc_id"));

                SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT);

                try{
                    Date mDate = sdf.parse(job.getString("jr_time_created"));
                    j.setTimeCreated(mDate);
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }

                mGlobalRetainer.addAssignedJob(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        asAdapter.notifyDataSetChanged();
    }


    public void retrievePendingJobs() throws JSONException {

        String url = ""+String.format(Config.RETRIEVE_PENDING_JOB_URL,mGlobalRetainer.get_grClient().getId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            // String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            if(!error){

                                //JSONObject jobsList = jsonResponse.getJSONObject("jobs");
                                JSONArray jobsjsonArray = jsonResponse.getJSONArray("jobs");

                                UpdatePendingJobList(jobsjsonArray);

                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){};
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


    public void retrieveAssignedJobs() throws JSONException {

        String url = ""+String.format(Config.RETRIEVE_ASS_JOB_URL,mGlobalRetainer.get_grClient().getId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            // String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            if(!error){

                                //JSONObject jobsList = jsonResponse.getJSONObject("jobs");
                                JSONArray jobsjsonArray = jsonResponse.getJSONArray("jobs");

                                UpdateAssignJobList(jobsjsonArray);

                            }
                        }catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){};
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }


}
