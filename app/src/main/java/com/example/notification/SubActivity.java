package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// 알림 클릭했을 때 인텐트 타고 들어가는 테스트용 액티비티
// 그 밖의 기능은 없다.
public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
    }
}