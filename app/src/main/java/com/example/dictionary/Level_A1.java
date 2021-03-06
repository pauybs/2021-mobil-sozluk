package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;

public class Level_A1 extends AppCompatActivity  implements  NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;
    int firstClick=0;
    Toolbar toolbar;
    TextView teztext;
    TextView teztext2;
    static boolean activeA1;
    RadioGroup radioGroup;
    public boolean stopTimer=false;
    Timer countdownTimer;
    ArrayList<Integer>kontrolArrayList =new ArrayList<>();
    TextToSpeech textToSpeech;
    ImageButton imgbtn_listen;
    SeekBar sb_speed;
    SeekBar sb_pitch;
    TextView tv_soru;
    ProgressBar progressBar;
    int progressbarDurum=0;
    Button btn_next;
    ScoreboardGetir scoreboardGetir;
    int m=0;
    TextView tv_timer;
    CountDownTimer countDownTimer;
    long timeLeftMiliSeconds;
    int sayac=0;
    BroadcastReceiver mReceiver;
    RelativeLayout relative_dur;
    boolean checkKontrol=false;
    int startCountdown = 10100;
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
    final ArrayList gelenveri= new ArrayList();
    int baslangicScore;
    String baslangicMail;
    String baslangicNickname;
    DatabaseReference dbreference2 = FirebaseDatabase.getInstance().getReference("Getir6");
    final DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Getir");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level__a1);
       // startStop();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
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

        sb_pitch=findViewById(R.id.sb_pitch);
        sb_speed=findViewById(R.id.sb_speed);
        imgbtn_listen=findViewById(R.id.imgbtn_listen);

        radioGroup = findViewById(R.id.rdGroup);

        FirebaseAuth firebaseAuth;

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser account = firebaseAuth.getCurrentUser();
        if (account != null){
            String personImage = Objects.requireNonNull(account.getPhotoUrl()).toString();
            ImageView userImage = findViewById(R.id.defaultPicture);
            Glide.with(this).load(personImage).into(userImage);
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





        IntentFilter filt = new IntentFilter(Intent.ACTION_USER_PRESENT);
        //   filt.addAction(Intent.ACTION_SCREEN_ON);
        mReceiver = new UserPresentReciever();
        registerReceiver(mReceiver, filt);


         tv_soru = (TextView) findViewById(R.id.tv_soru);

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

                 dbreference2=FirebaseDatabase.getInstance().getReference("Getir6").
                         child(possibleEmail.replace(".","")
                                 .replace("$","").replace("#","")
                                 .replace("[","").replace("]",""));

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
               // startCountdownTimer();
               firstClick++;
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
                            while (array [1]==gelenveri.get(randomNumber).toString().split("-")[1] &&
                                    !kontrolArrayList.contains(m1)){//iki defa doğru cevabı yazdrımamızı engelliyor
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
                  if (rbtn_a.isChecked()) {

                        if (rbtn_a.getText() == dogruCevap) {

                            baslangicScore=baslangicScore+1;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",1000).show();

                        } else {
                          Toasty.error(getApplicationContext(), "Wrong Answer", 1000).show();
                          Toasty.info(getApplicationContext(), dogruCevap, 1000).show();
                        }

                      //  rbtn_a.setChecked(false);
                    } else if (rbtn_b.isChecked()) {
                        if (rbtn_b.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+1;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",1000).show();



                        } else {
                           Toasty.error(getApplicationContext(), "Wrong Answer", 1000).show();
                        Toasty.info(getApplicationContext(), dogruCevap,1000).show();

                        }

                        //rbtn_b.setChecked(false);
                    } else if (rbtn_c.isChecked()) {
                        if (rbtn_c.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+1;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",1000).show();


                        } else {
                           Toasty.error(getApplicationContext(), "Wrong Answer",1000).show();
                          Toasty.info(getApplicationContext(), dogruCevap, 1000).show();

                        }

                       // rbtn_c.setChecked(false);
                    } else if (rbtn_d.isChecked()) {
                        if (rbtn_d.getText() == dogruCevap) {
                            baslangicScore=baslangicScore+1;
                            Map<String, Object> updates = new HashMap<String,Object>();
                            updates.put("mail",baslangicMail);
                            updates.put("nickname",baslangicNickname);
                            updates.put("score",baslangicScore);
                            dbreference2.updateChildren(updates);
                            Toasty.success(getApplicationContext(),"Correct",1000).show();
                            rbtn_d.setChecked(false);
                        } else {
                           Toasty.error(getApplicationContext(), "Wrong Answer",1000).show();
                           Toasty.info(getApplicationContext(), dogruCevap, 1000).show();

                        }

                       // rbtn_d.setChecked(false);
                    } else {
                      /*  btn_next.setText("NEXT");
                        Toasty.warning(getApplicationContext(), "Time's UP",1000).show();
                        Toasty.info(getApplicationContext(), dogruCevap, 1000).show();
                        relative_dur.setVisibility(View.INVISIBLE);*/
                    }
                    countDownTimer.cancel();
                    btn_next.setText("NEXT");
            }
                else{

                    kontrolArrayList.clear();


                radioGroup.clearCheck();

                    sayac++;
                    relative_dur.setVisibility(View.VISIBLE);

                    timeLeftMiliSeconds=startCountdown;




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


      //  new veriAl().execute();
        //  geciciVeriTut = tv_sonuc.getText().toString();


        // Map<String, String> sozluk = new HashMap<String, String>();

      /*  String[] parcalanan = geciciVeriTut.replaceAll(": ", ":").split(":?");


        for (int i = 0; i < parcalanan.length; i += 2) {
            sozluk.put(parcalanan[i], parcalanan[i + 1]);


        }
        /*
        Map<String, String> map = new HashMap<String, String>();
        String test = "pet:cat::car:honda::location:Japan::food:sushi";

        // split on ':' and on '::'
        String[] parts = test.split("::?");

        for (int i = 0; i < parts.length; i += 2) {
            map.put(parts[i], parts[i + 1]);
        }

        for (String s : map.keySet()) {
            System.out.println(s + " is " + map.get(s));
        }*/







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

 /*   public void YazdirCorrect(){

        LayoutInflater inflater = getLayoutInflater();
        View layout=inflater.inflate(R.layout.toast_layout,(ViewGroup)findViewById(R.id.lyt_toast));

        TextView tv_correct=layout.findViewById(R.id.tv_correct);
        ImageView img_correct=layout.findViewById(R.id.img_correct);

        tv_correct.setText("Correct");
        img_correct.setImageResource(R.drawable.ic_check_circle_black_24dp);

        Toast toast =new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        toast.show();

    }*/
   /* public void bekle()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 5000);



    }*/



  /*  private class veriAl extends AsyncTask<Void,Void,Void>
    {
        String URL="https://www.memrise.com/course/1015967/ingilizcedeki-en-onemli-1250-kelime/1/";
        //final  ArrayList gelenVeri=new ArrayList();
        String veri;
        ProgressDialog dialog;
        @Override
        protected  void onPreExecute()
        {
            super.onPreExecute();
            dialog=new ProgressDialog(Level_A1.this);
            dialog.setTitle("Jsoup Uygulama.");
            dialog.setMessage("Veri getiriliyor");
            dialog.setIndeterminate(false);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc=  Jsoup.connect(URL).get();//Siteye bağlantı sağlanıyor.
                Elements elements;
                Elements element;

                for (int i=4;i<=253;i++){
                     elements=doc.select("div > div.things.clearfix > div:nth-child("+i+") > div.col_a.col.text > div");
                     element=doc.select("div > div.things.clearfix > div:nth-child("+i+") > div.col_b.col.text > div");
                    veri=elements.html();//istenilen html taglarını çeker.
                    soru=Jsoup.parse(veri).text();
                    veri=element.html();
                    cevap=Jsoup.parse(veri).text().toString();//html taglarını texte çevirir.
                    sozlukiki.put(soru,cevap);

                    String id = dbreference.push().getKey();

                    Getir getira1= new Getir(soru,cevap);
                    dbreference.child(id).setValue(getira1);
                }
*/

               // Elements element=doc.select("#content > div > div > div.things.clearfix");






            /*    for (Element element : elements) {
                    System.out.println(element.ownText());
                }*/

                /*element.select("div > div.things-header.things-row").remove();
                element.select("div > div.ignore-ui.things-row.clearfix").remove();
                element.select(" #content > div > div > div.things.clearfix > div:nth-child(3)").removeClass("ignore-ui things-row");
                element.select("div > div.things.clearfix > div:nth-child(3) > strong").remove();*/

                /*for (int i=4;i<=253;i++){
                    elements.select("div > div.things.clearfix > div:nth-child("+i+") > div.col_a.col.text > div");
                    elements.select("div > div.things.clearfix > div:nth-child(3) > strong").remove();
                    veri=elements.html();//istenilen html taglarını çeker.
                    soru=Jsoup.parse(veri).text();
                    element.select("div > div.things.clearfix > div:nth-child("+i+") > div.col_b.col.text > div");
                    element.select("div > div.things.clearfix > div:nth-child(3) > strong").remove();/*
                    veri=elements.html();//istenilen html taglarını çeker.
                    cevap=Jsoup.parse(veri).text();

                    // Toast.makeText(getApplicationContext(),"key :"+soru+"cevap:"+cevap,Toast.LENGTH_LONG).show();

                    sozlukiki.put(soru,cevap);
                }




                //  elements.select("div > div.things.clearfix > div:nth-child(4) > div.thinguser").remove();
              //  elements.select("div > div.things.clearfix > div:nth-child(4) > div.col_a.col.text");
              //  elements.select("div > div.things.clearfix > div:nth-child(4) > div.col_b.col.text");

               /* elements.select("div > code:nth-child(1) > div.icerikdt");
                elements.select("img").remove();//elements nesnesi içindeki img tagları siliniyor.
                elements.select("p");
                elements.select("#content > div > code:nth-child(1) > div.text.wordbox.words_box.pd10").remove();//wordbox sınıfı siliniyor.*/

        /*
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid)
        {
            // tv_sonuc.setText(gelenVeri.get(0).toString());

           /* geciciVeriTut=gelenVeri.get(0).toString();
            String [] parca=geciciVeriTut.split(" ",2);//ignore dan kurtuldum
            parca[1].replaceAll(", ",",");
            String eklenecek=parca[1];

            Map<String, String> sozluk = new HashMap<String, String>();
            String []parcalanmis=eklenecek.split("A-Z",2);
            for (int i=0;i<parcalanmis.length-1;i+=2){
                sozluk.put(parcalanmis[i],parcalanmis[i+1]);

            }*/

            /*
            dialog.dismiss();

            //Toast.makeText(getApplicationContext(),"key :"+sozluk.get(""),Toast.LENGTH_LONG).show();

            for (Map.Entry<String, String> sozluk : sozlukiki.entrySet()){
                Toast.makeText(getApplicationContext(),sozluk.getKey() + " = " + sozluk.getValue(),Toast.LENGTH_LONG).show();
            }


            tv_sonuc.setText(soru+"-"+cevap);

        }*/


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

        unregisterReceiver(mReceiver);
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
                Uri link=Uri.parse("https://github.com/pauybs/2021-mobil-sozluk/tree/master");
                Intent intent3 =new Intent(Intent.ACTION_VIEW,link);
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
        activeA1 = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        activeA1 = false;
    }




    }





