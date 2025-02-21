package com.example.eva_shop


import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView // Menampilkan username sebagai phone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inisialisasi TextView
        nameTextView = findViewById(R.id.tv_name)
        emailTextView = findViewById(R.id.tv_email)
        phoneTextView = findViewById(R.id.tv_phone)

        // Ambil data pengguna yang dikirim dari RegisterActivity
        val user: User? = intent.getParcelableExtra("USER_DATA")
        if (user != null) {
            // Menampilkan data pengguna
            nameTextView.text = "Name: ${user.name}"
            emailTextView.text = "Email: ${user.email}"
            phoneTextView.text = "Phone: ${user.username}" // Menampilkan username sebagai phone
        } else {
            Toast.makeText(this, "Error: User data not found!", Toast.LENGTH_SHORT).show()
        }
    }
}
