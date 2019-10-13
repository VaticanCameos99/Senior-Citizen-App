package com.example.SeniorCitizenCare;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

public class AlarmReceiver extends  BroadcastReceiver {
    @Override
    public void onReceive(Context context , Intent intent) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(notification == null) {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone r = RingtoneManager.getRingtone(context ,  notification);
        r.play();
        Toast.makeText(context , "It's  time for medicine!", Toast.LENGTH_LONG).show();
    }
}