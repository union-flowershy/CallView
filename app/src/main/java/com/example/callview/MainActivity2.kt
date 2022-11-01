package com.example.callview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.schedule


class MainActivity2 : AppCompatActivity() {

    var list: ArrayList<String> = ArrayList()
    lateinit var v_fllipper: ViewFlipper
    var mHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val textBox = findViewById<TextView>(R.id.textBox)

//        var list: ArrayList<String> = ArrayList()
//        for(i in 1 until 10) {
//            list.add(String.format("TEXT %d", i))
//            Log.e(i.toString(), "번째 작동")
//        }

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

        recyclerView.layoutManager = LinearLayoutManager(this)
        Log.e(list.toString(), "Adapter로 넘어가기 전 list의 수")

        thread()

    }
        // 이미지 슬라이더 구현 메서드
    private fun fllipperImages(image: Int) {
        val imageView: ImageView = ImageView(this)
            imageView.setBackgroundResource(image)
            v_fllipper.addView(imageView);      // 이미지 추가
            v_fllipper.flipInterval = 3000;       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
            v_fllipper.isAutoStart = true;          // 자동 시작 유무 설정
            // animation
            v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
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
//            val socket: Socket = Socket("192.168.10.19", 55555)
            val socket: Socket = Socket("192.168.1.164", 55555)
            val output = socket.getOutputStream()
            val input: InputStream = socket.getInputStream()
            val reader: BufferedReader = BufferedReader(InputStreamReader(input))
            var orderNum: String
                while(true) {
                    orderNum = reader.readLine().trim()
                    if(!list.contains(orderNum)) {
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