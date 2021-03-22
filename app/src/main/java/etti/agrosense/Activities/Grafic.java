package etti.agrosense.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import etti.agrosense.Helpers.TinyDB;
import etti.agrosense.Models.MeasurementData;
import etti.agrosense.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;

public class Grafic extends AppCompatActivity
{
    private LineChart mChart;
    TextView mText;
    int dayvalue, color;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        TinyDB tinydb = new TinyDB(this);
        Bundle extras = getIntent().getExtras();
        final String value = extras.getString("payload");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafic);

        mText = findViewById(R.id.mText);
        mText.setText(value);
        mChart = findViewById(R.id.chart1);


        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        ArrayList<Entry> yValues = new ArrayList<>();

        if(value.equals("Temperature(°C)"))
        {
            ArrayList<MeasurementData> tempList = tinydb.getListMeasurementData("tempList");
            Log.d("SENSOR", "TEMPERATURE");
            for (MeasurementData elem_ : tempList) {
                yValues.add(new Entry(elem_.getId(), elem_.getValue()));
                mChart.getXAxis().setLabelCount(dayvalue);
                dayvalue++;
                color=1;
            }
        }

        if(value.equals("Pressure(Pa)"))
        {
            ArrayList<MeasurementData> pressList = tinydb.getListMeasurementData("pressList");
            Log.d("SENSOR", "PRESSURE");
            for (MeasurementData elem_ : pressList) {
                yValues.add(new Entry(elem_.getId(), elem_.getValue()));
                mChart.getXAxis().setLabelCount(dayvalue);
                dayvalue++;
                color=2;
            }
        }
        if(value.equals("Luminosity(%)"))
        {
            ArrayList<MeasurementData> luminList = tinydb.getListMeasurementData("luminList");
            Log.d("SENSOR", "LUMINOSITY");
            for (MeasurementData elem_ : luminList) {
                yValues.add(new Entry(elem_.getId(), elem_.getValue()));
                mChart.getXAxis().setLabelCount(dayvalue);
                dayvalue++;
                color=3;
            }
        }
        if(value.equals("CO2(ppm)"))
        {
            ArrayList<MeasurementData> co2List = tinydb.getListMeasurementData("co2List");
            Log.d("SENSOR", "CO2");
            for (MeasurementData elem_ : co2List) {
                yValues.add(new Entry(elem_.getId(), elem_.getValue()));
                mChart.getXAxis().setLabelCount(dayvalue);
                dayvalue++;
                color=4;
            }
        }
        if(value.equals("Humidity(%)"))
        {
            ArrayList<MeasurementData> humidList = tinydb.getListMeasurementData("humidList");
            Log.d("SENSOR", "HUMIDITY");
            for (MeasurementData elem_ : humidList) {
                yValues.add(new Entry(elem_.getId(), elem_.getValue()));
                mChart.getXAxis().setLabelCount(dayvalue);
                dayvalue++;
                color=5;
            }
        }

        LineDataSet set1 = new LineDataSet(yValues, "Data set");

        //GRAPH STYLE:

        //highlighter width
        set1.setHighlightLineWidth(1f);

        //changes the color of the line and the value points depending on the graph value
        set1.setFillAlpha(200);
        set1.setDrawFilled(true);

        switch (color)
        {
            case 1:
                //color for temp
                set1.setColor(Color.parseColor("#ff4d4d"));
                set1.setCircleColor(Color.parseColor("#DE3535"));
                set1.setHighLightColor(Color.parseColor("#ff6666"));
                set1.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.graph_gradient_temp));
                break;
            case 2:
                //color for press
                set1.setColor(Color.parseColor("#3454EA"));
                set1.setCircleColor(Color.parseColor("#1D3FDE"));
                set1.setHighLightColor(Color.parseColor("#4263FF"));
                set1.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.graph_gradient_press));
                break;
            case 3:
                //color for luminosity
                set1.setColor(Color.parseColor("#D68B38"));
                set1.setCircleColor(Color.parseColor("#C66D09"));
                set1.setHighLightColor(Color.parseColor("#E9983D"));
                set1.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.graph_gradient_luminosity));
                break;
            case 4:
                //color for co2
                set1.setColor(Color.parseColor("#6EDE9A"));
                set1.setCircleColor(Color.parseColor("#1CB658"));
                set1.setHighLightColor(Color.parseColor("#5BE08F"));
                set1.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.graph_gradient_co2));
                break;
            case 5:
                //color for hum
                set1.setColor(Color.parseColor("#66ccff"));
                set1.setCircleColor(Color.parseColor("#3399ff"));
                set1.setHighLightColor(Color.parseColor("#3366ff"));
                set1.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.graph_gradient_humidity));
                break;
        }

        //modifies the size of the line and the value points
        set1.setLineWidth(3f);
        set1.setCircleRadius(0f);

        //disables the values on top of the graph
        set1.setDrawValues(false);

        //creates animations
        mChart.animateX(1000);
        mChart.setDragEnabled(true);

        //disables graph description
        mChart.getDescription().setEnabled(false);

        //deactivates the graph grid
        mChart.getAxisLeft().setDrawGridLines(false);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.getAxisRight().setDrawGridLines(false);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        //modifies graph border and disables the legend
        mChart.getAxisLeft().setEnabled(true);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
    }

    public void Back(View view)
    {
        startActivity(new Intent(getApplicationContext(), History.class));
        finish();
    }

}
