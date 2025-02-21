package com.example.eva_shop

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TshirtDetailActivity : AppCompatActivity() {

    private lateinit var productRecyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private lateinit var productList: ArrayList<Product>
    private lateinit var searchEditText: EditText
    private lateinit var favoriteList: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tshirt_detail)

        // Ambil data kategori yang dipilih
        val categoryName = intent.getStringExtra("CATEGORY_NAME")
        val categoryTextView = findViewById<TextView>(R.id.categoryNameTextView)
        categoryTextView.text = "Selected Category: $categoryName"

        // Inisialisasi RecyclerView
        productRecyclerView = findViewById(R.id.recycler_view_products)
        productRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Menentukan produk berdasarkan kategori
        productList = getProductsForCategory(categoryName)

        // Set adapter untuk RecyclerView
        productAdapter = ProductAdapter(
            productList,
            { product, position -> toggleFavorite(product, position) },
            { product, position -> toggleCart(product, position) },
            { product -> showProductDetail(product) },
            { product, position -> increaseQuantity(product, position) },
            { product, position -> decreaseQuantity(product, position) }
        )

        productRecyclerView.adapter = productAdapter

        // Inisialisasi EditText untuk pencarian
        searchEditText = findViewById(R.id.et_search)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Daftar favorit
        favoriteList = ArrayList()

    }

    private fun getProductsForCategory(categoryName: String?): ArrayList<Product> {
        val allProducts = arrayListOf(
            Product("Luna Dress Cradenza Silk", "120.000", R.drawable.luna_dress, false, false, "Luna Dress berbahan silk dengan desain modern."),
            Product("Love Shirt Kemeja", "100.000", R.drawable.love_shirt, false, false, "Kemeja Love Shirt dengan bahan nyaman."),
            Product("Pamela Sweater Rajut Korean", "140.000", R.drawable.pamela, false, false, "Pamela Sweater berbahan rajut Korean Style."),
            Product("Viora Pants Kulot Crincle", "110.000", R.drawable.viora, false, false, "Viora Pants kulot stylish dengan bahan crinkle.")
        )

        // Filter produk berdasarkan kategori
        return when (categoryName) {
            "Blouse" -> allProducts.filter { it.name.contains("Shirt") }.toCollection(ArrayList())
            "Dress" -> allProducts.filter { it.name.contains("Dress") }.toCollection(ArrayList())
            "Pants" -> allProducts.filter { it.name.contains("Pants") }.toCollection(ArrayList())
            "Cardigan" -> allProducts.filter { it.name.contains("Sweater") }.toCollection(ArrayList())
            "Vest" -> allProducts.filter { it.name.contains("Vest") }.toCollection(ArrayList())
            "Sweater" -> allProducts.filter { it.name.contains("Sweater") }.toCollection(ArrayList())
            else -> ArrayList()
        }
    }

    private fun filterList(query: String) {
        val filteredList = productList.filter { it.name.contains(query, ignoreCase = true) }
        productAdapter.updateList(ArrayList(filteredList))
    }

    private fun toggleFavorite(product: Product, position: Int) {
        product.isFavorite = !product.isFavorite
        if (product.isFavorite && !favoriteList.contains(product)) {
            favoriteList.add(product)
        } else {
            favoriteList.remove(product)
        }
        productAdapter.notifyItemChanged(position)
    }

    private fun toggleCart(product: Product, position: Int) {
        product.isInCart = !product.isInCart
        productAdapter.notifyItemChanged(position)
    }

    private fun showProductDetail(product: Product) {
        // Add code to show product details
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
