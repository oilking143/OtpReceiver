package xyz.betauto.otpreceiver

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException
import java.net.NetworkInterface
import java.net.SocketException
import org.json.JSONObject

import android.R.attr.name
import org.json.JSONArray


class HardWareActivity: AppCompatActivity() {
    lateinit var mac: TextView
    lateinit var imei:TextView
    var client = OkHttpClient()
    var flag=0
    @SuppressLint("HardwareIds")
    var androId = ""
    lateinit var resulJson:JSONObject
    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_harware)
        mac=findViewById(R.id.mac)
        imei=findViewById(R.id.imei)

        var wifi= applicationContext.getSystemService(WIFI_SERVICE) as WifiManager;
        androId=Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

//        checkPermission()

        getWholeData()

    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun checkPermission() {

        val checkPermission = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_PHONE_STATE)

        if(checkPermission == PackageManager.PERMISSION_GRANTED)
        {
            telephoneManagerDetails()
        }
        else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE,android.Manifest.permission.ACCESS_WIFI_STATE
                ,android.Manifest.permission.READ_SMS), 1 )
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) {
            1 -> {

                if(grantResults.size >=0 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                {
                    telephoneManagerDetails()
                }
                else
                {
                    Toast.makeText(applicationContext,"You don't have permission", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun getWholeData()
    {
        val request: Request = Request.Builder()
            .url("https://script.google.com/macros/s/AKfycbx1h5cBh_khLfYZIxiHlChUWzP44J6Gdes9lhf4G-XLNYiwphs/exec")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
              getWholeData()

            }

            @RequiresApi(Build.VERSION_CODES.P)
            override fun onResponse(call: Call, response: Response) {
                val result = response.body!!.string()

                resulJson = JSONObject(result)
                var user=resulJson.get("user") as JSONArray
                for(i in 0 until user.length())
                {

                    val json =  user[i] as JSONObject
                    if(androId==json.get("name").toString())
                    {
                       flag++
                    }

                }

             if(flag==0)
             {
                 Thread {
                     val msg = Message()
                     msg.what = 0
                     mHandler.sendMessage(msg)

                 }.start()
             }
                else{
                 Thread {
                     val msg = Message()
                     msg.what = 4
                     mHandler.sendMessage(msg)

                 }.start()
             }

            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("MissingPermission", "HardwareIds")
    private fun telephoneManagerDetails() {

        val tm  = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var networkOperatorName=""
        val telManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            mac.text=getMacAddress()
            imei.text=androId
            networkOperatorName=telManager.networkOperatorName
        } else {
            val deviceId: String = tm.getDeviceId()
            imei.text=deviceId
        }

//      https://script.google.com/macros/s/AKfycbzkNv2pVEJ_TpFylqwj1cRMSybAyCo4OxT9WKclFFaVrx5bax83/exec

            val url: HttpUrl = HttpUrl.Builder()
                .scheme("https")
                .host("script.google.com")
                .addPathSegment("macros")
                .addPathSegment("s")
                .addPathSegment("AKfycbzkNv2pVEJ_TpFylqwj1cRMSybAyCo4OxT9WKclFFaVrx5bax83")
                .addPathSegment("exec")
                .addQueryParameter("url", "https://docs.google.com/spreadsheets/d/1OYZEb9max4UH5fr8VOExdXY0ITyT-6fwjS9KL7OKVuI/edit#gid=0")
                .addQueryParameter("data", getMacAddress()+","+androId+","+networkOperatorName)
                .addQueryParameter("row", "2")
                .addQueryParameter("name", "工作表1")
                .addQueryParameter("column", "3")
                .addQueryParameter("insertType", "bottom")
                .build()

            val request: Request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("result","${call.request().body}")
                    Thread {
                        val msg = Message()
                        msg.what = 0
                        mHandler.sendMessage(msg)

                    }.start()
                }

                override fun onResponse(call: Call, response: Response) {
                    val result = response.body!!.string()
                    Log.d("result",result)
                    Thread {
                        val msg = Message()
                        msg.what = 1
                        mHandler.sendMessage(msg)

                    }.start()
                }

            })

    }

    private val mHandler = object : Handler(Looper.getMainLooper()) {

        @RequiresApi(Build.VERSION_CODES.P)
        override fun handleMessage(msg: Message) {

            when(msg.what)
            {
                0 -> {
                    checkPermission()

                }

                1 -> {

                    Toast.makeText(this@HardWareActivity,"通信成功",Toast.LENGTH_LONG).show()

                }

                2->{
                    telephoneManagerDetails()
                }

                4->{
                    Toast.makeText(this@HardWareActivity,"已送出帳號",Toast.LENGTH_LONG).show()
                }

            }
        }

    }

    fun getMacAddress(): String? {
        var macAddress: String? = null
        val buf = StringBuffer()
        var networkInterface: NetworkInterface? = null
        try {
            networkInterface = NetworkInterface.getByName("eth1")
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0")
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02"
            }
            macAddress = if(networkInterface.hardwareAddress !=null) {
                val addr: ByteArray = networkInterface.hardwareAddress
                for (b in addr) {
                    buf.append(String.format("%02X:", b))
                }
                if (buf.isNotEmpty()) {
                    buf.deleteCharAt(buf.length - 1)
                }
                buf.toString()
            } else {
                "null"
            }


        } catch (e: SocketException) {
            e.printStackTrace()
            return "02:00:00:00:00:02"
        }
        return macAddress
    }

}