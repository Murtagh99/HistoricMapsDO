package com.example.historicmapsdo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class ChangeMap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_map)
        val radioGroup: RadioGroup = findViewById(R.id.mapChoices)
        val data = intent.getStringArrayListExtra("mapList")
        var id = 5
        data?.forEach {
            val radioButton = RadioButton(this)
            radioButton.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            radioButton.text = it
            radioButton.id = id++
            radioGroup.addView(radioButton)
        }
    }

    fun applyMapChange(view: View){
        val radioGroup: RadioGroup = findViewById(R.id.mapChoices)
        val checkedId: Int = radioGroup.checkedRadioButtonId

        if(checkedId!=-1) {
            val selectedMap = findViewById<RadioButton>(checkedId).text
            setResult(RESULT_OK, Intent().putExtra("selectedMap", selectedMap))
            finish()
        } else {
            Toast.makeText(applicationContext, "Select a Map!", Toast.LENGTH_SHORT).show()
        }
    }
}