package com.example.historicmapsdo

import kotlin.properties.Delegates

class JSONConsumer {
    lateinit var type: String
    lateinit var geometry: Geo
    lateinit var properties: Props

    class Geo {
        lateinit var type: String
        lateinit var coordinates: Array<Double>
    }

    class Props {
        lateinit var name: String
        lateinit var pictureName: String
        var width = 0.0f
        var height = 0.0f
    }
}