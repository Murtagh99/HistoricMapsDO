package com.example.historicmapsdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner

class ChangeMap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_map)
        val spinner: Spinner = findViewById(R.id.mapChoices)
        val data = intent.getStringArrayListExtra("mapList")
        data?.add(0, "Standard Karte")
        if (data != null) {
            ArrayAdapter(this, android.R.layout.simple_spinner_item, data.toMutableList()).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        }
    }

    fun applyMapChange(view: View){
        val spinner: Spinner = findViewById(R.id.mapChoices)
        val selectedMap = spinner.selectedItem
        setResult(RESULT_OK, Intent().putExtra("selectedMap", selectedMap as String))
        finish()
    }
}