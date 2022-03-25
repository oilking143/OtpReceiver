package xyz.betauto.otpreceiver

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class ServiceDemo : Service() {
    private val binder = MyBinder()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    fun getServiceName():String{
        return ServiceDemo::class.java.name
    }

    fun getServiceB():String{
        return "thius is service message"
    }

    inner class MyBinder : Binder() {
        val service: ServiceDemo
            get() = this@ServiceDemo
    }
}