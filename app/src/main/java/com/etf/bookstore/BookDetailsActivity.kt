package com.etf.bookstore

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.FirebaseDatabase


class BookDetailsActivity : ComponentActivity(){

    private lateinit var tvBookId: TextView
    private lateinit var tvBookName: TextView
    private lateinit var tvBookReleaseYear: TextView
    private lateinit var tvBookPrice: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        initView()
        setValuesToViews()
        btnUpdate.setOnClickListener {
            openUpdateDialog(
                intent.getStringExtra("bookId").toString(),
                intent.getStringExtra("bookName").toString()
            )
        }

        btnDelete.setOnClickListener {
            deleteRecord(
                intent.getStringExtra("bookId").toString()
            )
        }


    }

    private fun deleteRecord(
        id: String
    ){
        val dbRef = FirebaseDatabase.getInstance().getReference("Books").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Book data deleted", Toast.LENGTH_LONG).show()

            val intent = Intent(this, FetchingActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{ error ->
            Toast.makeText(this, "Deleting Err ${error.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun openUpdateDialog(bookId: String, bookName: String) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val etBookName = mDialogView.findViewById<EditText>(R.id.etBookName)
        val etBookReleaseYear = mDialogView.findViewById<EditText>(R.id.etBookReleaseYear)
        val etBookPrice = mDialogView.findViewById<EditText>(R.id.etBookPrice)

        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        etBookName.setText(intent.getStringExtra("bookName").toString())
        etBookReleaseYear.setText(intent.getStringExtra("bookReleaseYear").toString())
        etBookPrice.setText(intent.getStringExtra("bookPrice").toString())

        mDialog.setTitle("Updating $bookName Record")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updateBookData(
                bookId,
                etBookName.text.toString(),
                etBookReleaseYear.text.toString(),
                etBookPrice.text.toString()
            )

            Toast.makeText(applicationContext, "Book Data Updated", Toast.LENGTH_LONG).show()

            //we are setting updated data to our textviews
            tvBookName.text = etBookName.text.toString()
            tvBookReleaseYear.text = etBookReleaseYear.text.toString()
            tvBookPrice.text = etBookPrice.text.toString()

            alertDialog.dismiss()
        }
    }

    private fun updateBookData(
        id: String,
        name: String,
        releaseYear: String,
        price: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Books").child(id)
        val empInfo = BookModel(id, name, releaseYear, price)
        dbRef.setValue(empInfo)
    }

    private fun initView() {
        tvBookId = findViewById(R.id.tvBookId)
        tvBookName = findViewById(R.id.tvBookName)
        tvBookReleaseYear = findViewById(R.id.tvBookReleaseYear)
        tvBookPrice = findViewById(R.id.tvBookPrice)

        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)
    }

    private fun setValuesToViews() {
        tvBookId.text = intent.getStringExtra("bookId")
        tvBookName.text = intent.getStringExtra("bookName")
        tvBookReleaseYear.text = intent.getStringExtra("bookReleaseYear")
        tvBookPrice.text = intent.getStringExtra("bookPrice")

    }
}