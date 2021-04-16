package com.example.historicmapsdo

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker

class MarkerClass(var activity: Activity) : GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener,
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
            val nummer: Button = activity.findViewById<Button>(R.id.cm_but)
            mLastSelectedMarker = mActiveSelectedMarker.position
            mActiveSelectedMarker.position = p0
            // Open Popup Menu - findViewById(R.id.cm_but)
            val popupMenu: PopupMenu = PopupMenu(activity, nummer)
            popupMenu.menuInflater.inflate(R.menu.popup_menu,popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_switchToNewLocation ->
                        Toast.makeText(activity, "Switching location", Toast.LENGTH_SHORT).show()
                    R.id.action_revertToOldLocation ->
                        mActiveSelectedMarker.position = mLastSelectedMarker
                }
                true
            })
            popupMenu.show()
        }
        else {
            println("No LatLng found...")
        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        return (p0 is Marker)
    }
}