package com.example.callview

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket
import java.util.*
import kotlin.concurrent.schedule


class MainActivity2 : AppCompatActivity() {

    lateinit var v_fllipper: ViewFlipper
    var mHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //가로모드고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContentView(R.layout.activity_main2)

        val images = intArrayOf(
            R.drawable.did_img1,
            R.drawable.did_img2,
            R.drawable.did_img3,
            R.drawable.did_img4,
            R.drawable.did_img5
        )

        v_fllipper = findViewById<View>(R.id.image_slide) as ViewFlipper

        for (image in images) {
            fllipperImages(image)
        }

    }

        // 이미지 슬라이더 구현 메서드
        private fun fllipperImages(image: Int) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(image);
            v_fllipper.addView(imageView);      // 이미지 추가
            v_fllipper.flipInterval = 3000;       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
            v_fllipper.isAutoStart = true;          // 자동 시작 유무 설정
            // animation
            v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
            v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right)
        }

}