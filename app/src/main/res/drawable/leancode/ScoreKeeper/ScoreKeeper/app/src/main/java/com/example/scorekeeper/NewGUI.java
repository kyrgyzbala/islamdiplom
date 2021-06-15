package com.example.scorekeeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewGUI extends AppCompatActivity {
    TextView tv_score;
    int lastscore;
    int best1, best2,best3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best);
        tv_score = (TextView) findViewById(R.id.tv_score);
        SharedPreferences preferences = getSharedPreferences("Press",0);
        lastscore = preferences.getInt("lastscore:",0);
        best1 = preferences.getInt("best1:",0);
        best2 = preferences.getInt("best2:",0);
        best3 = preferences.getInt("best3:",0);

        if(lastscore>best3){
            best3 = lastscore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("Best3",best3);
            editor.apply();
        }
        if(lastscore>best2){
            int temp = best2;
            best2 = lastscore;
            best3 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("Best3",best3);
            editor.putInt("Best2",best2);
            editor.apply();
        }
        if(lastscore>best1){
            int temp = best1;
            best1 = lastscore;
            best2 = temp;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("Best2",best2);
            editor.putInt("Best1",best1);
            editor.apply();
        }

        tv_score.setText("Last Score:"+ lastscore+"\n"+"Best1:"+ best1+"\n"+"Best2"+ best2+"\n"+"Best3:"+ best3);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
