package ansteph.com.beecab.view.callacab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import ansteph.com.beecab.R;

public class SetRoute2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_route2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new SetRouteFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addToBackStack(SetRouteFragment.TAG);
        fragmentTransaction.replace(R.id.container_body, fragment,SetRouteFragment.TAG);
        fragmentTransaction.commit();

    }

    protected EditText txtFormatAddress;

    public  void testaccess(){

         AddressMapFragment adf =(AddressMapFragment) getSupportFragmentManager().findFragmentByTag(AddressMapFragment.TAG);

        View view = adf.getView();
        if(view!=null){
            txtFormatAddress = (EditText) view.findViewById(R.id.txtFormatAddress);
            txtFormatAddress.setText("yes it got it! Setroute2");
        }

        // AddressMapFragment adf = new AddressMapFragment(); adapater.getRegisteredFragment(viewPager.getCurrentItem())
        //View view = adf.getView();MyclassFragment instanceFragment =
        // (MyclassFragment)getFragmentManager().findFragmentById(R.id.idFragment);
        // if(view!=null)
        //{
        // txtFormatAddress = (EditText) view.findViewById(R.id.txtFormatAddress);
       // txtFormatAddress.setText("yes it got it!");
        // }
    }

    public String getsometext()
    {
        return "you got me";
    }

}
