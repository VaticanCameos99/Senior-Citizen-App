package com.example.SeniorCitizenCare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DietDetailsActivity extends AppCompatActivity {

    TextView dietDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_details);

        getIntent();

        dietDetails = findViewById(R.id.diet_details);
        dietDetails.setText("HElllooooo");
    }
}
