package etti.agrosense.Helpers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Measure;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import etti.agrosense.Activities.History;
import etti.agrosense.Models.MeasurementData;
import etti.agrosense.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;

public class RetrieveDataHelper {

    static DatabaseReference ref, ref2;
    static String value;
    static SharedPreferences tempPref, luminPref, humidPref, co2Pref, pressPref, nrPref;
    static float valueMeasurement;
    static long timestampMeasurement;
    static int tempID, luminID, humidID, co2ID, pressID, numberOfHistoricMeasurements;

    public static void getTemp(int daysCount, Context context)
    {
        final TinyDB tinydb = new TinyDB(context);
        tempPref = context.getSharedPreferences("valueTemp", Context.MODE_PRIVATE);
        ref2= FirebaseDatabase.getInstance().getReference().child("Sensors").child("DailyMeasurements").child("Temperature");
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref2.keepSynced(true);
        ref.keepSynced(true);
        if(daysCount==0)
        {
            ValueEventListener eventListener =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        value = ds.child("value").getValue(Float.class) + ds.child("measure_unit").getValue(String.class);
                    }
                    tempPref.edit().putString("valueTemp",value).apply();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref2.orderByKey().limitToLast(1).addValueEventListener(eventListener);
        }

        else
        {
            ValueEventListener eventListener2 =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MeasurementData> tempList = new ArrayList<MeasurementData>();
                    tempID = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        valueMeasurement = ds.child("Temperature").getValue(Float.class);
                        timestampMeasurement = ds.child("Timestamp").getValue(Integer.class);

                        MeasurementData newData = new MeasurementData(valueMeasurement, timestampMeasurement, tempID, "°C");
                        tempList.add(newData);

                        tempID = tempID + 1;
                    }
                    tinydb.putListMeasurementData("tempList", tempList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref.orderByKey().limitToLast(daysCount).addValueEventListener(eventListener2);
        }
    }

    public static void getLumin(int daysCount, Context context)
    {
        final TinyDB tinydb = new TinyDB(context);
        luminPref = context.getSharedPreferences("valueLumin", Context.MODE_PRIVATE);
        ref2= FirebaseDatabase.getInstance().getReference().child("Sensors").child("DailyMeasurements").child("Luminosity");
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref2.keepSynced(true);
        ref.keepSynced(true);
        if(daysCount==0)
        {
            ValueEventListener eventListener =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        value = ds.child("value").getValue(Integer.class) + ds.child("measure_unit").getValue(String.class);
                    }
                    luminPref.edit().putString("valueLumin",value).apply();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref2.orderByKey().limitToLast(1).addValueEventListener(eventListener);
        }

        else
        {
            ValueEventListener eventListener2 =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MeasurementData> luminList = new ArrayList<MeasurementData>();
                    luminID = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        valueMeasurement = ds.child("Luminosity").getValue(Float.class);
                        timestampMeasurement = ds.child("Timestamp").getValue(Integer.class);

                        MeasurementData newData = new MeasurementData(valueMeasurement, timestampMeasurement, luminID, "ppb");
                        luminList.add(newData);

                        luminID = luminID + 1;
                    }
                    tinydb.putListMeasurementData("luminList", luminList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref.orderByKey().limitToLast(daysCount).addValueEventListener(eventListener2);
        }
    }
    public static void getHumid(int daysCount, Context context)
    {
        final TinyDB tinydb = new TinyDB(context);
        humidPref = context.getSharedPreferences("valueHumid", Context.MODE_PRIVATE);
        ref2= FirebaseDatabase.getInstance().getReference().child("Sensors").child("DailyMeasurements").child("Humidity");
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref2.keepSynced(true);
        ref.keepSynced(true);
        if(daysCount==0)
        {
            ValueEventListener eventListener =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        value = ds.child("value").getValue(Integer.class) + ds.child("measure_unit").getValue(String.class);
                    }
                    humidPref.edit().putString("valueHumidity",value).apply();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref2.orderByKey().limitToLast(1).addValueEventListener(eventListener);
        }

        else
        {
            ValueEventListener eventListener2 =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MeasurementData> humidList = new ArrayList<MeasurementData>();
                    humidID = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        valueMeasurement = ds.child("Humidity").getValue(Float.class);
                        timestampMeasurement = ds.child("Timestamp").getValue(Integer.class);

                        MeasurementData newData = new MeasurementData(valueMeasurement, timestampMeasurement, humidID, "°C");
                        humidList.add(newData);

                        humidID = humidID + 1;
                    }
                    tinydb.putListMeasurementData("humidList", humidList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref.orderByKey().limitToLast(daysCount).addValueEventListener(eventListener2);
        }
    }
    public static void getCO2(int daysCount, Context context)
    {
        final TinyDB tinydb = new TinyDB(context);
        co2Pref = context.getSharedPreferences("valueCO2", Context.MODE_PRIVATE);
        ref2= FirebaseDatabase.getInstance().getReference().child("Sensors").child("DailyMeasurements").child("CO2");
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref2.keepSynced(true);
        ref.keepSynced(true);
        if(daysCount==0)
        {
            ValueEventListener eventListener =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        value = ds.child("value").getValue(Float.class) + ds.child("measure_unit").getValue(String.class);
                    }
                    co2Pref.edit().putString("valueCO2",value).apply();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref2.orderByKey().limitToLast(1).addValueEventListener(eventListener);
        }

        else
        {
            ValueEventListener eventListener2 =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MeasurementData> co2List = new ArrayList<MeasurementData>();
                    co2ID = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        valueMeasurement = ds.child("CO2").getValue(Float.class);
                        timestampMeasurement = ds.child("Timestamp").getValue(Integer.class);

                        MeasurementData newData = new MeasurementData(valueMeasurement, timestampMeasurement, co2ID, "ppm");
                        co2List.add(newData);

                        co2ID = co2ID + 1;
                    }
                    tinydb.putListMeasurementData("co2List", co2List);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref.orderByKey().limitToLast(daysCount).addValueEventListener(eventListener2);
        }
    }
    public static void getPress(int daysCount, Context context)
    {
        final TinyDB tinydb = new TinyDB(context);
        pressPref = context.getSharedPreferences("valuePressure", Context.MODE_PRIVATE);
        ref2= FirebaseDatabase.getInstance().getReference().child("Sensors").child("DailyMeasurements").child("Pressure");
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref2.keepSynced(true);
        ref.keepSynced(true);
        if(daysCount==0)
        {
            ValueEventListener eventListener =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        value = ds.child("value").getValue(Float.class) + ds.child("measure_unit").getValue(String.class);
                    }
                    pressPref.edit().putString("valuePressure",value).apply();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref2.orderByKey().limitToLast(1).addValueEventListener(eventListener);
        }

        else
        {
            ValueEventListener eventListener2 =  new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<MeasurementData> pressList = new ArrayList<MeasurementData>();
                    pressID = 1;
                    for(DataSnapshot ds : dataSnapshot.getChildren())
                    {
                        valueMeasurement = ds.child("Pressure").getValue(Float.class);
                        timestampMeasurement = ds.child("Timestamp").getValue(Integer.class);

                        MeasurementData newData = new MeasurementData(valueMeasurement, timestampMeasurement, pressID, "Pa");
                        pressList.add(newData);

                        pressID = pressID + 1;
                    }
                    tinydb.putListMeasurementData("pressList", pressList);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("RetrieveData:", databaseError.getMessage());
                }
            };
            ref.orderByKey().limitToLast(daysCount).addValueEventListener(eventListener2);
        }
    }


    public static void getNumberOfHistoricMeasurements(Context context)
    {
        nrPref = context.getSharedPreferences("NumberOfHistoricMeasurements", Context.MODE_PRIVATE);
        ref = FirebaseDatabase.getInstance().getReference().child("Sensors").child("HistoricMeasurements");
        ref.keepSynced(true);
        numberOfHistoricMeasurements=0;
        ValueEventListener eventListener2 =  new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<MeasurementData> tempList = new ArrayList<MeasurementData>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    numberOfHistoricMeasurements++;
                }
                nrPref.edit().putInt("NumberOfHistoricMeasurements",numberOfHistoricMeasurements).apply();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("RetrieveData:", databaseError.getMessage());
            }
        };
        ref.orderByKey().addValueEventListener(eventListener2);
    }
}