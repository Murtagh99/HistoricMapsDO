package com.example.historicmapsdo

import android.content.Intent
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
        val checkedId: Int = radioGroup.checkedRadioButtonId

        if(checkedId!=-1) {
            val year = findViewById<RadioButton>(checkedId).text.substring(9)
            if (year.toIntOrNull() != null)
                setResult(RESULT_OK, Intent().putExtra("year", year))
            finish()
        } else {
            Toast.makeText(applicationContext, "Select a Map!", Toast.LENGTH_SHORT).show()
        }
    }
}