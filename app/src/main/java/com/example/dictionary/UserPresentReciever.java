package com.example.dictionary;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;




public class UserPresentReciever extends BroadcastReceiver {

    SharedPreferences sharedPref;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"hell") ;
//lock screen e basıldığında secilen uygulamaya geçmeyi sağlar
        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage("com.example.dictionary");
         sharedPref = context.getSharedPreferences("take",Context.MODE_PRIVATE);

        //kilit ekranınından açmamızı sağlıyor
        boolean yourLocked = sharedPref.getBoolean("switch_kontrol", true);
        boolean yourLocked2 = sharedPref.getBoolean("GecisKontrol", true);


        //uygulama otomatik başlıyor
        if (launchIntent != null && yourLocked==true) {
          context.startActivity(launchIntent);//null pointer check in case package name was not found

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("GecisKontrol",true);
            editor.commit();


        }

    }



}
