package xyz.betauto.otpreceiver

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import android.animation.AnimatorListenerAdapter
import android.os.Build
import androidx.annotation.RequiresApi


class AnimationActivity: AppCompatActivity() {
    lateinit var img: ImageView
    lateinit var img2: ImageView
    lateinit var img3: ImageView
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        img=findViewById(R.id.car1)
        img2=findViewById(R.id.car2)
        img3=findViewById(R.id.car3)

        val animator1go = ObjectAnimator.ofFloat(img, "TranslationX", 0.0f, -500.0f)
        val animator1back = ObjectAnimator.ofFloat(img, "TranslationX", -500.0f, -50.0f)

        val animator2go = ObjectAnimator.ofFloat(img2, "TranslationX", 0.0f, -350.0f)
        val animator2back = ObjectAnimator.ofFloat(img2, "TranslationX", -350.0f, -120.0f)

        val animator3go = ObjectAnimator.ofFloat(img3, "TranslationX", 0.0f, -400.0f)
        val animator3back = ObjectAnimator.ofFloat(img3, "TranslationX", -400.0f, -80.0f)

        val aniset= AnimatorSet()
        aniset.duration = 2000
        aniset.play(animator1back).after(animator1go)
        aniset.play(animator2back).after(animator2go)
        aniset.play(animator3back).after(animator3go)
        aniset.start()

        aniset.addListener(object : AnimatorListenerAdapter() {
            private var mCanceled = false
            override fun onAnimationStart(animation: Animator) {
                mCanceled = false
            }

            override fun onAnimationCancel(animation: Animator) {
                mCanceled = true
            }

            override fun onAnimationEnd(animation: Animator) {
                if (!mCanceled) {
                    animation.start()
                }
            }
        })



    }
}