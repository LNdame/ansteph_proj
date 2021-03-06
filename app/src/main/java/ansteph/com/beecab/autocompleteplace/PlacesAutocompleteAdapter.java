package ansteph.com.beecab.autocompleteplace;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import ansteph.com.beecab.R;

/**
 * Created by loicStephan on 21/12/2016.
 */

public class PlacesAutocompleteAdapter extends RecyclerView.Adapter<PlacesAutocompleteAdapter.PredictionHolder> implements Filterable {

    private static final String TAG = PlacesAutocompleteAdapter.class.getSimpleName();
    private ArrayList<PlaceAutocomplete> mResultList;
    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private AutocompleteFilter mPlaceFilter;

    private Context mContext;
    private int layout;

private static final CharacterStyle STYLE = new StyleSpan(Typeface.NORMAL);
    public PlacesAutocompleteAdapter(Context context, int resource, GoogleApiClient googleApiClient,
                                     LatLngBounds bounds, AutocompleteFilter filter) {
        mContext = context;
        layout = resource;
        mGoogleApiClient = googleApiClient;
        mBounds = bounds;
        mPlaceFilter = filter;
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = layoutInflater.inflate(layout, viewGroup, false);
        PredictionHolder mPredictionHolder = new PredictionHolder(convertView);
        return mPredictionHolder;
    }

    @Override
    public void onBindViewHolder(PredictionHolder mPredictionHolder, final int i) {
        mPredictionHolder.mPrediction.setText(mResultList.get(i).description);
        /*mPredictionHolder.mRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetLatLonCallback.getLocation(resultList.get(i).toString());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        if(mResultList != null)
            return mResultList.size();
        else
            return 0;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getAutocomplete(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        if (mGoogleApiClient.isConnected()) {


            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, mPlaceFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
              //  Toast.makeText(mContext, "Error contacting API: " + status.toString(),Toast.LENGTH_SHORT).show();
//                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            //Log.i("", "Query completed. Received " + autocompletePredictions.getCount() + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getFullText(STYLE)));
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        //Log.e("", "Google API client is not connected for autocomplete query.");
        return null;
    }

    public PlaceAutocomplete getItem(int position) {
        return mResultList.get(position);
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        mBounds = bounds;
    }


    public class PredictionHolder extends RecyclerView.ViewHolder {
        private TextView mPrediction;
        private RelativeLayout mRow;
        public PredictionHolder(View itemView) {

            super(itemView);
            mPrediction = (TextView) itemView.findViewById(R.id.address);
            mRow=(RelativeLayout)itemView.findViewById(R.id.autocomplete_row);
        }

    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    public class PlaceAutocomplete {

        public CharSequence placeId;
        public CharSequence description;

        PlaceAutocomplete(CharSequence placeId, CharSequence description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }


    public void clearData ()
    {
        int size = this.mResultList.size();
        if(size>0){
          //  mResultList.clear();

            for (int i = 0; i < size-1; i++) {
                this.mResultList.remove(0);

            }
            notifyItemRangeChanged(0, size);
        }

    }
}
