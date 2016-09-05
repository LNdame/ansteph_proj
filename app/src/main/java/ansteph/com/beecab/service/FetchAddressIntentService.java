package ansteph.com.beecab.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ansteph.com.beecab.R;

/**
 * Created by loicStephan on 03/07/16.
 */
public class FetchAddressIntentService extends IntentService {


    public static final String TAG = FetchAddressIntentService.class.getSimpleName();
    public FetchAddressIntentService() {
        super(TAG);
    }
    protected ResultReceiver mReceiver;



    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage ="";

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        //check if receiver was properly register
        if(mReceiver == null)
        {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results");
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

        if(location == null)
        {
            errorMessage = getString(R.string.no_location_data_provided);
            Log.wtf(TAG, errorMessage);
            deliverResultToReceiver(Constants.FAILURE_RESULT,errorMessage);
            return;
        }


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses = null;

        try{

            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single addres
                    1
            );
        }catch (IOException ioe){

            errorMessage = getString(R.string.service_not_available);
            // Log.e(TAG, errorMessage,ioe);

        }catch (IllegalArgumentException illegalArgumentException)
        {// Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
           /* Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);*/

        }

        // Handle case where no address was found.
        if(addresses ==null || addresses.size()==0){
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                // Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
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
            // Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT,
                    TextUtils.join(System.getProperty("line.separator"), addressFragments));
        }

    }



    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, bundle);
    }

}
