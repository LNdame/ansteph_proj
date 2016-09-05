package ansteph.com.beecab.view.callacab;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.model.Client;
import ansteph.com.beecab.model.JourneyRequest;
import ansteph.com.beecab.service.FirebaseServerRegistration;
import ansteph.com.beecab.view.profile.EditProfile;
import ansteph.com.beecab.view.profile.ImageListView;

public class CabCaller extends AppCompatActivity {
    SessionManager sessionManager;

    GlobalRetainer mGlobalRetainer;
    public ArrayList<JourneyRequest >pendingJobs ;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_caller);

        sessionManager = new SessionManager(getApplicationContext());
        mGlobalRetainer= (GlobalRetainer) getApplicationContext();

        HashMap<String, String> user = sessionManager.getUserDetails();

        mGlobalRetainer.set_grClient(new Client(user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_NAME),user.get(SessionManager.KEY_EMAIL)
                ,user.get(SessionManager.KEY_MOBILE),user.get(SessionManager.KEY_APIKEY)));


        //Try to register the firebase messaging token
        FirebaseMessaging.getInstance().subscribeToTopic("BeeCab");
       String token= FirebaseInstanceId.getInstance().getToken();

        Toast.makeText(getApplicationContext(), token,Toast.LENGTH_LONG).show();
        if(!token.isEmpty())
        {

            FirebaseServerRegistration fbRegistration = new FirebaseServerRegistration
                    (getApplicationContext(), mGlobalRetainer.get_grClient(),token);

            fbRegistration.registerFBToken();
           // registerFBToken(token);
        }

        pendingJobs = new ArrayList<>();


        Fragment fragment = new CabCallerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addToBackStack(CabCallerFragment.TAG);
        fragmentTransaction.replace(R.id.container_body, fragment);
        fragmentTransaction.commit();



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
            Intent i = new Intent(getApplicationContext(), ImageListView.class);
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
}
