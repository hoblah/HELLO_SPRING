package com.kosmo.a30xmljsonparser02;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

//BaseAdapter 클래스를 상속받아 커스텀 어댑터를 정의한다.
public class ActorAdapter extends BaseAdapter {

    //멤버변수.
    private Context context; //MainActivity에서 사용하기 위한 컨텍스트.
    private List<ActorVO> items; //어댑터에서 사용할 데이터를 저장한 컬렉션.
    private int layoutResId; //커스텀 레이아웃의 리소스 아이디.

    //생성자
    public ActorAdapter(Context context, List<ActorVO> items, int layoutResId)
    {
        this.context = context;
        this.items = items;
        this.layoutResId =layoutResId;
    }
    //메소드 오버라이딩.
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //하나의 항목을 표현하는 메소드.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //생성된 커스텀 레이아웃이 없다면 inflate한다.
        if(convertView==null){
            convertView = View.inflate(context, layoutResId, null);
        }

        //커스텀뷰(actor_layout.xml)에서 위젯 가져오기.
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvAge = convertView.findViewById(R.id.tv_age);
        TextView tvHobbys = convertView.findViewById(R.id.tv_hobbys);
        TextView tvLogin = convertView.findViewById(R.id.tv_login);
        ImageView profileImg = convertView.findViewById(R.id.imageView);

        //컬렉션에 저장된 값을 통해 각 위젯을 설정(컬렉션에 저장된 VO객체를 하나씩 가져옴).
        tvName.setText(items.get(position).getName());
        tvAge.setText(items.get(position).getAge());
        tvHobbys.setText(items.get(position).getHobbys());
        tvLogin.setText(items.get(position).getLogin());
        profileImg.setImageResource(items.get(position).getProfileImg());

        //리스트뷰에 스프라이트 효과를 주기위한 연산.
        if(position%2 == 0){
            convertView.setBackgroundColor(0x99dadada);
        }
        else{
            convertView.setBackgroundColor(0x99ffffff);
        }

        return convertView;
    }
}
