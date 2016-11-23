package ansteph.com.beecab.app;

/**
 * Created by loicStephan on 28/06/16.
 */
public class Config {

    /****************************************************---Domain---*************************************************************/

     // private static String DOMAIN="http://10.0.0.3:8888/taxi/";
     //private static String DOMAIN="http://10.102.139.55:8888/taxi/";http://beecab.net/api
   // private static String DOMAIN="http://www.beecab.co.za/";
    private static String DOMAIN="http://beecab.net/api/";

    /****************************************************---Route---*************************************************************/


    public static final String URL_VERIFY_OTP = DOMAIN+"v1/activate_user";
    public static final String REGISTER_JOB_RESPONSE = DOMAIN+"v1/retrievejour_response/%s";
    public static final String REGISTER_URL = DOMAIN+"v1/register_client";

    public static final String RETRIEVE_USER_URL =DOMAIN+"v1/retrievetcuser/%s/%s";
    public static final String REGISTER_FB =DOMAIN+"v1/register_fbNot";

    public static final String CREATE_JOB_URL =DOMAIN+"v1/createjob";
    public static final String ASSIGN_JOB_URL =DOMAIN+"v1/createacceptedrequest";

    public static final String RETRIEVE_PENDING_JOB_URL =DOMAIN+"v1/retrievependingjob/%s";
    public static final String UPDATE_PWD_URL =DOMAIN+"v1/changepassword/%s/%s";
    public static final String RETRIEVE_ASS_JOB_URL =DOMAIN+"v1/retrieveassignjob/%s";

    public static final String CANCEL_JOB = DOMAIN+"v1//updatejob/%s/%s";
    public static final String RETRIEVE_USER_PROFILE_URL = DOMAIN+"v1/retrieve_cl_profile/%s";

    public static final String RETRIEVE_DRIVER_PROFILE_URL = DOMAIN+"v1/retrieve_dr_profile_for_client/%s";


    public static final String UPLOAD_URL =DOMAIN+"v1/save_driver_profile_image";
    public static final String UPLOAD_URL_EN =DOMAIN+"v1/save_client_profile_en_image";

    public static final String RETRIEVE_REFERRAL_URL =DOMAIN+"v1/retrieve_client_referral/%s";
    public static final String CREATE_CLIENT_REFERRAL_URL =DOMAIN+"v1/create_client_referral";

    public static final String RETRIEVE_USER_IMAGE_URL = DOMAIN+"v1/retrieve_dr_profile_image/%s";

    public static final String URL_RESEND_OTP = DOMAIN+"v1/resendotp/%s";

    /****************************************************---SMS FLAGS---*************************************************************/

    // SMS provider identification
    // It should match with your SMS gateway origin /retrieveallpendingjob/:jrID  save_driver_profile_en_image
    // You can use  MSGIND, TESTER and ALERTS as sender ID
    // If you want custom sender Id, approve MSG91 to get one
    public static final String SMS_ORIGIN = "ANSTEPHIM";

    // special character to prefix the otp. Make sure this character appears only once in the sms
    public static final String OTP_DELIMITER = ":";

   // server URL configuration http://localhost:8888/taxi/v1/activate_user
    public static final String URL_REQUEST_SMS = "http://10.0.0.7:8888/android_sms/request_sms.php";



    /****************************************************---Params Keys---*************************************************************/

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    //Key params for Job request
    public static final String KEY_JOB_PICKUPADDR = "pickupAddr";
    public static final String KEY_JOB_DESTADDR = "destAddr";
    public static final String KEY_JOB_PICKUPTIME = "pickupTime";
    public static final String KEY_JOB_FARE = "proposedFare";
    public static final String KEY_JOB_CALL = "callAllowed";
    public static final String KEY_JOB_PUCOORD = "pickupCoord";
    public static final String KEY_JOB_DESTCOORD = "destCoord";
    public static final String KEY_JOB_TCID = "tcID";
    public static final String KEY_TC_ID = "id";
    public static final String KEY_JOB_SHARED = "shared";

    public static final String KEY_GENDER= "gender";

    //Keys to send username, password, phone and otp
    public static final String KEY_NAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_EMAIL = "email";

    public static final String KEY_EN_IMAGE = "encodeimage";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_OTP = "otp";
    public static final String TAG_IMAGE_URL = "profile_picture_url";

    public static final String TAG_DRIVER_IMAGE_URL = "car_picture_url";
    //JSON Tag from response from server
    public static final String ERROR_RESPONSE= "error";
    public static final String MSG_RESPONSE= "message";
    public static final String KEY_IMAGE = "image";


    public static final String KEY_CAR_MODEL = "car_model";
    public static final String KEY_CAR_NUMPLATE = "car_numberplate";
    public static final String KEY_PRO_ID= "taxi_driver_id";
    public static final String KEY_CURRENT_CITY = "current_city";
    public static final String KEY_PRO_RATING = "profile_rating";

    public static final String KEY_DRIVER_NAME = "td_name";
    public static final String KEY_DRIVER_EMAIL = "td_email";
    public static final String KEY_DRIVER_MOBILE = "td_mobile";

    public static final String KEY_DRIVER_LICENSE = "td_license";
    public static final String KEY_DRIVER_YEAR = "td_year";



}
