package com.example.dictionary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WordAdd extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_add);
        final   EditText et_kelime= (EditText) findViewById(R.id.et_kelime);
        final   EditText et_anlam = (EditText) findViewById(R.id.et_anlam);
        Button btn_kelimeKayit=(Button)findViewById(R.id.btn_kelimeKayit);

        final DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("Getir5");

        boolean bayrak=false;

        int generateNumber;

       /* while(bayrak!=true){
            generateNumber=(int)(Math.random()*142);

            if(generateNumber%2==0){
                bayrak=true;
            }
        }*/





        btn_kelimeKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = dbreference.push().getKey();
                Getir getir= new Getir(et_kelime.getText().toString(),et_anlam.getText().toString());
                dbreference.child(id).setValue(getir);
                et_kelime.setText("");
                et_anlam.setText("");


            }
        });




    }
}