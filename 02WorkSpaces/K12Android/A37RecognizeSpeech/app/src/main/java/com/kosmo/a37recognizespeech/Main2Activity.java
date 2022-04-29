package com.kosmo.a37recognizespeech;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


//RecognitionListener를 메인액티비티에서 구현한 후 작성한다.
public class Main2Activity extends AppCompatActivity implements RecognitionListener {

    private static final String TAG = "iKosmo";
    //결과값을 돌려받기 위한 요청코드.
    private static final  int REQUEST_CODE = 1000;
    TextView textView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textView1 = findViewById(R.id.textView1);
    }

    public void onBtnClicked(View v) {

        try {
            //음선인식을 실행하기 위한 인덴트 객체 생성.
            // 음성 인식의 실행 (1)
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성 검색");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            //인덴트를 통해 음석을 Google로 전달후 결과를 되돌려받기 위한 호출.
            startActivityForResult(intent, REQUEST_CODE);
        }
        catch (ActivityNotFoundException e) {
            Log.d(TAG, "ActivityNotFoundException");
        }
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

        String message;
        switch (i) {
            case SpeechRecognizer
                    .ERROR_AUDIO:
                message = "오디오 에러";
            break;
            case SpeechRecognizer
                    .ERROR_CLIENT:
                message = "클라이언트 에러";
                break;
            case SpeechRecognizer
                    .ERROR_INSUFFICIENT_PERMISSIONS:
                message = "퍼미션없음";
                break;
            case SpeechRecognizer
                    .ERROR_NETWORK:
                message = "네트워크 에러";
                break;
            case SpeechRecognizer
                    .ERROR_NETWORK_TIMEOUT:
                message = "네트워크 타임아웃";
                break;
            case SpeechRecognizer
                    .ERROR_NO_MATCH:
                message = "찾을수 없음";
                break;
            case SpeechRecognizer
                    .ERROR_RECOGNIZER_BUSY:
                message = "BUSY";
                break;
            case SpeechRecognizer
                    .ERROR_SERVER:
                message = "서버이상";
                break;
            case SpeechRecognizer
                    .ERROR_SPEECH_TIMEOUT:
                message = "시간초과";
                break;
            default:
                message = "알수없음";
                break;
        }

        Log.d(TAG, "SPEECH ERROR : " + message);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches =
                bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String str = "";
        for (int i = 0; i < matches.size(); i++) {
            str = str = matches.get(i) + "\n";
            Log.d(TAG, "onResults text : " + matches.get(i));
        }
        //음성인식의 결과를 텍스트뷰에 설정.
        textView1.setText(str);
    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    //startActivityForResult를 통해 인덴트를 실행한 후 결과를 콜백받을때 사용하는 함수.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case  REQUEST_CODE : {
                if (requestCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    String str = "";
                    for(int i = 0; i < text.size() ; i++){
                        str = str + text.get(i) + "\n";
                        Log.i(TAG, "onActivityResult text : " +
                        text.get(i));
                    }
                    textView1.setText(str);

                    String[] rs = new String[text.size()];
                    text.toArray(rs);

                    textView1.setText(rs[0]);

                    /*
                    하나의 문장으로 2번이상의 명령실행이 필요한 경우 아래와같이
                    리스트로 만들어서 사용하면된다.
                    => 코딩 교육에는 자바가 가장 좋은 언어입니다.
                     */
                    String[] searchLists = {"코딩", "자바", "명령", "팀프로젝트"};
                    String[] searchTxtArr = rs[0].split(" ");
                    for(String s : searchTxtArr){
                        for(String li : searchLists) {
                            if(s.contains(li)){
                                if(li.equals("코딩")){
                                    Toast.makeText(getApplicationContext(),
                                            "명령1:"+li+"이(가) 인식되었습니다.",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else  if(li.equals("자바")){
                                    Toast.makeText(getApplicationContext(),
                                            "명령2:"+li+"이(가) 인식되었습니다.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                }

                break;
            }
        }






















    }
}