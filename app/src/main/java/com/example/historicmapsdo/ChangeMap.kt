package com.example.historicmapsdo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class ChangeMap : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_map)
    }

    fun applyMapChange(view: View){
        val radioGroup: RadioGroup = findViewById(R.id.mapChoices)
        val checkedId = radioGroup.checkedRadioButtonId
        val checkedRadioButton: RadioButton = findViewById(checkedId)
        if(checkedId!=-1) {
            Toast.makeText(applicationContext, "Selected ${checkedRadioButton.text}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "Select a Map!", Toast.LENGTH_SHORT).show()
        }
    }
}