package etti.agrosense.Activities;

import androidx.appcompat.app.AppCompatActivity;
import etti.agrosense.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class History extends AppCompatActivity
{
    String payload;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    public void temp(View view)
    {
        payload = "Temperature(°C)";
        Intent i = new Intent(History.this, SelectDaysForGraph.class);
        i.putExtra("payload", payload);
        startActivity(i);
        finish();
    }

    public void press(View view)
    {
        payload = "Pressure(Pa)";
        Intent i = new Intent(History.this, SelectDaysForGraph.class);
        i.putExtra("payload", payload);
        startActivity(i);
        finish();
    }

    public void hum(View view)
    {
        payload = "Humidity(%)";
        Intent i = new Intent(History.this, SelectDaysForGraph.class);
        i.putExtra("payload", payload);
        startActivity(i);
        finish();
    }

    public void lumin(View view)
    {
        payload = "Luminosity(%)";
        Intent i = new Intent(History.this, SelectDaysForGraph.class);
        i.putExtra("payload", payload);
        startActivity(i);
        finish();
    }

    public void co2(View view)
    {
        payload = "CO2(ppm)";
        Intent i = new Intent(History.this, SelectDaysForGraph.class);
        i.putExtra("payload", payload);
        startActivity(i);
        finish();
    }

    public void back(View view)
    {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

}