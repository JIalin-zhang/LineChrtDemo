package com.example.zhangjl.linechrtdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
   private int Yinvalue;
   private  PointViewClass pointViewClass;
   private  Button button;
    private  Button stop;
    private Thread thread;
    private Boolean ooo = true;
   int jishu  = 0;
   private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button =(Button) findViewById(R.id.buttonone);
        stop =(Button) findViewById(R.id.end);

        pointViewClass = findViewById(R.id.lineview);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setrodom();
                pointViewClass.setTest(true);
                //ooo = true;
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // thread.stop();
                pointViewClass.setTest(false);
                ooo = false;
            }
        });
        context = this;
        HomeReceiver receiver = new HomeReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
    }
    class HomeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)){
                String reason = intent.getStringExtra("reason");
                if (reason!=null){
                    if (reason.equals("homekey")){
                        Log.e("1111111111","home");
                    }else if (reason.equals("recentapps")){
                        Log.e("222222222222","recentapps");
                        ImageView imageView = new ImageView(context);
                        imageView.setImageResource(R.drawable.card_bind_bg);
                        imageView.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(400,

                                1000);//两个400分别为添加图片的大小
                        ViewGroup group = getWindow().getDecorView().findViewById(android.R.id.content);
                        group.addView(imageView,params);
                    }else if (reason.equals("assist")){
                        Log.e("333333333333","assist");

                    }
                }
            }
        }
    }
    private void setrodom(){
         thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (ooo){
                        Random rand = new Random();
                        Yinvalue = rand.nextInt(150)+30;
                        Log.e("987798789",String.valueOf(Yinvalue));
                        pointViewClass.setPointValue(Yinvalue);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    jishu++;
                  Log.e("1111",String.valueOf(jishu));
                }
            }
        });
        thread.start();
    }
}
