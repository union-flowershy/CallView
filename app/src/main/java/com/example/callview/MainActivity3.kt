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
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.Socket
import java.util.*
import kotlin.concurrent.schedule


class MainActivity3 : AppCompatActivity() {

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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
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

        textBox()
        textHide()
        thread()
    }

    private fun thread() {
        val thread: Thread = NetworkThread()
        Log.e("소켓연결 확인", thread.toString())
        thread.start()
        Log.e("소켓연결 시작", thread.toString())
    }

    inner class NetworkThread: Thread() {
        @SuppressLint("SuspiciousIndentation")
        override fun run() = try {
            val socket: Socket = Socket("192.168.10.19", 55555)
//            val socket: Socket = Socket("192.168.1.164", 55555)
            var input: InputStream = socket.getInputStream()
            val reader: BufferedReader = BufferedReader(InputStreamReader(input))
            var orderNum: String
            val resetText: String = reader.readLine().trim()
            anime = AnimationUtils.loadAnimation(this@MainActivity3,R.anim.textanimation)
            rightout = AnimationUtils.loadAnimation(this@MainActivity3,R.anim.rightout)
            while(true) {
                    orderNum = reader.readLine()
                                runOnUiThread{
                                if(!orderNum.toString().equals("reset")) {
                                    if(mainNum2.text.toString() == "") {
                                        mainNum2.append(orderNum.toString())
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
                                                        textBox.append(orderNum.toString())
                                                    }else if(textHide1.text.toString() == "" && textHide2.text.toString() == ""){
                                                    textHide1.append(textBox.text)
                                                    textHide1.visibility = View.VISIBLE
                                                    textBox.text = ""
                                                    textBox.append(orderNum.toString())
                                                }else if(textHide1.text.toString() != null && textHide2.text.toString() == ""){
                                                    textHide2.append(textHide1.text)
                                                    textHide2.visibility = View.VISIBLE
                                                    textHide1.text = ""
                                                    textHide1.append(textBox.text)
                                                    textBox.text = ""
                                                    textBox.append(orderNum.toString())
                                                }else if(textHide1.text.toString() != null && textHide2.text.toString() != null && textHide3.text.toString() == ""){
                                                        textHide3.append(textHide2.text)
                                                        textHide3.visibility = View.VISIBLE
                                                        textHide2.text = ""
                                                        textHide2.append(textHide1.text)
                                                        textHide1.text = ""
                                                        textHide1.append(textBox.text)
                                                        textBox.text = ""
                                                        textBox.append(orderNum.toString())
                                                }else if(textHide1.text.toString() != null && textHide2.text.toString() != null && textHide3.text.toString() != null && textHide4.text.toString() == ""){
                                                        textHide4.append(textHide3.text)
                                                        textHide4.visibility = View.VISIBLE
                                                        textHide3.text = ""
                                                        textHide3.append(textHide2.text)
                                                        textHide2.text = ""
                                                        textHide2.append(textHide1.text)
                                                        textHide1.text = ""
                                                        textHide1.append(textBox.text)
                                                        textBox.text = ""
                                                        textBox.append(orderNum.toString())
                                                }else if(textHide1.text.toString() != null && textHide2.text.toString() != null && textHide3.text.toString() != null && textHide4.text.toString() != null && textHide5.text.toString() == ""){
                                                        textHide5.append(textHide4.text)
                                                        textHide5.visibility = View.VISIBLE
                                                        textHide4.text = ""
                                                        textHide4.append(textHide3.text)
                                                        textHide3.text = ""
                                                        textHide3.append(textHide2.text)
                                                        textHide2.text = ""
                                                        textHide2.append(textHide1.text)
                                                        textHide1.text = ""
                                                        textHide1.append(textBox.text)
                                                        textBox.text = ""
                                                        textBox.append(orderNum.toString())
                                                }else if(textHide1.text.toString() != null && textHide2.text.toString() != null && textHide3.text.toString() != null && textHide4.text.toString() != null && textHide5.text.toString() != null && textHide6.text.toString() ==""){
                                                        textHide6.append(textHide5.text)
                                                        textHide6.visibility = View.VISIBLE
                                                        textHide5.text = ""
                                                        textHide5.append(textHide4.text)
                                                        textHide4.text = ""
                                                        textHide4.append(textHide3.text)
                                                        textHide3.text = ""
                                                        textHide3.append(textHide2.text)
                                                        textHide2.text = ""
                                                        textHide2.append(textHide1.text)
                                                        textHide1.text = ""
                                                        textHide1.append(textBox.text)
                                                        textBox.text = ""
                                                        textBox.append(orderNum.toString())
                                                    }else {

                                                    }
                                                }
                                            }, 5500)

                                    } else if(textHide1.text.toString() == "") {
                                        textHide1.append(orderNum.toString())
                                    } else if(textHide2.text.toString() == "") {
                                        textHide2.append(orderNum.toString())
                                    } else if(textHide3.text.toString() == "") {
                                        textHide3.append(orderNum.toString())
                                    } else if(textHide4.text.toString() == "") {
                                        textHide4.append(orderNum.toString())
                                    } else if(textHide5.text.toString() == "") {
                                        textHide5.append(orderNum.toString())
                                    } else {
                                        textHide6.append(orderNum.toString())
                                        Log.e("HIde6 확인 = ", textHide6.toString())
                                    }
                                } else {
                                    textBox()
                                    textHide()
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
            textHide1.visibility = View.INVISIBLE
            textHide2.visibility = View.INVISIBLE
            textHide3.visibility = View.INVISIBLE
            textHide4.visibility = View.INVISIBLE
            textHide5.visibility = View.INVISIBLE
            textHide6.visibility = View.INVISIBLE
            mainNum.visibility = View.GONE
            mainNum2.visibility = View.GONE

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