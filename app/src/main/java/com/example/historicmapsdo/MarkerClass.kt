package com.example.historicmapsdo

import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MarkerClass : GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener,
    GoogleMap.OnMarkerClickListener {
    lateinit var mActiveSelectedMarker: Marker
    lateinit var mLastSelectedMarker: LatLng

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
            //Toast.makeText(applicationContext, "Revert to old marker location", Toast.LENGTH_SHORT).show()
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