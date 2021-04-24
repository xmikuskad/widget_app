package com.falconsocka.mywidgetapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myDate = Calendar.getInstance()
        println(myDate.get(Calendar.DAY_OF_YEAR))
        //val day = myDate.get()
    }
}