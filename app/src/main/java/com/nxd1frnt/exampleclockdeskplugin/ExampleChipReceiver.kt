package com.nxd1frnt.exampleclockdeskplugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExampleChipReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REQUEST_DATA = "com.nxd1frnt.clockdesk2.ACTION_REQUEST_CHIP_DATA"
        const val ACTION_UPDATE_DATA = "com.nxd1frnt.clockdesk2.ACTION_UPDATE_CHIP_DATA"
        const val CLOCKDESK_PACKAGE = "com.nxd1frnt.clockdesk2"
        const val UPDATE_INTERVAL_SEC = 5

        private val chipTexts = listOf(
            "Hello!",
            "So smart",
            "Such a smart chip",
            "Well, bye!"
        )
        private var currentTextIndex = 0
        private var isHiddenPhase = false
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == ACTION_REQUEST_DATA) {
            Log.d("ExampleChipReceiver", "Received data request from ClockDesk")

            val pendingResult = goAsync()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // Small simulation delay without hitting ClockDesk 6s response timeout
                    Thread.sleep(200)

                    val responseIntent = Intent(ACTION_UPDATE_DATA).apply {
                        setPackage(CLOCKDESK_PACKAGE)
                        putExtra("plugin_package_name", context.packageName)
                        putExtra("chip_click_activity", ".ExamplePluginDetailsActivity")
                        putExtra("update_interval_seconds", UPDATE_INTERVAL_SEC)

                        if (isHiddenPhase) {
                            putExtra("chip_visible", false)
                            isHiddenPhase = false
                            currentTextIndex = 0
                            Log.d("ExampleChipReceiver", "Sent hidden phase update")
                        } else {
                            val text = chipTexts[currentTextIndex]
                            putExtra("chip_visible", true)
                            putExtra("chip_text", text)
                            putExtra("chip_icon_name", "ic_android_black")

                            currentTextIndex++
                            if (currentTextIndex >= chipTexts.size) {
                                currentTextIndex = 0
                                isHiddenPhase = true
                            }
                            Log.d("ExampleChipReceiver", "Sent text: '$text' (visible=true)")
                        }
                    }

                    context.sendBroadcast(responseIntent)

                } catch (e: Exception) {
                    Log.e("ExampleChipReceiver", "Error fetching plugin data", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
