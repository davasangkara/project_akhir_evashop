package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson

class HomeActivity : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productList: ArrayList<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchEditText: EditText

    private var favoriteList = ArrayList<Product>() // Daftar favorit
    private var cartList = ArrayList<Product>() // Keranjang untuk produk

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        searchEditText = findViewById(R.id.et_search)
        productRecyclerView = findViewById(R.id.recycler_view_products)
        productRecyclerView.layoutManager = GridLayoutManager(this, 2)

        productList = arrayListOf(
            Product("Luna Dress Cradenza Silk", "120.000", R.drawable.luna_dress, false, false,
                "Luna Dress berbahan silk dengan desain modern."),
            Product("Love Shirt Kemeja", "100.000", R.drawable.love_shirt, false, false,
                "Kemeja Love Shirt dengan bahan nyaman."),
            Product("Pamela Sweater Rajut Korean", "140.000", R.drawable.pamela, false, false,
                "Pamela Sweater berbahan rajut Korean Style."),
            Product("Viora Pants Kulot Crincle", "110.000", R.drawable.viora, false, false,
                "Viora Pants kulot stylish dengan bahan crinkle.")
        )

        productAdapter = ProductAdapter(productList, { product, position ->
            toggleFavorite(product, position)
        }, { product, position ->
            toggleCart(product, position)
        }, { product ->
            showProductDetail(product)
        })

        productRecyclerView.adapter = productAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Handle Bottom Navigation Click
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_category -> {
                    val intent = Intent(this, CategoryActivity::class.java)
                    intent.putParcelableArrayListExtra("FAVORITE_LIST", favoriteList) // Mengirim favoriteList
                    startActivity(intent)
                    true
                }
                R.id.nav_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    // Kirim cartList ke CartActivity
                    saveCartListToPreferences()
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun showProductDetail(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT", product)
        startActivity(intent)
    }

    private fun filterList(query: String) {
        val filteredList = productList.filter { it.name.contains(query, ignoreCase = true) }
        productAdapter.updateList(ArrayList(filteredList))
    }

    private fun toggleFavorite(product: Product, position: Int) {
        product.isFavorite = !product.isFavorite
        if (product.isFavorite) {
            if (!favoriteList.contains(product)) {
                favoriteList.add(product)
            }
        } else {
            favoriteList.remove(product)
        }
        productAdapter.notifyItemChanged(position) // Pastikan RecyclerView diperbarui
    }

    private fun toggleCart(product: Product, position: Int) {
        product.isInCart = !product.isInCart
        if (product.isInCart) {
            if (!cartList.contains(product)) {
                cartList.add(product) // Produk ditambahkan ke cartList
            }
        } else {
            cartList.remove(product) // Produk dihapus dari cartList
        }
        productAdapter.notifyItemChanged(position) // Pastikan RecyclerView diperbarui
        saveCartListToPreferences() // Simpan perubahan cartList ke SharedPreferences
    }

    // Menyimpan cartList ke SharedPreferences
    private fun saveCartListToPreferences() {
        val sharedPreferences = getSharedPreferences("CartData", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val jsonCartList = gson.toJson(cartList)
        editor.putString("CART_LIST", jsonCartList)
        editor.apply()
    }
}
