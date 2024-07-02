package com.example.androidlabs;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        String name = getIntent().getStringExtra("name");
        String height = getIntent().getStringExtra("height");
        String mass = getIntent().getStringExtra("mass");

        if (savedInstanceState == null) {
            DetailsFragment fragment = DetailsFragment.newInstance(name, height, mass);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();
        }
    }
}