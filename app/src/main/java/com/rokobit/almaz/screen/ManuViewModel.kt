package com.rokobit.almaz.screen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.net.NetworkInfo
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MenuViewModel : ViewModel() {
    
    private val printerName = java.lang.String.format("\"%s\"", "CNC Almaz")

    private val printerConnectionLive = MutableLiveData<Boolean>(false)

    fun printerConnection(context: Context): LiveData<Boolean> {
        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager

        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val wifiInfo = wifiManager.connectionInfo

                if (wifiInfo.ssid == printerName) {
                    printerConnectionLive.postValue(true)
                }
                else {
                    printerConnectionLive.postValue(false)
                }
            }
        }, intentFilter)

        val wifiInfo = wifiManager.connectionInfo

        if (wifiInfo.ssid == printerName) {
            printerConnectionLive.postValue(true)
        }
        else {
            printerConnectionLive.postValue(false)
        }

        return printerConnectionLive
    }

    fun connectPrinter(context: Context) {
        val wifiConfig = WifiConfiguration()
        wifiConfig.SSID = printerName
        wifiConfig.preSharedKey = String.format("\"%s\"", "123456789")

        val wifiManager = context.getSystemService(WIFI_SERVICE) as WifiManager?

        val netId = wifiManager!!.addNetwork(wifiConfig)
        wifiManager.disconnect()
        wifiManager.enableNetwork(netId, true)
        wifiManager.reconnect()
    }

}