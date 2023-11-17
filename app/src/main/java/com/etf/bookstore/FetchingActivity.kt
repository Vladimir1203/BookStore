package com.etf.bookstore;

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FetchingActivity : ComponentActivity(){

    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var tvLoadingData: TextView
    private lateinit var bookList: ArrayList<BookModel>
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching)

        bookRecyclerView = findViewById(R.id.rvBook)
        bookRecyclerView.layoutManager = LinearLayoutManager(this)
        bookRecyclerView.setHasFixedSize(true)
        tvLoadingData = findViewById(R.id.tvLoadingData)

        bookList = arrayListOf<BookModel>()

        getBooksData()

    }

    private fun getBooksData() {

        bookRecyclerView.visibility = View.GONE
        tvLoadingData.visibility = View.VISIBLE

        dbRef = FirebaseDatabase.getInstance().getReference("Books")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookList.clear()
                if (snapshot.exists()){
                    for (empSnap in snapshot.children){
                        val bookData = empSnap.getValue(BookModel::class.java)
                        bookList.add(bookData!!)
                    }
                    val mAdapter = BookAdapter(bookList)
                    bookRecyclerView.adapter = mAdapter

                    mAdapter.setOnItemClickListener(object : BookAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val intent = Intent(this@FetchingActivity, BookDetailsActivity::class.java)

                            //put extras
                            intent.putExtra("bookId", bookList[position].bookId)
                            intent.putExtra("bookName", bookList[position].bookName)
                            intent.putExtra("bookReleaseYear", bookList[position].bookReleaseYear)
                            intent.putExtra("bookPrice", bookList[position].bookPrice)
                            startActivity(intent)
                        }

                    })

                    bookRecyclerView.visibility = View.VISIBLE
                    tvLoadingData.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}
