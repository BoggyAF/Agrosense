package etti.agrosense.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import etti.agrosense.Helpers.RetrieveDataHelper;
import etti.agrosense.Helpers.TinyDB;
import etti.agrosense.Models.MeasurementData;
import etti.agrosense.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    SharedPreferences tempPref, humidPref, pressPref, luminPref, co2Pref;

    TextView tempView, pressView, humidView, luminView, co2View;

    Button mHistory;
    Button mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setData();

        getSupportActionBar().hide(); // remove title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // enable full screen

    }

    public void refresh(View view)
    {
        setData();
    }

    public void history(View view)
    {
        mHistory = findViewById(R.id.mHistory);
        mHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), History.class));
                finish();
            }
        });
    }

    public void settings(View view)
    {
        mSettings = findViewById(R.id.settingsButton);
        mSettings.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(), UserSettings.class));
                finish();
            }
        });
    }

    public void logout(View view)
    {
        //De-logeaza user-ul si il trimite pe pagina de login
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }



    public void setData()
    {

        tempView = (TextView)findViewById(R.id.tempView);
        pressView = (TextView)findViewById(R.id.pressView);
        luminView = (TextView)findViewById(R.id.luminView);
        humidView = (TextView)findViewById(R.id.humidView);
        co2View = (TextView)findViewById(R.id.co2View);

        tempPref = this.getSharedPreferences("valueTemp", Context.MODE_PRIVATE);
        tempView.setText(tempPref.getString("valueTemp", ""));

        pressPref = this.getSharedPreferences("valuePressure", Context.MODE_PRIVATE);
        pressView.setText(pressPref.getString("valuePressure", ""));

        luminPref = this.getSharedPreferences("valueLumin", Context.MODE_PRIVATE);
        luminView.setText(luminPref.getString("valueLumin", ""));

        humidPref = this.getSharedPreferences("valueHumid", Context.MODE_PRIVATE);
        humidView.setText(humidPref.getString("valueHumidity", ""));

        co2Pref = this.getSharedPreferences("valueCO2", Context.MODE_PRIVATE);
        co2View.setText(co2Pref.getString("valueCO2", ""));

    }

}
