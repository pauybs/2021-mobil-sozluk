package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Phrasal_Verbs extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    boolean checkKontrol=false;
    private DrawerLayout drawer;
    Toolbar toolbar;
    TextView teztext;
    TextView teztext2;
    static boolean activephrasal;
    public boolean stopTimer=false;
    TextToSpeech textToSpeech;
    ImageButton imgbtn_listen;
    SeekBar sb_speed;
    SeekBar sb_pitch;
    TextView tv_soru;
    ArrayList<Integer>kontrolArrayList =new ArrayList<>();
    ProgressBar progressBar ;
    RadioGroup radioGroup;
    Button btn_next;
    ImageButton btn_home;
    boolean timesupkontrol=false;
    boolean resetkontrol=false;
    int m=0;
    TextView tv_timer;
    CountDownTimer countDownTimer;
    long timeLeftMiliSeconds;
    boolean timerRuning;
    int sayac=0;
    BroadcastReceiver mReceiver;
    RelativeLayout relative_dur;
    ScoreboardGetir scoreboardGetir;
   /* public void resetTimer() {
        timeLeftMiliSeconds=0;
        updateTimer();
        startTimer();


    }*/

   /* public void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftMiliSeconds,1000) {
            @Override
            public void onTick(long l) {
                tv_timer.setVisibility(View.VISIBLE);
                /*if(sayac%2==0 && sayac!=0){
                    timeLeftMiliSeconds=6000;

                }*/
                //else{
           /*     timeLeftMiliSeconds=l;
                //}
                updateTimer();
            }*/



           /* @Override
            public void onFinish() {
                // TODO: restart counter
                //   cancel();  // there is no need the call the cancel() method here
                timerRuning=false;
                //  timeLeftMiliSeconds = 0;


                //this.start();


                tv_timer.setText("1");


            }
        }.start();

        timerRuning=true;
    }*/

   /* public void stopTimer(){
        countDownTimer.cancel();
        timerRuning=false;

    }*/
    /*public void updateTimer(){

        int secs=(int)timeLeftMiliSeconds/1000;

        String gidicekzaman=""+secs;
        tv_timer.setText(gidicekzaman);

        if(Integer.parseInt(gidicekzaman)==1 && timesupkontrol==false){
            timesupkontrol=true;
            resetTimer();
            Toasty.warning(getApplicationContext(), "Time's UP").show();
            Toasty.info(getApplicationContext(), dogruCevap, Toast.LENGTH_LONG).show();
            btn_next.setText("NEXT");

        }

        if(resetkontrol==true){
            startTimer();


        }


    }*/
   Timer countdownTimer;
   int startCountdown = 10100;
    int currentCountdown;
    Handler countdownHandler = new Handler();

    public void startCountdownTimer() {
        countDownTimer=new CountDownTimer(timeLeftMiliSeconds,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMiliSeconds=millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftMiliSeconds=0;
                updateCountDownText();
            }
        }.start();

    }
    public void updateCountDownText(){
        int saniye=(int)(timeLeftMiliSeconds/1000);
        if(saniye!=0){
            tv_timer.setText(String.valueOf(saniye));
        }
        else{
            btn_next.setText("NEXT");
            Toasty.warning(getApplicationContext(), "Time's UP",1000).show();
            Toasty.info(getApplicationContext(), dogruCevap, 1000).show();
            relative_dur.setVisibility(View.INVISIBLE);
        }




    }


    TextView tv_userMail;

    private ImageView defaultPicture;

    String possibleEmail;


    String dogruCevap;
    Map<String, String> sozluk = new HashMap<String, String>();
    Map<String, String> sozlukiki = new HashMap<String, String>();
    final ArrayList gelenveri= new ArrayList();
    final DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Getir5");
    int baslangicScore;
    String baslangicMail;
    String baslangicNickname;
    DatabaseReference dbreference2 = FirebaseDatabase.getInstance().getReference("Getir6");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrasal__verbs);

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;

                break;
            }
        }


        progressBar=(ProgressBar) findViewById(R.id.progressBar);

        relative_dur=(RelativeLayout)findViewById(R.id.relative_dur);
        relative_dur.setVisibility(View.VISIBLE);
        //Custom Toast

        sb_pitch=findViewById(R.id.sb_pitch);
        sb_speed=findViewById(R.id.sb_speed);
        imgbtn_listen=findViewById(R.id.imgbtn_listen);

        tv_soru = (TextView) findViewById(R.id.tv_soru);


        NavigationView navigationView=findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tv_userMail = (TextView) header.findViewById(R.id.tv_userMail);
        tv_userMail.setText(possibleEmail);

        toolbar= (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.drawer_layout);


        navigationView.setNavigationItemSelectedListener(this);

        radioGroup = findViewById(R.id.rdGroup);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        teztext=(TextView)findViewById(R.id.teztext);
        teztext2=(TextView)findViewById(R.id.teztext2);


        final RadioButton rbtn_a = (RadioButton) findViewById(R.id.rbtn_a);
        final RadioButton rbtn_b = (RadioButton) findViewById(R.id.rbtn_b);
        final RadioButton rbtn_c = (RadioButton) findViewById(R.id.rbtn_c);
        final RadioButton rbtn_d = (RadioButton) findViewById(R.id.rbtn_d);

        btn_next = (Button) findViewById(R.id.btn_next);


        tv_timer =(TextView)findViewById(R.id.tv_timer);

        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        imgbtn_listen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Speak();

            }
        });
        btn_next.setText("CHECK");
        dbreference2.orderByChild("mail").equalTo(possibleEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dbreference2=FirebaseDatabase.getInstance().getReference("Getir6").child(possibleEmail.replace(".","").replace("$","").replace("#","").replace("[","").replace("]",""));

                for(DataSnapshot verigetir2 : dataSnapshot.getChildren())
                {

                    baslangicScore=Integer.parseInt(verigetir2.child("score").getValue().toString());
                    baslangicMail=verigetir2.child("mail").getValue().toString();
                    baslangicNickname=verigetir2.child("nickname").getValue().toString();
                    scoreboardGetir=new ScoreboardGetir(baslangicMail,baslangicNickname,baslangicScore);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbreference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //startCountdownTimer();
                relative_dur.setVisibility(View.VISIBLE);
                long childrenCount = dataSnapshot.getChildrenCount();
                int count = (int) childrenCount;

                for(DataSnapshot verigetir : dataSnapshot.getChildren())
                {
                    Getir getir = new Getir(
                            verigetir.child("kelime").getValue().toString(),
                            verigetir.child("anlam").getValue().toString()
                    );
                    gelenveri.add(getir.getKelime() + "-"
                            + getir.getAnlam()
                    );
                }
                timeLeftMiliSeconds=startCountdown;
                int randomNumberCevap=1+new Random().nextInt(4);//hangi şıkka koyulacak

                dogruCevap=" ";
                int randomNumber ;
                randomNumber = new Random().nextInt(count);//kayıt sayısı ile 1 arasında sayı üretildi
                String []veriayir=gelenveri.get(randomNumber).toString().split("-");


                while (m<5){


                    if(m==0){
                        tv_soru.setText(veriayir[0]);//soru olarak yazıldı
                        dogruCevap=veriayir[1];//doğru cevabı tuttuğumuz değişken
                        m++;

                    }


                    if(m==1){//buton boş ise

                        if(m==randomNumberCevap){//random cevabın indexi ile m karşılaştırılır
                            rbtn_a.setText(dogruCevap);
                        }
                        else{
                            int m1=new Random().nextInt(count);
                            String []array =gelenveri.get(m1).toString().split("-");
                            while (array [1]==gelenveri.get(randomNumber).toString().split("-")[1] && !kontrolArrayList.contains(m1)){//iki defa doğru cevabı yazdrımamızı engelliyor
                                m1=new Random().nextInt(count);
                                array=gelenveri.get(m1).toString().split("-");
                            }
                            kontrolArrayList.add(m1);
                            rbtn_a.setText(array[1]);
                        }
                        m++;
                    }
                    else if(m==2){
                        if(m==randomNumberCevap){
                            rbtn_b.setText(dogruCevap);
                        }
                        else{
                            int m2=new Random().nextInt(count);
                            String []array =gelenveri.get(m2).toString().split("-");
                            while (array [1]==gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m2)){
                                m2=new Random().nextInt(count);
                                array=gelenveri.get(m2).toString().split("-");
                            }
                            rbtn_b.setText(array[1]);
                            kontrolArrayList.add(m2);
                        }
                        m++;
                    }
                    else if(m==3 ){
                        if(m==randomNumberCevap){
                            rbtn_c.setText(dogruCevap);
                        }
                        else{
                            int m3=new Random().nextInt(count);
                            String []array =gelenveri.get(m3).toString().split("-");

                            while (array [1]==gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m3)){
                                m3=new Random().nextInt(count);
                                array=gelenveri.get(m3).toString().split("-");
                            }
                            rbtn_c.setText(array[1]);
                            kontrolArrayList.add(m3);

                        }
                        m++;
                    }
                    else if (m==4){
                        if(m==randomNumberCevap){
                            rbtn_d.setText(dogruCevap);
                        }
                        else{
                            int m4=new Random().nextInt(count);
                            String []array =gelenveri.get(m4).toString().split("-");
                            while (array [1]==gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m4)){
                                m4=new Random().nextInt(count);
                                array=gelenveri.get(m4).toString().split("-");
                            }
                            rbtn_d.setText(array[1]);
                            kontrolArrayList.add(m4);

                        }
                        m++;
                    }


                }
                timeLeftMiliSeconds=startCountdown;
                startCountdownTimer();

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"çalışmaz",Toast.LENGTH_LONG).show();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btn_next.getText() == "CHECK") {
                    relative_dur.setVisibility(View.INVISIBLE);
                    sayac++;
                    stopTimer=true;
                    checkKontrol=true;

                    ///////Şıkların doğruluğu kontrol ediliyor 32896


                    //   while (tv_timer.getText()!="1"){

                    if (rbtn_a.isChecked()) {
                        if (rbtn_a.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+4;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();

                        } else {
                            Toasty.error(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                            Toasty.info(getApplicationContext(), dogruCevap, Toast.LENGTH_LONG).show();


                        }

                      //  rbtn_a.setChecked(false);
                    } else if (rbtn_b.isChecked()) {
                        if (rbtn_b.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+4;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();

                        } else {
                            Toasty.error(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                            Toasty.info(getApplicationContext(), dogruCevap, Toast.LENGTH_LONG).show();

                        }

                       // rbtn_b.setChecked(false);
                    } else if (rbtn_c.isChecked()) {
                        if (rbtn_c.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+4;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();

                        } else {
                            Toasty.error(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                            Toasty.info(getApplicationContext(), dogruCevap, Toast.LENGTH_LONG).show();

                        }

                      //  rbtn_c.setChecked(false);
                    } else if (rbtn_d.isChecked()) {
                        if (rbtn_d.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+4;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();
                        } else {
                            Toasty.error(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                            Toasty.info(getApplicationContext(), dogruCevap, Toast.LENGTH_LONG).show();

                        }


                      //  rbtn_d.setChecked(false);
                    } else {
                        /*btn_next.setText("NEXT");
                        Toasty.warning(getApplicationContext(), "Time's UP",1000).show();
                        Toasty.info(getApplicationContext(), dogruCevap, 1000).show();
                        relative_dur.setVisibility(View.INVISIBLE);*/
                    }
                    countDownTimer.cancel();
                    btn_next.setText("NEXT");


                    /*rbtn_a.setChecked(false);
                    rbtn_b.setChecked(false);
                    rbtn_c.setChecked(false);
                    rbtn_d.setChecked(false);*/


                }
                else{

                    kontrolArrayList.clear();
                    radioGroup.clearCheck();
                    sayac++;
                    relative_dur.setVisibility(View.VISIBLE);

                  /*  checkKontrol=false;
                    stopTimer=false;*/
                 //   startCountdownTimer();
                    timeLeftMiliSeconds=startCountdown;

                    //     countDownTimer.cancel();



                    /* int randomNumberCevap=1+new Random().nextInt(4);//hangi şıkka koyulacak

                dogruCevap=" ";
                int randomNumber ;
                randomNumber = new Random().nextInt(count);//kayıt sayısı ile 1 arasında sayı üretildi
                String []veriayir=gelenveri.get(randomNumber).toString().split("-");*/


                    int randomNumberCevap=1+new Random().nextInt(4);//hangi şıkka koyulacak
                    int boyut=gelenveri.size();
                    dogruCevap=" ";
                    int randomNumber ;
                    randomNumber = new Random().nextInt(boyut);//kayıt sayısı ile 1 arasında sayı üretildi
                    String []veriayir=gelenveri.get(randomNumber).toString().split("-");

                    m=0;


                    while (m<5) {



                        if (m == 0) {
                            tv_soru.setText(veriayir[0]);//soru olarak yazıldı
                            dogruCevap = veriayir[1];//doğru cevabı tuttuğumuz değişken
                            m++;

                        }


                        if (m == 1) {//buton boş ise

                            if (m == randomNumberCevap) {//random cevabın indexi ile m karşılaştırılır
                                rbtn_a.setText(dogruCevap);
                            } else {
                                int m1=new Random().nextInt(boyut);
                                String[] array = gelenveri.get(m1).toString().split("-");
                                while (array[1] == gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m1)) {//iki defa doğru cevabı yazdrımamızı engelliyor
                                    m1=new Random().nextInt(boyut);
                                    array = gelenveri.get(m1).toString().split("-");
                                }

                                kontrolArrayList.add(m1);
                                rbtn_a.setText(array[1]);
                            }
                            m++;
                        } else if (m == 2) {
                            if (m == randomNumberCevap) {
                                rbtn_b.setText(dogruCevap);
                            } else {
                                int m2=new Random().nextInt(boyut);
                                String[] array = gelenveri.get(m2).toString().split("-");
                                while (array[1] == gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m2)) {
                                    m2=new Random().nextInt(boyut);
                                    array = gelenveri.get(m2).toString().split("-");
                                }
                                kontrolArrayList.add(m2);
                                rbtn_b.setText(array[1]);

                            }
                            m++;
                        } else if (m == 3) {
                            if (m == randomNumberCevap) {
                                rbtn_c.setText(dogruCevap);
                            } else {
                                int m3=new Random().nextInt(boyut);
                                String[] array = gelenveri.get(m3).toString().split("-");
                                while (array[1] == gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m3)) {
                                    m3=new Random().nextInt(boyut);
                                    array = gelenveri.get(m3).toString().split("-");
                                }
                                kontrolArrayList.add(m3);
                                rbtn_c.setText(array[1]);

                            }
                            m++;
                        } else if (m == 4) {
                            if (m == randomNumberCevap) {
                                rbtn_d.setText(dogruCevap);
                            } else {
                                int m4=new Random().nextInt(boyut);
                                String[] array = gelenveri.get(m4).toString().split("-");
                                while (array[1] == gelenveri.get(randomNumber).toString().split("-")[1]&& !kontrolArrayList.contains(m4)) {
                                    m4=new Random().nextInt(boyut);
                                    array = gelenveri.get(m4).toString().split("-");
                                }
                                kontrolArrayList.add(m4);
                                rbtn_d.setText(array[1]);
                            }
                            m++;
                        }

                    }


                    startCountdownTimer();
                    btn_next.setText("CHECK");

                }








                /* Thread zaman=new Thread(){
                    public void run(){
                        try {
                            sleep(7800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.i("tago","giriş ekranı çalışmadı");//Log ile sadece geliştirme aşamasında bizim görebileceğimiz mesajlar alabiliyoruz
                        }finally {
                            Intent i =new Intent(Level_A1.this,Progress.class);
                            startActivity(i);
                        }
                    }

                };//işlem class
                zaman.start();//Thread çalıştı manifestte gerekli değişiklikler yapıldı*/

                /*while ()
                try {
                    // to sleep 10 seconds
                    Thread.sleep(7800);

                } catch (InterruptedException e) {
                    // recommended because catching InterruptedException clears interrupt flag
                    Thread.currentThread().interrupt();
                    // you probably want to quit if the thread is interrupted
                    return;
                }*/




            }
        });


    }
    @Override
    protected void onDestroy(){ //Activity Kapatıldığı zaman receiver durduralacak.Uygulama arka plana alındığı zamanda receiver çalışmaya devam eder

        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        super.onDestroy();
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        try {

            unregisterReceiver(mReceiver);

        } catch(IllegalArgumentException e) {

            e.printStackTrace();
        }

    }
    private void Speak() {
        String kelimeal= tv_soru.getText().toString();
        float pitch=(float)sb_pitch.getProgress()/50;
        if(pitch<0.1) {
            pitch=0.1f;
        }
        float speed=(float)sb_speed.getProgress()/50;
        if(speed<0.1) {
            speed=0.1f;
        }
        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);
        textToSpeech.speak(kelimeal,TextToSpeech.QUEUE_FLUSH,null);




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

    @Override
    public void onStart() {
        super.onStart();
        activephrasal = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        activephrasal = false;
    }

}