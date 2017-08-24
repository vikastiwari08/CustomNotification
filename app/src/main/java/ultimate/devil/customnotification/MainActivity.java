package ultimate.devil.customnotification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Notification notification;
    private RemoteViews remoteViews;
    private final String PLAY = "play";
    private BroadcastReceiver receiver;
    private boolean play = true;
    private final String CANCEL = "cancel";
    private NotificationManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = intent.getAction();
                if (data.equals(PLAY)){
                    if (play){
                        Toast.makeText(MainActivity.this, "PLAY", Toast.LENGTH_SHORT).show();
                        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_pause);
                        play=false;
                        manager.notify(0,notification);
                    }else {
                        Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_SHORT).show();
                        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play);
                        play=true;
                        manager.notify(0,notification);
                    }
                }else if (data.equals(CANCEL)){
                    manager.cancel(0);
                }
            }
        };

        IntentFilter intentFilter =  new IntentFilter(PLAY);
        IntentFilter intentFilter2 =  new IntentFilter(CANCEL);
        registerReceiver(receiver,intentFilter);
        registerReceiver(receiver,intentFilter2);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(),R.layout.ultimate_notification);
        notification =  new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker("Ultimate Notification")
                .setContent(remoteViews)
                .build();
        manager.notify(0,notification);

        Intent playIntent =  new Intent();
        playIntent.setAction(PLAY);

        Intent cancelIntent =  new Intent();
        playIntent.setAction(CANCEL);

        PendingIntent play = PendingIntent.getBroadcast(this,99,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent cancel = PendingIntent.getBroadcast(this,99,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.play,play);
        remoteViews.setOnClickPendingIntent(R.id.cancel,cancel);


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
