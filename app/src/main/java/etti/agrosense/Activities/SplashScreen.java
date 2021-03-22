package etti.agrosense.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import etti.agrosense.Helpers.RetrieveDataHelper;
import etti.agrosense.R;

import static android.util.Log.d;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getSupportActionBar().hide(); // remove title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // enable full screen

        RetrieveDataHelper.getTemp(0,SplashScreen.this);
        RetrieveDataHelper.getHumid(0,SplashScreen.this);
        RetrieveDataHelper.getCO2(0,SplashScreen.this);
        RetrieveDataHelper.getPress(0,SplashScreen.this);
        RetrieveDataHelper.getLumin(0,SplashScreen.this);

        new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

            @Override

            public void run() {

                Intent mainIntent = new Intent(SplashScreen.this,Register.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }

        }, 5*1000);


    }




}
