package com.example.eva_shop

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private var cartList: ArrayList<Product>,
    private val onUpdateTotal: () -> Unit,
    private val onDeleteItem: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private val selectedItems = HashSet<Int>()

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.iv_product_image)
        val productName: TextView = itemView.findViewById(R.id.tv_product_name)
        val productPrice: TextView = itemView.findViewById(R.id.tv_product_price)
        val selectCheckBox: CheckBox = itemView.findViewById(R.id.cb_select_product)
        val deleteIcon: ImageView = itemView.findViewById(R.id.iv_delete_product) // Pastikan ID ini ada di XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = cartList[position]
        holder.productImage.setImageResource(product.image)
        holder.productName.text = product.name
        holder.productPrice.text = product.price

        holder.selectCheckBox.setOnCheckedChangeListener(null)
        holder.selectCheckBox.isChecked = selectedItems.contains(position)

        holder.selectCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedItems.add(position)
            } else {
                selectedItems.remove(position)
            }
            onUpdateTotal()
        }

        // Pastikan `position` valid sebelum menghapus
        holder.deleteIcon.setOnClickListener {
            if (position >= 0 && position < cartList.size) {
                onDeleteItem(position)
            }
        }
    }

    override fun getItemCount(): Int = cartList.size

    fun getTotalSelectedPrice(): Int {
        return selectedItems.sumOf { index ->
            cartList[index].price.replace(".", "").toIntOrNull() ?: 0
        }
    }
}
