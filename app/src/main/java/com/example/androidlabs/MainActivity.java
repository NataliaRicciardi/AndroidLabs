package com.example.androidlabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText edt;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initialize button
        final Button nextbtn = findViewById(R.id.nextBtn);
        // initialize edittext
        edt = findViewById(R.id.textBox);

        // Load data from sharedPreferences and put in edittext if it's not empty
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String savedText = prefs.getString("savedmessage", "");
        if (!savedText.isEmpty()) {
            edt.setText(savedText);
        }

        // initialize intent
        Intent nextPage = new Intent(MainActivity.this, NameActivity.class);

        nextbtn.setOnClickListener(
            (click) -> {

                // onclick next button open next page
                String message = edt.getText().toString();
                nextPage.putExtra("savedmessage", message);
                startActivityForResult(nextPage, 101);
            }
        );
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("savedmessage", edt.getText().toString());
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101) {
            if (resultCode == 1) {
                finish();
            }
        }
    }
}