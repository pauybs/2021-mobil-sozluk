package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.regex.Pattern;

import static android.app.PendingIntent.getActivity;

public class Settings extends AppCompatActivity   implements  NavigationView.OnNavigationItemSelectedListener{
   Switch sw_switch;
   Spinner sp_spinner;
    boolean isSpinnerTouched;
    SharedPreferences sharedPref;

    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView teztext;
    TextView teztext2;
    TextView tv_userMail;

    private ImageView defaultPicture;

    String possibleEmail;
    // boolean switchBayrak=true;
  BroadcastReceiver mReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);



        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;

                break;
            }
        }


        sp_spinner = (Spinner) findViewById(R.id.sp_spinner);
        sw_switch = (Switch) findViewById(R.id.sw_switch);

        IntentFilter filt = new IntentFilter(Intent.ACTION_USER_PRESENT);
        //   filt.addAction(Intent.ACTION_SCREEN_ON);
        mReceiver = new UserPresentReciever();
        registerReceiver(mReceiver, filt);





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



     sharedPref = this.getSharedPreferences("take",Context.MODE_PRIVATE);


        boolean yourLocked = sharedPref.getBoolean("switch_kontrol", true);
        final boolean a1kontrol = sharedPref.getBoolean("a1kontrol", true);
        final boolean a2kontrol= sharedPref.getBoolean("a2kontrol", false);
        final boolean b1kontrol = sharedPref.getBoolean("b1kontrol", false);
        sw_switch.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGenel));

        if(yourLocked==true){
            sw_switch.setChecked(true);

        }
        else{
            sw_switch.setChecked(false);
        }

        String[] plants = new String[]{
                "A1",
                "A2",
                "B1"
        };

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,plants
        );

        int spinnerPosition;









        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        sp_spinner.setAdapter(spinnerArrayAdapter);

        if(a1kontrol==true){

            spinnerPosition = spinnerArrayAdapter.getPosition("A1");

            sp_spinner.setSelection(spinnerPosition,false);

        }
        else if(a2kontrol==true){
            spinnerPosition = spinnerArrayAdapter.getPosition("A2");

            sp_spinner.setSelection(spinnerPosition,false);

        }
        else if(b1kontrol==true){
            spinnerPosition = spinnerArrayAdapter.getPosition("B1");

            sp_spinner.setSelection(spinnerPosition,false);

        }


        Switch_Change(sharedPref);


         isSpinnerTouched = false;

        sp_spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isSpinnerTouched = true;
                return false;
            }
        });



        sp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (!isSpinnerTouched) return;

                String s=sp_spinner.getSelectedItem().toString();
                Toast.makeText(getApplicationContext(),"S="+s,Toast.LENGTH_LONG).show();

            //    String Locked2 = sharedPref2.getString("LEVEL", "A1");



                if(s.equals("A1")){

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("a1kontrol",true);
                    editor.putBoolean("a2kontrol",false);
                    editor.putBoolean("b1kontrol",false);
                    editor.apply();


                }
                else if(s.equals("A2")){
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putBoolean("a2kontrol",true);
                    editor.putBoolean("a1kontrol",false);
                    editor.putBoolean("b1kontrol",false);
                    editor.apply();

                }
                else if(s.equals("B1")){
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("b1kontrol",true);
                    editor.putBoolean("a2kontrol",false);
                    editor.putBoolean("a1kontrol",false);
                    editor.apply();

                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("Options",s);
                editor.apply();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                //SharedPreferences.Editor editor = sharedPref.edit();

                 //   editor.putString("LEVEL","A1");
                  //  editor.apply();




            }
        });






    }







    public void Switch_Change(final SharedPreferences sharedPref) {
        sw_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

             //   boolean yourLocked = sharedPref.getBoolean("switch_kontrol", true);



                if (sw_switch.isChecked()) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("switch_kontrol",true);
                    editor.apply();


                    sp_spinner.setEnabled(true);



                } else {



                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean("switch_kontrol",false);
                    editor.apply();
                    boolean yourLocked = sharedPref.getBoolean("switch_kontrol", true);


                    sp_spinner.setEnabled(false);



                }
            }
        });

    }

    @Override
    protected void onDestroy(){ //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder




        super.onDestroy();


    /*    String s=sp_spinner.getSelectedItem().toString();
        Toast.makeText(getApplicationContext(),"S="+s,Toast.LENGTH_LONG).show();

        //    String Locked2 = sharedPref2.getString("LEVEL", "A1");

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Options",s);
        editor.apply();*/


        unregisterReceiver(mReceiver);
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