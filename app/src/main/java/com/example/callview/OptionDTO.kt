package com.example.callview

data class OptionDTO(
        
        var recycler_backColor: Int? = null  // 호출화면 배경 색상
       ,var recycler_textBoxColor: Int? = null // 주문번호 배경 색상
       ,var recycler_textColor: Int? = null // 주문번호 글자 색상
       ,var recycler_textSize: Int? = null // 주문번호 글자크기
       ,var bottom_textComent: String? = null // 하단 안내 문구

//       ,var callNum_textSize: Int? = null // 호출넘버 텍스트 사이즈 조정

//
//
//        // 나중에 구현
//       ,var slide_backColor: Int? = null   // 백그라운드_이미지 슬라이드


        
)