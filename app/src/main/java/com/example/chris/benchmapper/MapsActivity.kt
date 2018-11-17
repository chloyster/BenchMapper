package com.example.chris.benchmapper

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    val CONNECTON_TIMEOUT_MILLISECONDS = 60000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val url = "https://code.org/schools.json"
        GetSchoolsAsyncTask().execute(url)

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val start = LatLngBounds(LatLng(34.919467, -120.823020), LatLng(35.628227, -120.171557))
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(start, 10, 10, 0))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start.center, 10f))
    }





    inner class GetSchoolsAsyncTask : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            // Before doInBackground
        }

        override fun doInBackground(vararg urls: String?): String {
            var urlConnection: HttpURLConnection? = null

            try {
                val url = URL(urls[0])

                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = CONNECTON_TIMEOUT_MILLISECONDS
                urlConnection.readTimeout = CONNECTON_TIMEOUT_MILLISECONDS

                //var inString = streamToString(urlConnection.inputStream)

                // replaces need for streamToString()
                val inString = urlConnection.inputStream.bufferedReader().readText()

                publishProgress(inString)
            } catch (ex: Exception) {
                println("HttpURLConnection exception" + ex)
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect()
                }
            }

            return " "
        }

        override fun onProgressUpdate(vararg values: String?) = try {
            var myJson = JSONObject(values[0])

            var myIter = 0

            val mySchools = myJson.getJSONArray("schools")

            var filteredJson = JSONObject()

            for(i in 0..(mySchools.length() - 1)){
                val item = mySchools.getJSONObject(i)
                val zip = item.get("zip")
                if(zip.toString().startsWith("98")){
                    filteredJson.put(myIter.toString(), item)
                    myIter += 1
                }
            }

            Log.d("Schools", filteredJson.toString())

            for (i in 0..(filteredJson.length() - 1)){
                val theSchool = filteredJson.getJSONObject(i.toString())
                val theLat = theSchool.get("latitude")
                val theLong = theSchool.get("longitude")
                val theName = theSchool.get("name")
                val theCity = theSchool.get("city")
                val theState = theSchool.get("state")
                val theZip = theSchool.get("zip")
                mMap.addMarker(MarkerOptions().position(LatLng(theLat as Double, theLong as Double))
                    .title(theName.toString()).snippet(theCity.toString()
                            + ", " + theState.toString()
                            + " " + theZip.toString()
                            + " " + theLat.toString()
                            + " " + theLong.toString()))
            }

            /*val query = myJson.getJSONObject("query")
            val results = query.getJSONObject("results")
            val channel = results.getJSONObject("channel")

            val location = channel.getJSONObject("location")
            val city = location.get("city")
            val country = location.get("country")

            val humidity = channel.getJSONObject("atmosphere").get("humidity")

            val condition = channel.getJSONObject("item").getJSONObject("condition")
            val temp = condition.get("temp")
            val text = condition.get("text")*/
        } catch (ex: Exception) {
            println("JSON parsing exception" + ex.printStackTrace())
        }
    }
}
