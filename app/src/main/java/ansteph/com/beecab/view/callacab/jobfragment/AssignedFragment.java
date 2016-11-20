package ansteph.com.beecab.view.callacab.jobfragment;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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
import java.util.concurrent.TimeUnit;

import ansteph.com.beecab.R;
import ansteph.com.beecab.adapter.JobListViewAdapter;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.model.JourneyRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssignedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssignedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TITLE = "param1";
    private static final String PAGE = "param2";

    // TODO: Rename and change types of parameters
    private String title;
    private String page;



    private ListView listViewAssigned;

    GlobalRetainer mGlobalRetainer;
    JobListViewAdapter asAdapter;


    public AssignedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param title Parameter 1.
     * @param page Parameter 2.
     * @return A new instance of fragment AssignedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssignedFragment newInstance(String title, String page) {
        AssignedFragment fragment = new AssignedFragment();
        Bundle args = new Bundle();
        args.putString(TITLE, title);
        args.putString(PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(TITLE);
            page = getArguments().getString(PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_assigned, container, false);
        mGlobalRetainer =(GlobalRetainer) getActivity().getApplicationContext();

        listViewAssigned =(ListView) rootView.findViewById(R.id. listViewAssigned);

        if(mGlobalRetainer.get_grAssignedJobs()!=null)
        {
            asAdapter = new JobListViewAdapter(getActivity(), R.layout.job_listview_item, mGlobalRetainer.get_grAssignedJobs());
            listViewAssigned.setAdapter(asAdapter);
        }


        try {
            retrieveAssignedJobs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        primeTimer();
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

                j.setStatus(job.getInt("jr_status"));
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



    private void primeTimer()
    {
        long countdownmill = 120000;

        new CountDownTimer(countdownmill, 30000){

            @Override
            public void onTick(long millisUntilFinished) {
                //    txtTimer.setText("Time remaining: " + millisUntilFinished/1000);
                try {
                    retrieveAssignedJobs();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                primeTimer();
            }
        }.start();
    }
}
