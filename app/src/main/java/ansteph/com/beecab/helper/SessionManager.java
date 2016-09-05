package ansteph.com.beecab.helper;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;
import android.content.Intent;

import java.util.HashMap;

import ansteph.com.beecab.view.callacab.CabCaller;
import ansteph.com.beecab.view.registration.Login;

/**
 * Created by loicStephan on 11/07/16.
 */
public class SessionManager {

    //Shared Preferences
    SharedPreferences preferences;


    //Editor for shared preferences
    Editor editor;

    //Context
    Context _context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "BeeCabPref";

    // All Shared Preferences Keys
    private static final String KEY_IS_WAITING_FOR_SMS = "IsWaitingForSms";
    private static final String KEY_MOBILE_NUMBER = "mobile_number";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_MOBILE = "mobile";
    public static final String KEY_ID = "id";
    public static final String KEY_APIKEY = "apikey";


    //Constructor
    public SessionManager(Context context)
    {
        this._context = context;
        preferences =_context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }


    /**
     * Create login session
     * */
    public void createLoginSession (String id,String name, String email, String mobile , String apikey)
    {
        //storing login value as true
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_APIKEY, apikey);

        editor.commit();
    }
/**
 * Check login method wil check user login status
 * If false it will redirect user to login page
 * Else won't do anything
 * */

    public void checkLogin(){
        //Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }else{
            Intent i = new Intent(_context, CabCaller.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //Starting Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String>getUserDetails(){

        HashMap<String, String> user = new HashMap<>();

        //user id
        user.put(KEY_ID, preferences.getString(KEY_ID,null));
        //user name
        user.put(KEY_NAME, preferences.getString(KEY_NAME,null));
        //user email
        user.put(KEY_EMAIL, preferences.getString(KEY_EMAIL,null));
        //user mobile
        user.put(KEY_MOBILE, preferences.getString(KEY_MOBILE,null));

        //user apikey
        user.put(KEY_APIKEY, preferences.getString(KEY_APIKEY,null));
        return user;
    }


    /**
     * Clear session details
     * */
    public void logoutUser(){
        //Clearing all the data from Shared Preferences
        editor.clear();
        editor.commit();

        //After logout redirect user to login Activity

        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Starting Login Activity
        _context.startActivity(i);

        //
    }


    /**
     * Quick check for login
     * **/
// Get Login State
    public boolean isLoggedIn() {
    return preferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }



    }
