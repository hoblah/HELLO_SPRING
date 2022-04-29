package com.kosmo.a40fragmentlistview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MenuFragment2 extends Fragment {

    public static final String TAG = "iKosmo";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "MenuFragment4 > onCreateView()");
        View view = inflater.inflate(R.layout.menu_fragment2, container, false);

        ((Button)view.findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Toast.makeText(view.getContext(),
                "두번째 프레그먼트 입니다.",
                Toast.LENGTH_LONG).show();*/

                View ct = View.inflate(view.getContext(), R.layout.custom_toast, null);
                TextView textView = ct.findViewById(R.id.message_tv);
                textView.setText("토스트도 커스텀 되네요");
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setTextColor(Color.BLACK);

                Toast toast = new Toast(view.getContext());
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(ct);
                toast.show();
            }
        });

        return view;
    }
}

