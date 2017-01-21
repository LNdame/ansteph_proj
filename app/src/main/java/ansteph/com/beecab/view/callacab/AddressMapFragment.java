package ansteph.com.beecab.view.callacab;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ansteph.com.beecab.R;
import ansteph.com.beecab.app.GlobalRetainer;
import ansteph.com.beecab.autocompleteplace.PlacesAutocompleteAdapter;
import ansteph.com.beecab.autocompleteplace.RecyclerItemClickListener;
import ansteph.com.beecab.helper.PermissionUtils;
import ansteph.com.beecab.service.Constants;
import ansteph.com.materialshowcase.MaterialShowcaseView;

/**
 * A simple {@link Fragment} subclass.
 * factory method to
 * create an instance of this fragment.
 */
public class AddressMapFragment extends Fragment implements
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener,OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnCameraChangeListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    protected static String TAG = AddressMapFragment.class.getSimpleName();

    public AddressMapFragment() {
        // Required empty public constructor

    }

    private View rootView;
    MapView mMapView;

    GoogleMap mGoogleMap;
    Projection projection;

    private TextView mCameraTxt;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private Marker mPE;

    private String mCity=null;

    public static final CameraPosition PE =
            new CameraPosition.Builder().target(new LatLng(-33.7139, 25.5207))
                    .zoom(15.5f)
                    .bearing(0)
                    .tilt(25)
                    .build();


     OnFragmentInteractionListener mListener;
    /*******Geocoder********/
    protected static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    protected static final String LOCATION_ADDRESS_KEY = "location-address";


    protected TextView mLocationAddressTextview;

   public EditText txtFormatAddress;

   // public  TextView txtAddress;

    private int grabFlag;

    private Location mLocation;

    /******these were added to satisfy the autocomplete***/
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    private PlacesAutocompleteAdapter mAutocompleteAdapter;
    protected GoogleApiClient mGoogleApiClient;
    ImageView clearText;
    private static final LatLngBounds SAbounds = new LatLngBounds(new LatLng(-31.029191, 18.070882), new LatLng(-28.293093, 32.446854));


    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // TODO: Rename and change types and number of parameters



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        buildGoogleApiClient();
        mLocation = new Location("");
        Bundle args = getArguments();
        if(args!=null)
        {
            if(args.containsKey(Constants.ADDRESS)){
               grabFlag= args.getInt(Constants.ADDRESS);
            }
        }

        try{
            rootView = inflater.inflate(R.layout.fragment_address_map, container, false);
            MapsInitializer.initialize(getActivity());
            mMapView=(MapView) rootView.findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }catch(InflateException e){
            Log.e("OnCreateView","Inflate exception");

        }
        mCameraTxt= (TextView) rootView.findViewById(R.id.address_text);

     txtFormatAddress = (EditText) rootView.findViewById(R.id.txtFormatAddress);
        // Set defaults, then update using values stored in the Bundle.
       // txtFormatAddress.setText("Dummies");

        clearText = (ImageView) rootView.findViewById(R.id.imgClear);

        Button btnback = (Button) rootView.findViewById(R.id.btnBack);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Bundle bundle = new Bundle();
                bundle.putString("DestinationLocation",txtFormatAddress.getText().toString());

                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(SetRouteFragment.TAG);
              //  fragment.setArguments(bundle);
               // Fragment fragment = new SetRouteFragment();
               // fragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction()
                        .addToBackStack(SetRouteFragment.TAG);
                fragmentTransaction.replace(R.id.container_body, fragment,SetRouteFragment.TAG);
                fragmentTransaction.commit();

                if(grabFlag==Constants.PICKUP_GRAB)
                ((SetRoute)getActivity()).setCity(mCity);

                onOkButtonPressed(txtFormatAddress.getText().toString(), mLocation);
            }
        });


        mAutocompleteAdapter = new PlacesAutocompleteAdapter(GlobalRetainer.getAppContext(), R.layout.search_row, mGoogleApiClient, SAbounds, null);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAutocompleteAdapter);


        txtFormatAddress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mRecyclerView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        txtFormatAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    mAutocompleteAdapter.getFilter().filter(s.toString());
                } else if (!mGoogleApiClient.isConnected()) {
                    //Toast.makeText(getActivity(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    //Log.e(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(GlobalRetainer.getAppContext(), new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                final PlacesAutocompleteAdapter.PlaceAutocomplete item = mAutocompleteAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getCount() == 1) {
                            //Do the things here on Click.....

                          //  Toast.makeText(getActivity(), String.valueOf(places.get(0).getLatLng()), Toast.LENGTH_SHORT).show();
                          // txtFormatAddress.setText(item.description);
                            movethePin(places.get(0).getLatLng());

                        } else {
                            Toast.makeText(getActivity(), Constants.SOMETHING_WENT_WRONG, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Log.i("TAG", "Clicked: " + item.description);
                Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                mRecyclerView.setVisibility(View.GONE);
                // mAutocompleteAdapter.clearData();
            }
        }));

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFormatAddress.setText("");
            }
        });





        return rootView;
    }


    public void onOkButtonPressed (String selectedAddress, Location location){
        if(mListener!=null)
        {
            mListener.onFragmentInteraction(selectedAddress,location, grabFlag);
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
        mListener= (OnFragmentInteractionListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+ "must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // txtFormatAddress =(EditText) getView().findViewById(R.id.txtFormatAddress);
     //  txtFormatAddress.setText(((SetRoute)getActivity()).getsometext());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Port Elizabeth, South Africa.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();

        if(mGoogleApiClient.isConnected()){
            Log.v("Google API","Dis-Connecting");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();

        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
            Log.v("Google API","Connecting");
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMapClickListener(this);
        mGoogleMap.setOnMapLongClickListener(this);
        mGoogleMap.setPadding(40,250,40,40);
        mGoogleMap.setOnCameraChangeListener(this);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);


        // Add a marker in PE and move the camera
        LatLng pe = new LatLng(-33.9736, 25.5983);
        projection = mGoogleMap.getProjection();
        mPE = mGoogleMap.addMarker(new MarkerOptions().position(pe).title("BeeCab")
        .snippet("Drag to desire location").
                        icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_pin)).draggable(true));

       // icon(BitmapDescriptorFactory.fromResource(R.drawable.arrow))
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pe,14.5f));
        mGoogleMap.setOnMyLocationButtonClickListener(this);

        enableMyLocation();

       mGoogleMap.setOnMarkerDragListener(this);
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission to access the location is missing.
            //use permision in the set route.
         PermissionUtils.requestPermission((SetRoute)getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
             Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mGoogleMap != null) {
            // Access to the location has been granted to the app.
           mGoogleMap.setMyLocationEnabled(true);
        }
    }


    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        LatLng markerLocation = marker.getPosition();

        Point screenPosition = projection.toScreenLocation(markerLocation);


        Location loc  = new Location("");
        loc.setLatitude(markerLocation.latitude);
        loc.setLongitude(markerLocation.longitude);
        mLocation= loc;
        try
        {
            handleAddressSearch(loc);
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Oops! Cannot find this address!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
       // ((SetRoute)getActivity()).testaccess();



    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        Location loc  = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);

        //no longer necessary
       // handleAddressSearch(loc);





    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


    public void handleAddressSearch(Location location)
    {
        String errormsg ="";

        if(location == null)
        {
            errormsg=getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errormsg);
            return;
        }

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        }catch (IOException ioe)
        {
            errormsg =getString(R.string.service_not_available);
            Log.e(TAG, errormsg,ioe);

        }catch (IllegalArgumentException illegalArgumentException)
        {
            errormsg = getString(R.string.invalid_lat_long_used);
           Log.e(TAG, errormsg + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException) ;
        }


        // Handle case where no address was found.
        if(addresses == null || addresses.size()==0){
            if(errormsg.isEmpty()){
                errormsg = getString(R.string.no_address_found);
               Log.e(TAG, errormsg);
            }
        }else{
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            Log.i(TAG, address.getLocality());
            if (address.getAddressLine(0)!=null)
                  Log.i(TAG, address.getAddressLine(0));

            if (address.getAddressLine(1)!=null)
                 Log.i(TAG, address.getAddressLine(1));

            Log.i(TAG, getString(R.string.address_found));
            mCity = address.getLocality();

           // String addressOutput = TextUtils.join(System.getProperty("line.separator"),addressFragments);
            String addressOutput = TextUtils.join(", ",addressFragments);
          //  Toast.makeText(getActivity(),addressOutput,Toast.LENGTH_LONG).show();
            txtFormatAddress.setText(addressOutput);
        }




    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
       // mCameraTxt.setText(cameraPosition.target.toString());

       // mPE.setPosition(cameraPosition.target);
        animateMarker(mPE,cameraPosition.target,false);
    //get the adddress at the middle of the screen from camera target
        Location loc  = new Location("");
        loc.setLatitude(cameraPosition.target.latitude);
        loc.setLongitude(cameraPosition.target.longitude);
        mLocation= loc;

        try
        {
            handleAddressSearch(loc);
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Oops! Cannot find this address!", Toast.LENGTH_SHORT).show();
        }

    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String selectedAddress, Location location,int flag);
    }


    /*
    * Method to animate the movement of a marker
     * not used right now
       * source stackOver http://stackoverflow.com/questions/13728041/move-markers-in-google-map-v2-android
       * author: Sandeep Dhull**/

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mGoogleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }


    /*******************************************************these were added to satisfy the autocomplete**************/

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(GlobalRetainer.getAppContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
    }


    /**
     * move the camera and the pointer.
     *
     */

    public void movethePin(LatLng latLng)
    {
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14.5f));
        mPE.setPosition(latLng);

        Location loc  = new Location("");
        loc.setLatitude(latLng.latitude);
        loc.setLongitude(latLng.longitude);
        mLocation= loc;

        try
        {
            handleAddressSearch(loc);
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Oops! Cannot find this address!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.v("Google API Callback", "Connection Done");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.v("Google API Callback", "Connection Suspended");
        Log.v("Code", String.valueOf(i));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.v("Google API Callback","Connection Failed");
        Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
        //Toast.makeText(GlobalRetainer.getAppContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
    }




}
