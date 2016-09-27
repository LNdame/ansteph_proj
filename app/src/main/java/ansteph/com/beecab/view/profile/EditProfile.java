package ansteph.com.beecab.view.profile;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ansteph.com.beecab.R;
import ansteph.com.beecab.view.callacab.CabCallerFragment;

public class EditProfile extends AppCompatActivity implements  EditUsernameFragment.OnFragmentInteractionListener,
        ViewProfileFragment.OnFragmentInteractionListener{

    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        Fragment fragment = new ViewProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addToBackStack(ViewProfileFragment.TAG);
        fragmentTransaction.replace(R.id.container_body, fragment,ViewProfileFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(String text) {
        setUsername(text);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
