package xyz.betauto.otpreceiver

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.hardware.usb.UsbDevice.getDeviceId
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.VelocityTrackerCompat.getXVelocity
import androidx.core.view.VelocityTrackerCompat.getYVelocity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import java.util.*
import kotlin.concurrent.timerTask
import android.telephony.TelephonyManager
import androidx.fragment.app.FragmentActivity


class MainActivity : AppCompatActivity() {
    val devtest="testtttt"
    val REQ_CODE_CONTACT = 2
    val mediaType = "application/json; charset=utf-8".toMediaType()
    val client = OkHttpClient()
    lateinit var set:Button
    lateinit var cancel:Button
    lateinit var confirm:Button
    lateinit var gesture:Button
    lateinit var failtext:TextView
    lateinit var editText:EditText
    lateinit var pref: SharedPreferences
    private var mVelocityTracker: VelocityTracker? = null
    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupPermission()

        set=findViewById(R.id.set_btn)
        cancel=findViewById(R.id.set_cancel)
        confirm=findViewById(R.id.set_confirm)
        editText=findViewById(R.id.editText)
        failtext=findViewById(R.id.failtime)
        gesture=findViewById(R.id.gerture_btn)


        pref = getSharedPreferences("myPrefs", AppCompatActivity.MODE_PRIVATE)

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val failtime=pref.getString("failtime", "")!!.split(",")

        if(pref.getString("phone", "")!="")
        {
            editText.setText(pref.getString("phone", "")!!)
        }

        if(pref.getString("message", "")!="")
        {
            failtext.text=pref.getString("message", "")
        }



//        if(failtime.isNotEmpty())
//        {
//            when(failtime.size)
//            {
//                1->{
//                    failtext.text=failtime[failtime.size-1]
//                }
//
//                2->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]
//                }
//
//                3->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]+"\n"+failtime[failtime.size-3]
//                }
//
//                4->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]+
//                                  "\n"+failtime[failtime.size-3]+"\n"+failtime[failtime.size-4]
//                }
//
//                5->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]+
//                            "\n"+failtime[failtime.size-3]+"\n"+failtime[failtime.size-4]+"\n"+failtime[failtime.size-5]
//                }
//
//                6->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]+
//                            "\n"+failtime[failtime.size-3]+"\n"+failtime[failtime.size-4]+"\n"+failtime[failtime.size-5]+
//                    "\n"+failtime[failtime.size-6]
//                }
//                else->{
//                    failtext.text=failtime[failtime.size-1]+"\n"+failtime[failtime.size-2]+
//                            "\n"+failtime[failtime.size-3]+"\n"+failtime[failtime.size-4]+"\n"+failtime[failtime.size-5]+
//                            "\n"+failtime[failtime.size-6]
//                }
//            }
//
//        }
        Timer().schedule(timerTask {
            if(connectivityManager.activeNetwork==null)
            {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder!!.setMessage("There is no internet service")
                builder!!.setPositiveButton("确定", DialogInterface.OnClickListener { dialog, _ ->

                    dialog.dismiss()
                    finish()
                })
                builder!!.show()
            }

        }, 60000)

        gesture.setOnClickListener {

            val intent = Intent(this, TouchActivity::class.java)
            startActivity(intent)

        }

        set.setOnClickListener {

            editText.isEnabled=true
            editText.hint="Please Enter Your PhoneNumber"
            cancel.visibility=View.VISIBLE
            confirm.visibility=View.VISIBLE
            set.visibility=View.GONE
        }
        cancel.setOnClickListener{

            editText.isEnabled=false
            if(pref.getString("phone", "")!="")
            {
                editText.hint="press set to set phone number"
            }
            else
            {
                editText.hint=pref.getString("phone", "")
            }

            cancel.visibility=View.GONE
            confirm.visibility=View.GONE
            set.visibility=View.VISIBLE
        }

        confirm.setOnClickListener {
            pref.edit()
                    .putString("phone", editText.text.toString())
                    .apply()

            editText.isEnabled=false
            if(pref.getString("phone", "")!="")
            {
                editText.hint="press set to set phone number"
            }
            cancel.visibility=View.GONE
            confirm.visibility=View.GONE
            set.visibility=View.VISIBLE
            Toast.makeText(this, pref.getString("phone", "")!!, Toast.LENGTH_LONG).show()
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                     // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    computeCurrentVelocity(1000)
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    Log.d("", "X velocity: ${getXVelocity(pointerId)}")
                    Log.d("", "Y velocity: ${getYVelocity(pointerId)}")
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
        }
        return true
    }

    private fun setupPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE), 101)
        } else {

        }

    }


}