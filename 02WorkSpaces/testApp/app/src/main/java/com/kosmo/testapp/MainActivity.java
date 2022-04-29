package com.kosmo.testapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    String time,kcal,menu;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();
    Cursor cursor;
    public static final String TAG = "★CalendarActivityLog★";
    MaterialCalendarView materialCalendarView;
    ArrayList<String> exam_name = new ArrayList<String>();
    ArrayList<String> exam_date = new ArrayList<String>();
    ArrayList<String> user_name;
    ArrayList<String> content;
    ProgressDialog dialog;

    int Year =0 ;
    int Month = 0;
    int Day = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "CalendarActivityLog > onCreateView()");

        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.activity_main, container, false);

        Bundle bundle = new Bundle();
        exam_date = bundle.getStringArrayList("exam_date");
        user_name = bundle.getStringArrayList("user_name");
        content = bundle.getStringArrayList("content");
        materialCalendarView = viewGroup.findViewById(R.id.calendarView);
        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator());

        new ApiSimulator(exam_date).executeOnExecutor(Executors.newSingleThreadExecutor());

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget,
                                       @NonNull CalendarDay date, boolean selected) {
                Year = date.getYear();
                Month = date.getMonth() + 1;
                Day = date.getDay();

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");



                String month = null;
                if(Month < 10) {
                    month = "0" + Month;
                    Log.d(TAG, "월 앞의 0붙혔는데 나왓니??>>"+month);
                }else {
                    month = String.valueOf(Month);
                }
                String day = null;
                if(Day < 10){
                    day = "0" + Day;
                    Log.d(TAG, "일 앞의 0붙혔는데 나왓니??>>"+day);
                }else {
                    day = String.valueOf(day);
                }

                String printDate = Year + "-" + month + "-" + day + "-";
                Log.d(TAG, "쿼리를위한 년월일조합>>"+day);

                //저장된 id가져오기.서버에 보내야함.
                String user_id = String.valueOf(201701700);
                Log.d(TAG, "유저아디확인 넘어왓나??"+ user_id);


                //서버의 spring bybatis로 인해 쿼리실행후 결과값받아옴.
                /*
                new AsyncHttpServer().execute(
                        "http://172.30.1.1:9999/schline/android/examList.do",
                        "user_id="+user_id,
                        "year="+Year,
                        "month="+month,
                        "day="+day
                        );

                 */

                //월년도 잘넘어왔나로그. Run에서 D/juhee로 확인가능.
                //Log.d(TAG, "년도 뭐지>>"+Year);
                //Log.d(TAG, "월 뭐지>>"+java.time.Month);
                //Log.d(TAG, "일 뭐지>>"+Day);

                String shot_Day = Year + "-" + Month + "-" + Day;

                boolean b = false;
                AlertDialog.Builder alertdialogBuilder =
                        new AlertDialog.Builder(MainActivity.this);
                for(int i=0; i<exam_date.size() ; i++){
                    if(shot_Day.equals(exam_date.get(i))){
                        b=true;
                        alertdialogBuilder.setTitle(user_name.get(i)+"님의 일정입니다."); //제목
                        alertdialogBuilder.setMessage(exam_date.get(i)); // 메시지
                        alertdialogBuilder.setIcon(R.drawable.icon_subject); // 아이콘 설정
                        //버튼 클릭시 동작
                        alertdialogBuilder.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    }
                }
                Log.i("shot_Day test", shot_Day + "");
                if(b){
                    alertdialogBuilder.show();
                }
                else{
                    alertdialogBuilder.setTitle("등록된 일정이 없습니다.");
                    alertdialogBuilder.setMessage(""); // 메시지
                    alertdialogBuilder.show();
                }
            }
        });
        return viewGroup;
    }
    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        ArrayList<String> regidate2;

        ApiSimulator(ArrayList<String> regidate2){
            this.regidate2 = regidate2;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < regidate2.size() ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                Log.i(TAG, regidate2.get(i));
                String[] time = regidate2.get(i).split("-");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            materialCalendarView.addDecorator(
                    new EventDecorator(Color.RED, calendarDays,
                            (Activity) getApplicationContext()));
        }
    }



    //서버와 통신.
    class AsyncHttpServer extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute() {
            super.onPreExecute();

            if (!dialog.isShowing())
                dialog.show();
        }

        //doInBackground -스레드에 의해 처리될 내용을 담기위한 함수.
        @Override
        protected String doInBackground(String... strings) {
            // Log.d(TAG, "doInBackground진입함.");
            //스프링 서버에서 반환하는 JSON데이터를 저장할 변수
            StringBuffer receiveData = new StringBuffer();

            /**
             * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
             * */
            try {
                // Log.d(TAG, "-doInBackground(Try)문 진입함.");
                // [2-1]. urlConn 설정.
                //0번째 인자를 통해 접속할URL로 객체를 생성한다.
                URL url = new URL(strings[0]);// 파라미터1 : 요청URL
                Log.i(TAG, "url > " + url);
                //URL을 연결할 객체 생성
                HttpURLConnection conn =
                        (HttpURLConnection) url.openConnection();
                //서버와 통신할때의 방식(GET or POST)
                conn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.
                conn.setDoOutput(true);

                // [2-2]. parameter 전달 및 데이터 읽어오기.
                /*
                요청 파라미터를 설정한다. 해당 예제에서는 서버와 통신시
                전달할 별도의 파라미터가 없으므로 아래 부분에 별다른 설정이 없다.
                 */
                //Log.d(TAG, "쿼리1 > "+strings[1]);
                //Log.d(TAG, "쿼리2 > "+strings[2]);
                // Log.d(TAG, "쿼리3 > "+strings[3]);
                //Log.d(TAG, "쿼리4 > "+strings[4]);

                OutputStream out = conn.getOutputStream();
                out.write(strings[1].getBytes("UTF-8")); //파라미터2 :사용자아이디.
                out.write("&".getBytes("UTF-8"));//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[2].getBytes("UTF-8")); //파라미터3 : 사용자가클릭한 년월.
                out.write("&".getBytes("UTF-8"));//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[3].getBytes("UTF-8")); //파라미터4 : 사용자가클릭한 년월.
                out.write("&".getBytes("UTF-8"));//&를 사용하여 쿼리스트링 형태로 만들어준다.
                out.write(strings[4].getBytes("UTF-8")); //파라미터4 : 사용자가클릭한 년월.
                out.flush();// 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                out.close();// 출력 스트림을 닫고 모든 시스템 자원을 해제.


                //* 403에러남.
                //Log.d(TAG, "conn.getResponseCode()확인!!!>"+conn.getResponseCode());
                //* 200에러남. 요청 정상 처리.
                //Log.d(TAG, " HttpURLConnection.HTTP_OK확인!!!>"+HttpURLConnection.HTTP_OK);

                //왜안되져...?
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    Log.i(TAG, "HTTP OK 성공");

                    //스프링 서버에 연결성공한 경우 JSON데이터를 읽어서 저장한다.
                    // [2-4]. 읽어온 결과물 리턴.
                    // 요청한 URL의 출력물을 BufferedReader로 받는다.
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(),
                                    "UTF-8")
                    );
                    // 출력물의 라인과 그 합에 대한 변수.
                    String responseData;
                    // 라인을 받아와 합친다.
                    while ((responseData = reader.readLine()) != null) {
                        receiveData.append(responseData + "\r\n");
                    }
                    reader.close();

                } else {
                    //서버 접속에 실패한경우..
                    Log.d(TAG, "HTTP_OK 안됨. 연결실패.");
                }
                //################성공후##################################
                //읽어온 JSON데이터를 로그로 출력
                Log.i(TAG,receiveData.toString());
                //먼저 JSON배열로 파싱.
                JSONArray jsonArray = new JSONArray(receiveData.toString());
                //StringBuffer 객체를 비움
                receiveData.setLength(0);
                //배열 크기만큼 반복
                for(int i=0; i<jsonArray.length(); i++) {
                    //배열의 요소는 객체이므로 JSON객체로 파싱
                    JSONObject jsonObject = jsonArray.getJSONObject(i);



                    if(jsonObject.getString("exam_date").equals(clickDate)){

                        Log.i(TAG, "jsonObject.getString(exam_date) > " + jsonObject.getString("exam_date"));


                        //각 Key에 해당하는 값을 가져와서 StringBuffer객체에 저장.
                        receiveData.append("★ " +
                                jsonObject.getString("exam_name")+"\n");
                        receiveData.append("마감일 : " +
                                jsonObject.getString("exam_date")+"\n");
                        Log.i(TAG, "클릭한날짜프린트중!!!!!!!!!!" + textTest.getText().toString());

                    }

                    //ArrayList<String> getExam_date;
                    //ArrayList<String> getExam_name;

                }
            }
            catch (Exception e){
                e.printStackTrace();
            }

            //로그출력
            //저장된 내용을 로그로 출력한 후 onPostExecute()로 반환한다.
            //이로그는 문자열만 뜨고 값은 안뜬다..뭐지..?
            Log.i(TAG, "receiveData.toString()>> "+receiveData.toString());
            //서버에서 내려준 JSON정보를 저장한후 반환.
            /*
            파싱이 완료된 StringBuffer객체를 String으로 변환하여 반환한다.
            여기서 반환된 값은 onPostExecute()로 전달된다.
             */
            return receiveData.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        ////doInBackgoround()에서 반환한 값은 해당 메소드로 전달한다.
        //onPostExecute(Result result):
        // AsyncTask의 모든 작업이 완료된 후 가장 마지막에 한 번 호출.
        // doInBackground() 함 수의 최종 값을 받기 위해 사용.
        //doInBackground()가 정상종료되면 해당 함수가 호출된다.
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute문 진입함.");

            //통신해서 받은값을 postExecute에서 넣음 된다..??
            Log.d(TAG, "-textResult>>>>" + textResult);
            //이로그에서는 아무것도 출력되지않음 주소이상인가..?
            Log.d(TAG, "onPostExecute문에서 !!!!!!!  리절트값s " + s);


            //진행대화창을 닫아준다.
            dialog.dismiss();

            //리절트값 받는구간..  -- 아무것도안뜸..ㅎ
            //결과값을 텍스트뷰에 출력한다.
            textResult.setText(s);


        }
    }////AsyncHttpRequest 끝.

}

