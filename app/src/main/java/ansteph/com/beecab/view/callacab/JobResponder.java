package ansteph.com.beecab.view.callacab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.card.Card;
import ansteph.com.beecab.card.CardProvider;
import ansteph.com.beecab.card.MaterialListView;
import ansteph.com.beecab.card.OnActionClickListener;
import ansteph.com.beecab.card.TextViewAction;
import ansteph.com.beecab.card.listener.OnDismissCallback;
import ansteph.com.beecab.card.listener.RecyclerItemClickListener;
import ansteph.com.beecab.model.JourneyRequest;
import ansteph.com.beecab.model.JourneyRequestResponse;
import ansteph.com.beecab.view.profile.DriverProfileView;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

//add a thtow error if no journey request found

public class JobResponder extends AppCompatActivity {

    JourneyRequest journeyRequest;
GlobalRetainer mGlobalRetainer;
    private Context mContext;
    private MaterialListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_responder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalRetainer = (GlobalRetainer) getApplicationContext();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        journeyRequest = null;


        // Save a reference to the context
        mContext = this;

        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            journeyRequest =(JourneyRequest) b.getSerializable("job");

            mGlobalRetainer.set_currentInspectedJR(journeyRequest);

        }else{
            journeyRequest= mGlobalRetainer.get_currentInspectedJR();
        }




        // Bind the MaterialListView to a variable
        mListView = (MaterialListView) findViewById(R.id.material_listview);
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        final ImageView emptyView = (ImageView) findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(this)
                .load(R.mipmap.ic_launcher)
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        try {
            retrieveJobReponses ();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                // Show a toast
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

// Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
    }



    private void loadCard(JSONArray jobArray){
        List<Card> cards = new ArrayList<>();

        for (int i=0; i<jobArray.length(); i++)
        {
            try {
                JSONObject job = jobArray.getJSONObject(i);

                JourneyRequestResponse jr=  new JourneyRequestResponse(job.getInt("id"), job.getString("TaxiID"),job.getInt("JorID"),job.getString("td_name"),job.getString("co_name"),job.getString("td_email"),job.getString("td_mobile")
                        ,job.getInt("jre_proposed_fare"),job.getInt("jre_counter_offer"),job.getString("car_picture_url"),job.getString("image_tag"));


                cards.add(createCard(jr));

                //jobList.add(j);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        mListView.getAdapter().addAll(cards);
    }


    private Card createCard(final  JourneyRequestResponse jr){
        return new Card.Builder(this)
                .setTag("JOR_BUTTONS_CARD")
                .setDismissible()
                .withProvider(new CardProvider())
                .setLayout(R.layout.journey_response_layout)
                .setTitle(jr.getName())
                .setFare("R "+jr.getCounterOffer())
                .setSubtitle(jr.getCoName())

                .addAction(R.id.left_text_button, new TextViewAction(this)
                        .setText("Assign")
                        .setTextResourceColor(R.color.orange_button).setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                try {
                                    assignRequest(jr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // Intent i = new Intent(mContext, JobDetails.class);
                               // i.putExtra("job", j);
                               // startActivity(i);
                            }
                        }))

                .addAction(R.id.right_text_button, new TextViewAction(this)
                        .setText("View profile")
                        .setTextResourceColor(R.color.black_button).setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                Intent i = new Intent(getApplicationContext(), DriverProfileView.class);
                                i.putExtra("taxiID", jr.getTaxiID());
                                startActivity(i);

                            }
                        }))
                        .setDrawable((jr.getCoName()=="BeeCab")? R.drawable.fleet:R.drawable.taxiicon)
                .endConfig()
                .build();
    }




    public void retrieveJobReponses () throws JSONException {

        // Displaying the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Getting responses","Just retrieving the responses", false, false);

        String url = String.format(Config.REGISTER_JOB_RESPONSE, journeyRequest.getId());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            // String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            if(!error){
                                loading.dismiss();
                                //JSONObject jobsList = jsonResponse.getJSONObject("jobs");
                                JSONArray jobsjsonArray = jsonResponse.getJSONArray("jobs");

                                loadCard(jobsjsonArray);

                            }
                        }catch (JSONException e)
                        {
                            loading.dismiss();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){};
        RequestQueue requestQueue =  Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void gotoCabCaller()
    {
        Intent i = new Intent(getApplicationContext(), CabCaller.class);
        startActivity(i);
    }

    public void assignRequest (final JourneyRequestResponse jrr) throws JSONException{
        final ProgressDialog loading = ProgressDialog.show(this, "Sending Job","Asking the driver to come get you", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.ASSIGN_JOB_URL,
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
                                Toast.makeText(getApplication(), serverMsg, Toast.LENGTH_LONG).show();
                                //if success redirect to cabcaller lander
                                gotoCabCaller();
                            }else{
                               Toast.makeText(getApplicationContext(), serverMsg, Toast.LENGTH_LONG).show();

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
                        Toast.makeText(getApplication(), error.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String>params= new HashMap<>();
                //adding params 'pickupAddr','destAddr', 'pickupCoord', 'destCoord','acceptedFare', 'city', 'jrID','tcID', 'tdID'
                params.put("pickupAddr", journeyRequest.getPickupAddr());
                params.put("destAddr", journeyRequest.getDestinationAddr());
                params.put("pickupCoord", journeyRequest.getPickupCoord());
                params.put("destCoord", journeyRequest.getDestinationCoord());
                params.put("acceptedFare",String.valueOf(jrr.getCounterOffer()) );
                params.put("city", "PE");
                params.put("jrID",String.valueOf(jrr.getJorID()));
                params.put("tcID", mGlobalRetainer.get_grClient().getId());
                params.put("tdID", jrr.getTaxiID());


                // params.put(Config.KEY_JOB_SHARED,mJourneyRequest.isShared()==true?"1":"0");
                return params;
            }
        };
        RequestQueue requestQueue =  Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }



}
