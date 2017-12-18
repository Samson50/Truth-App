package com.example.jacob.navdrawer

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_nav_drawer.*
import kotlinx.android.synthetic.main.app_bar_nav_drawer.*
//https://developers.google.com/identity/sign-in/android/start-integrating



class NavDrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var userState = "IL"

    fun setState(state:String) {
        userState = state
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.flContent, HomeFragment.newInstance(), "Default")
                    .commit()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        var fragment: Fragment? = null
        when (item.itemId) {
            R.id.legis_profile -> {
                fragment = LegislatorsListFragment.newInstance(userState)
            }
            R.id.home_menu -> {
                fragment = HomeFragment.newInstance()
            }
            R.id.bills_list -> {
                fragment = BillsListFragment.newInstance()
            }
            else -> {
                fragment = LegislatorFragment.newInstance()
            }
        }

        // Insert the fragment by replacing any existing fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit()

        // Highlight the selected item has been done by NavigationView
        item.isChecked = true
        // Set action bar title
        title = item.title
        // Close the navigation drawer
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
