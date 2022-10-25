package com.example.callview

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.renderscript.ScriptGroup.Input
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket
import java.util.*
import kotlin.concurrent.schedule


class MainActivity : AppCompatActivity() {

    lateinit var v_fllipper: ViewFlipper
    lateinit var textBox: TextView
    lateinit var textHide1: TextView
    lateinit var textHide2: TextView
    lateinit var textHide3: TextView
    lateinit var textHide4: TextView
    lateinit var textHide5: TextView
    lateinit var textHide6: TextView
    lateinit var mainNum: TextView
    lateinit var mainNum2: TextView
    lateinit var anime: Animation
    lateinit var rightout: Animation
    var mHandler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
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
        mainNum = findViewById<TextView>(R.id.mainNum)
        mainNum2 = findViewById<TextView>(R.id.mainNum2)

        val images = intArrayOf(
            R.drawable.coffee1,
            R.drawable.coffee2,
            R.drawable.coffee3
        )

        v_fllipper = findViewById<View>(R.id.image_slide) as ViewFlipper

        for (image in images) {
            fllipperImages(image)
        }

        textBox()
        textHide()
        thread()
    }

    fun thread() {
        val thread: Thread = NetworkThread()
        Log.e("소켓연결 확인", thread.toString())
        thread.start()
        Log.e("소켓연결 시작", thread.toString())
    }

    inner class NetworkThread: Thread() {
        @SuppressLint("SuspiciousIndentation")
        override fun run() = try {
            val socket: Socket = Socket("192.168.10.19", 55555)
            var input: InputStream = socket.getInputStream()
            val reader: BufferedReader = BufferedReader(InputStreamReader(input))
            var orderNum: String
            anime = AnimationUtils.loadAnimation(this@MainActivity,R.anim.textanimation)
            rightout = AnimationUtils.loadAnimation(this@MainActivity,R.anim.rightout)
            while(true) {
                    orderNum = reader.readLine()
                                runOnUiThread{
                                    if(mainNum2.text.toString() == "") {
                                        mainNum2.append(orderNum)
                                        mainNum.visibility = View.VISIBLE
                                        mainNum2.visibility = View.VISIBLE
                                        mainNum2.startAnimation(anime)

                                        Timer().schedule(4000) {
                                            mainNum.startAnimation(rightout)
                                            mainNum2.startAnimation(rightout)

                                            mHandler.postDelayed(Runnable {
                                                run() {
                                                    mainNum.visibility = View.GONE
//                                                    mainNum2.visibility = View.GONE
                                                    mainNum.text = ""
                                                    mainNum2.text = ""

                                                }
                                            }, 1000)
                                        }
                                            mHandler.postDelayed(Runnable {
                                                run() {
                                                    if(textBox.text.toString() == "") {
                                                        textBox.append(orderNum)
                                                    }else {}
                                                }
                                            }, 5500)

                                    } else if(textHide1.text.toString() == "") {
                                        textHide1.append(orderNum)
                                    } else if(textHide2.text.toString() == "") {
                                        textHide2.append(orderNum)
                                    } else if(textHide3.text.toString() == "") {
                                        textHide3.append(orderNum)
                                    } else if(textHide4.text.toString() == "") {
                                        textHide4.append(orderNum)
                                    } else if(textHide5.text.toString() == "") {
                                        textHide5.append(orderNum)
                                    } else {

                                        textHide6.append(orderNum)
                                        Log.e("HIde6 확인 = ", textHide6.toString())

                                    }
                                }
            }

        }catch(e: Exception) {
            e.printStackTrace()
        }
    }

    private fun textBox() {
        textBox.text = ""
        textHide1.text = ""
        textHide2.text = ""
        textHide3.text = ""
        textHide4.text = ""
        textHide5.text = ""
        textHide6.text = ""


    }

    private fun textHide() {
        textHide1.setVisibility(View.INVISIBLE )
        textHide2.setVisibility(View.INVISIBLE )
        textHide3.setVisibility(View.INVISIBLE )
        textHide4.setVisibility(View.INVISIBLE )
        textHide5.setVisibility(View.INVISIBLE )
        textHide6.setVisibility(View.INVISIBLE )
        mainNum.setVisibility(View.GONE)
        mainNum2.setVisibility(View.GONE)

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