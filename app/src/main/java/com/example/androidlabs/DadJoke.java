package com.example.androidlabs;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;

public class DadJoke extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dad_joke);

        setupDrawerAndToolbar();

    }
}