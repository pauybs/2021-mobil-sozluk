package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.appcompat.widget.Toolbar;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;

import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.LeaderboardsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.regex.Pattern;

public class Anasayfa extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{
    TextView tv_userMail;

    private ImageView defaultPicture;
    private DrawerLayout drawer;
    String possibleEmail;
    BroadcastReceiver mReceiver;
    private static final String LOG_TAG = "Otomatik internet Kontrol¸";
    private NetworkChangeReceiver receiver;
    String nickname;



//


   // private boolean greetingDisplayed;


//postRef = ref.child("BusNumber");
 DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference().child("Getir6");

 Toolbar toolbar;
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        //greetingDisplayed=false;
       /* AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        initialize(new GdxGame(this), config);*/


        /*GoogleSignInClient user;

        user = GoogleSignIn.getClient(this,
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).build());
        user.silentSignIn();*/

        defaultPicture=findViewById(R.id.defaultPicture);



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


        dbreference.orderByChild("mail").equalTo(possibleEmail).addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){


                    }
                    else{
                       Toast.makeText(getApplicationContext(), "ccccccccccccccccccccccc", Toast.LENGTH_SHORT).show();
                        ScoreboardGetir scoreboardGetir=new ScoreboardGetir(possibleEmail,getAlphaNumericString(7),0);
                      dbreference.child(possibleEmail.replace(".","").replace("$","").replace("#","").replace("[","").replace("]","")).setValue(scoreboardGetir);
                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });





    /*   String gelenMail=mailGetir.getEmail(this);

       tv_userMail.setText(gelenMail);*/



         toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);


        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);

      //  GoogleApiAvailability.makeGooglePlayServicesAvailable(this.getApplicationContext());
       /* boolean baglantivarmi= isGooglePlayServicesAvailable(getApplicationContext());
        Toast.makeText(getApplicationContext(),"Sonuc: "+baglantivarmi,Toast.LENGTH_LONG).show();*/
        final Button btn_start=(Button)findViewById(R.id.btn_start);

        final Button btn_alarm=(Button)findViewById(R.id.btn_alarm);

        final Button btn_similarTwoWords=(Button)findViewById(R.id.btn_similarTwoWords);
        final Button btn_phrasal=(Button)findViewById(R.id.btn_phrasal);
//shared preferences okuyoruz

        final SharedPreferences sharedPref  = this.getSharedPreferences("take", Context.MODE_PRIVATE);
        boolean yourLocked = sharedPref.getBoolean("switch_kontrol", true);


        final SharedPreferences sharedPref2 = this.getSharedPreferences("take2", Context.MODE_PRIVATE);
        boolean yourLocked2 = sharedPref.getBoolean("switch_kontrol", false);







        String quizText="<big> <font color='#d4fefe'>"
                + "QUIZ" + "</font> </big>" + "<br />"
                + "<small>" + "Select your level and start" + "</small>";;

        btn_start.setText(Html.fromHtml(quizText));

        String similarText="<big> <font color='#d4fefe'>"
                + "TWO WORDS" + "</font> </big>" + "<br />"
                + "<small>" + "Find the right one from two words that look alike" + "</small>";;

        btn_similarTwoWords.setText(Html.fromHtml(similarText));


        String phrasalText="<big> <font color='#d4fefe'>"
                + "PHRASAL VERBS" + "</font> </big>" + "<br />"
                + "<small> " + "Find the meaning of phrasal verb" + "</small>";;

        btn_phrasal.setText(Html.fromHtml(phrasalText));

        String alarmText="<big> <font color='#d4fefe'>"
                + "ALARM" + "</font> </big>" + "<br />"
                + "<small>" + "Turn off the alarm with your voice when setup sounds" + "</small>";;

        btn_alarm.setText(Html.fromHtml(alarmText));


        //registerlama işlemini yapıyoruz
        IntentFilter filt = new IntentFilter(Intent.ACTION_USER_PRESENT);
      //   filt.addAction(Intent.ACTION_SCREEN_ON);
         mReceiver = new UserPresentReciever();
        registerReceiver(mReceiver, filt);





        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btn_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),AlarmClock.class);
                startActivity(intent);
            }
        });




        btn_similarTwoWords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SimilarTwoWords.class);
                startActivity(intent);
            }
        });

        btn_phrasal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),Phrasal_Verbs.class);
                startActivity(intent);



            }
        });






    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_home:
                Intent intent = new Intent(getApplicationContext(),Anasayfa.class);
                startActivity(intent);
                break;
            case R.id.nav_scoreboard:
                Intent intent5=new Intent(getApplicationContext(),Scoreboard.class);
                startActivity(intent5);
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
    ////new

    static String getAlphaNumericString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return "User"+"-"+sb.toString();
    }


    @Override
    protected void onDestroy(){ //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder

        super.onDestroy();

        unregisterReceiver(receiver);//receiver durduruluyor
        unregisterReceiver(mReceiver);
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

    @Override
    protected void onResume() {
        super.onResume();
      //  signInSilently();
    }
    /*public boolean isGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }
*/


}

