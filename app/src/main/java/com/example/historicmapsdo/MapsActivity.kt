package com.example.historicmapsdo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private val defaultLocationDortmund = LatLng(51.514426, 7.467263)

    private lateinit var mLastSelectedMarker: Marker
    private lateinit var mActiveSelectedMarker : Marker

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

        mActiveSelectedMarker = mMap.addMarker(MarkerOptions().position(defaultLocationDortmund).title("Marker in Dortmund").draggable(true))
        mMap.setOnMarkerDragListener(this)
        mMap.setOnMapClickListener(this)
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocationDortmund, 15f))
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
        if (mapStatus != 0) {
            val latLng = LatLng(51.51399991201712, 7.4639976024627686)
            val mapOver = GroundOverlayOptions()
            when (mapStatus) {
                1858 -> {
                    mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_1858))
                }
                1945 -> {
                    mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_1945))
                }
                2015 -> {
                    mapOver.image(BitmapDescriptorFactory.fromResource(R.drawable.dortmund_2015))
                }
            }
            mapOver.position(latLng, 1375f)
            mMap.addGroundOverlay(mapOver)
        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {}

    override fun onMarkerDragStart(p0: Marker?) {
        if (p0 != null) {
            mLastSelectedMarker = p0
        }
    }

    override fun onMarkerDrag(p0: Marker?) {}

    override fun onMapClick(p0: LatLng?) {
        if (p0 is LatLng) {
  //          mActiveSelectedMarker = mMap.addMarker(MarkerOptions().position(p0).title("Marker in Dortmund").draggable(true))
            mActiveSelectedMarker.position = p0
        }
        else {
            println("No LatLng found...")
        }
    }
}