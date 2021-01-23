package com.example.dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.Ringtone;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VoiceToText extends AppCompatActivity {

    SpeechRecognizer speechRecognizer;
    ProgressDialog loadingDialog;
    Context context;
    int sayac=0;
    int sayac2=0;
    TextView tv_mesaj;
    ImageButton imgbtn_voice;
    String ses;
    String gelenKelime;
    String gelenCevap;
    private String cizgiColor="#4c98cf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_voice_to_text);
        context = this;

        requestRecordAudioPermission();

        tv_mesaj=findViewById(R.id.tv_mesaj);
        imgbtn_voice=findViewById(R.id.imgbtn_voice);


        //0-143 arası rastgele sayı oluşturuldu.
        final ArrayList gelenveri= new ArrayList();


        final DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Getir4");

        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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

                int secilenIndex=(int)(Math.random()* 143);
                String []veriayir=gelenveri.get(secilenIndex).toString().split("-");
                 gelenKelime=veriayir[0];
                gelenCevap=veriayir[1];

                tv_mesaj.setText(gelenCevap);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        imgbtn_voice.setOnClickListener(new View.OnClickListener() {//konuşma penceresi tanımlandı.
            @Override
            public void onClick(View v) {

                loadingDialog = new ProgressDialog(context,R.style.MyAlertDialogStyle);

                loadingDialog.setCancelable(false);

                loadingDialog.setMessage("Please Speak");


                loadingDialog.show();

                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);

                final ArrayList kayit=new ArrayList();

                speechRecognizer.setRecognitionListener(new RecognitionListener() {

                    @Override public void onRmsChanged(float rmsdB) {

                        // TODO Auto-generated method stub

                    }

                    @Override public void onResults(Bundle results) {

                        loadingDialog.dismiss();

                        ArrayList speechResultSentence = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

                        ses="";
                        for(int i=0;i<speechResultSentence.size();i++){
                            ses+=speechResultSentence.get(i);
                        }
                        if(gelenKelime.toLowerCase().equals(ses)){
                            Toast.makeText(getApplicationContext(),ses,Toast.LENGTH_LONG).show();
                            Ringtone ringtone = AlarmReceiver.ringtone;
                            ringtone.stop();
                        }
                        else{
                            sayac2++;
                            if(sayac2==3){
                                imgbtn_voice.setVisibility(View.INVISIBLE);

                                AlertDialog.Builder alert3 = new AlertDialog.Builder(context,R.style.AlertDialogStyle);
                                alert3.setTitle("Answered Wrong 3 Times");
                                alert3.setMessage("Do You Want To Turn Off The Alarm By Entering Text?");





                                alert3.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        AlertDialog.Builder alert4 = new AlertDialog.Builder(context,R.style.AlertDialogStyle);
                                        alert4.setTitle("ANSWER");
                                        alert4.setMessage(gelenKelime.toLowerCase());


                                        final EditText input = new EditText(context);
                                        input.setTextColor(Integer.parseInt("#d4fefe"));
                                        alert4.setView(input);

                                        alert4.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                boolean dialogKapat=false;

                                                while(dialogKapat!=true){
                                                    String value = input.getText().toString();
                                                    if(gelenKelime.toLowerCase().equals(value)){

                                                        dialogKapat=true;

                                                    }
                                                }
                                                //Alarm Kapanmadı

                                                Ringtone ringtone = AlarmReceiver.ringtone;
                                                ringtone.stop();


                                            }
                                        });
                                        alert4.show();
                                    }

                                });

                                alert3.setNegativeButton("NO",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // TODO Auto-generated method stub
                                                sayac=0;
                                                imgbtn_voice.setVisibility(View.VISIBLE);

                                            }
                                        });
                                alert3.show();









                            }

                        }








                    } @Override public void onReadyForSpeech(Bundle params) {

                        // TODO Auto-generated method stub

                    } @Override public void onPartialResults(Bundle partialResults) {

                        // TODO Auto-generated method stub

                    } @Override public void onEvent(int eventType, Bundle params) {

                        // TODO Auto-generated method stub

                    } @Override public void onError(int error) {

                        loadingDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "An Error Has Occurred, Please Try Again...", Toast.LENGTH_LONG).show();

                        sayac++;
                        if(sayac==3){
                            imgbtn_voice.setVisibility(View.INVISIBLE);

                            AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertDialogStyle);
                            alert.setTitle("Answered Wrong 3 Times");
                            alert.setMessage("Do You Want To Turn Off The Alarm By Entering Text?");





                            alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    AlertDialog.Builder alert2 = new AlertDialog.Builder(context,R.style.AlertDialogStyle);
                                    alert2.setTitle("ANSWER");
                                    alert2.setMessage(gelenKelime.toLowerCase());


                                    final EditText input = new EditText(context);
                                    alert2.setView(input);

                                    alert2.setPositiveButton("COMPLETE", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            boolean dialogKapat=false;

                                            while(dialogKapat!=true){
                                                String value = input.getText().toString();
                                                if(gelenKelime.toLowerCase().equals(value)){

                                                    dialogKapat=true;

                                                }
                                            }
                                            //Alarm Kapanmadı

                                            Ringtone ringtone = AlarmReceiver.ringtone;
                                            ringtone.stop();


                                        }
                                    });
                                    alert2.show();
                                }

                            });

                            alert.setNegativeButton("NO",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            sayac=0;
                                            imgbtn_voice.setVisibility(View.VISIBLE);

                                        }
                                    });
                            alert.show();









                        }


                    } @Override public void onEndOfSpeech() { loadingDialog .setMessage("Recording's Over.Fetching Results");

                    } @Override public void onBufferReceived(byte[] buffer) { }

                    @Override public void onBeginningOfSpeech() {

                        loadingDialog.setMessage("Recording Started");

                    } });

                Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);//voice to text servisi çağırıldı
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());//paketin ismi servise gönderildi.
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS , 100);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                speechRecognizer.startListening(recognizerIntent);







            }
        });



    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (speechRecognizer != null) {
            speechRecognizer.stopListening();
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // do nothing.
    }

    @Override
    protected void onPause() {
        super.onPause();

        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);

        activityManager.moveTaskToFront(getTaskId(), 0);
    }


}