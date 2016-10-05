package ansteph.com.beecab.view.referral;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import ansteph.com.beecab.R;
import ansteph.com.beecab.adapter.ReferralListAdapter;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;

import ansteph.com.beecab.model.Referral;

public class ReferralHistory extends AppCompatActivity {

    ListView mRefferalListView;
    ReferralListAdapter adapter;
    ArrayList<Referral> mRefferalList;
    GlobalRetainer mGlobalRetainer;

    TextView txtScore , txtReferred;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_history);

        mGlobalRetainer =(GlobalRetainer) getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                startActivity(new Intent(getApplication(), SendReferral.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mRefferalListView = (ListView) findViewById(R.id.listView);

        mRefferalList = new ArrayList<>();
      //  String providedContact, String senderID, String dateSent, int status
     //   mRefferalList.add(new Referral("07254342142","tcmaster","20/10/2016 12-00-00",0) );






      //  adapter = new JobListViewAdapter(getActivity(), R.layout.job_listview_item, mGlobalRetainer.get_grPendingJobs());
      //  listViewPending.setAdapter(adapter);

        adapter = new ReferralListAdapter(this,R.layout.referrallistitem,mRefferalList );

        mRefferalListView.setAdapter(adapter);

        try {
            retrievePendingJobs();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtReferred = (TextView) findViewById(R.id.txtReferred);



    }


    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.title_activity_referral_history));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(getString(R.string.title_activity_referral_history));
                    isShow = false;
                }
            }
        });
    }


    public void settheScore(int lenght, int count){

        txtReferred.setText(txtReferred.getText()+" "+String.valueOf(lenght));
        txtScore.setText(txtScore.getText()+" "+String.valueOf(count*100));


    }


    private void UpdateReferralList(JSONArray refArray)
    {

        int statusCount=0;

        for (int i=0; i<refArray.length(); i++)
        {

            try {
                JSONObject ref = refArray.getJSONObject(i);
                Referral referral=  new Referral(ref.getInt("id"), ref.getString("ref_provided_contact"),ref.getString("taxi_client_id"),ref.getString("ref_date_sent"),ref.getInt("ref_status"));

                SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT);

                if(referral.getStatus() == 1)
                {
                    statusCount++;
                }


               /* try{
                    Date mDate = sdf.parse(job.getString("jr_time_created"));
                    j.setTimeCreated(mDate);
                }catch (ParseException e)
                {
                    e.printStackTrace();
                }*/

                mRefferalList.add(referral);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        settheScore(refArray.length(),statusCount);

        adapter.notifyDataSetChanged();
    }

    public void retrievePendingJobs() throws JSONException {

        String url = ""+String.format(Config.RETRIEVE_REFERRAL_URL,mGlobalRetainer.get_grClient().getId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                             String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            if(!error){

                                //JSONObject jobsList = jsonResponse.getJSONObject("jobs");
                                JSONArray refsjsonArray = jsonResponse.getJSONArray("ref");

                                UpdateReferralList(refsjsonArray);

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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

}
