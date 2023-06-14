package com.keshav.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.keshav.bookhub.R
import com.keshav.bookhub.adapter.DashBoardRecycleAdapter
import com.keshav.bookhub.adapter.FavouriteRecycleAdapter
import com.keshav.bookhub.database.BookDatabase
import com.keshav.bookhub.database.BookEntity
import com.keshav.bookhub.util.ConnectionManager


class FavourateFragment : Fragment() {
    lateinit var progressLayout: RelativeLayout
    lateinit var progressVar: ProgressBar
    lateinit var recycleFavouite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recycleAdapter: FavouriteRecycleAdapter
    var dbbooklist = listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourate, container, false)

        progressVar = view.findViewById(R.id.progressVar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        recycleFavouite = view.findViewById(R.id.recycleFavourite)
        layoutManager = GridLayoutManager(activity,2)
        dbbooklist = RetriveFavourite(activity as Context).execute().get()

if(activity != null){
    progressLayout.visibility = View.GONE
    recycleAdapter =  FavouriteRecycleAdapter(context as Context,dbbooklist)
    recycleFavouite.adapter = recycleAdapter
    recycleFavouite.layoutManager = layoutManager

}
        return view
    }
    class RetriveFavourite(val context: Context) :  AsyncTask<Void, Void, List<BookEntity>>(){
        override fun doInBackground(vararg params: Void?):  List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()

            return db.bookDao().getAllBook()
        }

    }


}