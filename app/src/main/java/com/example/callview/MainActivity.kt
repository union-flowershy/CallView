package com.example.callview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData.Item
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nvt.color.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main2.bottomText
import kotlinx.android.synthetic.main.activity_main2.recyclerView
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_toolbar.*

class MainActivity : AppCompatActivity() {

    var list: ArrayList<String> = ArrayList()
    private lateinit var textSlide: TextView
    var openColor: Button? = null
    var setColorBtn: Button? = null
    private lateinit var v_fllipper: ViewFlipper
    var standardSize_X: Int? = null
    var standardSize_Y: Int? = null
    var density: Float? = null
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null
    var backColor: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //가로모드고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContentView(R.layout.main)

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

        // 환경설정 메뉴바 생성
        setSupportActionBar(main_layout_toolbar)    // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.list_large)  // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 툴바에 타이틀 안보이게 설정
//        main_navigationView?.setNavigationItemSelectedListener(this)

        // 환경설정 기본값
        backColor = ContextCompat.getColor(this, R.color.sky).toString()

//        // 환경설정 저장
//        setColorBtn = findViewById<Button>(R.id.setBtn)
//        setColorBtn!!.setOnClickListener(btnListener)

        // 색상선택 호출
        openColor = findViewById(R.id.account) as Button?
        openColor?.setOnClickListener(btnListener)

        getStandardSize()
        setRecyclerView()
    }

    // 저장버튼 클릭시
    var btnListener = OnClickListener { view ->
        when(view.id) {
            R.id.setBtn -> {
                val optionDTO = OptionDTO()
                optionDTO.backColor = ""
            }
            R.id.account -> {
                
                //여기 수정해야함
                Log.e("account", "클릭")
                val colorPicker = ColorPickerDialog(this, Color.BLACK, true, object : ColorPickerDialog.OnColorPickerListener {
                    override fun onCancel(dialog: ColorPickerDialog?) {
                        // handle click button Cancel
                    }

                    override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                        // handle click button OK
                        bojobackground.setBackgroundColor(color)
                    }
                })
                colorPicker.show()
            }
        }
    }


    // 액션바 생성
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {  // 메뉴버튼
                main_drawer_layout.openDrawer(GravityCompat.START)  // 네비게이션 드로어 열기
            }
            R.id.account -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    // 액션바 닫기
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            main_drawer_layout.closeDrawers()
            //테스트용
            Toast.makeText(this,"back btn clicked", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
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