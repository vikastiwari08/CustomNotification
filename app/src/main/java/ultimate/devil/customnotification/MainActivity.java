package ultimate.devil.customnotification;



import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder notification;
    private RemoteViews remoteViews;
    private final String PLAY = "play";
    private final int NOTIFICATION_ID = 99;
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

        final ImageView icon = (ImageView) findViewById(R.id.icon);

        receiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String data = intent.getAction();

                Log.d("TAG", "onReceive: "+data);
                if (data.equals(PLAY)){
                    if (play){
                        Toast.makeText(MainActivity.this, "PLAY", Toast.LENGTH_SHORT).show();
                        icon.setImageResource(R.drawable.ic_pause);
                        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_pause);
                        play=false;
                        manager.notify(NOTIFICATION_ID,notification.build());
                    }else {
                        Toast.makeText(MainActivity.this, "Pause", Toast.LENGTH_SHORT).show();
                        icon.setImageResource(R.drawable.ic_play);
                        remoteViews.setImageViewResource(R.id.play,R.drawable.ic_play);
                        play=true;
                        manager.notify(NOTIFICATION_ID,notification.build());
                    }
                }else if (data.equals(CANCEL)){
                    icon.setImageResource(R.drawable.ic_delete);
                    manager.cancel(NOTIFICATION_ID);
                }
            }
        };

        IntentFilter intentFilter =  new IntentFilter(PLAY);
        IntentFilter intentFilter2 =  new IntentFilter(CANCEL);
        registerReceiver(receiver,intentFilter);
        registerReceiver(receiver,intentFilter2);

        notification =  new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(false)
                .setTicker("Ultimate Notification");
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(),R.layout.ultimate_notification);
        /*notification =  new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_icon)
                .setTicker("Ultimate Notification")
                .setContent(remoteViews);*/
        notification.setContent(remoteViews);




        Intent playIntent =  new Intent();
        playIntent.setAction(PLAY);

        Intent cancelIntent =  new Intent();
        cancelIntent.setAction(CANCEL);

        PendingIntent play = PendingIntent.getBroadcast(this,99,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent cancel = PendingIntent.getBroadcast(this,99,cancelIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.play,play);
        remoteViews.setOnClickPendingIntent(R.id.cancel,cancel);

        manager.notify(NOTIFICATION_ID,notification.build());


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
