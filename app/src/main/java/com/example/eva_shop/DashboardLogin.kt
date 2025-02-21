package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardLogin : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var togglePasswordVisibility: ImageView
    private lateinit var createAccountTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var userDatabaseHelper: UserDatabaseHelper
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_login)

        usernameEditText = findViewById(R.id.et_username)
        passwordEditText = findViewById(R.id.et_password)
        loginButton = findViewById(R.id.btn_login)
        togglePasswordVisibility = findViewById(R.id.iv_toggle_password)
        createAccountTextView = findViewById(R.id.tv_create_account)
        forgotPasswordTextView = findViewById(R.id.tv_forgot_password)
        userDatabaseHelper = UserDatabaseHelper(this)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            } else {
                if (userDatabaseHelper.checkUser(username, password)) {
                    // Login successful, navigate to HomeActivity
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        togglePasswordVisibility.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            passwordEditText.inputType =
                if (isPasswordVisible) android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                else android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        createAccountTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
