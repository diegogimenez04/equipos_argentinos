package com.example.equiposargentinos.main

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.equiposargentinos.R
import com.example.equiposargentinos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun clickBack(@Suppress("UNUSED_PARAMETER") view: View?) {
        findViewById<TextView>(R.id.toolbar_title).text = getString(R.string.equipos_argentinos)
        findViewById<ImageView>(R.id.iv_go_back).visibility = View.GONE
        onBackPressed()
    }
}