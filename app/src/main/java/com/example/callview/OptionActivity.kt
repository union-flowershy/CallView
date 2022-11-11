package com.example.callview

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import com.nvt.color.ColorPickerDialog
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.main_layout.*

class OptionActivity  : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var optionDTOs: OptionDTO = OptionDTO()



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_navigationView.setNavigationItemSelectedListener(this)


    }

    init {

        val recycler_backColor = optionDTOs.recycler_backColor
//        val callNum_textSize = optionDTOs.callNum_textSize
//        val bottom_textComent = optionDTOs.bottom_textComent

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
                            recyclerBackground.setBackgroundColor(color)
                            optionDTOs.recycler_backColor = color
                            
                            Log.e(optionDTOs.recycler_backColor.toString(), "컬러확인")
                        }
                    })
                colorPicker.show()
            }
            R.id.item2 -> {

            }
        }
        return false
    }

}