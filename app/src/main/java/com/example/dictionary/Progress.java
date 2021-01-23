package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Progress extends AppCompatActivity {
    ProgressBar progressBar ;
    int progressbarDurum;
    Handler handler=new Handler();
    Intent progressK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        progressBar= findViewById(R.id.progressBar);
        progressbarDurum=0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressbarDurum < 100) {
                    progressbarDurum += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressbarDurum);
                        }
                    });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    finally {

                    }
                }





            }
        }).start();





        if(Level_A1.activeA1){
            progressK =new Intent(Progress.this,Level_A1.class);
            startActivity(progressK);
        }
        else if(Level_A2.activeA2){
            progressK =new Intent(Progress.this,Level_A2.class);
            startActivity(progressK);

        }
        else if(Level_A3.activeA3){
            progressK =new Intent(Progress.this,Level_A3.class);
            startActivity(progressK);

        }
        else if(Phrasal_Verbs.activephrasal){
            progressK =new Intent(Progress.this,Phrasal_Verbs.class);
            startActivity(progressK);
        }
        else if(SimilarTwoWords.twoWords){

            progressK =new Intent(Progress.this,SimilarTwoWords.class);
            startActivity(progressK);
        }





      /*  Intent i =new Intent(Progress.this,Level_A1.class);
        startActivity(i);*/

    }
}
