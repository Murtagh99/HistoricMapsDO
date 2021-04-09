package com.example.historicmapsdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*

class ChangeMap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_map)
        val spinner: Spinner = findViewById(R.id.mapChoices)
        val data = intent.getStringArrayListExtra("mapList")
        data?.add(0, "Standard Karte")
        var id = 5
        if (data != null) {
            ArrayAdapter(this, android.R.layout.simple_spinner_item, data.toMutableList()).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner.adapter = adapter
            }
        }
        //spinner.adapter = if (data != null) ArrayAdapter(this, android.R.layout.simple_spinner_item, data.toMutableList()) else null
        /*data?.forEach {
            val radioButton = RadioButton(this)
            radioButton.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            radioButton.text = it
            radioButton.id = id++
            radioGroup.addView(radioButton)
        }*/
    }

    fun applyMapChange(view: View){
        val spinner: Spinner = findViewById(R.id.mapChoices)
        val selectedMap = spinner.selectedItem
        setResult(RESULT_OK, Intent().putExtra("selectedMap", selectedMap as String))
        finish()
    }
}