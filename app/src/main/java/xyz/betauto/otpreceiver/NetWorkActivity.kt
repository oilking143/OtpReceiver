package xyz.betauto.otpreceiver

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class NetWorkActivity: AppCompatActivity() {

    private val mLoaclServiceConnection = LoaclServiceConnection()
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}

class LoaclServiceConnection : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, service: IBinder) {
        //透過Binder調用Service內的方法
        val binder = service as ServiceDemo.MyBinder
       Log.d("name", binder.service.getServiceName())
        Log.d("name", binder.service.getServiceB())

    }

    override fun onServiceDisconnected(name: ComponentName) {
        //service 物件設為null
    }

}