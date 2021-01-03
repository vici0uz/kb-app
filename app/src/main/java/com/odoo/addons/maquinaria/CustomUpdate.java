package com.odoo.addons.maquinaria;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.odoo.R;

import static com.odoo.R.id.sape;

//import static com.odoo.R.id.sape;


public class CustomUpdate extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        Button button = (Button) findViewById(sape);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Log.i("ALAN DEBUG: ", "hoal alan");
//        AppUpdater appUpdater
    }
}
