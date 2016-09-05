package ansteph.com.beecab.view.callacab;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import ansteph.com.beecab.R;
import ansteph.com.beecab.adapter.FixedViewPager;
import ansteph.com.beecab.service.Constants;
import ansteph.com.beecab.service.FetchAddressIntentService;

public class SetRoute extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, AddressMapFragment.OnFragmentInteractionListener {

    public static FragmentManager fragmentManager;
    protected static String TAG = SetRoute.class.getSimpleName();




    private CabCallerPagerAdapter mAdapter;
    //private ViewPager mPager;
    public FixedViewPager mPager;




    /*******Geocoder********/
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";

    public GoogleApiClient mGoogleApiClient;

    protected Location mLastLocation; // this the geographical location

    protected boolean mAddressRequested;

    protected String mAddressOutput;// the formatted location address
    protected String mCity;

    private AddressResultReceiver mResultReceiver; //Receiver registered with this activity to get the response from FetchAddressIntentService

    protected EditText txtFormatAddress;
    protected EditText txtEdtDestination;

    public String DestinationLocation ="";
    public String PickupLocation="";

    public Location DestLocationCoor = new Location("");
    public Location PickupLocationCoor = new Location("");


    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_route);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager= getSupportFragmentManager();

        Fragment fragment = new SetRouteFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                .addToBackStack(SetRouteFragment.TAG);
        fragmentTransaction.replace(R.id.container_body, fragment,SetRouteFragment.TAG);
        fragmentTransaction.commit();


       // txtFormatAddress = (EditText) getv
        //Geocoder
        mResultReceiver = new AddressResultReceiver(new Handler());
        mAddressRequested = false;
        mAddressOutput = "";

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //updateValuesFromBundle(savedInstanceState);
        buildGoogleApiClient();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(String selectedAddress,Location location, int flag) {
       // Toast.makeText(this,"I just launched", Toast.LENGTH_SHORT).show();
        //handle the location
        if(flag== Constants.DESTINATION_GRAB){
            setDestinationLocation(selectedAddress);
            setDestLocationCoor(location);
        }


        if(flag== Constants.PICKUP_GRAB){
            setPickupLocation( selectedAddress);
            setPickupLocationCoor(location);
        }

        /*SetRouteFragment srf = (SetRouteFragment) getSupportFragmentManager().findFragmentByTag(SetRouteFragment.TAG);

        View view = srf.getView();
        if(view!=null){
            txtEdtDestination =(EditText) view.findViewById(R.id.edtDestination);
            txtEdtDestination.setText(selectedAddress);
        }*/

        //srf.updateAdrressField(selectedAddress);
    }




    /** A simple FragmentPagerAdapter that returns two TextFragment and a SupportMapFragment. to be deleted gone back to traditional fragment*/
    public static class CabCallerPagerAdapter extends FragmentPagerAdapter {

        public CabCallerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;}
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return SetRouteFragment.newInstance();
                case 1:

                    return new AddressMapFragment();
                default:
                    return null;
            }
        }
    }



    /**************************************GeoCoding related************************************/


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build() ;

    }


    public void startIntentService(Location location )
    {
        Toast.makeText(this,"Started",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(Constants.RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.

        startService(intent);

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

    }


    protected void handleAddressOutput(int resultCode){
        Toast.makeText(this, "Looking",Toast.LENGTH_SHORT).show();
        if (resultCode== Constants.SUCCESS_RESULT)
        {
            Toast.makeText(this, "Found",Toast.LENGTH_SHORT).show();
          AddressMapFragment adf = new AddressMapFragment();
            View view = adf.getView();
            if(view!=null)
            {
                txtFormatAddress = (EditText) view.findViewById(R.id.txtFormatAddress);
                txtFormatAddress.setText(mAddressOutput);
            }
           // txtFormatAddress.setText(mAddressOutput); should display it the fragment with pager
        }else{
            Toast.makeText(this, "Not Found",Toast.LENGTH_SHORT).show();
            AddressMapFragment adf = new AddressMapFragment();
            View view = adf.getView();
            if(view!=null)
            {
                txtFormatAddress = (EditText) view.findViewById(R.id.txtFormatAddress);
                txtFormatAddress.setText(mAddressOutput);
            }
        }
    }



    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver (Handler handler) {super(handler);}
        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            handleAddressOutput(resultCode);

            mAddressRequested = false;
        }
    }


    public String getDestinationLocation() {
        return DestinationLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        DestinationLocation = destinationLocation;
    }

    public String getPickupLocation() {
        return PickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        PickupLocation = pickupLocation;
    }


    public Location getDestLocationCoor() {
        return DestLocationCoor;
    }

    public void setDestLocationCoor(Location destLocationCoor) {
        DestLocationCoor = destLocationCoor;
    }

    public Location getPickupLocationCoor() {
        return PickupLocationCoor;
    }

    public void setPickupLocationCoor(Location pickupLocationCoor) {
        PickupLocationCoor = pickupLocationCoor;
    }


    public String getCity() {
        return mCity;
    }

    public void setCity(String mCity) {
        this.mCity = mCity;
    }
}
