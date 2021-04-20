package etti.agrosense.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Switch;

import java.util.HashMap;

import etti.agrosense.Helpers.RetrieveDataHelper;
import etti.agrosense.R;

public class UserSettings extends AppCompatActivity {
    private TextView mRelay1Value , mRelay2Value;
    private Switch mRelay1Switch, mRelay2Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        mRelay1Value = findViewById(R.id.relay1inputview);
        mRelay2Value = findViewById(R.id.relay2inputview);
        mRelay1Switch = findViewById(R.id.relay1switchview);
        mRelay2Switch = findViewById(R.id.relay2switchview);


    }

    public void save(View view)
    {
        setData();
    }

    public void setData() {
        Boolean switch1State = mRelay1Switch.isChecked();
        Boolean switch2State = mRelay2Switch.isChecked();

        String relay1Value = mRelay1Value.getText().toString();
        String relay2Value = mRelay2Value.getText().toString();

        String relay1State;
        String relay2State;

        if (switch1State)
            relay1State = "0";
        else
            relay1State = "1";

        if (switch2State)
            relay2State = "0";
        else
            relay2State = "1";


        HashMap<String, String> relayMap = new HashMap<>();

        relayMap.put("relay1Value", relay1Value);
        relayMap.put("relay2Value", relay2Value);
        relayMap.put("relay1State", relay1State);
        relayMap.put("relay2State", relay2State);

        RetrieveDataHelper.setRelay(relayMap);
    }

    public void back(View view)
    {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}