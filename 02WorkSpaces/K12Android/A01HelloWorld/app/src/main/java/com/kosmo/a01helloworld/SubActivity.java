package com.kosmo.a01helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
리스너 부착방법 3 : View.OnClickListener 를 구현한 후 onclick메소드를
    오버로딩 하여 사용한다.
 */
public class SubActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Button button = (Button) findViewById(R.id.btnBackToMain);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //현재 실행된 액티비티를 종료한다.
        finish();
    }
}