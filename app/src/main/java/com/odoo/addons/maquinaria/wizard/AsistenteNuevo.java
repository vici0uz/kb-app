package com.odoo.addons.maquinaria.wizard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.support.OUser;
import com.odoo.core.support.OdooCompatActivity;
import com.odoo.core.utils.BitmapUtils;
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
//    private ArrayAdapter<String> adapterSpinnerMaquinas;
//    private ArrayAdapter<String> adapterSpinnerLugares;
    private OFileManager fileManager;
    private String newImage = null;
    private ImageView imgOdometroInicial;



    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wizard);

//        context = this.getApplicationContext();
        fileManager = new OFileManager(this);

        turnoTrabajo = new Trabajo(this, null);
        maquina = new Maquina(this, null);
        lugarTrabajo = new Destino(this, null);

        initializePages();

    }

    private void initializePages() {
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.showIndicators(true);
        coordinatorLayout.setScrollingEnabled(false);
        coordinatorLayout.addPage(R.layout.wizard_inicial1,R.layout.wizard_inicial2,R.layout.wizard_inicial3, R.layout.wizard_inicial4);

        spinnerMaquina = (Spinner) findViewById(R.id.spinner_maquina);
        spinnerLugares = (Spinner) findViewById(R.id.spinner_lugar);

        imgOdometroInicial = (ImageView) findViewById(R.id.odometro_inicial_img_view);
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

        int hidingItemIndex = 0;
        listaMaquinas.add(0, "Pick one");
        listaLugares.add(0, "Pick one");

        CustomAdapter adapterSpinnerMaquinas = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaMaquinas, hidingItemIndex);
        CustomAdapter adapterSpinnerLugares = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaLugares, hidingItemIndex);

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
                switch (page){
                }
                coordinatorLayout.setCurrentPage(page,true);

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
                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        OValues values = fileManager.handleResult(requestCode, resultCode, data);
        if (values != null && !values.contains("size_limit_exceed")){
            newImage = values.getString("datas");
            imgOdometroInicial.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgOdometroInicial.setColorFilter(null);
            imgOdometroInicial.setImageBitmap(BitmapUtils.getBitmapImage(this, newImage));
            imgOdometroInicial.setVisibility(View.VISIBLE);
            Toast.makeText(this, "joder hay foto",Toast.LENGTH_LONG).show();

        }
        else if (values != null){
            Toast.makeText(this, "Imagen muy grande", Toast.LENGTH_LONG).show();
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private int hidingItemIndex;

        CustomAdapter(Context context, int textViewResourceId, List<String> objects, int hidingItemIndex) {
            super(context, textViewResourceId, objects);
            this.hidingItemIndex = hidingItemIndex;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = null;
            if (position == hidingItemIndex) {
                TextView tv = new TextView(getContext());
                tv.setVisibility(View.GONE);
                v = tv;
            } else {
                v = super.getDropDownView(position, null, parent);
            }
            return v;
        }
    }
}
