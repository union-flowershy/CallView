package com.example.callview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*


class MainActivity : AppCompatActivity() {

    var list: ArrayList<String> = ArrayList()
    private lateinit var textSlide: TextView
    private lateinit var v_fllipper: ViewFlipper
    var standardSize_X: Int? = null
    var standardSize_Y: Int? = null
    var density: Float? = null
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null

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

        // 텍스트 슬라이드
        textSlide = findViewById(R.id.bottomText)
        textSlide.isSelected = true

        // Firebase 초기화
        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()

        getStandardSize()
        setRecyclerView()
    }

    // 스크린 크기값
    fun getScreenSize(activity: Activity): Point {
        val display: Display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        Log.e("getScreenSize", "실행")
        return size
    }

    fun getStandardSize() {
        Log.e("getStandardSize", "실행")
        val ScreenSize: Point = getScreenSize(this)
        density = resources.displayMetrics.density

        standardSize_X = (ScreenSize.x / density!!).toInt()
        standardSize_Y = (ScreenSize.y / density!!).toInt()

        bottomText.textSize = (standardSize_X!!/1).toFloat()
        bottomText.textSize = (standardSize_Y!!/11).toFloat()
    }

    // 이미지 슬라이더 구현 메서드
    @SuppressLint("SuspiciousIndentation")
    private fun fllipperImages(image: Int) {
        val imageView: ImageView = ImageView(this)
                imageView.setBackgroundResource(image)
                    v_fllipper.addView(imageView);      // 이미지 추가
                    v_fllipper.flipInterval = 3000       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
                    v_fllipper.isAutoStart = true          // 자동 시작 유무 설정
                    // animation
                    v_fllipper.setInAnimation(this,android.R.anim.slide_in_left)
                    v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right)
    }


    fun setRecyclerView() {
        Log.e("setRecyclerView", "실행")
        recyclerView.adapter = MyAdapter(this, firestore!!, uid!!)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}