package xyz.betauto.otpreceiver

import android.app.Instrumentation
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.os.SystemClock.sleep
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity

class TouchActivity: AppCompatActivity() {

    var inst=Instrumentation()
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
      setContentView(R.layout.acticity_touch)
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
            SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 47F, 90F, 0));
        sleep(500);
        inst.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(),
            SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, 48F, 123F, 0));
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val ret: Boolean = super.dispatchTouchEvent(ev)
        Log.d("TouchView",   "${ev!!.x}")
        Log.d("TouchView",   "${ev!!.y}")
        return ret
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val ret:Boolean = super.onTouchEvent(event)
        return ret
    }

}