package com.example.callview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.system.Os.uname
import android.util.Log
import android.view.Display
import android.view.LayoutInflater
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
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.nvt.color.ColorPickerDialog
import kotlinx.android.synthetic.main.activity_main2.bottomText
import kotlinx.android.synthetic.main.activity_main2.recyclerView
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_layout.*
import kotlinx.android.synthetic.main.main_toolbar.*
import org.w3c.dom.Text
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
    val itemList = arrayListOf<OptionDTO>()
    val result = arrayListOf<OptionDTO>()


    private lateinit var openColor: Button
    private lateinit var testView: View

    var optionDTOs: ArrayList<OptionDTO> = arrayListOf()
    var optionDTOs2 = OptionDTO()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.reference

    private lateinit var databaseRef: DatabaseReference

    private val db = FirebaseFirestore.getInstance()

    private var recyclerTextBoxColor: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //??????????????????
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

        // ????????? ????????????
        textSlide = findViewById(R.id.bottomText)
        textSlide.isSelected = true

        // Firebase ?????????
        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth //?????????????????? ??????

        // ???????????? ????????? ??????
        setSupportActionBar(main_layout_toolbar)    // ????????? ??????????????? ????????? ??????
        supportActionBar?.setDisplayHomeAsUpEnabled(true)   // ???????????? ?????? ??? ?????? ?????????
        supportActionBar?.setHomeAsUpIndicator(R.drawable.list_large)  // ????????? ????????? ??????
        supportActionBar?.setDisplayShowTitleEnabled(false)     // ????????? ????????? ???????????? ??????
//        main_navigationView?.setNavigationItemSelectedListener(this)

        //???????????? ????????????
        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.recyclerview_item, null)
            recyclerTextBoxColor = view.findViewById(R.id.textBox) as TextView


        getStandardSize()
        setRecyclerView()
        openSetBtn()
        onStartOption2()

    }

    private fun onStartOption2() {

        val setOp = db.collection(auth!!.currentUser!!.uid).document("OPTION")

        setOp.addSnapshotListener(EventListener<DocumentSnapshot> { snapshot, e ->
            if(snapshot != null && snapshot.exists()) {
                val recycler_backColor = snapshot.data!!["recycler_backColor"].toString()
                val recycler_textBoxColor = snapshot.data!!["recycler_textBoxColor"].toString()
                val recycler_textColor = snapshot.data!!["recycler_textColor"].toString()

                optionDTOs2.recycler_backColor = recycler_backColor.toInt()
                optionDTOs2.recycler_textBoxColor = recycler_textBoxColor.toInt()
                optionDTOs2.recycler_textColor = recycler_textColor.toInt()

                recyclerBackground.setBackgroundColor(optionDTOs2.recycler_backColor!!)
                recyclerTextBoxColor!!.setBackgroundColor(optionDTOs2.recycler_textBoxColor!!)
                recyclerTextBoxColor!!.setTextColor(optionDTOs2.recycler_textColor!!)

                Log.e("recycler_backColor", optionDTOs2.recycler_backColor.toString())
                Log.e("recycler_textBoxColor", optionDTOs2.recycler_textBoxColor.toString())
                Log.e("recycler_textColor", optionDTOs2.recycler_textColor.toString())
                Log.e("recyclerTextBoxColor", recyclerTextBoxColor.toString())

            }
        })
    }


//    private fun onStartOption2() {
//
//        firestore?.collection(auth!!.currentUser!!.uid)
//            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
//                optionDTOs.clear()
//                if(querySnapshot == null) return@addSnapshotListener
//
//                //????????? ???????????? // ????????? ?????? ????????? querySnapshot documents?????? ?????????????????? ????????? for?????? ????????? ?????? ????????? null?????? ?????????
//                //??? ????????? ???????????? ???????????? ???????????? ??? ???
//
//                    for(snapshot in querySnapshot.documents) {
//                        val item = snapshot.toObject(OptionDTO::class.java)
//                        val item2 = snapshot.data["OPTION"]?.toString()
//                        optionDTOs.add(item!!)
//
//                        Log.e("item2", item2.toString())
//                        Log.e("querySnapshot", querySnapshot.toString())
//                        Log.e("snapshot", snapshot.toString())
//                        Log.e("item", item.toString())
//
//                    }
//                }
//    }

    fun openSetBtn() {
        // ???????????? ??????
//        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val view: View = inflater.inflate(R.layout.main_drawer_header, null)

//        val setDrawer = View.inflate(this, R.layout.main_drawer_header, null)
//        setColorBtn2 = view.findViewById(R.id.setColorBtn) as Button
//        setColorBtn2.setOnClickListener(btnListener)

        val navigationView: NavigationView = findViewById(R.id.main_navigationView)

        val headerView: View = navigationView.getHeaderView(0)

        val setColorButton: Button = headerView.findViewById(R.id.setColorBtn) as Button

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
                            Log.e(color.toString(), "color ????????? ??????")

                            val strColor = color
                            val optionColor = strColor.toString()
                            Log.e(recyclerTextBoxColor.toString(), "recyclerTextBoxColor")
                            recyclerBackground.setBackgroundColor(optionColor.toInt())
                            optionDTOs2.recycler_backColor = optionColor.toInt()

                        }
                    })
                colorPicker.show()
            }
            R.id.recycler_textBoxColor -> {
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
                            Log.e(color.toString(), "color ????????? ??????")

                            val strColor = color
                            val optionColor = strColor.toString()

                            recyclerTextBoxColor!!.setBackgroundColor(optionColor.toInt())
                            optionDTOs2.recycler_textBoxColor = optionColor.toInt()

                        }
                    })
                colorPicker.show()
            }
            R.id.recycler_textColor -> {
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
                            Log.e(color.toString(), "color ????????? ??????")

                            val strColor = color
                            val optionColor = strColor.toString()

                            recyclerTextBoxColor!!.setTextColor(optionColor.toInt())
                            optionDTOs2.recycler_textColor = optionColor.toInt()

                        }
                    })
                colorPicker.show()
            }
            R.id.recycler_textSize -> {

            }

//

        }
        return false
    }

    // ???????????? ?????????
    private val btnListener = View.OnClickListener { view ->
        Log.e("setBtn", "??????")
        when(view.id) {
            R.id.setColorBtn -> {

                Log.e(optionDTOs2.recycler_backColor.toString(), "????????????")
                val resultDTO = ResultDTO()
                resultDTO.uid = auth?.currentUser?.uid

                Log.e(resultDTO.uid.toString(), "uid ??????")

                if(!optionDTOs2.recycler_backColor?.equals(null)!! && !optionDTOs2.recycler_textColor?.equals(null)!! && !optionDTOs2.recycler_textBoxColor?.equals(null)!!) {

                    firestore?.collection(auth!!.currentUser!!.uid)?.document("OPTION")
                        ?.set(optionDTOs2)

                    Log.e("setBtn2", "??????2")
//                databaseRefeence.child("recycler_backColor").push().setValue(optionDTOs.recycler_backColor)
                    Log.e(optionDTOs2.recycler_backColor.toString(), "optionDTOs")

                }
            }
        }
    }


    // ????????? ??????
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {  // ????????????
                main_drawer_layout.openDrawer(GravityCompat.START)  // ??????????????? ????????? ??????
            }
            R.id.account -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    // ????????? ??????
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            main_drawer_layout.closeDrawers()
            //????????????
            Toast.makeText(this,"back btn clicked", Toast.LENGTH_SHORT).show()
        } else {
            super.onBackPressed()
        }
    }

    // ????????? ?????????
    fun getScreenSize(activity: Activity): Point {
        val display: Display = activity.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        Log.e("getScreenSize", "??????")
        return size
    }

    fun getStandardSize() {
        Log.e("getStandardSize", "??????")
        val ScreenSize: Point = getScreenSize(this)
        density = resources.displayMetrics.density

        standardSize_X = (ScreenSize.x / density!!).toInt()
        standardSize_Y = (ScreenSize.y / density!!).toInt()

        bottomText.textSize = (standardSize_X!!/1).toFloat()
        bottomText.textSize = (standardSize_Y!!/11).toFloat()
    }

    // ????????? ???????????? ?????? ?????????
    @SuppressLint("SuspiciousIndentation")
    private fun fllipperImages(image: Int) {
        val imageView: ImageView = ImageView(this)
                imageView.setBackgroundResource(image)
                    v_fllipper.addView(imageView);      // ????????? ??????
                    v_fllipper.flipInterval = 3000       // ?????? ????????? ???????????? ???????????????(1000 ??? 1???)
                    v_fllipper.isAutoStart = true          // ?????? ?????? ?????? ??????
                    // animation
                    v_fllipper.setInAnimation(this,android.R.anim.slide_in_left)
                    v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right)
    }


    fun setRecyclerView() {
        Log.e("setRecyclerView", "??????")
        recyclerView.adapter = MyAdapter(this, firestore!!, uid!!)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

}