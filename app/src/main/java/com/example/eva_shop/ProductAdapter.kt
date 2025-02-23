package com.example.eva_shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private var productList: ArrayList<Product>,
    private val onFavoriteClick: (Product, Int) -> Unit,
    private val onCartClick: (Product, Int) -> Unit,
    private val onProductClick: (Product) -> Unit,
    private val onIncreaseQuantity: (Product, Int) -> Unit,  // New parameter for increase quantity
    private val onDecreaseQuantity: (Product, Int) -> Unit   // New parameter for decrease quantity
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val favoriteIcon: ImageView = itemView.findViewById(R.id.iv_favorite)
        val cartIcon: ImageView = itemView.findViewById(R.id.iv_cart)
        val quantityText: TextView = itemView.findViewById(R.id.tv_quantity)
        val increaseQuantity: ImageView = itemView.findViewById(R.id.iv_increase)
        val decreaseQuantity: ImageView = itemView.findViewById(R.id.iv_decrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productImage.setImageResource(product.image)
        holder.productName.text = product.name
        holder.productPrice.text = product.price
        holder.quantityText.text = product.quantity.toString()  // Display the quantity

        holder.favoriteIcon.setImageResource(
            if (product.isFavorite) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
        )

        holder.cartIcon.setImageResource(
            if (product.isInCart) R.drawable.ic_cart_filled else R.drawable.ic_cart_outline
        )

        holder.favoriteIcon.setOnClickListener {
            onFavoriteClick(product, position)
        }

        holder.cartIcon.setOnClickListener {
            onCartClick(product, position)
        }

        holder.itemView.setOnClickListener {
            onProductClick(product)
        }

        // Handling quantity increase and decrease
        holder.increaseQuantity.setOnClickListener {
            onIncreaseQuantity(product, position)
        }

        holder.decreaseQuantity.setOnClickListener {
            onDecreaseQuantity(product, position)
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: ArrayList<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}
