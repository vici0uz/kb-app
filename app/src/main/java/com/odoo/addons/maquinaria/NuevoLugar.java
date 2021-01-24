package com.odoo.addons.maquinaria;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.odoo.App;
import com.odoo.R;
import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.core.orm.OValues;
import com.odoo.core.support.OdooCompatActivity;

import odoo.controls.OForm;

public class NuevoLugar extends OdooCompatActivity implements View.OnClickListener {
    public static final String TAG = NuevoLugar.class.getSimpleName();

    private Destino destino;
    private OForm oForm;
    private Menu mMenu;
    private Toolbar toolbar;
    private App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lugar);
        toolbar = (Toolbar) findViewById(R.id.lugar_toolbar);
        setSupportActionBar(toolbar);

        destino = new Destino(this, null);

        app = (App) getApplicationContext();
        setup();
    }

    private void setup(){
        oForm = (OForm) findViewById(R.id.lugarForm);
        oForm.setEditable(true);
        oForm.initForm(null);

//a        checkControls();
    }

    public void checkControls(){
        findViewById(R.id.name).setOnClickListener(this);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_turno_detail, menu);
         mMenu = menu;
         return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_turno_save:
                OValues values = oForm.getValues();
                Toast.makeText(this, "sape", Toast.LENGTH_SHORT).show();
                    if (app.inNetwork()){
                        destino.insert(values);
                        destino.sync().requestSync(Destino.AUTHORITY);
                        Toast.makeText(app, "Nueva localidad guardada!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("¡Error de conexión!");
                        builder.setView(R.layout.dialog_crear_lugar);
                        builder.setPositiveButton("Intentar más tarde",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();

                        Toast.makeText(app, "Joder sin internet", Toast.LENGTH_SHORT).show();
                    }
                    break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

    }
}
