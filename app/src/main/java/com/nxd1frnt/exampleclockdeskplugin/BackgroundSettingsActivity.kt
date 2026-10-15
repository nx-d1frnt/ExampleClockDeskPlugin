package com.nxd1frnt.exampleclockdeskplugin

import android.graphics.Color
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.ChipGroup

class BackgroundSettingsActivity : AppCompatActivity() {

    private val colorMap = mapOf(
        R.id.chip_emerald to 0xFF0D5C3A.toInt(),
        R.id.chip_indigo to 0xFF1A103C.toInt(),
        R.id.chip_amber to 0xFF4A1525.toInt(),
        R.id.chip_crimson to 0xFF3D0C11.toInt(),
        R.id.chip_cyan to 0xFF0A3641.toInt(),
        R.id.chip_midnight to 0xFF151922.toInt(),
        R.id.chip_amoled to 0xFF000000.toInt()
    )

    private var currentColor: Int = ExampleBackgroundReceiver.DEFAULT_COLOR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_background_settings)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        val viewColorPreview = findViewById<FrameLayout>(R.id.view_color_preview)
        val textHexCode = findViewById<TextView>(R.id.text_hex_code)
        val chipGroup = findViewById<ChipGroup>(R.id.palette_chip_group)
        val btnApply = findViewById<MaterialButton>(R.id.btn_apply_color)

        currentColor = ExampleBackgroundReceiver.getSelectedColor(this)

        fun updatePreview(color: Int) {
            currentColor = color
            viewColorPreview.setBackgroundColor(color)
            textHexCode.text = String.format("#%06X", 0xFFFFFF and color)
        }

        updatePreview(currentColor)

        // Select initial chip
        val initialChipId = colorMap.entries.firstOrNull { it.value == currentColor }?.key ?: R.id.chip_emerald
        chipGroup.check(initialChipId)

        chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedId = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            val selectedColor = colorMap[checkedId] ?: return@setOnCheckedStateChangeListener
            updatePreview(selectedColor)
        }

        btnApply.setOnClickListener {
            ExampleBackgroundReceiver.setSelectedColor(this, currentColor)
            ExampleBackgroundReceiver.broadcastUpdate(this, currentColor)
            Toast.makeText(this, R.string.bg_apply_toast, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
