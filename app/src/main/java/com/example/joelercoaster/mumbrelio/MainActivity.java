package com.example.joelercoaster.mumbrelio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bForecast = findViewById(R.id.bForecast);
        final TextView tvTemp = findViewById(R.id.tvTemp);

        bForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ForecastTask(tvTemp).execute();
            }
        });
    }
}


