package com.example.eva_shop

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TshirtDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tshirt_detail)

        // Ambil data kategori yang dipilih
        val categoryName = intent.getStringExtra("CATEGORY_NAME")

        // Menampilkan kategori yang dipilih
        val categoryTextView = findViewById<TextView>(R.id.categoryNameTextView)
        categoryTextView.text = "Selected Category: $categoryName"
    }
}
