package com.example.historicmapsdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val dortmund = LatLng(51.514426, 7.467263)

    private var mapStatus: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        mMap.addMarker(MarkerOptions().position(dortmund).title("Marker in Dortmund"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dortmund, 15f))
    }

    fun openChangeMap(view: View) {
        startActivityForResult(Intent(this, ChangeMap::class.java), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mapStatus = if (data?.getStringExtra("year") != null) data.getStringExtra("year")!!.toInt() else 0
        changeMap()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun changeMap() {
        val latLng = LatLng(51.514426, 7.467263)
        val mapOver = GroundOverlayOptions()
        when(mapStatus){
            1858 -> {mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_1858))}
            1945 -> {mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_1945))}
            2015 -> {mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_2015))}
        }
        mapOver.position(latLng, 2000f)
        mMap.addGroundOverlay(mapOver)
    }

    /*override fun onResume() {
        Toast.makeText(applicationContext, "Called onResume", Toast.LENGTH_SHORT).show()
        super.onResume()
    }*/
}