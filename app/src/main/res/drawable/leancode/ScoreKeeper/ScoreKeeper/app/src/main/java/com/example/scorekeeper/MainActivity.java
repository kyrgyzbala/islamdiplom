package com.example.scorekeeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv_score;
    Button add, end;
    int score=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_score = (TextView) findViewById(R.id.tv_score);
        add = (Button) findViewById(R.id.add);
        end = (Button) findViewById(R.id.end);
        tv_score.setText("Score:"+score);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score++;
                tv_score.setText("Score:"+score);
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getSharedPreferences("Press",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("LastScore",score);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), NewGUI.class);
                startActivity(intent);
                finish();
            }
        });
    }
}