package com.example.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

// 알림을 확장하면 하단에 (텍스트) 버튼이 나타난다.
// 리시버에게 이벤트를 전달한다.
// 여기서 리시버는 전달이 잘 이루어졌다는 것을 보여주기 위해 토스트만 띄워주고 있다.
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");

        Toast.makeText(context, "My Receiver", Toast.LENGTH_SHORT).show();
    }
}