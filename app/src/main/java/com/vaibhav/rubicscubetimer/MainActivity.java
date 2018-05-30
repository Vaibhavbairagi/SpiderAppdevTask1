package com.vaibhav.rubicscubetimer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout mylayout = null;
    private TextView timer,stopwatch;
    private CountDownTimer countDownTimer;
    private long timeleftinmilliseconds = 15000;
    private ProgressBar progressBar;
    int counter,mins,secs,millisecs,tsecs,tmsecs;
    long MillisecondTime, StartTime ;
    Handler handler;
    private Button reset;
    MediaPlayer beep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = findViewById(R.id.timer);
        mylayout = findViewById(R.id.mylayout);
        mylayout.setOnTouchListener(touchListener);
        progressBar=findViewById(R.id.progressbar);
        stopwatch=findViewById(R.id.stopwatch);
        reset=findViewById(R.id.reset);
        counter=0;
        progressBar.setMax(15000);
        beep = MediaPlayer.create(this,R.raw.beepsound);
        progressBar.setProgress(15000);
        handler=new Handler();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetall();
            }
        });
    }

    public View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (counter == 0)
                starttimer();
            if (counter == 1) {
                stoptimer();
                startstopwatch();
            }
            if (counter == 2) {
                pausestopwatch();
                reset.setVisibility(View.VISIBLE);
            }
            counter++;
            return false;
        }
    };

    public void startstopwatch(){
        StartTime = SystemClock.uptimeMillis();
        handler.postDelayed(runnable, 0);
    }
    private void pausestopwatch(){
        handler.removeCallbacks(runnable);
    }

    public Runnable runnable = new Runnable() {

        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            secs = (int) (MillisecondTime / 1000);
            mins = secs / 60;
            secs = secs % 60;
            millisecs = (int) ((MillisecondTime/10) % 100);
            String str=String.format(Locale.getDefault(),"%02d:%02d:%02d",mins,secs,millisecs);
            stopwatch.setText(str);
            handler.postDelayed(this, 0);
        }

    };

    public void starttimer() {
        countDownTimer = new CountDownTimer(timeleftinmilliseconds, 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeleftinmilliseconds = millisUntilFinished;
                updatetimer();
                if(timeleftinmilliseconds==3000)
                    beep.start();
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                timer.setText("00:00");
                startstopwatch();
                counter=2;
            }
        }.start();
    }

    public void stoptimer() {
        countDownTimer.cancel();
        progressBar.setProgress((int) (timeleftinmilliseconds));
    }

    public void updatetimer() {
        tsecs = (int) timeleftinmilliseconds / 1000;
        tmsecs = (int) (timeleftinmilliseconds/10) % 100;
        String timetext= String.format(Locale.getDefault(),"%02d:%02d",tsecs,tmsecs);
        timer.setText(timetext);
        progressBar.setProgress((int) (timeleftinmilliseconds));

    }
    public void resetall(){
        MillisecondTime = 0L ;
        StartTime = 0L ;
        secs = 0 ;
        mins = 0 ;
        millisecs=0;
        tmsecs= 0 ;
        tsecs=0;
        timeleftinmilliseconds=15000;
        progressBar.setProgress(15000);
        timer.setText("15:00");
        stopwatch.setText("00:00:00");
        counter=0;
        reset.setVisibility(View.INVISIBLE);
    }

}