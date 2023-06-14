package com.keshav.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.keshav.bookhub.R
import com.keshav.bookhub.activity.AboutBookActivity

import com.keshav.bookhub.model.Book
import com.squareup.picasso.Picasso

class DashBoardRecycleAdapter(val context: Context, private val listof: ArrayList<Book>) :
    RecyclerView.Adapter<DashBoardRecycleAdapter.DashboardVeiwHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardVeiwHolder {
        val veiw = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycle_dashboard_single_row, parent, false)
        return DashboardVeiwHolder(veiw)
    }

    override fun onBindViewHolder(holder: DashboardVeiwHolder, position: Int) {
        val book = listof[position]
        holder.txtAuthor.text = book.bookAuthor
        holder.txtBookName.text = book.bookName
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.default_book_cover)
            .into(holder.imgBookImage)
        holder.rlContent.setOnClickListener {
            val intent = Intent(context, AboutBookActivity::class.java)
            intent.putExtra("BookId",book.bookId)
            context.startActivity(intent)
//      Toast.makeText(context, "Book", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return listof.size
    }
    class DashboardVeiwHolder(veiw: View) : RecyclerView.ViewHolder(veiw) {
        val txtBookName: TextView = veiw.findViewById(R.id.txtBookName)
        val txtAuthor: TextView = veiw.findViewById(R.id.txtAuthor)
        val txtBookPrice: TextView = veiw.findViewById(R.id.txtBookPrice)
        val txtBookRating: TextView = veiw.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView = veiw.findViewById(R.id.imgBookImage)
        val rlContent: LinearLayout = veiw.findViewById(R.id.rlContent)

    }
}