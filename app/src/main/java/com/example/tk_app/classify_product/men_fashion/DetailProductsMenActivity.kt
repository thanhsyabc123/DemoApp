package com.example.tk_app.classify_product.men_fashion
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tk_app.R
import com.example.tk_app.classify_product.CartActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DetailProductsMenActivity : AppCompatActivity() {
    private lateinit var tv_Type_Product_Men: TextView
    private lateinit var tv_Name_Product_Men: TextView
    private lateinit var tv_Price_Product_Men: TextView
    private lateinit var tv_Details_Product_Men: TextView
    private lateinit var tv_Origin_Product_Men: TextView
    private lateinit var tv_Material_Product_Men: TextView
    private lateinit var tv_Quantity_Product_Men: TextView
    private lateinit var ig_Images_Product_Men: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detai_products_men)

        tv_Type_Product_Men = findViewById(R.id.tv_type_product_men)
        tv_Name_Product_Men = findViewById(R.id.tv_name_product_men)
        tv_Price_Product_Men = findViewById(R.id.tv_price_product_men)
        ig_Images_Product_Men = findViewById(R.id.ig_images_product_men)
        tv_Origin_Product_Men = findViewById(R.id.tv_origin_product_men)
        tv_Material_Product_Men = findViewById(R.id.tv_material_product_men)
        tv_Quantity_Product_Men = findViewById(R.id.tv_quantity_product_men)
        tv_Details_Product_Men = findViewById(R.id.tv_details_product_men)
        // Lấy productmenId từ Intent
        val productmenId = intent.getStringExtra("productmenId") ?: ""

        // Tải thông tin sản phẩm từ Firebase bằng productmenId
        val databaseReference =
            FirebaseDatabase.getInstance().reference.child("Product").child("Classify")
                .child("Men_Fashion").child(productmenId)

        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val product = snapshot.getValue(ProductMenFashion::class.java)
                    if (product != null) {
                    // Hiển thị thông tin sản phẩm trong các TextView và ImageView
                    tv_Type_Product_Men.text = "Type: ${product?.type}"
                    tv_Name_Product_Men.text = "Product Name: ${product?.name}"
                    tv_Price_Product_Men.text = "Price: ${product?.price}"
                    tv_Details_Product_Men.text = "Details: ${product?.details}"
                    tv_Origin_Product_Men.text = "Origin: ${product?.origin}"
                    tv_Material_Product_Men.text = "Material: ${product?.material}"
                    tv_Quantity_Product_Men.text = "Quantity: ${product?.quantity}"


                        if (product?.imageUrl != null) {
                        Glide.with(this@DetailProductsMenActivity)
                            .load(product.imageUrl)
                            .into(ig_Images_Product_Men)
                    }
                        val btnShowDetails = findViewById<Button>(R.id.btn_show_details)
                        // Set an onClickListener for the button
                        btnShowDetails.setOnClickListener {
                            val dialogBuilder = AlertDialog.Builder(this@DetailProductsMenActivity)
                            dialogBuilder.setTitle("Product Details")

                            val dialogView = layoutInflater.inflate(R.layout.dialog_product_cart, null)

                            val dialogImageView = dialogView.findViewById<ImageView>(R.id.dialogImageView)
                            val dialogProductName = dialogView.findViewById<TextView>(R.id.dialogProductName)
                            val dialogProductPrice = dialogView.findViewById<TextView>(R.id.dialogProductPrice)
                            val btnDecrease = dialogView.findViewById<Button>(R.id.btnDecrease)
                            val editQuantity = dialogView.findViewById<EditText>(R.id.editQuantity)
                            val btnIncrease = dialogView.findViewById<Button>(R.id.btnIncrease)
                            val tv_Quantity_Show_Add_Men = dialogView.findViewById<TextView>(R.id.tv_quantity_show_add_men)




                            // Load the image into the dialog
                            if (product.imageUrl != null) {
                                Glide.with(this@DetailProductsMenActivity)
                                    .load(product.imageUrl)
                                    .into(dialogImageView)
                            }

                            // Set the product name in the dialog
                            tv_Quantity_Show_Add_Men.text = product.quantity
                            dialogProductName.text = product.name
                            dialogProductPrice.text = "Price: ${product.price}"
                            editQuantity.setText("1")
                            btnDecrease.setOnClickListener {
                                val currentQuantity = editQuantity.text.toString().toInt()
                                if (currentQuantity > 1) {
                                    editQuantity.setText((currentQuantity - 1).toString())
                                }
                            }

                            // Handle quantity increase button
                            btnIncrease.setOnClickListener {
                                val currentQuantity = editQuantity.text.toString().toInt()
                                editQuantity.setText((currentQuantity + 1).toString())
                            }
                            val btnSaveToDatabase = dialogView.findViewById<Button>(R.id.btn_save_to_database)

                            btnSaveToDatabase.setOnClickListener {
                                // Lấy ID người dùng từ Firebase Authentication
                                val userUID = FirebaseAuth.getInstance().currentUser?.uid

                                if (userUID != null) {
                                    // Tạo một đối tượng Firebase Realtime Database
                                    val databaseReference = FirebaseDatabase.getInstance().reference.child("Cart").child("Cart_Fashion").child(userUID)

                                    // Lấy các thuộc tính bạn muốn lưu
                                    val productName = product.name
                                    val productPrice = product.price
                                    val quantity = editQuantity.text.toString()
                                    val imageUrl = product.imageUrl

                                    val productData = mapOf(
                                        "userUID" to userUID,
                                        "name" to productName,
                                        "price" to productPrice,
                                        "quantity" to quantity,
                                        "productmenId" to productmenId,
                                        "status" to "confirmation pending",
                                        "imageUrl" to imageUrl
                                    )
                                    val query = databaseReference.orderByChild("productmenId").equalTo(productmenId)

                                    query.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                for (childSnapshot in dataSnapshot.children) {
                                                    // Cập nhật thông tin sản phẩm trong giỏ hàng và thêm trạng thái "wait"
                                                    childSnapshot.ref.updateChildren(productData + mapOf("status" to "wait"))
                                                }
                                            } else {
                                                // Sản phẩm không tồn tại trong giỏ hàng, tạo mới với trạng thái "wait"
                                                databaseReference.push().setValue(productData + mapOf("status" to "wait"))
                                            }
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            // Xử lý lỗi nếu cần
                                        }
                                    })


                                    val cartIntent = Intent(this@DetailProductsMenActivity, CartActivity::class.java)
                                    startActivity(cartIntent)
                                }
                            }

                            dialogBuilder.setView(dialogView)
                            dialogBuilder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

                            val dialog = dialogBuilder.create()
                            val lp = dialog.window?.attributes
                            lp?.gravity = Gravity.BOTTOM
                            dialog.window?.attributes = lp
                            dialog.show()
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Xử lý lỗi nếu cần
            }
        })
    }
}
