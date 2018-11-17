package com.example.chris.benchmapper

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MapFrag : Fragment(), OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val start = LatLng(39.0, -95.0)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(start))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 3f))
    }

    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.activity_maps, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val database = FirebaseDatabase.getInstance()
        var dbref = database.reference




        dbref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError){
                TODO("not implemented")
            }

            override fun onDataChange(p0: DataSnapshot){

                /*val handler = Handler()
                handler.postDelayed({
                    mMap.clear()

                    for (member in p0.children){
                        val myLatLong : HashMap<String, Long> = member.value as HashMap<String, Long>

                        val theLat = myLatLong["latitude"]!!.toDouble()

                        val theLong  = myLatLong["longitude"]!!.toDouble()

                        mMap.addMarker(MarkerOptions().position(LatLng(theLat, theLong)))
                    }
                }, 3000)*/




                mMap.clear()

                for (member in p0.children){
                    val myLatLong : HashMap<String, Long> = member.value as HashMap<String, Long>

                    val theLat = myLatLong["latitude"]!!.toDouble()

                    val theLong  = myLatLong["longitude"]!!.toDouble()

                    mMap.addMarker(MarkerOptions().position(LatLng(theLat, theLong)))
                }
            }
        })








        return rootView
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

}
