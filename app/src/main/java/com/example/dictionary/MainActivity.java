package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView teztext;
    TextView teztext2;
    BroadcastReceiver mReceiver;
    TextView tv_userMail;

    private ImageView defaultPicture;

    String possibleEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;

                break;
            }
        }

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

        Button btn_a1=(Button)findViewById(R.id.btn_a1);
        Button btn_a2=(Button)findViewById(R.id.btn_a2);
        Button btn_b1=(Button)findViewById(R.id.btn_b1);


        String a1Text="<big> <font color='#d4fefe'>"
                + "A1" + "</font> </big>" + "<br />"
                + "<small> <font color='#d4fefe'>" + "Beginner" + "</small>";;

        btn_a1.setText(Html.fromHtml(a1Text));
        String a2Text="<big> <font color='#d4fefe'>"
                + "A2" + "</font> </big>" + "<br />"
                + "<small> <font color='#d4fefe'>" + "Elementary" + "</small>";;

        btn_a2.setText(Html.fromHtml(a2Text));
        String b1Text="<big> <font color='#d4fefe'>"
                + "B1" + "</font> </big>" + "<br />"
                + "<small> <font color='#d4fefe'>" + "Pre-Intermediate" + "</small>";;

        btn_b1.setText(Html.fromHtml(b1Text));

        btn_a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secimgecis=new Intent(getApplicationContext(),Level_A1.class);
                startActivity(secimgecis);
            }
        });
        btn_a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secimgecis=new Intent(getApplicationContext(),Level_A2.class);
                startActivity(secimgecis);

            }
        });
        btn_b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent secimgecis=new Intent(getApplicationContext(),Level_A3.class);
                startActivity(secimgecis);
            }
        });



    }



    @Override
    protected void onDestroy(){ //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder

        super.onDestroy();


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
