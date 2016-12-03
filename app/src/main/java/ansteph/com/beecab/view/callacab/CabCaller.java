package ansteph.com.beecab.view.callacab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.model.Client;
import ansteph.com.beecab.model.JourneyRequest;
import ansteph.com.beecab.service.FirebaseServerRegistration;
import ansteph.com.beecab.util.NotificationUtils;
import ansteph.com.beecab.view.callacab.jobfragment.AssignedFragment;
import ansteph.com.beecab.view.callacab.jobfragment.PendingFragment;
import ansteph.com.beecab.view.profile.EditProfile;

public class CabCaller extends AppCompatActivity {

    private static final String TAG = CabCaller.class.getSimpleName();

    SessionManager sessionManager;

    GlobalRetainer mGlobalRetainer;
    public ArrayList<JourneyRequest> pendingJobs ;

    private RequestQueue requestQueue;

    FragmentPagerAdapter adapterViewPager;
    ViewPager viewPager;
    TabLayout tabLayout;
    Button btnCaller;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static boolean isInFront=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_caller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(getApplicationContext());
        mGlobalRetainer= (GlobalRetainer) getApplicationContext();

        HashMap<String, String> user = sessionManager.getUserDetails();

        mGlobalRetainer.set_grClient(new Client(user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_NAME),user.get(SessionManager.KEY_EMAIL)
                ,user.get(SessionManager.KEY_MOBILE),user.get(SessionManager.KEY_APIKEY)));


        /*/Try to register the firebase messaging token
        FirebaseMessaging.getInstance().subscribeToTopic("BeeCab");
        String token= FirebaseInstanceId.getInstance().getToken();

      //  Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
        if(!token.isEmpty())
        {

            FirebaseServerRegistration fbRegistration = new FirebaseServerRegistration
                    (getApplicationContext(), mGlobalRetainer.get_grClient(),token);

            fbRegistration.registerFBToken();
            // registerFBToken(token);
        }*/

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //Checking for the type of intent filter
                if(intent.getAction().equals(Config.REGISTRATION_COMPLETE))
                {
                    //gcm successful reg
                    //now subscribe to the topic beecab topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                }else if (intent.getAction().equals(Config.PUSH_NOTIFICATION))
                {
                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }

            }
        };

        displayFirebaseRegId();
        updateRegIDonServer();



        pendingJobs = new ArrayList<>();


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        adapterViewPager = new JobCatAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        btnCaller = (Button) findViewById(R.id.btnCaller);
        btnCaller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SetRoute.class);
                startActivity(i);
            }
        });

    }


    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (TextUtils.isEmpty(regId))
            Log.e(TAG, "Firebase reg id: Firebase Reg Id is not received yet! " );

    }

    private void updateRegIDonServer()
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        String regoldId = pref.getString("regoldId", null);

        if(!regId.equals(regoldId))
        {
            FirebaseServerRegistration fbRegistration = new FirebaseServerRegistration
                    (getApplicationContext(), mGlobalRetainer.get_grClient(),regId);

            fbRegistration.registerFBToken();



            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regoldId", regId);
            editor.commit();
        }

    }
    @Override
    public void onBackPressed() {
        //Do nothing...
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cabcaller, menu);

        MenuItem loggedUser = menu.findItem(R.id.action_loggedUser);
        if(mGlobalRetainer.get_grClient().getName()!=null)
        {
            loggedUser.setTitle(mGlobalRetainer.get_grClient().getName());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //  return true;
            Intent i = new Intent(getApplicationContext(), ActionList.class);
            startActivity(i);
        }


        if (id == R.id.action_logout) {
            sessionManager.logoutUser();
        }
        if (id == R.id.action_profile) {
            Intent i = new Intent(getApplicationContext(), EditProfile.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<JourneyRequest> getPendingJobs() {
        return pendingJobs;
    }

    public void setPendingJobs(ArrayList<JourneyRequest> pendingJobs) {
        this.pendingJobs = pendingJobs;
    }

    public void addPendingJob(JourneyRequest jr)
    {
        this.pendingJobs.add(jr);
    }



    public static class JobCatAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        private String tabTitles[] = new String[]{"Pending Job", "Assigned Job"};

        public JobCatAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0: return PendingFragment.newInstance("Page # 1","op");
                case 1: return AssignedFragment.newInstance("Page # 2","op");
                default: return null;
            }
        }


        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position] ;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setInFront(true);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(GlobalRetainer.getAppContext()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(GlobalRetainer.getAppContext()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(GlobalRetainer.getAppContext());
    }


    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(GlobalRetainer.getAppContext()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        setInFront(false);
    }

    public boolean isInFront() {
        return isInFront;
    }

    public void setInFront(boolean inFront) {
        isInFront = inFront;
    }
}
