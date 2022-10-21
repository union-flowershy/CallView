package com.example.callview

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStreamReader
import java.net.Socket


open class MainActivity : AppCompatActivity() {

    lateinit var v_fllipper: ViewFlipper
    lateinit var textBox: TextView
    lateinit var textHide1: TextView
    lateinit var textHide2: TextView
    lateinit var textHide3: TextView
    lateinit var textHide4: TextView
    lateinit var textHide5: TextView
    lateinit var textHide6: TextView

open override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //가로모드고정
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main)

        textBox = findViewById<TextView>(R.id.orderNum)
        textHide1 = findViewById(R.id.textView1)
        textHide2 = findViewById(R.id.textView2)
        textHide3 = findViewById(R.id.textView3)
        textHide4 = findViewById(R.id.textView4)
        textHide5 = findViewById(R.id.textView5)
        textHide6 = findViewById(R.id.textView6)

        val images = intArrayOf (
            R.drawable.coffee1,
            R.drawable.coffee2,
            R.drawable.coffee3
        )

        v_fllipper = findViewById<View>(R.id.image_slide) as ViewFlipper

        for (image in images) {
            fllipperImages(image)
        }

        textBox()
//        textHide()
            thread()
    }

    fun thread() {
        val thread: Thread = NetworkThread()
        Log.e("소켓연결 확인", thread.toString())
        thread.start()
        Log.e("소켓연결 시작", thread.toString())
    }

    inner class NetworkThread: Thread() {
        override fun run() = try {
                val socket = Socket("192.168.10.19", 55555)
                val input = socket.getInputStream()
                val reader: BufferedReader = BufferedReader(InputStreamReader(input))
                val dis = DataInputStream(input)
                var identify: Boolean = false
                var readValue: String
                var orderNum: String
//                    val orderNum = dis.readUTF()
//                    Log.e("test", dis.toString())

//            while((dis.readUTF()) != null) {
//                while((readValue = reader.readLine().toString()) != null) {
//                Log.e("데이터 읽기", reader.toString())
//                if (!identify) {    // 연결 후 한 번만 호출
//                    orderNum = reader.readLine()    //주문번호 저장
//                    identify = true;
//                    runOnUiThread {
//                        textBox.append("orderNum :  ${orderNum}\n")
////                           socket.close()
//                    }
//                    continue;
//                }else {}
//
//            }




        }catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun textBox() {
        textBox.setText("100")
        textHide1.setText("")
        textHide2.setText("테스트2")
        textHide3.setText("테스트3")
        textHide4.setText("테스트4")
        textHide5.setText("테스트5")
        textHide6.setText("테스트6")
    }

    private fun textHide() {
        textHide1.setVisibility(View.INVISIBLE );
        textHide2.setVisibility(View.INVISIBLE );
        textHide3.setVisibility(View.INVISIBLE );
        textHide4.setVisibility(View.INVISIBLE );
        textHide5.setVisibility(View.INVISIBLE );
        textHide6.setVisibility(View.INVISIBLE );
    }

    // 이미지 슬라이더 구현 메서드

        private fun fllipperImages(image: Int) {
            val imageView = ImageView(this)
            imageView.setBackgroundResource(image);

            v_fllipper.addView(imageView);      // 이미지 추가
            v_fllipper.setFlipInterval(3000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
            v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

            // animation
            v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
            v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right)
        }

}