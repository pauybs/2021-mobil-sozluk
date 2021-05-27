package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class Scoreboard extends AppCompatActivity {
    TextView nav_scoreboardTitle;
    ImageView imageView;
    int baslangicScore;
    boolean kontrol;
    String baslangicMail;
    String baslangicNickname;
    List<ScoreboardGetir> scoreboardGetir=new ArrayList<ScoreboardGetir>();;
    String possibleEmail;
    TableLayout nav_scoreboard;
    DatabaseReference dbreference ;
    TextView textView1;
    TextView textView2;
    TextView  textView3;
    TextView  textView4;
    ImageView imageView2;
    TextView nicknameScoreboard;
    TextView scoreScoreboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        dbreference = FirebaseDatabase.getInstance().getReference("Getir6");

        nav_scoreboard=findViewById(R.id.nav_scoreboard);



        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                possibleEmail = account.name;
                break;
            }
        }


         kontrol=false;
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(kontrol==true){
                    return;
                }
                kontrol=true;
                dbreference=FirebaseDatabase.getInstance().getReference("Getir6").child(possibleEmail.replace(".","").replace("$","").replace("#","").replace("[","").replace("]",""));
                int adet=0;
                for(DataSnapshot verigetir2 : dataSnapshot.getChildren())
                {
                    baslangicScore=Integer.parseInt(verigetir2.child("score").getValue().toString());
                    baslangicMail=verigetir2.child("mail").getValue().toString();
                    baslangicNickname=verigetir2.child("nickname").getValue().toString();
                    scoreboardGetir.add(new ScoreboardGetir(baslangicMail,baslangicNickname,baslangicScore));
                    adet=adet+1;

                }

                Collections.sort(scoreboardGetir, new Comparator<ScoreboardGetir>(){
                    public int compare(ScoreboardGetir o1, ScoreboardGetir o2)
                    {
                        return Integer.toString(o1.getScore()).compareTo(Integer.toString(o2.getScore()));
                    }
                });
                Collections.reverse(scoreboardGetir);
                init(scoreboardGetir);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    public void init(List <ScoreboardGetir> gelen) {


        TableLayout ll = (TableLayout) findViewById(R.id.nav_scoreboard);
        TableRow row0=new TableRow(this);

        View view=new View(this);

        textView3=new TextView(this);
        textView4=new TextView(this);
        imageView2=new ImageView(this);


        row0.setBackgroundColor(Color.parseColor("#4c98cf"));






         textView3.setText("NickName");
         textView3.setTextColor(Color.parseColor("#d4fefe"));




          textView3.setGravity(Gravity.LEFT);
         textView3.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
         textView3.setTextSize(15);



        row0.addView(textView3);


        imageView2.setImageResource(R.drawable.ic_nstar1);
        imageView2.setBackgroundColor(Color.parseColor("#4c98cf"));



        row0.addView(imageView2);


        /*
        LayoutParams params = (LayoutParams) imageView.getLayoutParams();
        params.width = 120;
        // existing height is ok as is, no need to edit it
        imageView.setLayoutParams(params);
         */




        textView4.setText("Score");
        textView4.setTextSize(15);
        textView4.setTextColor(Color.parseColor("#d4fefe"));
        textView4.setBackgroundColor(Color.parseColor("#4c98cf"));
        textView4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView4.setGravity(Gravity.RIGHT);

       // textView4.setPadding(18 ,18,0,0);


        row0.addView(textView4);
        ll.addView(row0);


        for (int i = 0; i < gelen.size(); i++) {


            TableRow row1 = new TableRow(this);
            row1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1));


            if(i%2==0){
                row1.setBackgroundColor(Color.parseColor("#5b538a"));
            }
            else{
                row1.setBackgroundColor(Color.parseColor("#4c98cf"));
            }




            imageView=new ImageView(this);
            //imageView.setLayoutParams(p);
            imageView.setImageResource(R.mipmap.ic_launcher_round);

          //  imageView.setPadding(15,15,15,15);





            row1.addView(imageView);

            textView1 = new TextView(this);
            textView2 = new TextView(this);



            textView1.setText(gelen.get(i).getNickname());
            textView1.setTextSize(15);
            textView1.setPadding(0,40,0,0);
            //textView1.setLayoutParams(p);
           // textView2.setLayoutParams(p);


            //  textView1.setHeight(imageView.getLayoutParams().height);
           /* textView1.setTextColor(Color.parseColor("#d4fefe"));
            textView2.setTextColor(Color.parseColor("#d4fefe"));*/

            if(possibleEmail.equals(gelen.get(i).getMail())){
                textView1.setTextColor(Color.parseColor("#44cfcd"));
                textView2.setTextColor(Color.parseColor("#44cfcd"));
            }
            else{
                textView1.setTextColor(Color.parseColor("#d4fefe"));
                textView2.setTextColor(Color.parseColor("#d4fefe"));
            }


         //   textView1.setPadding(15,15,15,15);
          /*  textView1.setGravity(Gravity.LEFT);
            textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);*/
            textView1.setGravity(Gravity.CENTER_HORIZONTAL);


            textView2.setTextSize(15);
            textView2.setPadding(0,40,0,0);
         //   textView2.setPadding(15,15,15,15);


            textView2.setText(Integer.toString(gelen.get(i).getScore()));
            textView2.setGravity(Gravity.CENTER_HORIZONTAL);
         /*   textView2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView2.setGravity(Gravity.RIGHT);*/




           // textView1.setText(gelen.get(i).getNickname());


           // textView2.setText(Integer.toString(gelen.get(i).getScore()));

            row1.addView(textView1);
            row1.addView(textView2);

            ll.addView(row1);
        }

    }





}