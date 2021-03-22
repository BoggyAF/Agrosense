package etti.agrosense.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import etti.agrosense.Helpers.RetrieveDataHelper;
import etti.agrosense.R;

public class SelectDaysForGraph extends AppCompatActivity {
    int dayvalue, NumberOfHistoricMeasurements;
    TextView valueText;
    SeekBar seekBar;
    String value;
    SharedPreferences nrPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        value = extras.getString("payload");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_days_for_graph);

        valueText=findViewById(R.id.valueTxt);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setProgress(1);
        seekBar.setMax(31);
        valueText.setText("1");

        //listener pentru seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b)
            {
                if(progress==0)
                {
                    seekBar.setProgress(1);
                    progress=1;
                }
                valueText.setText(""+progress);
                dayvalue=progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //Toast.makeText(getApplicationContext(),"seekbar touch started!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                //Toast.makeText(getApplicationContext(),"seekbar touch stopped!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setValue(View view)
    {
        Log.d("DEBUG", "MESAJ - SETVALUE");
        Log.d("DEBUG", "zile:" + dayvalue);
        RetrieveDataHelper.getNumberOfHistoricMeasurements(SelectDaysForGraph.this);
        nrPref = this.getSharedPreferences("NumberOfHistoricMeasurements", Context.MODE_PRIVATE);
        NumberOfHistoricMeasurements = nrPref.getInt("NumberOfHistoricMeasurements",0 );
        //if(dayvalue > NumberOfHistoricMeasurements )
        {
            //Toast.makeText(getApplicationContext(),"Nr maxim de zile salvate este: " + NumberOfHistoricMeasurements , Toast.LENGTH_SHORT).show();
        }
        //else
        {
            if(value.equals("Temperature(°C)"))
                RetrieveDataHelper.getTemp(dayvalue, SelectDaysForGraph.this);
            else if(value.equals("Pressure(Pa)"))
                RetrieveDataHelper.getPress(dayvalue, SelectDaysForGraph.this);
            else if(value.equals("Luminosity(%)"))
                RetrieveDataHelper.getLumin(dayvalue, SelectDaysForGraph.this);
            else if(value.equals("Humidity(%)"))
                RetrieveDataHelper.getHumid(dayvalue, SelectDaysForGraph.this);
            else if(value.equals("CO2(ppm)"))
                RetrieveDataHelper.getCO2(dayvalue, SelectDaysForGraph.this);
            Intent i = new Intent(SelectDaysForGraph.this, Grafic.class);
            i.putExtra("payload", value);
            startActivity(i);
            finish();
        }

    }
    public void back(View view)
    {
        startActivity(new Intent(getApplicationContext(), History.class));
        finish();
    }



}