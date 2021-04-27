package com.example.historicmapsdo

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import java.net.URL
import java.util.*

class NavigationHandler(var context: Context) {
    private val httpRumpf = "https://maps.googleapis.com/maps/api/directions/json?"
    private val apiKey = context.getString(R.string.directions_api)
    private lateinit var httpRequest: String
    private lateinit var jsonString: String
    lateinit var plo: PolylineOptions
    lateinit var polys: DirectionJsonConsumer

    fun doRequest(origin: LatLng, destination: LatLng){
        httpRequest = httpRumpf + "origin=" + origin.latitude + "," + origin.longitude + "&" + "destination=" + destination.latitude + "," + destination.longitude + "&" + "key=" + apiKey
        plo = PolylineOptions()
        jsonString = URL(httpRequest).readText()
        while (jsonString.isEmpty())
            Thread.sleep(10)
        polys = Gson().fromJson(jsonString, DirectionJsonConsumer::class.java)
        polys.routes.forEach { r -> r.legs.forEach { l -> l.steps.forEach { s -> plo.addAll(PolyUtil.decode(s.polyline.points)) } } }
    }

    class DirectionJsonConsumer {
        //lateinit var geocoded_waypoints: Array<Objects>
        lateinit var routes: Array<Route>
        //lateinit var status: String

        class Route {
            lateinit var bounds: Objects  //declare more fine
            //lateinit var copyrights: String
            lateinit var legs: Array<Leg>
            //lateinit var overview_polyline: Objects
            //lateinit var summary: String
            //lateinit var warnings: Array<Objects>
            //lateinit var waypoint_order: Array<Objects>

            class Leg {
                lateinit var distance: Objects      //declare more fine
                lateinit var duration: Objects      //declare more fine
                lateinit var end_address: String
                lateinit var end_location: LatLng
                lateinit var start_address: String
                lateinit var start_location: LatLng
                lateinit var steps: Array<Step>
                //lateinit var traffic_speed_entry: Array<Objects>
                //lateinit var via_waypoint: Array<Objects>

                class Step {
                    lateinit var distance: Objects
                    lateinit var duration: Objects
                    lateinit var end_location: LatLng
                    lateinit var html_instructions: String
                    lateinit var polyline: PL
                    lateinit var start_location: LatLng
                    lateinit var travel_mode: String

                    class PL {
                        lateinit var points: String
                    }
                }
            }
        }
    }
}