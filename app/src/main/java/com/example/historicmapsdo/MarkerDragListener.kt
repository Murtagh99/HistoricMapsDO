package com.example.historicmapsdo

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MarkerDragListener: GoogleMap.OnMarkerDragListener {
    override fun onMarkerDragStart(p0: Marker?) {
        println("Start")
    }

    override fun onMarkerDrag(marker: Marker){
        println("drag")
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        println("End")
    }
}