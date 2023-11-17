package com.etf.bookstore;

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertionActivity : ComponentActivity(){

    private lateinit var etBookName: EditText
    private lateinit var etBookReleaseYear: EditText
    private lateinit var etBookPrice: EditText
    private lateinit var btnSaveData: Button

    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insertion)
        etBookName = findViewById(R.id.etBookName)
        etBookReleaseYear = findViewById(R.id.etBookReleaseYear)
        etBookPrice = findViewById(R.id.etBookPrice)
        btnSaveData = findViewById(R.id.btnSave)

        dbRef = FirebaseDatabase.getInstance().getReference("Books")

        btnSaveData.setOnClickListener {
            saveBookData()
        }
    }
    private fun saveBookData() {

        //getting values
        val bookName = etBookName.text.toString()
        val bookReleaseYear = etBookReleaseYear.text.toString()
        val bookPrice = etBookPrice.text.toString()

        var isValid = true

        if (bookName.isEmpty()) {
            etBookName.error = "Please enter name"
            isValid = false;
        }
        if (bookReleaseYear.isEmpty()) {
            etBookReleaseYear.error = "Please enter release year"
            isValid = false;
        }
        if (bookPrice.isEmpty()) {
            etBookPrice.error = "Please enter price"
            isValid = false;
        }
        if (isValid) {
            val bookId = dbRef.push().key!!

            val book = BookModel(bookId, bookName, bookReleaseYear, bookPrice)

            dbRef.child(bookId).setValue(book)
                .addOnCompleteListener {
                    Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_LONG).show()

                    etBookName.text.clear()
                    etBookReleaseYear.text.clear()
                    etBookPrice.text.clear()


                }.addOnFailureListener { err ->
                    Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}