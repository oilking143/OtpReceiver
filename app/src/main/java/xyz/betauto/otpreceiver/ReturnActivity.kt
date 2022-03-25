package xyz.betauto.otpreceiver

import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

class ReturnActivity: AppCompatActivity() {

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val client = OkHttpClient()
    lateinit var pref: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

        setContentView(R.layout.activity_main)


       var mseeageText= intent.getStringExtra("mseeageText")!!
        pref = getSharedPreferences("betauto", AppCompatActivity.MODE_PRIVATE)


        Thread {
            val myJSONObject = JsonObject()
            myJSONObject.addProperty("OTP", "test")
            myJSONObject.addProperty("Mobile", "test")
            val requestBody = myJSONObject.toString().toRequestBody(mediaType)
            val request = Request.Builder()
                    .url("https://bato.0830cm.xyz/Order/OTPReceiver")
                    .post(requestBody)
                    .build()
            var response: Response = client.newCall(request).execute()
            Log.i("response", response.body!!.string())
        }.start()

    }
}