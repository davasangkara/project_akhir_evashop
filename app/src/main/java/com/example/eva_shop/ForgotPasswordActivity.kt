package com.example.eva_shop

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmButton: Button
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        newPasswordEditText = findViewById(R.id.et_new_password)
        confirmButton = findViewById(R.id.btn_confirm)
        userDatabaseHelper = UserDatabaseHelper(this)

        confirmButton.setOnClickListener {
            val newPassword = newPasswordEditText.text.toString()

            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show()
            } else {
                val username = intent.getStringExtra("username") // Pastikan username dikirim saat lupa password
                if (username != null) {
                    userDatabaseHelper.updatePassword(username, newPassword)
                    Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}
