package com.kosmo.a40fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

//Java코드로 프레그먼트를 삽입하기 위한 클래스 정의.
public class MyFragment3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
     @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //자바코드로 프레그먼트를 추가할때에는 inflate()의 세번째 인자는 false로 처리한다.
       View view = inflater.inflate(R.layout.my_fragment3, container, false);

       //화면에 있는 버튼에 리스너 부착.
        ((Button)view.findViewById(R.id.button3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(),
                        "세번째 프레그먼트 입니다.",
                        Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
