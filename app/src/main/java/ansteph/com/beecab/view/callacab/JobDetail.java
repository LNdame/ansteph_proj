package ansteph.com.beecab.view.callacab;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ansteph.com.beecab.R;
import ansteph.com.beecab.model.JourneyRequest;

public class JobDetail extends AppCompatActivity implements OnMapReadyCallback {

    public TextView  destination, pickup, proposedFare,  putime, txtdistance, txtTimeleft;

    MapView mMapView;

    GoogleMap mGoogleMap;
    Projection projection;
    JourneyRequest job;
    private Marker mPE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        job = new JourneyRequest();


        destination  =(TextView) findViewById(R.id.txtDestination);
        pickup  =(TextView) findViewById(R.id.txtPickup);
        proposedFare  =(TextView) findViewById(R.id.txtFare);
        putime  =(TextView) findViewById(R.id.txtTime);
        txtdistance=(TextView) findViewById(R.id.txtDistance);
        txtTimeleft=(TextView) findViewById(R.id.txttimeleft);

        Bundle extra = getIntent().getExtras();
        if(extra!=null)
        {
            job = (JourneyRequest) extra.getSerializable("job");
            destination.setText(job.getDestinationAddr());
            pickup.setText(job.getPickupAddr());
            proposedFare.setText("R"+job.getProposedFare());
            putime.setText(job.getPickupTime());


            if(job.getTimeCreated()!=null) primeTimer(job.getTimeCreated());
        }



        try{
            MapsInitializer.initialize(getApplicationContext());
            mMapView = (MapView) findViewById(R.id.map);
            mMapView.onCreate(savedInstanceState);
            mMapView.getMapAsync(this);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void primeTimer(Date sentDate)
    {
        long timeinMillisNow = (new Date()).getTime();
        long sentDateMillis = sentDate.getTime();

        long countdownmill = (sentDateMillis +600000) -timeinMillisNow;

        new CountDownTimer(countdownmill, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                //    txtTimer.setText("Time remaining: " + millisUntilFinished/1000);
                txtTimeleft.setText(""+String.format("%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                txtTimeleft.setText("all bets are off!");
            }
        }.start();
    }

/*
*  final  TextView txtSomedate = (TextView) findViewById(R.id.txtSomedate);
        final  TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        final  TextView txtTimenow = (TextView) findViewById(R.id.txtTimeNow);

        String givenDate = "2016-08-22 20:30:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long timeinMillisNow = (new Date()).getTime();
        long timeinMillseconds =0;
        txtTimenow.setText("" +String.format("%d", timeinMillisNow));
        try{
            Date mDate = sdf.parse(givenDate);
          timeinMillseconds = mDate.getTime();

            txtSomedate.setText("" +String.format("%d", timeinMillseconds));
        }catch (ParseException e)
        {
            e.printStackTrace();
        }catch (Exception e)
        {

        }


        long countdownmill =  timeinMillseconds -timeinMillisNow;


        new CountDownTimer(countdownmill, 1000){

            @Override
            public void onTick(long millisUntilFinished) {
                //    txtTimer.setText("Time remaining: " + millisUntilFinished/1000);
                txtTimer.setText(""+String.format("%d min, %d sec ", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                txtTimer.setText("all bets are off!");
            }
        }.start();
    }
*
* */

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
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
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        //put in a try the coord destination could be null


        String[] coord = job.getDestinationCoord().split(",");

        toLongitude =Double.parseDouble(coord[1]);
        toLatitude =Double.parseDouble(coord[0]);

        LatLng dest = new LatLng(Double.parseDouble(coord[0]),Double.parseDouble(coord[1]));

        String[] coorp = job.getPickupCoord().split(",");

        fromLongitude = Double.parseDouble(coorp[1]);
        fromLatitude =Double.parseDouble(coorp[0]);

        LatLng pick = new LatLng(Double.parseDouble(coorp[0]),Double.parseDouble(coorp[1]));

        Marker pickup = mGoogleMap.addMarker(new MarkerOptions().position(pick).title("Pickup").snippet(job.getPickupAddr()).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(false));

        Marker destination = mGoogleMap.addMarker(new MarkerOptions().position(dest).title("Destination").snippet(job.getDestinationAddr()).
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).draggable(false));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pick,12.5f));


        getDirection();
    }

    //Calculate path and distance

    //From -> the first coordinate from where we need to calculate the distance
    private double fromLongitude;
    private double fromLatitude;

    //To -> the second coordinate to where we need to calculate the distance
    private double toLongitude;
    private double toLatitude;


    public void getDirection()
    {
//Getting the URL
        String url = makeURL(fromLatitude, fromLongitude, toLatitude, toLongitude);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyA6ukiOzzaLCeoxmumYhbB5s0ksvpjWaBU");
        return urlString.toString();
    }

    //The parameter is the server response
    public void drawPath(String  result) {
        //Getting both the coordinates
        LatLng from = new LatLng(fromLatitude,fromLongitude);
        LatLng to = new LatLng(toLatitude,toLongitude);

        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to);
        Double  distinKM = distance/1000;

        BigDecimal bd = new BigDecimal(distinKM);
        bd= bd.setScale(2,BigDecimal.ROUND_HALF_UP);
        distinKM = bd.doubleValue();
        //Displaying the distance
        txtdistance.setText(String.valueOf(distinKM+" km"));
        // Toast.makeText(this,String.valueOf(distance+" Meters"),Toast.LENGTH_SHORT).show();


        try {
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(Color.BLUE)
                    .geodesic(true)
            );


        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    //end Calculate path and distance


}
