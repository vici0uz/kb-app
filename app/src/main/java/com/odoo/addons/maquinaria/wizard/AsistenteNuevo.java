package com.odoo.addons.maquinaria.wizard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.support.OUser;
import com.odoo.core.support.OdooCompatActivity;
import com.redbooth.WelcomeCoordinatorLayout;

import java.util.ArrayList;
import java.util.List;

public class AsistenteNuevo extends OdooCompatActivity  implements View.OnClickListener {
    private WelcomeCoordinatorLayout coordinatorLayout;
    private Trabajo turnoTrabajo;
    private Maquina maquina;
    private Destino lugarTrabajo;
    private ArrayList<String> listaMaquinas = new ArrayList<String>();
    private ArrayList<String> listaLugares = new ArrayList<String>();
    private Spinner spinnerMaquina;
    private Spinner spinnerLugares;
    private ArrayAdapter<String> adapterSpinnerMaquinas;
    private ArrayAdapter<String> adapterSpinnerLugares;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wizard);

        context = this.getApplicationContext();
        turnoTrabajo = new Trabajo(this, null);
        maquina = new Maquina(this, null);
        lugarTrabajo = new Destino(this, null);

        initializePages();


    }

    private void initializePages() {
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.showIndicators(true);
        coordinatorLayout.setScrollingEnabled(false);
        coordinatorLayout.addPage(R.layout.wizard_inicial1,R.layout.wizard_inicial2,R.layout.wizard_inicial4, R.layout.wizard_inicial3);
        spinnerMaquina = (Spinner) findViewById(R.id.spinner_maquina);
        spinnerLugares = (Spinner) findViewById(R.id.spinner_lugar);


        getRecords();
    }

    private void getRecords(){
        OModel modelMaquina = new OModel(this, "maquinaria.maquina", OUser.current(this));
        OModel modelLugar = new OModel(this, "maquinaria.destino", OUser.current(this));
        List<ODataRow> recordsMaquinas =modelMaquina.select();
        List<ODataRow> recordLugares = modelLugar.select();
        for (ODataRow row: recordsMaquinas) {
//            Log.i("ALAN DEBUG ", maquina.browse(row.getInt("id")).getString("name"));
            String maquinaName = maquina.browse(row.getInt("id")).getString("name");
            Log.i("ALAN DEBUG ", maquinaName);
            listaMaquinas.add(maquinaName);
        }
        for (ODataRow row: recordLugares){
            String lugarName = lugarTrabajo.browse(row.getInt("id")).getString("name");
            listaLugares.add(lugarName);
        }

        adapterSpinnerMaquinas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaMaquinas);
        adapterSpinnerLugares = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaLugares);

        spinnerMaquina.setAdapter(adapterSpinnerMaquinas);
        spinnerLugares.setAdapter(adapterSpinnerLugares);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btn_registrar_odometro_inicial_imagen).setOnClickListener(this);

//        model.
//        Log.i("ALAN DEBUG", model.select("maquinaria_maquina").toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                Toast.makeText(this, "joder", Toast.LENGTH_LONG).show();
                Log.i("ALAN DEBUG pagina", String.valueOf(coordinatorLayout.getPageSelected()));
                int page = coordinatorLayout.getPageSelected()+1;
                coordinatorLayout.setCurrentPage(page,true);
//                coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
//                    @Override
//                    public void onScrollPage(View v, float progress, float maximum) {
//                        Log.i("ALAN DEBUG", String.valueOf(coordinatorLayout.getPageSelected()));
//                    }
//
//                    @Override
//                    public void onPageSelected(View v, int pageSelected) {
//
//                    }
//                });
                if (coordinatorLayout.getPageSelected() == (coordinatorLayout.getNumOfPages() -1)){
                    findViewById(R.id.next).setVisibility(View.GONE);
                    findViewById(R.id.end).setVisibility(View.VISIBLE);
                }
                break;
            case R.id.back:
                if (coordinatorLayout.getPageSelected() == 0){
                    finish();
                }else {
                    coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected()-1,true);
                }
                break;
            case R.id.end:
                Toast.makeText(this, "Finalizar", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_registrar_odometro_inicial_imagen:

        }
    }
}
