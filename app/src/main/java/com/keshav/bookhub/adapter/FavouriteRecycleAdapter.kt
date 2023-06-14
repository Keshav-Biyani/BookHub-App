package com.keshav.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.keshav.bookhub.R
import com.keshav.bookhub.activity.AboutBookActivity
import com.keshav.bookhub.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecycleAdapter(val context: Context, val bookList: List<BookEntity>) :
    RecyclerView.Adapter<FavouriteRecycleAdapter.FavouriteVeiwHolder>() {
    class FavouriteVeiwHolder(veiw: View) : RecyclerView.ViewHolder(veiw) {
        val txtFavBookTitle: TextView = veiw.findViewById(R.id.txtFavBookTitle)
        val txtFavBookAuthor: TextView = veiw.findViewById(R.id.txtFavBookAuthor)
        val txtFavBookPrice: TextView = veiw.findViewById(R.id.txtFavBookPrice)
        val txtFavBookRating: TextView = veiw.findViewById(R.id.txtFavBookRating)
        val imgFavBookImage: ImageView = veiw.findViewById(R.id.imgFavBookImage)
        val llFavContent: LinearLayout = veiw.findViewById(R.id.llFavContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteVeiwHolder {
        val veiw = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_favourate_single_row, parent, false)
        return FavouriteVeiwHolder(veiw)
    }

    override fun onBindViewHolder(holder: FavouriteVeiwHolder, position: Int) {
        val book = bookList[position]
        var  p  = position
        var s = p.toString()
        var i = book.book_id
        var id = i.toString()
        holder.txtFavBookAuthor.text = book.bookAuthor
        holder.txtFavBookTitle.text = book.bookName
        holder.txtFavBookPrice.text = book.bookPrice
        holder.txtFavBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover)
            .into(holder.imgFavBookImage)
        holder.llFavContent.setOnClickListener {
            val intent1 = Intent(context, AboutBookActivity::class.java)
            intent1.putExtra("BookId",id)
            intent1.putExtra("postion",s)
            println(book.book_id)
            println("1212")
            println(s)
            context.startActivity(intent1)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}