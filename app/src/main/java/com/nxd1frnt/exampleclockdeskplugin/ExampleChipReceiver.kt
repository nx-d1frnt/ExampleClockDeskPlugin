package com.nxd1frnt.exampleclockdeskplugin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Location
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExampleChipReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_REQUEST_DATA = "com.nxd1frnt.clockdesk2.ACTION_REQUEST_CHIP_DATA"
        const val ACTION_UPDATE_DATA = "com.nxd1frnt.clockdesk2.ACTION_UPDATE_CHIP_DATA"
        const val CLOCKDESK_PACKAGE = "com.nxd1frnt.clockdesk2"
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Check if this is a request for data
        if (intent.action == ACTION_REQUEST_DATA) {
            Log.d("ExampleChipReceiver", "Received data request from ClockDesk")

            // Get the location provided by ClockDesk, if available
            val location = intent.getParcelableExtra<Location>("location")
            if (location != null) {
                Log.d("ExampleChipReceiver", "Received location: ${location.latitude}, ${location.longitude}")
            } else {
                Log.w("ExampleChipReceiver", "Location data was null")
            }

            // Use goAsync() to perform work on a background thread
            // A BroadcastReceiver's onReceive() has a 10-second limit on the main thread
            val pendingResult = goAsync()

            GlobalScope.launch(Dispatchers.IO) {
                try {
                    // --- This is where you do your plugin's work ---

                    // 1. (Simulate) Fetch data from a weather API using the location
                    // In a real app, you'd use Retrofit, OkHttp, etc.
                    // val weatherResponse = MyWeatherApiClient.fetchWeather(location)
                    // val weatherText = "${weatherResponse.temperature}°C"
                    // val weatherIcon = "ic_weather_${weatherResponse.condition}"

                    // For this example, we'll just return static data
                    Thread.sleep(1000) // Simulate network delay
                    val Text = "Smart chip plugin test"

                    // This icon name must match a drawable in this plugins res/drawable/
                    // e.g., res/drawable/ic_android_black.xml
                    val IconName = "ic_android_black"

                    // -----------------------------------------------------

                    // 2. Send the data back to ClockDesk
                    val responseIntent = Intent(ACTION_UPDATE_DATA).apply {
                        // IMPORTANT: Set the package to ensure only ClockDesk receives this
                        setPackage(CLOCKDESK_PACKAGE)

                        // Add the required data as extras
                        // plugin_package_name tells ClockDesk which plugin this data is from
                        putExtra("chip_visible", true) // Whether to show or hide the chip
                        putExtra("plugin_package_name", context.packageName)
                        putExtra("chip_text", Text)
                        putExtra("chip_icon_name", IconName)
                        putExtra("chip_click_activity", ".ExamplePluginDetailsActivity") // The activity to open on click
                    }

                    context.sendBroadcast(responseIntent)
                    Log.d("ExampleChipReceiver", "Sent data back to ClockDesk")

                } catch (e: Exception) {
                    // Handle any errors (e.g., network timeout)
                    Log.e("ExampleChipReceiver", "Error fetching plugin data", e)
                    // You could send a broadcast with error info if needed
                } finally {
                    // 3. Finish the async operation
                    pendingResult.finish()
                }
            }
        }
    }
}
