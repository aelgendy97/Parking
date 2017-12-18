package com.example.maupi.parkking;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import com.example.maupi.parkking.R;

public class Timer extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        // Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        int time = intent.getIntExtra("time", 5000);
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Notification notify = new Notification.Builder
                (getApplicationContext()).setContentTitle("Meter Update").setContentText("You have one more minute left on your meter").
                setContentTitle("Park King Meter").setSmallIcon(R.drawable.ic_stat_name).build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        // nm.notify(0,notify);
        new CountDownTimer(time-60000, 1000) {
            public void onTick(long noteTime) {
                // nm.notify(0, notify);
            }

            public void onFinish() {
                nm.notify(0,notify);
            }
        }.start();
        new CountDownTimer(time, 1000) {
            public void onTick(long noteTime) {
                // nm.notify(0, notify);
            }

            public void onFinish() {
                Toast.makeText(Timer.this, "Timer End", Toast.LENGTH_SHORT).show();
                stop();
            }
        }.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    public void stop()
    {
        this.stopSelf();
    }
}