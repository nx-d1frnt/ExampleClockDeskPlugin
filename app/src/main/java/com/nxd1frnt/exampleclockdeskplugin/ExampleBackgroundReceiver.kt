package com.nxd1frnt.exampleclockdeskplugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ExampleBackgroundReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "ExampleBgReceiver"

        // Actions defined by ClockDesk contract
        const val ACTION_QUERY_BACKGROUND_PLUGINS = "com.nxd1frnt.clockdesk2.ACTION_QUERY_BACKGROUND_PLUGINS"
        const val ACTION_REQUEST_BACKGROUND_DATA = "com.nxd1frnt.clockdesk2.ACTION_REQUEST_BACKGROUND_DATA"
        const val ACTION_UPDATE_BACKGROUND_DATA = "com.nxd1frnt.clockdesk2.ACTION_UPDATE_BACKGROUND_DATA"

        const val CLOCKDESK_PACKAGE = "com.nxd1frnt.clockdesk2"
        const val PLUGIN_ID = "example_palette"

        const val PREFS_NAME = "ExampleBackgroundPrefs"
        const val KEY_SAVED_COLOR = "selected_color"

        // Default: Nordic Emerald (#0D5C3A)
        const val DEFAULT_COLOR = 0xFF0D5C3A.toInt()

        fun getSelectedColor(context: Context): Int {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return prefs.getInt(KEY_SAVED_COLOR, DEFAULT_COLOR)
        }

        fun setSelectedColor(context: Context, color: Int) {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .edit()
                .putInt(KEY_SAVED_COLOR, color)
                .apply()
        }

        fun broadcastUpdate(context: Context, color: Int) {
            val intent = Intent(ACTION_UPDATE_BACKGROUND_DATA).apply {
                setPackage(CLOCKDESK_PACKAGE)
                putExtra("plugin_id", PLUGIN_ID)
                putExtra("plugin_package_name", context.packageName)
                putExtra("color", color)
            }
            context.sendBroadcast(intent)
            Log.d(TAG, "Sent background update: #${String.format("%06X", 0xFFFFFF and color)}")
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d(TAG, "onReceive: action=$action")

        when (action) {
            ACTION_QUERY_BACKGROUND_PLUGINS,
            ACTION_REQUEST_BACKGROUND_DATA -> {
                val color = getSelectedColor(context)
                broadcastUpdate(context, color)
            }
        }
    }
}
