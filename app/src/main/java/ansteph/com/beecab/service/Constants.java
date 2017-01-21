package ansteph.com.beecab.service;

/**
 * Created by loicStephan on 03/07/16.
 */
public class Constants {



    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT=1;
    public static final String PACKAGE_NAME= "ansteph.com.taxi.service";
    public static final String RECEIVER = PACKAGE_NAME+".RECEIVER";
    public static final String RESULT_DATA_KEY= PACKAGE_NAME +".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME +".LOCATION_DATA_EXTRA";

    public static final int DESTINATION_GRAB=1;
    public static final int PICKUP_GRAB=2;
    public static final String ADDRESS="AddressPick";



    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static String PlacesTag = "Google Places Auto Complete";


    //job related
    public static int JOB_STATUS_PENDING =0;
    public static int JOB_STATUS_ASSIGNED =1;
    public static int JOB_STATUS_CONFIRMED_BY_DRIVER =2;
    public static int JOB_STATUS_CLOSED =3;
}
