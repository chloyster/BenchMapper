package com.example.chris.benchmapper

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*



/*Christopher Gix
*
*
* So I got alot of what I wanted to done. My main goal for the prototype was to have the maps API up as well as
* ocations stored on firebase. All of that is working, and if you press the fab on the bottom right of the screen, you
* can add a new one (however there is not currently a way to delete them unless you are at the firebase console).
* The settings menu has nothing implemented yet, but it is there and can be accessed by pressing the
* button in the top right. The navigation drawer has a button for the map and for messages, however I am having some
* trouble switching intents right now. The map one wont go away, even though I put it in a frame layout so I will
* have to play round with that some more. Next steps for me will be to get an account system up, and then to implement
* the messaging system which will also use my firebase realtime database. The codes is a bit messy still since alot
* of it was generated from android studio, but I plan to clean it up soon.*/




class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    MapFrag.OnFragmentInteractionListener, BlankFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->

            val database = FirebaseDatabase.getInstance().reference

            var myLat : Double
            var myLong : Double


            val builder = AlertDialog.Builder(this)
            builder.setTitle("Input data")
            builder.setMessage("Latitude")
            val input = EditText(this)

            builder.setView(input)

            builder.setPositiveButton("CONFIRM"){ _, _ ->
                myLat = input.text.toString().toDouble()

                val builder2 = AlertDialog.Builder(this)
                builder2.setTitle("Input data")
                builder2.setMessage("Longitude")
                val input2 = EditText(this)
                builder2.setView(input2)

                builder2.setPositiveButton("CONFIRM"){ _, _ ->
                    myLong = input2.text.toString().toDouble()
                    val key = database.push().key

                    val theLatLong = LatLng(myLat, myLong)


                    database.child(key.toString()).setValue(theLatLong)
                }
                val dialog2 = builder2.create()
                dialog2.show()



            }
            val dialog = builder.create()
            dialog.show()



        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)



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
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val intent = Intent(this, SettingsActivity::class.java)
        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(intent)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.map, MapFrag::class.java.newInstance()).commit()
            }
            R.id.nav_gallery -> {
                val fragmentManager = supportFragmentManager
                fragmentManager.beginTransaction().replace(R.id.map, BlankFragment::class.java.newInstance()).commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
