package com.example.androidlabs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        final TextView welcome = findViewById(R.id.textThing);
        String message = intent.getStringExtra("savedmessage");
        assert message != null;
        if (!message.isEmpty()) {
            String welcomefren = getResources().getString(R.string.welcome2);
            welcome.setText(welcomefren + " " + message + "!");
        }

        final Button dontcall = findViewById(R.id.button1);
        final Button thank = findViewById(R.id.button2);

        dontcall.setOnClickListener(
            (click) -> {
                setResult(0);
                finish();
            }
        );
        thank.setOnClickListener(
            (click) -> {
                setResult(1);
                finish();
            }
        );
    }
}