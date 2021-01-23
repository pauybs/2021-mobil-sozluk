package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.logging.Level;

public class Gecis extends AppCompatActivity {
    BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecis);

        //Locked 1 Açık veya Kapalı
        //Locked 2 Seçim
        //Locked 3 Kilit/Uygulamaya girme


        IntentFilter filt = new IntentFilter(Intent.ACTION_USER_PRESENT);
        //   filt.addAction(Intent.ACTION_SCREEN_ON);
        mReceiver = new UserPresentReciever();
        registerReceiver(mReceiver, filt);
        //tek tek okutuyoruz
        final SharedPreferences sharedPref = this.getSharedPreferences("take",Context.MODE_PRIVATE);
        boolean Locked = sharedPref.getBoolean("switch_kontrol", true);


        boolean Locked3 = sharedPref.getBoolean("GecisKontrol", false);


       // boolean Locked2 = sharedPref.getString("Options", "A1");

        boolean Locked10 = sharedPref.getBoolean("a1kontrol", true);
        boolean Locked11 = sharedPref.getBoolean("a2kontrol", false);
        boolean Locked12 = sharedPref.getBoolean("b1kontrol", false);

        if(Locked==true && Locked3==true ){

            if(Locked10==true){
                Intent intent=new Intent(getApplicationContext(), Level_A1.class);
                startActivity(intent);
            }
            else if(Locked11==true){

                Intent intent=new Intent(getApplicationContext(), Level_A2.class);
                startActivity(intent);
            }
            else if(Locked12==true){

                Intent intent=new Intent(getApplicationContext(), Level_A3.class);
                startActivity(intent);
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("GecisKontrol",false);
            editor.commit();

        }
      else{
            Intent intent=new Intent(this,Splasher.class);
            startActivity(intent);
        }

    }




    @Override
    protected void onDestroy(){ //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder

        super.onDestroy();


        unregisterReceiver(mReceiver);
    }

}
