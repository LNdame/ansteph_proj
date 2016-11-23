package ansteph.com.beecab.app;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

import ansteph.com.beecab.model.Client;
import ansteph.com.beecab.model.JourneyRequest;

/**
 * Created by loicStephan on 26/07/16.
 * This my alternative to having to extends parcelable in some objects
 * and the existence of this object are needed at a moment notice
 */
public class GlobalRetainer extends Application {


    private static GlobalRetainer mInstance;
    private static Context mAppContext;

    public GlobalRetainer()
    {}

    @Override
    public void onCreate() {
        super.onCreate();
        GlobalRetainer.mAppContext = getApplicationContext();
        mInstance = this;
    }

    public static GlobalRetainer getInstance(){
        return mInstance;
    }


    public ArrayList<JourneyRequest > _grPendingJobs = new ArrayList<>();
    public ArrayList<JourneyRequest > _grAssignedJobs = new ArrayList<>();

    public Client _grClient = new Client();   //(only one per login)

    public JourneyRequest _currentInspectedJR = new JourneyRequest();


    public Client get_grClient() {
        return _grClient;
    }

    public void set_grClient(Client _grClient) {
        this._grClient = _grClient;
    }

    public ArrayList<JourneyRequest> get_grPendingJobs() {
        return _grPendingJobs;
    }

    public void set_grPendingJobs(ArrayList<JourneyRequest> _grPendingJobs) {
        this._grPendingJobs = _grPendingJobs;
    }

    public ArrayList<JourneyRequest> get_grAssignedJobs() {
        return _grAssignedJobs;
    }

    public void set_grAssignedJobs(ArrayList<JourneyRequest> _grAssignedJobs) {
        this._grAssignedJobs = _grAssignedJobs;
    }


    public JourneyRequest get_currentInspectedJR() {
        return _currentInspectedJR;
    }

    public void set_currentInspectedJR(JourneyRequest _currentInspectedJR) {
        this._currentInspectedJR = _currentInspectedJR;
    }

    public void addPendingJob(JourneyRequest jr)
    {
        this._grPendingJobs.add(jr);
    }

    public void addAssignedJob(JourneyRequest jr)
    {
        this._grAssignedJobs.add(jr);
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static void setAppContext(Context mAppContext) {
        GlobalRetainer.mAppContext = mAppContext;
    }
}
