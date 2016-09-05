package ansteph.com.beecab.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ansteph.com.beecab.Landing;
import ansteph.com.beecab.R;

/**
 * Created by loicStephan on 04/08/16.
 */
public class BeeCabFbMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getData().get("message"));
    }


    private void showNotification(String message)
    {
        Intent i = new Intent(this, Landing.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("BeeCab")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_stat_vusi)
                .setContentIntent(pendingIntent);


        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }
}
