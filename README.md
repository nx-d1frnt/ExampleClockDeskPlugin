# 📦 Example ClockDesk SmartChips Plugin

This repository demonstrates how to build a **SmartChips plugin** for [ClockDesk](https://github.com/nxd1frnt/clockdesk).  
SmartChips plugins are external Android applications that ClockDesk discovers and communicates with using `BroadcastReceiver`.  
With this architecture, developers can create and distribute plugins as standard APKs, enabling users to display custom data chips on their home screen.

---

## 🚀 How It Works

1. **Discovery (`ACTION_QUERY_SMART_CHIPS`)**  
   ClockDesk queries the system for apps that can receive this broadcast. This is how it finds your plugin.

2. **Metadata**  
   ClockDesk reads the meta-data from your `AndroidManifest.xml` to locate your `plugin_info.xml` file.  
   This file provides the plugin’s name (e.g., *Weather Plugin*) for ClockDesk’s settings.

3. **User Enablement**  
   The user enables your plugin in ClockDesk’s settings.

4. **Data Request (`ACTION_REQUEST_CHIP_DATA`)**  
   ClockDesk sends this broadcast when it needs data from your plugin. It may include useful extras, such as the user’s last known `Location`.

5. **Data Response (`ACTION_UPDATE_DATA`)**  
   Your plugin performs its work (e.g., API call, computation) and sends a broadcast back to ClockDesk with the chip data to display.

---

## 📂 Project Structure

    app/ 
    ├── src/main/
    │ 
    ├── AndroidManifest.xml # Declares the plugin receiver and activity 
    │ ├── java/com/nxd1frnt/exampleclockdeskplugin/ 
    │ │ ├── ExampleChipReceiver.kt # Handles data requests and responses 
    │ │ └── ExamplePluginDetailsActivity.kt # Simple activity opened when chip is clicked
    │ ├── res/layout/activity_plugin_details.xml # UI layout for details activity
    │ └── res/xml/plugin_info.xml # Metadata for ClockDesk settings


---

## 🛠️ Key Components

### 1. `AndroidManifest.xml`
Declares the plugin receiver and activity:

```xml
<receiver
    android:name=".ExampleChipReceiver"
    android:exported="true">

    <intent-filter>
        <action android:name="com.nxd1frnt.clockdesk2.ACTION_QUERY_SMART_CHIPS" />
    </intent-filter>

    <intent-filter>
        <action android:name="com.nxd1frnt.clockdesk2.ACTION_REQUEST_CHIP_DATA" />
    </intent-filter>

    <meta-data
        android:name="com.nxd1frnt.clockdesk2.smartchip.PLUGIN_RECEIVER"
        android:resource="@xml/plugin_info" />
</receiver>
```

### 2. `plugin_info.xml`
Provides metadata for ClockDesk’s settings screen:

```xml
<smart-chip-plugin
    xmlns:android="http://schemas.android.com/apk/res/android"
    preferenceKey="show_example_plugin"
    displayName="Example Plugin"
    priority="5"/>
```

### 3. `ExampleChipReceiver.kt`
Handles data requests and sends responses back to ClockDesk:

```xml
val responseIntent = Intent(ACTION_UPDATE_DATA).apply {
    setPackage(CLOCKDESK_PACKAGE)
    putExtra("chip_visible", true)
    putExtra("plugin_package_name", context.packageName)
    putExtra("chip_text", "Smart chip plugin test")
    putExtra("chip_icon_name", "ic_android_black")
    putExtra("chip_click_activity", ".ExamplePluginDetailsActivity")
}
context.sendBroadcast(responseIntent)
```

### 4. `ExamplePluginDetailsActivity.kt`
A simple optional activity that opens when the chip is clicked

## 🧪 Testing Your Plugin

#### 1. Add an Icon
   Place a drawable resource in `res/drawable/` with the exact name you specify (e.g., `ic_android_black.xml`).

#### 2. Install Both Apps
Install ClockDesk and your plugin APK on the same device/emulator.

#### 3. Enable the Plugin:

- Open ClockDesk

- Go to settings

- Enable your plugin by toggling its display name

#### 4. Trigger a Refresh
Wait for ClockDesk’s refresh cycle or force one if available.

#### 5. Check Logs
Use Logcat to monitor your plugin’s logs. Filter by `ExampleChipReceiver` to verify data requests and responses.

#### Use this repo as a template to build your own SmartChips plugin with custom data sources (weather, stocks, reminders, etc.).
