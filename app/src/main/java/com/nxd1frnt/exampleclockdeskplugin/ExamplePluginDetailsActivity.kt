package com.nxd1frnt.exampleclockdeskplugin

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ExamplePluginDetailsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_details)

        val closeButton = findViewById<Button>(R.id.close_button)
        closeButton.setOnClickListener { finish() }
    }
}