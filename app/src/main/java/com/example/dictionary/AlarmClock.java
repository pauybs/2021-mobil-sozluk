package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.regex.Pattern;

public class AlarmClock extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    //Handler ve runnable gecikmeli çalışmasını sağlar
    TextView tv_userMail;

    private ImageView defaultPicture;

    String possibleEmail;
    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView teztext;
    TextView teztext2;

    TextView tc_textclock;
    Handler handle=null;
    Runnable runnable=null;
    String zaman;
    Button btn_ayarla;
    TextClock AlarmaSaati;
    private TimePickerDialog timePickerDialog;
    final static int islemKodu=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);

        tc_textclock=findViewById(R.id.tc_textclock);
        btn_ayarla=findViewById(R.id.btn_ayarla);
        btn_ayarla.setOnClickListener((View.OnClickListener) this);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;

                break;
            }
        }



        NavigationView navigationView=findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_userMail = (TextView) header.findViewById(R.id.tv_userMail);
        tv_userMail.setText(possibleEmail);




        toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);


        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        teztext=(TextView)findViewById(R.id.teztext);
        teztext2=(TextView)findViewById(R.id.teztext2);



        final SimpleDateFormat gosterim =new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");//Gün-Ay-Yıl # Saat-Dakika-Saniye
        zaman=gosterim.format(new Date());//new date ile tarih alındı belirlenen formata dönüştürüldü.

        tc_textclock.setText(zaman);

        handle=new Handler();

        runnable= new Runnable() {
            @Override
            public void run() {
                zaman=gosterim.format(new Date());//new date ile tarih alındı belirlenen formata dönüştürüldü.
                tc_textclock.setText(zaman);

                handle.postDelayed(runnable,1000);//1 saniye bekle.
            }
        };
        runnable.run();




    }

    @Override
    public void onClick(View v) {
       openPickerDialog(true);
    }

    private void openPickerDialog(boolean t){

        Calendar calendar=Calendar.getInstance();
        timePickerDialog=new TimePickerDialog(AlarmClock.this,R.style.themeOnverlay_timePicker,onTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),t);
        timePickerDialog.setTitle("");
        timePickerDialog.show();

    }
    TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

         Calendar calendarNow=Calendar.getInstance();
         Calendar calendarSet=(Calendar)calendarNow.clone();
         //saat ve tarih seçimi yapıldı.
         calendarSet.set(Calendar.HOUR_OF_DAY,hourOfDay);
         calendarSet.set(Calendar.MINUTE,minute);
         calendarSet.set(Calendar.SECOND,0);
         calendarSet.set(Calendar.MILLISECOND,0);

        if(calendarSet.compareTo(calendarNow)<=0)//eski bir tarihe ayarladığımızı kontrol ettik
            calendarSet.add(Calendar.DATE,1);

            setAlarm(calendarSet);
        }

    };

    private void setAlarm(Calendar alarmCalendar){

        Toast.makeText(getApplicationContext(),"Alarm Set!",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(),AlarmReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(getBaseContext(),islemKodu,intent,0);
        AlarmManager alarmManager=(AlarmManager)getSystemService(Context.ALARM_SERVICE);//servisi kullanıyoruz
        alarmManager.set(AlarmManager.RTC_WAKEUP,alarmCalendar.getTimeInMillis(),pendingIntent);//Kapalıyken de çalışmasını sağlıyor.


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(),Anasayfa.class);
                startActivity(intent);
                break;

            case R.id.nav_settings:
                Intent intent2 = new Intent(getApplicationContext(),Settings.class);
                startActivity(intent2);
                break;
            case R.id.nav_about:
                Intent intent3 = new Intent(getApplicationContext(),About.class);
                startActivity(intent3);
                break;
            case R.id.nav_share:
                Intent intent4 = new Intent(getApplicationContext(),Share.class);
                startActivity(intent4);
                break;




        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

}