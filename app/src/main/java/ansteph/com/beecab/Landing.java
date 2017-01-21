package ansteph.com.beecab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.model.Client;

public class Landing extends AppCompatActivity {

    SessionManager sessionManager;
    GlobalRetainer mGlobalRetainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mGlobalRetainer=(GlobalRetainer)getApplicationContext();

        sessionManager = new SessionManager(getApplicationContext());
  //  Toast.makeText(getApplicationContext(), "firstlaunch: " + sessionManager.isFirstTimeLaunch(), Toast.LENGTH_LONG).show();

        if (sessionManager.isLoggedIn()){

            //if logged in load the global retainer for clein
            HashMap<String, String> user = sessionManager.getUserDetails();
            mGlobalRetainer.set_grClient(new Client(user.get(SessionManager.KEY_ID),user.get(SessionManager.KEY_NAME),
                    user.get(SessionManager.KEY_EMAIL) ,user.get(SessionManager.KEY_MOBILE),user.get(SessionManager.KEY_APIKEY)));


        }

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(4000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    /**
                     * Call this function whenever you want to check user login
                     * This will redirect user to Login is he is not
                     * logged in
                     * */
                    sessionManager.checkLogin();
                    //Intent intent = new Intent(getApplicationContext(), CabCaller.class);
                    //startActivity(intent);
                }
            }
        };
        timerThread.start();


    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
