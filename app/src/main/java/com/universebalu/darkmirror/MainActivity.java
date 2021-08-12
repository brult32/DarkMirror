package com.universebalu.darkmirror;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageView ivBack;
    TextView tvAnswer, tvIntro;
    int textCount, textDelay;
    boolean loopCount, introOut;
    private AdView mAdView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    // APP id: ca-app-pub-9422077617828313~8718344844
    // AD id: ca-app-pub-9422077617828313/7527895565

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        ivBack = (ImageView) findViewById(R.id.ivBack);
        tvAnswer = (TextView) findViewById(R.id.tvAnswer);
        tvIntro = (TextView) findViewById(R.id.tvIntro);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        introOut();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAnswer();
            }
        });

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                /*
                 * The following method, "handleShakeEvent(count):" is a stub //
                 * method you would use to setup whatever you want done once the
                 * device has been shook.
                 */
                getAnswer();
            }
        });

    }

    private void introOut() {
        int introCount = 0;
        int introDelay = 5000;
        while (introCount < 10) {
            introDelay += 100;
            tvIntro.postDelayed(new Runnable() {
                public void run() {
                    float alpha = tvIntro.getAlpha();
                    tvIntro.setAlpha((float) (alpha - 0.1));
                }
            }, introDelay);
            introCount++;
        }
        introOut = true;
    }

    public void getAnswer(){
        tvIntro.setVisibility(View.INVISIBLE);
        if(!loopCount){
            loopCount = true;
            tvAnswer.setAlpha((float) 0.0);
            Random rand = new Random();
            final int rAnswer = rand.nextInt(5);
            switch (rAnswer) {
                case 0:
                    tvAnswer.setText(R.string.answer_yes);
                    break;
                case 1:
                    tvAnswer.setText(R.string.answer_no);
                    break;
                case 2:
                    tvAnswer.setText(R.string.answer_maybe);
                    break;
                case 3:
                    tvAnswer.setText(R.string.answer_definitely_yes);
                    break;
                case 4:
                    tvAnswer.setText(R.string.answer_definitely_not);
                    break;
            }
            showText();
        }
    }

    public void showText(){
        textCount = 0;
        textDelay = 300;
        while (textCount<20){
            textDelay += 100;
            tvAnswer.postDelayed(new Runnable() {
                public void run() {
                    float alpha = tvAnswer.getAlpha();
                    tvAnswer.setAlpha((float) (alpha + 0.05));
                }
            }, textDelay);
            textCount++;
        }
        textCount = 0;
        textDelay = 4000;
        while (textCount<10){
            textDelay += 50;
            tvAnswer.postDelayed(new Runnable() {
                public void run() {
                    float alpha = tvAnswer.getAlpha();
                    tvAnswer.setAlpha((float) (alpha - 0.1));
                    if (textCount == 10){
                        loopCount = false;
                    }
                }
            }, textDelay);
            textCount++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mShaker.resume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //mShaker.pause();
        mSensorManager.unregisterListener(mShakeDetector);

    }
}
