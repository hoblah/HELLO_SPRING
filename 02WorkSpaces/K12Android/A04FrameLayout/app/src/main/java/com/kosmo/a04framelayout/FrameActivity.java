package com.kosmo.a04framelayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class FrameActivity extends AppCompatActivity {

    // 이미지를 표현할때 사용하는 뷰 객체
    ImageView dream01;  // 숨김상태
    ImageView dream02;  // 보임상태

    // 이미지 토글을 위한 변수
    int imageToggle = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);

        //res에 저장된 이미지를 id값을 통해 얻어와서 변수에 저장
        dream01 = (ImageView)findViewById(R.id.dream01);
        dream02 = (ImageView)findViewById(R.id.dream02);
    }//// onCreate() 끝

    /*
    - 버튼을 클릭하여 함수를 호출할 때느 인자로 View객체를 전달한다.
    - 그래서 메소드를 정의할 때는 반드시 매개변수로 선언해야 한다.      */
    public void imageChange(View view){


        if(imageToggle == 0){
            dream01.setVisibility(view.VISIBLE);        // 보임처리
            dream02.setVisibility(view.INVISIBLE);      // 숨김처리
            imageToggle=1;
        }
        else if(imageToggle==1){
            dream01.setVisibility(view.INVISIBLE);
            dream02.setVisibility(view.VISIBLE);
            imageToggle=0;
        }
    }

    public void returnMain(View v){
        // 현재 실행중인 액티비티 종료
        finish();
    }
}