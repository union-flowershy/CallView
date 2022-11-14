package com.example.callview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.system.Os.uname
import android.util.Log
import android.view.Display
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nvt.color.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main2.bottomText
import kotlinx.android.synthetic.main.activity_main2.recyclerView
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_toolbar.*
import kotlin.math.log


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var list: ArrayList<String> = ArrayList()
    private lateinit var textSlide: TextView
    private lateinit var setColorBtn2: Button
    private lateinit var v_fllipper: ViewFlipper
    var standardSize_X: Int? = null
    var standardSize_Y: Int? = null
    var density: Float? = null
    private var firestore: FirebaseFirestore? = null
    private var auth : FirebaseAuth? = null
    private var uid: String? = null
    var backColor: Int? = null


    private lateinit var openColor: Button
    private lateinit var testView: View

    var optionDTOs: ArrayList<OptionDTO> = arrayListOf()
    var optionDTOs2 = OptionDTO()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    private lateinit var databaseRef: DatabaseReference

    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //가로모드고정
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        setContentView(R.layout.main)

        databaseRef = FirebaseDatabase.getInstance().reference

        main_navigationView.setNavigationItemSelectedListener(this)

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
        auth = Firebase.auth //파이어베이스 가입

        // 환경설정 메뉴바 생성
        setSupportActionBar(main_layout_toolbar)    // 툴바를 액티비티의 앱바로 지정
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // 드로어를 꺼낼 홈 버튼 활성화
        supportActionBar?.setHomeAsUpIndicator(R.drawable.list_large)  // 홈버튼 이미지 변경
        supportActionBar?.setDisplayShowTitleEnabled(false)     // 툴바에 타이틀 안보이게 설정
//        main_navigationView?.setNavigationItemSelectedListener(this)

//        // 환경설정 기본값
//        backColor = ContextCompat.getColor(this, R.color.sky)



        getStandardSize()
        setRecyclerView()
        openSetBtn()
        onStartOption2()

    }

    private fun onStartOption2() {

        firestore?.collection(auth!!.currentUser!!.uid)?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if(querySnapshot == null) return@addSnapshotListener

            //데이터 가져오기 // 여러번 하는 이유는 querySnapshot documents에서 주문번호까지 전부다 for문을 돌려서 그럼 그래서 null값이 반영됨
            //이 부분만 해결해서 데이터를 찾아오면 될 듯
            for (snapshot in querySnapshot.documents) {
                val item = snapshot.toObject(OptionDTO::class.java)
                optionDTOs.add(item!!)

//                recyclerBackground.setBackgroundColor(optionDTOs2.recycler_backColor!!)
                Log.e("item", item.toString())
                Log.e(optionDTOs[0].recycler_backColor.toString(), "")


//                recyclerBackground.setBackgroundColor(optionDTOs)

            }
        }

    }

    fun openSetBtn() {
        // 환경설정 저장
//        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view: View = inflater.inflate(R.layout.main_drawer_header, null)

//        val setDrawer = View.inflate(this, R.layout.main_drawer_header, null)
//        setColorBtn2 = view.findViewById(R.id.setColorBtn) as Button
//        setColorBtn2.setOnClickListener(btnListener)

        val navigationView: NavigationView = findViewById(R.id.main_navigationView)
        val headerView: View = navigationView.getHeaderView(0)
        val setColorButton: Button = headerView.findViewById(R.id.setColorBtn)
            setColorButton.setOnClickListener(btnListener)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.account -> {
                val colorPicker = ColorPickerDialog(
                    this,
                    Color.BLACK,
                    true,
                    object : ColorPickerDialog.OnColorPickerListener {
                        override fun onCancel(dialog: ColorPickerDialog?) {
                            // handle click button Cancel
                        }
                        override fun onOk(dialog: ColorPickerDialog?, color: Int) {
                            // handle click button OK
                            Log.e(color.toString(), "color 컬러값 확인")

                            val strColor = color
                            val optionColor = strColor.toString().replace(("[^\\d.]").toRegex(), "")

                            recyclerBackground.setBackgroundColor(optionColor.toInt())
                            optionDTOs2.recycler_backColor = optionColor.toInt()


                        }
                    })
                colorPicker.show()
            }

        }
        return false
    }

    // 저장버튼 클릭시
    private val btnListener = View.OnClickListener { view ->
        Log.e("setBtn", "클릭")
        when(view.id) {
            R.id.setColorBtn -> {

                Log.e(optionDTOs2.recycler_backColor.toString(), "컬러확인")
                val resultDTO = ResultDTO()
                resultDTO.uid = auth?.currentUser?.uid

                Log.e(resultDTO.uid.toString(), "uid 확인")
                firestore?.collection(auth!!.currentUser!!.uid)?.document("OPTION")?.set(optionDTOs2)

                Log.e("setBtn2", "클릭2")
//                databaseRefeence.child("recycler_backColor").push().setValue(optionDTOs.recycler_backColor)
                Log.e(optionDTOs2.recycler_backColor.toString(), "optionDTOs")
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
