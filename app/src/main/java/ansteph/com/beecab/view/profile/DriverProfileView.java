package ansteph.com.beecab.view.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

import ansteph.com.beecab.R;
import ansteph.com.beecab.adapter.CustomVolleyRequest;
import ansteph.com.beecab.app.Config;
import ansteph.com.beecab.model.DriverProfile;

public class DriverProfileView extends AppCompatActivity implements View.OnClickListener {


    //Imageloader to load images
    private ImageLoader imageLoader;

    LinearLayout lnImage1;
    LinearLayout lnImage2;
    LinearLayout lnImage3;

    ImageView goleft1,goleft2,goleft3, goright1, goright2, goright3;
    TextView  txtEmail,txtPhone, txtName, txtCarModel, txtNumberPlate,txtCabLicence,txtYear, txtRating, txtCurrentCity;

    DriverProfile driverProfile  ;
    ViewAnimator viewAnimator;

    String taxiID;

    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        driverProfile =null ;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lnImage1 = (LinearLayout) findViewById(R.id.lnImage1) ;
        lnImage2 = (LinearLayout) findViewById(R.id.lnImage2) ;
        lnImage3 = (LinearLayout) findViewById(R.id.lnImage3) ;

        goleft1 = (ImageView) findViewById(R.id.goleft1) ;
        goleft2 = (ImageView) findViewById(R.id.goleft2) ;
        goleft3 = (ImageView) findViewById(R.id.goleft3) ;

        goright1 = (ImageView) findViewById(R.id.goright1) ;
        goright2 = (ImageView) findViewById(R.id.goright2) ;
        goright3 = (ImageView) findViewById(R.id.goright3) ;

        setTextField();

        Bundle b = getIntent().getExtras();

        if (b!=null)
        {
           // txtID.setText(txtID.getText().toString() + " "+ b.getString("taxiID"));
            taxiID = b.getString("taxiID");
        }

        if(taxiID!=null &&  !taxiID.isEmpty())
        {
            getImageData();
             getProfileData();
        }

        ratingBar = (RatingBar) findViewById(R.id.ratingBar) ;
        ratingBar.setRating(3);

        viewAnimator =(ViewAnimator) findViewById(R.id.viewAnimator);

        final Animation inAmin = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_in_left);
        final Animation outAmin = AnimationUtils.loadAnimation(getApplicationContext(),android.R.anim.slide_out_right);

        viewAnimator.setInAnimation(inAmin);
        viewAnimator.setOutAnimation(outAmin);

        goleft1.setOnClickListener(this);
        goleft2.setOnClickListener(this);
        goleft3.setOnClickListener(this);

        goright1.setOnClickListener(this);
        goright2.setOnClickListener(this);
        goright3.setOnClickListener(this);

    }


    private void setTextField()
    {

        // initialize and give a value
        txtName = (TextView) findViewById(R.id.txtfullname);
        txtPhone = (TextView) findViewById(R.id.txtCellphone);
        txtEmail = (TextView) findViewById(R.id.txtemail);
        txtCarModel = (TextView) findViewById(R.id.txtCarModel);
        txtNumberPlate= (TextView) findViewById(R.id.txtNumberPlate);
        txtCabLicence = (TextView) findViewById(R.id.txtCabLicence);
        txtCurrentCity = (TextView) findViewById(R.id.txtCurrentCity);
        txtRating = (TextView) findViewById(R.id.txtRating);

        txtYear = (TextView) findViewById(R.id.txtyear);

       // txtName.setText(mGlobalretainer.get_grDriver().getName());
        //txtPhone.setText(mGlobalretainer.get_grDriver().getMobile());
        //txtEmail.setText(mGlobalretainer.get_grDriver().getEmail());

       // txtCabLicence.setText(mGlobalretainer.get_grDriver().getLicence());
       // txtYear.setText(mGlobalretainer.get_grDriver().getYear());



    }



    private void updateUI() {

        if(driverProfile!=null)
        {
            txtCarModel.setText(driverProfile.getCarModel());
            txtNumberPlate.setText(driverProfile.getCarNumberPlate());
            txtCurrentCity.setText(driverProfile.getCurrentCity());
            txtRating.setText(driverProfile.getProfileRating());
            txtName.setText(driverProfile.getName());
            txtPhone.setText(driverProfile.getMobile());
            txtEmail.setText(driverProfile.getEmail());
            txtCabLicence.setText(driverProfile.getLicense());
            txtYear.setText(driverProfile.getYearoflicense());

        }
    }

    private void getProfileData()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait ","Fetching data...",false,false);
        String url = String.format(Config.RETRIEVE_DRIVER_PROFILE_URL,taxiID);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                JSONObject jsonResponse = null;


                try {

                    jsonResponse = new JSONObject(response);
                    boolean error = jsonResponse.getBoolean(Config.ERROR_RESPONSE);
                    String serverMsg = jsonResponse.getString(Config.MSG_RESPONSE);
                    if(!error)
                    {
                        //get the user detail from the server  (String carModel, String carNumberPlate, String currentCity, String profileRating,
                        // String name, String email, String mobile) {

                        JSONArray profile = jsonResponse.getJSONArray("profile");
                        JSONObject user = profile.getJSONObject(0); //getString(Config.KEY_ID)
                        // String carModel, String carNumberPlate, String currentCity, String profileRating
                        driverProfile = new DriverProfile(user.getString(Config.KEY_CAR_MODEL),
                                user.getString(Config.KEY_CAR_NUMPLATE),
                                user.getString(Config.KEY_CURRENT_CITY),
                                user.getString(Config.KEY_PRO_RATING),
                                user.getString(Config.KEY_DRIVER_NAME),
                                user.getString(Config.KEY_DRIVER_EMAIL),
                                user.getString(Config.KEY_DRIVER_MOBILE)

                        );

                        driverProfile.setYearoflicense(user.getString(Config.KEY_DRIVER_YEAR));
                        driverProfile.setLicense(user.getString(Config.KEY_DRIVER_LICENSE));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    updateUI();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(getApplicationContext(),"Oops! Profile unreachable! Please check your internet connection", Toast.LENGTH_LONG).show();
            }
        }){

        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding our request to the queue
        requestQueue.add(stringRequest);

    }


    private void getImageData()
    {
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait ","Fetching data...",false,false);

        String url = String.format(Config.RETRIEVE_USER_IMAGE_URL,taxiID);

        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        loading.dismiss();

                        //Displaying our grid
                        loadImage(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void loadImage(JSONArray jsonArray)
    {

        //Creating a json object of the current index
        JSONObject obj = null;
        String imageurl1 =null;
        String imageurl2 =null;
        String imageurl3 =null;

        try {
            obj = jsonArray.getJSONObject(0);
            imageurl1 = obj.getString(Config.TAG_DRIVER_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(imageurl1!=null){
            //NetworkImageView
            NetworkImageView networkImageView = new NetworkImageView(this);

            imageLoader = CustomVolleyRequest.getInstance(this).getImageLoader();
            imageLoader.get(imageurl1, ImageLoader.getImageListener(networkImageView,R.drawable.taxiicon,android.R.drawable.ic_dialog_alert));

            //seting the image to load
            networkImageView.setImageUrl(imageurl1,imageLoader);

            networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            // networkImageView.setLayoutParams(new GridView.LayoutParams(400,400));
            lnImage1.addView(networkImageView, params);

            //save internally

        }


        try {
            obj = jsonArray.getJSONObject(1);
            imageurl2 = obj.getString(Config.TAG_DRIVER_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(imageurl2!=null){
            //NetworkImageView
            NetworkImageView networkImageView = new NetworkImageView(this);

            imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
            imageLoader.get(imageurl2, ImageLoader.getImageListener(networkImageView,R.drawable.taxiicon,android.R.drawable.ic_dialog_alert));

            //seting the image to load
            networkImageView.setImageUrl(imageurl2,imageLoader);

            networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            // networkImageView.setLayoutParams(new GridView.LayoutParams(400,400));
            lnImage2.addView(networkImageView, params);

            //save internally

        }


        try {
            obj = jsonArray.getJSONObject(2);
            imageurl3 = obj.getString(Config.TAG_DRIVER_IMAGE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        if(imageurl3!=null){
            //NetworkImageView
            NetworkImageView networkImageView = new NetworkImageView(this);

            imageLoader = CustomVolleyRequest.getInstance(getApplicationContext()).getImageLoader();
            imageLoader.get(imageurl3, ImageLoader.getImageListener(networkImageView,R.drawable.taxiicon,android.R.drawable.ic_dialog_alert));

            //seting the image to load
            networkImageView.setImageUrl(imageurl3,imageLoader);

            networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);

            LinearLayoutCompat.LayoutParams params = new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT,LinearLayoutCompat.LayoutParams.MATCH_PARENT);
            // networkImageView.setLayoutParams(new GridView.LayoutParams(400,400));
            lnImage3.addView(networkImageView, params);

            //save internally

        }




    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.goleft1:
            case R.id.goleft2:
            case R.id.goleft3:viewAnimator.showPrevious(); break;

            case R.id.goright1:
            case R.id.goright2:
            case R.id.goright3:viewAnimator.showNext();break;



            default:


        }

    }

}
