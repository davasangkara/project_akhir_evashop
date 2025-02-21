package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class TshirtActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tshirt)

        // Inisialisasi ListView
        val listView = findViewById<ListView>(R.id.listViewCategories)

        // Daftar kategori yang akan ditampilkan
        val categories = arrayOf("Blouse", "Dress", "Pants", "Cardigan", "Vest", "Sweater")

        // Adapter untuk menampilkan data di ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter

        // Set listener untuk item yang dipilih
        listView.setOnItemClickListener { _, _, position, _ ->
            // Menampilkan kategori yang dipilih
            val category = categories[position]
            val intent = Intent(this, TshirtDetailActivity::class.java)
            intent.putExtra("CATEGORY_NAME", category)
            startActivity(intent)
        }
    }
}
