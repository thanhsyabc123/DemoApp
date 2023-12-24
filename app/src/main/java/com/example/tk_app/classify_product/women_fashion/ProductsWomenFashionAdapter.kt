package com.example.tk_app.classify_product.women_fashion

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tk_app.R

class ProductsWomenFashionAdapter (private val products2: List<ProductWomenFashion>) : RecyclerView.Adapter<ProductsWomenFashionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_Images_Show_Women: ImageView = view.findViewById(R.id.tv_images_show_women)
        val tv_Price_Show_Women: TextView = view.findViewById(R.id.tv_price_show_women)
        val tv_Name_Show_Women: TextView = view.findViewById(R.id.tv_name_show_women)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products_womensfashion, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products2.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products2[position]

        Glide.with(holder.tv_Images_Show_Women.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.baseline_person_24)
            .into(holder.tv_Images_Show_Women)
        holder.tv_Price_Show_Women.text = "price: ${product.price}"
        holder.tv_Name_Show_Women.text = "name: ${product.name}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailProductsWomenActivity::class.java)
            val productWomenId = product.productWomenId // Lấy productmenId từ sản phẩm hiện tại
            intent.putExtra("productWomenId", productWomenId) // Truyền productmenId qua Intent
            holder.itemView.context.startActivity(intent)
        }
    }
}
