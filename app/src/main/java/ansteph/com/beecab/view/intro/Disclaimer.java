package ansteph.com.beecab.view.intro;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.helper.SessionManager;
import ansteph.com.beecab.view.callacab.CabCaller;

public class Disclaimer extends AppCompatActivity {


    private Button btnCancel, btnAccept;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disclaimer2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(GlobalRetainer.getAppContext());
        if(!sessionManager.isFirstTimeLaunch())
        {
           goToWelcome();
            finish();
        }

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnAccept = (Button) findViewById(R.id.btn_accept);


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToWelcome();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onBackPressed();
            }
        });
    }


    private void goToWelcome()
    {
        startActivity(new Intent(getApplication(), WelcomePage.class));
        finish();
    }
}
