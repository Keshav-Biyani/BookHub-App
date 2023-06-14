package com.keshav.bookhub.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.keshav.bookhub.R
import com.keshav.bookhub.database.BookDatabase
import com.keshav.bookhub.database.BookEntity
import com.keshav.bookhub.fragment.FavourateFragment
import com.keshav.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import org.json.JSONObject

class AboutBookActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var txtBookReview: TextView
    lateinit var btnAddFav: TextView
    var dbbooklist = listOf<BookEntity>()
    var bookId: String? = "100"
    var position: String? = "-1"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_book)
        txtBookName = findViewById(R.id.txtBookNamee)
        txtAuthor = findViewById(R.id.txtAuthorr)
        txtBookPrice = findViewById(R.id.txtBookPricee)
        txtBookRating = findViewById(R.id.txtBookRatingg)
        imgBookImage = findViewById(R.id.imgBookImagee)
        toolbar = findViewById(R.id.toolbarr)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Review"
        txtBookReview = findViewById(R.id.txtBookReview)
        btnAddFav = findViewById(R.id.btnAddFav)
        if (intent != null) {
            bookId = intent.getStringExtra("BookId")
            position = intent.getStringExtra("postion")
            println(bookId)
            println(position)
            println("1111")

        } else {
            finish()
            Toast.makeText(this@AboutBookActivity, "Some unexpected Eroor ", Toast.LENGTH_SHORT)
                .show()
        }
        if (bookId == "100") {
            finish()
            Toast.makeText(this@AboutBookActivity, "Some unexpected Eroor ", Toast.LENGTH_SHORT)
                .show()

        }


        val queue = Volley.newRequestQueue(this@AboutBookActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"
        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookId)
        if (ConnectionManager().checkConectivity(this@AboutBookActivity)) {
            val jsonObjectRequest =
                @SuppressLint("SetTextI18n")
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonobject = it.getJSONObject("book_data")
                            val bookImageUrl = bookJsonobject.getString("image")
                            Picasso.get().load(bookJsonobject.getString("image"))
                                .error(R.drawable.default_book_cover).into(imgBookImage)

                            txtBookName.text = bookJsonobject.getString("name")
                            txtAuthor.text = bookJsonobject.getString("author")
                            txtBookRating.text = bookJsonobject.getString("rating")
                            txtBookPrice.text = bookJsonobject.getString("price")
                            txtBookReview.text = bookJsonobject.getString("description")
                            val bookEntity = BookEntity(
                                bookId?.toInt() as Int,
                                txtBookName.text.toString(),
                                txtAuthor.text.toString(),
                                txtBookPrice.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookReview.text.toString(),
                                bookImageUrl
                            )
                            val checkFav = DBAsyncTask(applicationContext, bookEntity, 1).execute()
                            val isFav = checkFav.get()
                            if (isFav) {
                                btnAddFav.text = "Remove From Fav"
                                val colorFav =
                                    ContextCompat.getColor(applicationContext, R.color.purple_500)
                                btnAddFav.setBackgroundColor(colorFav)
                            } else {
                                btnAddFav.text = "Add To Fav"
                                val noColorFav =
                                    ContextCompat.getColor(applicationContext, R.color.purple_700)
                                btnAddFav.setBackgroundColor(noColorFav)

                            }
                            btnAddFav.setOnClickListener {
                                if (!DBAsyncTask(applicationContext, bookEntity, 1).execute()
                                        .get()
                                ) {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 2).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@AboutBookActivity,
                                            "Book Added Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        btnAddFav.text = "Remove From Fav"
                                        val colorFav =
                                            ContextCompat.getColor(
                                                applicationContext,
                                                R.color.purple_500
                                            )
                                        btnAddFav.setBackgroundColor(colorFav)
                                    } else {
                                        Toast.makeText(
                                            this@AboutBookActivity,
                                            "Error f",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }


                                } else {
                                    val async =
                                        DBAsyncTask(applicationContext, bookEntity, 3).execute()
                                    val result = async.get()
                                    if (result) {
                                        Toast.makeText(
                                            this@AboutBookActivity,
                                            "Book Removed Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        btnAddFav.text = "Add To Fav"
                                        val noColorFav =
                                            ContextCompat.getColor(
                                                applicationContext,
                                                R.color.purple_700
                                            )
                                        btnAddFav.setBackgroundColor(noColorFav)
                                    } else {
                                        Toast.makeText(
                                            this@AboutBookActivity,
                                            "Error s",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }

                                }
                            }
                        } else {
                            Toast.makeText(this@AboutBookActivity, "Error T", Toast.LENGTH_SHORT)
                                .show()
                            dbbooklist = FavourateFragment.RetriveFavourite(this).execute().get()

                            val s = position?.toInt()

                            val book1 = dbbooklist[s!!]
                            println(book1)
                            Picasso.get().load(book1.bookImage)
                                .error(R.drawable.default_book_cover).into(imgBookImage)

                            txtBookName.text = book1.bookName
                            txtAuthor.text = book1.bookAuthor
                            txtBookRating.text = book1.bookRating
                            txtBookPrice.text = book1.bookPrice
                            txtBookReview.text = book1.bookDesc


                        }
                    } catch (e: Exception) {
                        Toast.makeText(this@AboutBookActivity, "Error", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(this@AboutBookActivity, "Error", Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "4ba2f2f19161d2"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        } else {
            Toast.makeText(this@AboutBookActivity, "Internet Error", Toast.LENGTH_SHORT).show()
            dbbooklist = FavourateFragment.RetriveFavourite(this).execute().get()

            val s = position?.toInt()

            val book1 = dbbooklist[s!!]
            println(book1)
            Picasso.get().load(book1.bookImage)
                .error(R.drawable.default_book_cover).into(imgBookImage)

            txtBookName.text = book1.bookName
            txtAuthor.text = book1.bookAuthor
            txtBookRating.text = book1.bookRating
            txtBookPrice.text = book1.bookPrice
            txtBookReview.text = book1.bookDesc
        }
    }

    class DBAsyncTask(val context: Context, val bookEntity: BookEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {
        //context given so that we add book from button it will know us about click of button
        /*
        Mode1 -> check DB if favourite is added or not
        Mode2-> Save book in DB
        Mode3->Remove the Favourite book
        */
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()
            when (mode) {
                1 -> {
                    //check DB if favourite is added or not
                    val book: BookEntity? = db.bookDao().getBookById(bookEntity.book_id.toString())
                    db.close()
                    return book != null// if book is not equal to null it will give true
                }
                2 -> {
                    // Save book in DB
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //Remove the Favourite book
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }
            return false

        }

    }
}