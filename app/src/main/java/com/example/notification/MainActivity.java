package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.Person;
import androidx.core.graphics.drawable.IconCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.graphics.drawable.IconCompatParcelizer;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button basicBtn;
    Button largeImageBtn;
    Button largeBlockOfTextBtn;
    Button inboxStyleBtn;
    Button progressBarBtn;
    Button headsUpBtn;
    Button messageBtn;

    // 알림을 띄우려면 채널을 만든 뒤, 매니저를 생성해서 notify()
    NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicBtn = findViewById(R.id.main_basic);
        largeImageBtn = findViewById(R.id.main_expandable_large_image);
        largeBlockOfTextBtn = findViewById(R.id.main_expandable_large_block_of_text);
        inboxStyleBtn = findViewById(R.id.main_expandable_inbox_style);
        progressBarBtn = findViewById(R.id.main_progress_bar);
        headsUpBtn = findViewById(R.id.main_heads_up);
        messageBtn = findViewById(R.id.main_message);

        basicBtn.setOnClickListener(this);
        largeImageBtn.setOnClickListener(this);
        largeBlockOfTextBtn.setOnClickListener(this);
        inboxStyleBtn.setOnClickListener(this);
        progressBarBtn.setOnClickListener(this);
        headsUpBtn.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String channelId = "first-channel";
        String channelName = "My First Channel";
        String channelDesc = "My First Channel Description";

        // Android 8.0 부터는 채널을 만들어야 한다.
        // 채널을 여러 개 만들 수 있고, 앱별 알림 설정에 들어가 보면 채널별로 알림을 끄고 켤 수 있다.
        // 대표적으로 구글 앱이 채널이 많다.
        // 채널 생성 후 NotificationManager에 설정한다.
        createNotificationChannel(channelName, channelDesc, channelId);


        // 알림 기본 설정--아이콘, 제목, 내용 등
        NotificationCompat.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(this, channelId);
        } else {
            // API 레벨 26 이하는 이전 방식을 적용한다.
            builder = new NotificationCompat.Builder(this);
        }
        // 상태 바에 조그맣게 뜨는 아이콘
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay);
        builder.setContentTitle("제목");
        builder.setContentText("내용");
        // 알림을 클릭하면 해제해주는 모양인데 서비스가 포그라운드에 있으면 작동을 안 한다고 한다(?)
        builder.setAutoCancel(true);

        // 위에 것 말고, 바를 내렸을 때 나타나는 오른쪽에 위치한 조금 더 큰 아이콘을 말한다.
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.noti_large);
        builder.setLargeIcon(largeIcon);


        // 알림 클릭 시 인텐트 설정
        // 알림 클릭했을 때 인텐트 타고 들어가는 테스트용 액티비티
        // 그 밖의 기능은 없다.
        Intent intent = new Intent(this, SubActivity.class);
        // 알림을 클릭 후 새 액티비티를 시작할 때 태스크를 어떻게 할 지 결정한다.
        // 알림 클릭 시 기존 태스크를 모두 삭제하고 새 태스크가 만들어지게 설정했는데, 지금 별로 중요한 건 아니다.
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);


        Intent _intent = new Intent(this, MyReceiver.class);
        PendingIntent _pendingIntent = PendingIntent.getBroadcast(this, 0, _intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 알림을 확장하면 하단에 (텍스트) 버튼이 나타난다.
        // 리시버에게 이벤트를 전달한다.
        // 여기서 리시버는 전달이 잘 이루어졌다는 것을 보여주기 위해 토스트만 띄워주고 있다.
        builder.addAction(
                new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_share, "토스트", _pendingIntent).build()
        );


        // 다양한 형태의 Notification
        if (v == largeImageBtn) {
            // [확장형/큰 이미지]
            Bitmap largeImage = BitmapFactory.decodeResource(getResources(), R.drawable.noti_big);
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle(builder)
                    .bigPicture(largeImage);
            builder.setStyle(style);

        } else if (v == largeBlockOfTextBtn) {
            // [확장형/긴 텍스트]
            NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle(builder);
            style.setSummaryText("Summary Text");

            style.bigText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam quis tristique dui.");

            builder.setStyle(style);

        } else if (v == inboxStyleBtn) {
            // [확장형/목록]
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle(builder);
            style.setSummaryText("마파두부 레시피");

            style.addLine("연두부");
            style.addLine("생마늘 다진 것");
            style.addLine("화자오유");
            style.addLine("두반장");
            style.addLine("고기 아무거나--돼지고기/소고기 다짐육 등");
            style.addLine("전분");

            builder.setStyle(style);

        } else if (v == progressBarBtn) {
            // [기본형/프로그래스 바] 게이지가 찰 때 마다 진동이 울리는데, 진동 설정은 채널을 조작해야 하니 여기(예제)서는 패스한다
            // channel.enableVibration(...)

            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i <= 10; i ++) {
                        // 알림을 클릭하면 해제해주는 모양인데 서비스가 포그라운드에 있으면 작동을 안 한다고 한다(?)
                        builder.setAutoCancel(false);
                        // 지속 여부를 결정 즉, 사용자가 알림을 제거(방해)할 수 없도록 막아놓는다.
                        builder.setOngoing(true);
                        // indeterminate를 true로 하면 게이지가 막 지나가는(?) 말 그대로 진행도를 알 수 없는 불확실한 프로그래스 바가 나타나고
                        // false로 하면 진행도에 따라 게이지가 차는 일반적인 바가 나타난다.
                        builder.setProgress(10, i, false);

                        manager.notify(222, builder.build());
                        // 프로그래스 바가 완료되면(게이지가 다 차면) 알림을 해제한다.
                        if (i >= 10) {
                            manager.cancel(222);
                        }
                        // 1초 마다 게이지가 찬다.
                        SystemClock.sleep(1000);
                    }
                }
            }).start();

        } else if (v == headsUpBtn) {
            // [Heads Up형]은 단순히 상태 바에 알림이 오는 게 아니라 전면 상단에 둥실 떠있는 것을 말한다.
            // 대표적으로 게임 중에 전화가 오는 경우에 사용된다.
            // 채널 importance가 IMPORTANCE_HIGH, 빌더 priority는 PRIORITY_HIGH 이상으로 해야 한다.
            // 채널 설정은 한 번 해놓으면 바꿀 수 없기 때문에, 코드가 변경되면 앱을 지우고 다시 설치해서 테스트해야 한다.
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setFullScreenIntent(pendingIntent, true);

        } else if (v == messageBtn) {
            // [기본형/메시지 스타일]
            Person user1 = new Person.Builder()
                    .setName("kkang")
                    .setIcon(IconCompat.createWithResource(this, R.drawable.person1))
                    .build();
            Person user2 = new Person.Builder()
                    .setName("kim")
                    .setIcon(IconCompat.createWithResource(this, R.drawable.person2))
                    .build();
            // user2(kim)가 'hello'라고 메시지를 보낸다.
            NotificationCompat.MessagingStyle.Message message =
                    new NotificationCompat.MessagingStyle.Message("hello", System.currentTimeMillis(), user2);
            // 위 처럼 Message 오브젝트를 따로 만들어서 넣어도 되고, addMessage()에 바로 해도 된다.
            // user1(kkang)이 'world'라고 메시지를 보낸다.
            NotificationCompat.MessagingStyle style = new NotificationCompat.MessagingStyle(user1)
                    .addMessage("world", System.currentTimeMillis(), user1)
                    .addMessage(message);
            builder.setStyle(style);
        }
        // 알림을 띄운다.
        manager.notify(222, builder.build());
    }

    // Android 8.0 부터는 채널을 만들어야 한다.
    // 채널을 여러 개 만들 수 있고, 앱별 알림 설정에 들어가 보면 채널별로 알림을 끄고 켤 수 있다.
    // 대표적으로 구글 앱이 채널이 많다.
    // 채널 생성 후 NotificationManager에 설정한다.
    private void createNotificationChannel(String channelName, String channelDescription, String channelId) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = channelName;
//            String description = channelDescription;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            // 채널 설정은 한 번 해놓으면 바꿀 수 없기 때문에, 코드가 변경되면 앱을 지우고 다시 설치해서 테스트해야 한다.
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(channelDescription);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            // 알림을 띄우려면 채널을 만든 뒤 매니저를 생성해서 notify()
            manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}