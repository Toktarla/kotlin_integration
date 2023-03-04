 package com.example.testing_app

import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel


class MainActivity: FlutterActivity() {
    private lateinit var channel: MethodChannel
    private val BATTERY_CHANNEL = "com.toktarsultan/battery"



    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger,BATTERY_CHANNEL)
        channel.setMethodCallHandler{call,result ->
            if (call.method == "getBatteryLevel"){
                val arguments = call.arguments as Map<String, String>
                val name = arguments["name"];
                val batteryLevel = getBatteryLevel()
                result.success("$name says: $batteryLevel%")

            }
            else if (call.method == "getMessageFromKotlin"){
                val message = getMessage()
                if(message.isNotEmpty()){
                    result.success(message)
                }else{
                    result.error("hey","hey",null)
                }
            }
            else{
                result.notImplemented()
            }
        }


    }

    private fun getBatteryLevel(): Int {
        val batteryLevel: Int = if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            val batteryManager = getSystemService(BATTERY_SERVICE) as BatteryManager
            batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(
                null,
                IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            )
            intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 /
                    intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        }
        return batteryLevel
    }
    private fun getMessage(): String {
        return "Message From Kotlin code Dude! Shut your mouth!"
    }


}
