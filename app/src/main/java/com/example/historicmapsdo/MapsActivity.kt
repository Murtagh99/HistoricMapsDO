package com.example.historicmapsdo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import java.io.File
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var mapOverlay: GroundOverlay

    private val defaultLocationDortmund = LatLng(51.514426, 7.467263)

    private lateinit var mLastSelectedMarker: LatLng
    private lateinit var mActiveSelectedMarker: Marker

    private lateinit var mapStatus: String

    private var mapList: ArrayList<JSONConsumer> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        readAssets()
    }

    private fun readAssets() {
        val maps = assets.list("")?.filter { it.endsWith(".json") }
        maps?.forEach {
            val jsonFileString = getJsonDataFromAsset(applicationContext, it)
            val gson = Gson()
            mapList.add(gson.fromJson(jsonFileString, JSONConsumer::class.java))
        }
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
        val namesOfMapList = arrayListOf<String>()
        mapList.forEach { namesOfMapList.add(it.properties.name) }
        startActivityForResult(Intent(this, ChangeMap::class.java).putStringArrayListExtra("mapList", namesOfMapList), 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mapStatus = if (data?.getStringExtra("selectedMap") != "Standard Karte") data?.getStringExtra("selectedMap")!! else ""
        changeMap()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun changeMap() {
        if (mapStatus != "") {
            val selectedMap: JSONConsumer = mapList.filter { it.properties.name == mapStatus }.first()
            println(selectedMap.geometry.coordinates[1])
            println(selectedMap.properties.width)
            val latLng = LatLng(selectedMap.geometry.coordinates[1], selectedMap.geometry.coordinates[0])
            val mapOverOpt = GroundOverlayOptions()
            mapOverOpt.image(BitmapDescriptorFactory.fromResource(resources.getIdentifier(selectedMap.properties.pictureName, "drawable", applicationContext.packageName)))
            mapOverOpt.position(latLng, selectedMap.properties.width)
            mapOverlay = mMap.addGroundOverlay(mapOverOpt)
        } else {
            if(this::mapOverlay.isInitialized)
                mapOverlay.remove()
        }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    override fun onMarkerDragEnd(p0: Marker?) {}

    override fun onMarkerDragStart(p0: Marker?) {
        if (p0 != null) {
            mLastSelectedMarker = p0.position
        }
    }

    override fun onMarkerDrag(p0: Marker?) {}

    override fun onMapClick(p0: LatLng?) {
        if (p0 is LatLng) {
            // Change Marker Location
            mLastSelectedMarker = mActiveSelectedMarker.position
            mActiveSelectedMarker.position = p0
            // Open Popup Menu

            // check if user wants to change location or whatever
            // if ()

            // else -> revert state
            Toast.makeText(applicationContext, "Revert to old marker location", Toast.LENGTH_SHORT).show()
            mActiveSelectedMarker.position = mLastSelectedMarker
        }
        else {
            println("No LatLng found...")
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return (p0 is Marker)
    }
}