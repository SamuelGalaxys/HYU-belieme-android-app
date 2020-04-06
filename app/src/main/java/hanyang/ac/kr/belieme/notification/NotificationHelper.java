package hanyang.ac.kr.belieme.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import hanyang.ac.kr.belieme.R;

public class NotificationHelper extends ContextWrapper {

    public String channelId;
    private String channelName;

    private NotificationManager manager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannels() {
        channelId = "channel" + (int)System.currentTimeMillis()/1000;
        channelName = "Channel " + (int)System.currentTimeMillis()/1000;
        NotificationChannel channel1 = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager() {
        if(manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }
}
