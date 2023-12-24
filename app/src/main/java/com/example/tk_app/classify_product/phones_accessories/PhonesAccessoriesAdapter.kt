package com.example.tk_app.classify_product.phones_accessories

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tk_app.R
import com.example.tk_app.classify_product.women_fashion.DetailProductsWomenActivity
import com.example.tk_app.classify_product.women_fashion.ProductWomenFashion
import com.example.tk_app.classify_product.women_fashion.ProductsWomenFashionAdapter

class PhonesAccessoriesAdapter(private val products3: List<ProductPhonesAccessories>) : RecyclerView.Adapter<PhonesAccessoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_Images_Show_Phone: ImageView = view.findViewById(R.id.tv_images_show_phone)
        val tv_Price_Show_Phone: TextView = view.findViewById(R.id.tv_price_show_phone)
        val tv_Name_Show_Phone: TextView = view.findViewById(R.id.tv_name_show_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_products_phone, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products3.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products3[position]

        Glide.with(holder.tv_Images_Show_Phone.context)
            .load(product.imageUrl)
            .placeholder(R.drawable.baseline_person_24)
            .into(holder.tv_Images_Show_Phone)
        holder.tv_Price_Show_Phone.text = "price: ${product.price}"
        holder.tv_Name_Show_Phone.text = "name: ${product.name}"

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailPhonesAccessoriesActivity::class.java)
            val productphoneId = product.productphoneId // Lấy productmenId từ sản phẩm hiện tại
            intent.putExtra("productphoneId", productphoneId) // Truyền productmenId qua Intent
            holder.itemView.context.startActivity(intent)
        }
    }
}
