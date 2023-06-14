package com.keshav.bookhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.keshav.bookhub.*
import com.keshav.bookhub.fragment.AboutFragment
import com.keshav.bookhub.fragment.DashboardFragment
import com.keshav.bookhub.fragment.FavourateFragment
import com.keshav.bookhub.fragment.ProfileFragment

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationVeiw: NavigationView
    var previousmenuitem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frameLayout)
        navigationVeiw = findViewById(R.id.navigationView)
        toolbarSetUp()
        openDashboard()


        navigationVeiw.setNavigationItemSelectedListener {
            if (previousmenuitem != null) {
                previousmenuitem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousmenuitem = it


            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()


                    drawerLayout.closeDrawers()
                }
                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, FavourateFragment())
                        .commit()
                    supportActionBar?.title = " Favorites"
                    drawerLayout.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, ProfileFragment())
                        .commit()
                    supportActionBar?.title = " Profile"
                    drawerLayout.closeDrawers()

                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout, AboutFragment())
                        //.addToBackStack("About")
                        .commit()
                    supportActionBar?.title = "About"
                    drawerLayout.closeDrawers()

                }


            }
            return@setNavigationItemSelectedListener true
        }

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )// it will make action bar toongle working
        drawerLayout.addDrawerListener(actionBarDrawerToggle)//it will attach toongle with drawer layout and create hamburgor icon
        actionBarDrawerToggle.syncState()// it will sync hamburgor with action bar
    }

    fun toolbarSetUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {// it will make functional hamorgur
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)

        }
        return super.onOptionsItemSelected(item)
    }

    fun openDashboard() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, DashboardFragment())
            .commit()
        supportActionBar?.title = "Dashboard"
        navigationVeiw.setCheckedItem(R.id.dashboard)
    }

    override fun onBackPressed() {
        val frag =
            supportFragmentManager.findFragmentById(R.id.frameLayout)// we sill find id of fragment
        when (frag) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }

    }
}