package com.example.callview

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity()  {

    lateinit var userId: EditText
    lateinit var userPwd: EditText
    lateinit var loginBtn: Button
    lateinit var joinBtn: Button
    private var auth : FirebaseAuth? = null
    lateinit var activityMain: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        userId = findViewById<View>(R.id.edit_id) as EditText
        userPwd = findViewById<View>(R.id.edit_pwd) as EditText
        loginBtn = findViewById<View>(R.id.btn_login) as Button
        joinBtn = findViewById<Button>(R.id.btn_join) as Button
        loginBtn.setOnClickListener(btnListener)
        joinBtn.setOnClickListener(btnListener)
        activityMain = findViewById<View>(R.id.login_activity) as ConstraintLayout

        auth = Firebase.auth //파이어베이스 가입

        //텍스트 바깥 레이아웃 클릭시 키보드 사라짐
        activityMain.setOnTouchListener(View.OnTouchListener { v, event ->
            hideKeyboard()
            false
        })

    }

    // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
//    public override fun onStart() {
//        super.onStart()
//        moveMainPage(auth?.currentUser)
//    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if(user!= null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    //텍스트 바깥 레이아웃 클릭시 키보드 사라짐
    fun hideKeyboard() {
        val inputManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    // 파이어베이스 회원가입 구현
    private fun createAccount(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this, "계정 생성 완료.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Log.w(ContentValues.TAG, "왜 실패?", task.exception)
                        Toast.makeText(
                            this, "계정 생성 실패",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    // 로그인
    private fun signIn(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth?.signInWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "로그인에 성공 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        moveMainPage(auth?.currentUser)
                    } else {
                        Toast.makeText(
                            baseContext, "로그인에 실패 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    var btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.btn_join -> {
                Log.e("조인버튼", "눌렀음")
                val loginid = userId.text.toString().trim()
                val loginpw = userPwd.text.toString().trim()

                Log.e(loginid.toString(), "loginid")
                Log.e(loginpw.toString(), "loginpw")
                createAccount(loginid, loginpw)
            }
            R.id.btn_login -> {
                Log.e("조인버튼", "눌렀음")
                val loginid = userId.text.toString().trim()
                val loginpw = userPwd.text.toString().trim()

                Log.e(loginid.toString(), "loginid")
                Log.e(loginpw.toString(), "loginpw")
                signIn(loginid, loginpw)
            }
        }
    }

}