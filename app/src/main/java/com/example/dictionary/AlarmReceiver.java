package com.example.dictionary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;
import android.content.Intent;
public  class AlarmReceiver extends BroadcastReceiver {
    static Ringtone ringtone;
     static boolean alarmdurumu=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Good Morning , Time To Get Up!",Toast.LENGTH_LONG).show();

        Uri melodi= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if(melodi==null){
            melodi=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone=RingtoneManager.getRingtone(context,melodi);
        ringtone.play();




        Intent scheduledIntent = new Intent(context, VoiceToText.class);
        scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(scheduledIntent);







    }




}
