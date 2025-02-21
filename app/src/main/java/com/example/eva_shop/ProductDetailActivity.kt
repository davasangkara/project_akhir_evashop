package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProductDetailActivity : AppCompatActivity() {

    private var cartList = ArrayList<Product>() // Keranjang yang akan ditambahkan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val product = intent.getParcelableExtra<Product>("PRODUCT")
        cartList = getCartListFromPreferences() // Ambil cartList dari SharedPreferences

        val productImage: ImageView = findViewById(R.id.iv_product_detail_image)
        val productName: TextView = findViewById(R.id.tv_product_detail_name)
        val productPrice: TextView = findViewById(R.id.tv_product_detail_price)
        val productDescription: TextView = findViewById(R.id.tv_product_detail_description)
        val addToCartButton: Button = findViewById(R.id.btn_add_to_cart)

        product?.let {
            productImage.setImageResource(it.image)
            productName.text = it.name
            productPrice.text = it.price
            productDescription.text = it.description
        }

        addToCartButton.setOnClickListener {
            product?.let { p ->
                addToCart(p) // Menambahkan produk ke keranjang
            }
        }
    }

    private fun addToCart(product: Product) {
        cartList.add(product) // Menambahkan produk ke cartList
        saveCartListToPreferences()

        // Kirim cartList ke CartActivity
        val intent = Intent(this, CartActivity::class.java)
        startActivity(intent) // Pindah ke CartActivity setelah produk ditambahkan
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
}
