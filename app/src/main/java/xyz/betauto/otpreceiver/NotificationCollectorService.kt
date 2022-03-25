package xyz.betauto.otpreceiver

import android.annotation.SuppressLint
import android.app.Notification
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
class NotificationCollectorService : NotificationListenerService (){
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)


        Log.i("Oilking", "open-----${sbn!!.packageName}")
        Log.i("Oilking", "open-----${sbn!!.notification}")
        Log.i("Oilking", "open-----${sbn!!.notification.tickerText}")
        Log.i("Oilking", "open-----${sbn!!.notification.extras.get("android.title")}")
        Log.i("Oilking", "open-----${sbn!!.notification.extras.get("android.text")}")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }
}