package xyz.betauto.otpreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val extras=intent!!.extras
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val client = OkHttpClient()

        if(extras!=null)
        {
            var prefs = context!!.getSharedPreferences(
                "myPrefs",
                Context.MODE_PRIVATE
            )

            val sms=extras.get("pdus") as Array<Any>
            val format=extras.getString("format")
            for(i in sms.indices)
            {
                var SmsMessages=if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M)
                {
                    SmsMessage.createFromPdu(sms[i] as ByteArray, format)
                }else
                {
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                }


//                val phoneNumber=prefs.getString("phone", GlobalData.Phone)!!
//                val mseeageText=SmsMessages.messageBody.toString()
//
//                Toast.makeText(context, prefs.getString("phone", "")!!, Toast.LENGTH_LONG).show()
//
//                        val myJSONObject = JsonObject()
//                        myJSONObject.addProperty("OTP", mseeageText)
//                        myJSONObject.addProperty("Mobile", prefs.getString("phone", "")!!)
//                        val requestBody = myJSONObject.toString().toRequestBody(mediaType)
//                        val request = Request.Builder()
//                            .url("https://bato.0830cm.xyz/Order/OTPReceiver")
//                            .post(requestBody)
//                            .build()
//                        client.newCall(request).enqueue(object : Callback {
//                            override fun onFailure(call: Call, e: IOException) {
//                                val mCal: Calendar = Calendar.getInstance()
//                                val dateformat = "yyyy-MM-dd hh:mm:ss"
//                                val df = SimpleDateFormat(dateformat)
//                                val today: String = df.format(mCal.time)
//
//                                val failtime=prefs.getString("failtime", "")!!
//
//                                val buffer=StringBuffer(failtime)
//
//                                buffer.append("FailTime:$today _${e}+${call.execute().body}").append(",")
//
//                                prefs.edit()
//                                    .putString("failtime", buffer.toString())
//                                    .apply()
//                            }
//
//                            @Throws(IOException::class)
//                            override fun onResponse(call: Call, response: Response) {
//                                val result = response.body!!.string()
//                                val resJson = JSONObject(result)
//                                val code=resJson.getInt("code")
//                                val msg=resJson.getString("message")
//
//
//                                Log.d("response","$code"+"__"+"$msg")
//
//                                if(code!=0)
//                                {
//                                    val mCal: Calendar = Calendar.getInstance()
//                                    val dateformat = "yyyy-MM-dd hh:mm:ss"
//                                    val df = SimpleDateFormat(dateformat)
//                                    val today: String = df.format(mCal.time)
//
//                                    val failtime=prefs.getString("failtime", "")!!
//
//                                    val buffer=StringBuffer(failtime)
//
//                                    buffer.append("FailTime:$today _$msg").append(",")
//
//                                    prefs.edit()
//                                        .putString("failtime", buffer.toString())
//                                        .apply()
//                                }
//
//                            }
//
//
//                        })





            }

        }

    }


}