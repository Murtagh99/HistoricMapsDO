package com.example.historicmapsdo

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapOverlay: GroundOverlay

    private val defaultLocationDortmund = LatLng(51.514426, 7.467263)
    private val markerClass: MarkerClass = MarkerClass(this)

    private var mapStatus: String = "Standard Karte"
    private var mapList: ArrayList<JSONConsumer> = arrayListOf()

    private val req = registerForActivityResult(ActivityResultContracts.RequestPermission()){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
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
        markerClass.mActiveSelectedMarker = mMap.addMarker(
            MarkerOptions().position(
                defaultLocationDortmund
            ).title("Standortmarker").draggable(true)
        )
        mMap.setOnMarkerDragListener(markerClass)
        mMap.setOnMapClickListener(markerClass)
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocationDortmund, 15f))
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            req.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        mMap.isMyLocationEnabled = true
    }

    fun openChangeMap(view: View) {
        val namesOfMapList = arrayListOf<String>()
        mapList.forEach { namesOfMapList.add(it.properties.name) }
        startActivityForResult(
            Intent(this, ChangeMap::class.java).putStringArrayListExtra(
                "mapList",
                namesOfMapList
            ), 1
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mapStatus = if (data != null) data.getStringExtra("selectedMap")!! else "Standard Karte"
        changeMap()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun changeMap() {
        if(this::mapOverlay.isInitialized)
            mapOverlay.remove()
        if (mapStatus != "Standard Karte") {
            val selectedMap: JSONConsumer = mapList.filter { it.properties.name == mapStatus }.first()
            val latLng = LatLng(
                selectedMap.geometry.coordinates[1],
                selectedMap.geometry.coordinates[0]
            )
            val mapOverOpt = GroundOverlayOptions()
            mapOverOpt.image(
                BitmapDescriptorFactory.fromResource(
                    resources.getIdentifier(
                        selectedMap.properties.pictureName,
                        "drawable",
                        applicationContext.packageName
                    )
                )
            )
            mapOverOpt.position(latLng, selectedMap.properties.width)
            mapOverlay = mMap.addGroundOverlay(mapOverOpt)
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
}