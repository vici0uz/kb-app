package com.odoo.addons.maquinaria;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.odoo.R;

//import static com.odoo.R.id.sape;


public class CustomUpdate extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        Button button = (Button) findViewById(R.id.btn_buscar_actualizacion);
        button.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_buscar_actualizacion:
                new AppUpdater(this)
                        .setUpdateFrom(UpdateFrom.JSON)
                        .setDisplay(Display.DIALOG)
                        .showAppUpdated(true)
                        .setUpdateJSON("http://www.xiton.net/app/update.json")
                        .start();
                break;
        }
//        AppUpdater appUpdater
    }
}
