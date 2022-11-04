package com.example.callview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket


class MainActivity2 : AppCompatActivity() {

    var list: ArrayList<String> = ArrayList()
    private lateinit var textSlide: TextView
    private lateinit var v_fllipper: ViewFlipper
    var standardSize_X: Int? = null
    var standardSize_Y: Int? = null
    var density: Float? = null
    private var auth : FirebaseAuth? = null // Firebase 이메일

    var mHandler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //가로모드고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContentView(R.layout.activity_main2)

        auth = Firebase.auth // Firebase 이메일

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.e(list.toString(), "Adapter로 넘어가기 전 list의 수")

        thread()
        getStandardSize()
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

    // 파이어베이스 로그인 구현
    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish() // 가입창 종료
                    } else {
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
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

    private fun thread() {
        val thread: Thread = NetworkThread()
        Log.e("소켓연결 확인", thread.toString())
        thread.start()
        Log.e("소켓연결 시작", thread.toString())
    }

    inner class NetworkThread: Thread() {
        override fun run() = try {
            val socket: Socket = Socket("192.168.10.19", 55555)
//            val socket: Socket = Socket("192.168.1.164", 55555)
            val output = socket.getOutputStream()
            val input: InputStream = socket.getInputStream()
            val reader: BufferedReader = BufferedReader(InputStreamReader(input))
            var orderNum: String
                while(true) {
                        orderNum = reader.readLine().trim()
                        if (!list.contains(orderNum)) {
                            list.add(orderNum)
                            Log.e(list.toString(), "list")
                            Log.e(orderNum.toString(), "orderNum")
                            runOnUiThread {
                                setRecyclerView()
                            }
                        } else {
                            val writer: PrintWriter = PrintWriter(output, true)
                            writer.println("overlap")
                            //여기까지 작업하다 말음 오버랩 뜸.
                        }
                    }
            } catch(e: Exception) {
                e.printStackTrace()
        }
    }

    fun setRecyclerView() {
        recyclerView.adapter = MyAdapter(this, list)
    }

}