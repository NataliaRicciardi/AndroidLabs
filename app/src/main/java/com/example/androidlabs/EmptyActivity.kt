package com.example.androidlabs

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EmptyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_empty)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val name = intent.getStringExtra("name");
        val height = intent.getStringExtra("height");
        val mass = intent.getStringExtra("mass");

        // Create and add DetailsFragment
        if (savedInstanceState == null) {
            val fragment = DetailsFragment.newInstance(name, height, mass)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame, fragment)
                    .commit();

        }

    }
}