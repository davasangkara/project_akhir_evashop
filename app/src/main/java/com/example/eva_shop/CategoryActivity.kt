package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
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

        // Menerima daftar favorit yang dikirim dari HomeActivity
        favoriteList = intent.getParcelableArrayListExtra("FAVORITE_LIST") ?: arrayListOf()

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
                { product -> showProductDetail(product) },
                { product, position -> increaseQuantity(product, position) },
                { product, position -> decreaseQuantity(product, position) }
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

    private fun toggleFavorite(product: Product, position: Int) {
        product.isFavorite = !product.isFavorite
        if (!product.isFavorite) {
            favoriteList.remove(product)
        } else {
            if (!favoriteList.contains(product)) {
                favoriteList.add(product)
            }
        }
        productAdapter.notifyItemChanged(position)
        if (favoriteList.isEmpty()) {
            favoriteRecyclerView.visibility = View.GONE
            emptyMessage.visibility = View.VISIBLE
        }
    }

    private fun toggleCart(product: Product, position: Int) {
        product.isInCart = !product.isInCart
        if (product.isInCart) {
            if (!cartList.contains(product)) {
                cartList.add(product)
            }
        } else {
            cartList.remove(product)
        }
        productAdapter.notifyItemChanged(position)
    }

    private fun showProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }

    private fun increaseQuantity(product: Product, position: Int) {
        product.quantity++
        productAdapter.notifyItemChanged(position)
    }

    private fun decreaseQuantity(product: Product, position: Int) {
        if (product.quantity > 1) {
            product.quantity--
            productAdapter.notifyItemChanged(position)
        }
    }
}
