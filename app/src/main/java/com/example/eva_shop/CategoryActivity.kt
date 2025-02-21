package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CategoryActivity : AppCompatActivity() {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var favoriteList: ArrayList<Product>
    private lateinit var productAdapter: ProductAdapter
    private val cartList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        favoriteRecyclerView = findViewById(R.id.recycler_view_favorites)
        emptyMessage = findViewById(R.id.tv_empty_message)

        favoriteRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // DEBUGGING: Cek apakah data benar-benar diterima
        favoriteList = intent.getParcelableArrayListExtra("FAVORITE_LIST") ?: arrayListOf()
        Log.d("CategoryActivity", "Received favoriteList: $favoriteList")

        if (favoriteList.isEmpty()) {
            favoriteRecyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        } else {
            favoriteRecyclerView.visibility = View.VISIBLE
            emptyMessage.visibility = View.GONE

            productAdapter = ProductAdapter(
                favoriteList,
                { product, position -> toggleFavorite(product, position) },
                { product, position -> toggleCart(product, position) },
                { product -> showProductDetail(product) },  // OnProductClick
                { product, position -> increaseQuantity(product, position) },  // OnIncreaseQuantity
                { product, position -> decreaseQuantity(product, position) }  // OnDecreaseQuantity
            )

            favoriteRecyclerView.adapter = productAdapter
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    finish()
                    true
                }
                R.id.nav_category -> true
                R.id.nav_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    intent.putParcelableArrayListExtra("CART_LIST", ArrayList(cartList))
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    // Fungsi untuk menambah atau menghapus produk dari favorit
    private fun toggleFavorite(product: Product, position: Int) {
        product.isFavorite = !product.isFavorite
        if (!product.isFavorite) {
            favoriteList.remove(product)
        } else {
            if (!favoriteList.contains(product)) {
                favoriteList.add(product)
            }
        }
        productAdapter.notifyItemChanged(position) // Notify that the item has changed
        if (favoriteList.isEmpty()) {
            favoriteRecyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        }
    }

    // Fungsi untuk menambah atau menghapus produk dari keranjang
    private fun toggleCart(product: Product, position: Int) {
        product.isInCart = !product.isInCart
        if (product.isInCart) {
            if (!cartList.contains(product)) {
                cartList.add(product)
            }
        } else {
            cartList.remove(product)
        }
        productAdapter.notifyItemChanged(position) // Notify that the item has changed
    }

    // Menampilkan detail produk ketika item di klik
    private fun showProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }

    // Increase product quantity
    private fun increaseQuantity(product: Product, position: Int) {
        // Assume you have a field for quantity in Product, which is an integer
        product.quantity++
        productAdapter.notifyItemChanged(position)
    }

    // Decrease product quantity
    private fun decreaseQuantity(product: Product, position: Int) {
        if (product.quantity > 1) {
            product.quantity--
            productAdapter.notifyItemChanged(position)
        }
    }
}
