package com.example.SeniorCitizenCare;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends  BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context , Intent intent) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(context ,  notification);
        r.play();
        String s = intent.getStringExtra("Medicine Name");
   //     Toast.makeText(context , "It's  time for medicine! Remember to take: " + s, Toast.LENGTH_LONG).show();
//        channel.setDescription("Reminder for medicine");
//        NotificationChannel channel = new NotificationChannel("channel_id" , "Alarm Notification" , NotificationManager.IMPORTANCE_HIGH);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context ,  "channel_id")
//                .setSmallIcon(R.drawable.applogo)
//                .setContentTitle("It's  time for medicine!")
//                .setContentText(intent.getStringExtra("Medicine Name"))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setAutoCancel(true);
    }
}