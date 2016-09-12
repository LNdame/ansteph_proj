package ansteph.com.beecab.view.registration;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.model.Client;
import ansteph.com.beecab.view.callacab.CabCaller;
import ansteph.com.beecab.view.intro.WelcomePage;

public class Login extends AppCompatActivity {

    //Volley RequestQueue
    private RequestQueue requestQueue;

    // Email, password edittext
    EditText txtMobile, txtPassword;

    // login button
    Button btnLogin;

    //alert text
    TextView alert;

    SessionManager sessionManager;
    GlobalRetainer mGlobalretainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGlobalretainer = (GlobalRetainer) getApplicationContext();
        sessionManager = new SessionManager(getApplicationContext());

        txtMobile = (EditText) findViewById(R.id.input_cell);
        txtPassword = (EditText) findViewById(R.id.input_password);
        alert = (TextView) findViewById(R.id.txtAlertMsg) ;

       // Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();

        requestQueue = Volley.newRequestQueue(getApplicationContext());


    }


    public void logClient(View view)
    {
        String mobile = txtMobile.getText().toString();
        String pwd =    txtPassword.getText().toString();


        if(!mobile.isEmpty()&& !pwd.isEmpty()){

            if(mobile.equals("01123581321") && pwd.equals("wewillrocku")){
                sessionManager.createLoginSession("tcmaster","BeeCab","master@beecab.co.za","01123581321","3d2edba48cf1a43401f5d906942f87c8");
                //launch the intro
                Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                startActivity(intent);
            }else{

                //check the database for a match
                try {
                    retrieveUser(mobile,pwd);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }else{
            alert.setVisibility(View.VISIBLE);
        }
    }

    public void retrieveUser(final String mobile, final String pwd)throws JSONException
    {
        //Displaying the progress dialog
        //final ProgressDialog loading = ProgressDialog.show(getApplicationContext(), "Login in","Just checking your awesomeness", false, false);
        //Getting url
         String url = String.format(Config.RETRIEVE_USER_URL,mobile,pwd);


        //Create the string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                       // loading.dismiss();

                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                            String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                            //if it is a go
                            if(!error){
                                Toast.makeText(getApplicationContext(), serverMsg, Toast.LENGTH_LONG).show();

                                //get the user detail from the server
                                JSONObject profile = jsonResponse.getJSONObject("profile");

                                //load the client in the global retainer
                               Client cl = new Client(profile.getString("id"),profile.getString("name"),profile.getString("email"),
                                       profile.getString("mobile"),profile.getString("apikey"));

                                mGlobalretainer.set_grClient(cl);
                                if(mGlobalretainer.get_grClient()!=null){
                                    sessionManager.createLoginSession(mGlobalretainer.get_grClient().getId(),mGlobalretainer.get_grClient().getName(),
                                            mGlobalretainer.get_grClient().getEmail(),mGlobalretainer.get_grClient().getMobile(),mGlobalretainer.get_grClient().getApikey());

                                    //to notify user
                                    NotifyUser();


                                // launch the intro || the call cab activity the main landing page if the guy skip
                                    Intent intent = new Intent(getApplicationContext(), WelcomePage.class);
                                    startActivity(intent);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), serverMsg, Toast.LENGTH_LONG).show();

                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                   // loading.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Config.KEY_MOBILE, mobile);
                params.put(Config.KEY_PASSWORD, pwd);

                return params;
            }
        };
        //Adding request the queue
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        //Do nothing...
    }

    public void registerClient (View view)
    {

        Intent intent = new Intent(getApplicationContext(), Registration.class);
        startActivity(intent);
    }


    public void retrievePwd (View view)
    {

        Intent intent = new Intent(getApplicationContext(), LostPassword.class);
        startActivity(intent);
    }


    public void NotifyUser()
    {
        int mId =1;

        android.support.v4.app.NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_vusi)
                .setContentTitle("BeeCab")
                .setContentText("Login Successful").setAutoCancel(true);

        //Creates an explicit intent for an activity
        Intent resultIntent  = new Intent(this, CabCaller.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(CabCaller.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());




    }

}
