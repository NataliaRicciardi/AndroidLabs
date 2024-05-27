package com.example.androidlabs;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_linear);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final Button btn = findViewById(R.id.pressbtn);
        final TextView txt = findViewById(R.id.text);
        final EditText edt = findViewById(R.id.editbox);

        btn.setOnClickListener(
                (click) -> {
                    txt.setText(edt.getText().toString());
                    String message = getResources().getString(R.string.toast_message);
                    Toast.makeText(MainActivity.this,  message, Toast.LENGTH_SHORT).show();
                }
        );
        CheckBox cb = findViewById( R.id.check );
        cb.setOnCheckedChangeListener( (compoundButton, b) -> {
            String message;
            if (b) {
                message = getResources().getString(R.string.checkbox_on);
            }
            else {
                message = getResources().getString(R.string.checkbox_off);
            }
            Snackbar.make(cb,getResources().getString(R.string.snackbar_message) + " " + message, Snackbar.LENGTH_LONG)
                    .setAction("Undo", click -> cb.setChecked(!b))
                    .show();
        });
    }
}