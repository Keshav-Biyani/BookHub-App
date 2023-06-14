package com.keshav.bookhub.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.keshav.bookhub.R
import com.keshav.bookhub.R.drawable.ps_ily
import com.keshav.bookhub.adapter.DashBoardRecycleAdapter
import com.keshav.bookhub.model.Book
import com.keshav.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class DashboardFragment : Fragment() {
    lateinit var recycleDashboard: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recycleAdapter: DashBoardRecycleAdapter
    lateinit var btnInternet: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressVar: ProgressBar
    val bookInfoList: ArrayList<Book> = ArrayList()
    var rattingComparator = Comparator<Book> { book1, book2 ->
       if( book1.bookRating.compareTo(book2.bookRating, true)== 0 ){
           book1.bookName.compareTo(book2.bookName, true)
       }else{
           book1.bookRating.compareTo(book2.bookRating, true)
       }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)// this only need in the fragemnt
/*
        val booklist = arrayListOf<String>(
            "Rich Dad",
            "Smart",
            "Human",
            " People",
            "Mommy",
            "Daddy",
            "Son",
            "Daughter",
            "German",
            "Microsoft"
        )
*/


/*        val bookInfoList = arrayListOf<Book>(
            Book("P.S I Love You", "Cecellia Albern", "Rs244", "4.4", ps_ily),
            Book(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                "Rs. 399",
                "4.1",
                R.drawable.great_gatsby
            ),
            Book("Anna Karenina", "Leo Tolstoy", "Rs. 199", "4.3", R.drawable.anna_kare),
            Book("Madame Bovary", "Gustave Flaubert", "Rs. 500", "4.0", R.drawable.madame),
            Book("War and Peace", "Leo Tolstoy", "Rs. 249", "4.8", R.drawable.war_and_peace),
            Book("Lolita", "Vladimir Nabokov", "Rs. 349", "3.9", R.drawable.lolita),
            Book("Middlemarch", "George Eliot", "Rs. 599", "4.2", R.drawable.middlemarch),
            Book(
                "The Adventures of Huckleberry Finn",
                "Mark Twain",
                "Rs. 699",
                "4.5",
                R.drawable.adventures_finn
            ),
            Book("Moby-Dick", "Herman Melville", "Rs. 499", "4.5", R.drawable.moby_dick),
            Book(
                "The Lord of the Rings",
                "J.R.R Tolkien",
                "Rs. 749",
                "5.0",
                R.drawable.lord_of_rings
            )
        )*/


        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        progressVar = view.findViewById(R.id.progressVar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

//        btnInternet = view.findViewById(R.id.btnInternet)
        /*   btnInternet.setOnClickListener {
               if (ConnectionManager().checkConectivity(activity as Context)) {
                   val dialog = AlertDialog.Builder(activity as Context)
                   dialog.setTitle("Success")
                   dialog.setMessage("Internet Connection Found")
                   dialog.setPositiveButton("Ok") { text, listener ->

                   }
                   dialog.setNegativeButton("Cancel") { text, listener ->

                   }
                   dialog.create()
                   dialog.show()
               } else {
                   val dialog = AlertDialog.Builder(activity as Context)
                   dialog.setTitle("Failiur")
                   dialog.setMessage("No Internet Connection Found")
                   dialog.setPositiveButton("Ok") { text, listener ->

                   }
                   dialog.setNegativeButton("Cancel") { text, listener ->

                   }
                   dialog.create()
                   dialog.show()

               }
           }*/
        if (ConnectionManager().checkConectivity(activity as Context)) {
            val queue = Volley.newRequestQueue(activity as Context)
            val url = getString(R.string.url)
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val success = it.getBoolean("success")
                        if (success) {
                            progressLayout.visibility = View.GONE
                            val data = it.getJSONArray("data")
                            for (i in 0 until data.length()) {
                                val bookJsonbject = data.getJSONObject(i)
                                val bookObject = Book(
                                    bookJsonbject.getString("book_id"),
                                    bookJsonbject.getString("name"),
                                    bookJsonbject.getString("author"),
                                    bookJsonbject.getString("rating"),
                                    bookJsonbject.getString("price"),
                                    bookJsonbject.getString("image")
                                )
                                bookInfoList.add(bookObject)
                                recycleDashboard =
                                    view.findViewById(R.id.recycleDashboard)// its an fragment and we have to give a view
                                layoutManager =
                                    LinearLayoutManager(activity)// we can`t use this because we are in fragemnt
                                recycleAdapter =
                                    DashBoardRecycleAdapter(activity as Context, bookInfoList)
                                recycleDashboard.adapter = recycleAdapter
                                recycleDashboard.layoutManager = layoutManager
                                /*    recycleDashboard.addItemDecoration(
                                        DividerItemDecoration(
                                            recycleDashboard.context,
                                            (layoutManager as LinearLayoutManager).orientation
                                        )
                                    )*/


                            }
                        } else {
                            Toast.makeText(activity as Context, "Some Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(activity as Context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener {
                    if (activity != null) {
                        Toast.makeText(activity as Context, "Error", Toast.LENGTH_SHORT).show()
                    }

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
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failiur")
            dialog.setMessage("No Internet Connection Found")
            dialog.setPositiveButton("open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()

            }
            dialog.setNegativeButton("Cancel") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }

        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater?.inflate(R.menu.menu_dashboard, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if (id == R.id.short_item) {
            Collections.sort(bookInfoList, rattingComparator)

        }
        println(bookInfoList)
        bookInfoList.reverse()
        recycleAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

}