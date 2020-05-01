package com.yzm.copyfrompc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private EditText host, port;
    private Intent intent;
    private PendingIntent copyintent;
    private boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBuilder = new NotificationCompat.Builder(this, "CopyfromPC");
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("CopyfromPC", "Copy from PC", NotificationManager.IMPORTANCE_DEFAULT);
            mBuilder.setChannelId("CopyfromPC");
            notificationManager.createNotificationChannel(channel);
        }
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Copy from PC");
        mBuilder.setContentText("Press to sync your PC clipboard.");
        mBuilder.setOngoing(true);
        intent = new Intent(getBaseContext(), NotificationReceiver.class);
        copyintent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(copyintent);
        host = findViewById(R.id.Host);
        port = findViewById(R.id.Port);
        findViewById(R.id.Ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("address", "http://" + host.getText() + ":" + port.getText());
                copyintent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                Toast.makeText(MainActivity.this, "The address is set!", Toast.LENGTH_SHORT).show();
                if (!notify) {
                    notify = true;
                    notificationManager.notify(0, mBuilder.build());
                }
            }
        });
        findViewById(R.id.Exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationManager.cancel(0);
                finish();
            }
        });
    }

}
