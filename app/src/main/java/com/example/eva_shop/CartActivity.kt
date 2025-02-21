package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartActivity : AppCompatActivity() {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var checkoutButton: Button
    private lateinit var cartList: ArrayList<Product>
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        cartRecyclerView = findViewById(R.id.recycler_view_cart)
        emptyMessage = findViewById(R.id.tv_empty_message)
        totalPriceTextView = findViewById(R.id.tv_total_price)
        checkoutButton = findViewById(R.id.btn_checkout)

        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        // Ambil cartList dari SharedPreferences
        cartList = getCartListFromPreferences()

        Log.d("CartActivity", "Received cartList: $cartList")

        if (cartList.isEmpty()) {
            showEmptyState()
        } else {
            cartAdapter = CartAdapter(cartList, { updateTotalPrice() }, { position -> removeItem(position) })
            cartRecyclerView.adapter = cartAdapter
            showCartState()
        }

        checkoutButton.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putParcelableArrayListExtra("CART_LIST", cartList)
            startActivity(intent)
        }

        updateTotalPrice()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
                    true
                }
                R.id.nav_category -> {
                    startActivity(Intent(this, CategoryActivity::class.java))
                    true
                }
                R.id.nav_cart -> true
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun getCartListFromPreferences(): ArrayList<Product> {
        val sharedPreferences = getSharedPreferences("CartData", MODE_PRIVATE)
        val gson = Gson()
        val jsonCartList = sharedPreferences.getString("CART_LIST", null)

        return if (jsonCartList != null) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            gson.fromJson(jsonCartList, type)
        } else {
            ArrayList() // Mengembalikan list kosong jika tidak ada data
        }
    }

    private fun saveCartListToPreferences() {
        val sharedPreferences = getSharedPreferences("CartData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonCartList = gson.toJson(cartList)
        editor.putString("CART_LIST", jsonCartList)
        editor.apply()
    }

    private fun updateTotalPrice() {
        val total = cartAdapter.getTotalSelectedPrice()
        totalPriceTextView.text = "Total: Rp $total"
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < cartList.size) {
            cartList.removeAt(position)
            cartAdapter.notifyItemRemoved(position)
            cartAdapter.notifyItemRangeChanged(position, cartList.size)
            Log.e("CartActivity", "Item removed, new cartList size: ${cartList.size}")
            updateTotalPrice()

            saveCartListToPreferences() // Simpan perubahan cartList ke SharedPreferences

            if (cartList.isEmpty()) {
                showEmptyState()
            }
        }
    }

    private fun showEmptyState() {
        cartRecyclerView.visibility = View.GONE
        emptyMessage.visibility = View.VISIBLE
        checkoutButton.visibility = View.GONE
    }

    private fun showCartState() {
        cartRecyclerView.visibility = View.VISIBLE
        emptyMessage.visibility = View.GONE
        checkoutButton.visibility = View.VISIBLE
    }
}
