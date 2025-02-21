package com.example.eva_shop


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val getStartedButton: Button = findViewById(R.id.btn_get_started)

        getStartedButton.setOnClickListener {
            // Ganti ke aktivitas lain jika diperlukan
            val intent = Intent(this, DashboardLogin::class.java)
            startActivity(intent)
        }
    }
}