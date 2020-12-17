package com.odoo.addons.maquinaria.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.NuevoLugar;
import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OTimestamp;
import com.odoo.core.support.OUser;
import com.odoo.core.support.OdooCompatActivity;
import com.odoo.core.utils.BitmapUtils;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.ODateUtils;
import com.redbooth.WelcomeCoordinatorLayout;

import java.util.ArrayList;
import java.util.List;

public class AsistenteNuevo extends OdooCompatActivity  implements View.OnClickListener {
    private WelcomeCoordinatorLayout coordinatorLayout;


    private OFileManager fileManager;
    private OValues values = new OValues();

    /* TABLAS */
    private Trabajo maquinariaTrabajoLinea;
    private Maquina maquinariaMaquina;
    private Destino maquinariaDestino;

    // Modelos
    private OModel modelLugar;
    private OModel modelMaquina;

    private ArrayList<String> listaMaquinas = new ArrayList<String>();
    private ArrayList<String> listaLugares = new ArrayList<String>();
    private List<ODataRow> recordMaquinas = null;
    private List<ODataRow> recordLugares = null;

//    Spinners
    private Spinner spinnerMaquina;
    private Spinner spinnerLugares;

    private EditText entradaOdometro;
    private TextView infoRecopilada;
    private String newImage = null;
    private ImageView imgOdometroInicial;
    private Bundle extras;


    private int rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wizard_root);

        fileManager = new OFileManager(this);

        maquinariaTrabajoLinea = new Trabajo(this, null);
        maquinariaMaquina = new Maquina(this, null);
        maquinariaDestino = new Destino(this, null);

        extras = this.getIntent().getExtras();

        initializePages();
    }

    private void initializePages() {
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.showIndicators(true);
        coordinatorLayout.setScrollingEnabled(false);
        coordinatorLayout.addPage(R.layout.wizard_inicial1,R.layout.wizard_inicial2,R.layout.wizard_pagina_odometro, R.layout.wizard_info_confirmacion);

        spinnerMaquina = (Spinner) findViewById(R.id.spinner_maquina);
        spinnerLugares = (Spinner) findViewById(R.id.spinner_lugar);

        entradaOdometro = (EditText) findViewById(R.id.entrada_odometro);
        entradaOdometro.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        entradaOdometro.setImeOptions(EditorInfo.IME_ACTION_DONE);

        imgOdometroInicial = (ImageView) findViewById(R.id.odometro_img_view);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btn_registrar_odometro_img).setOnClickListener(this);
        findViewById(R.id.label_lugar_perdido).setOnClickListener(this);

        getRecords();
    }

    private void getRecords(){
        modelMaquina = new OModel(this, "maquinaria.maquina", OUser.current(this));
        modelLugar = new OModel(this, "maquinaria.destino", OUser.current(this));

        recordMaquinas = modelMaquina.select();
        recordLugares = modelLugar.select();
        rowId = extras.getInt(OColumn.ROW_ID);
        int defaultPos=0;
        for (ODataRow row: recordMaquinas) {
            String maquinaName = maquinariaMaquina.browse(row.getInt("id")).getString("name");
            if (row.getInt("id").equals(rowId)){
                defaultPos = recordMaquinas.indexOf(row)+1;
            }
            listaMaquinas.add(maquinaName);
        }
        for (ODataRow row: recordLugares){
            String lugarName = maquinariaDestino.browse(row.getInt("id")).getString("name");
            listaLugares.add(lugarName);
        }

        int hidingItemIndex = 0;
        listaMaquinas.add(0,getString(R.string.pick_one_item) );
        listaLugares.add(0, getString(R.string.pick_one_item));

        CustomAdapter adapterSpinnerMaquinas = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaMaquinas, hidingItemIndex);
        CustomAdapter adapterSpinnerLugares = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaLugares, hidingItemIndex);

        spinnerMaquina.setAdapter(adapterSpinnerMaquinas);
        spinnerLugares.setAdapter(adapterSpinnerLugares);
        if(defaultPos !=0)
            spinnerMaquina.setSelection(defaultPos);

    }

    @Override
    public void onClick(View v) {
        int page = coordinatorLayout.getPageSelected();
        switch (v.getId()){
            case R.id.next:
                switch (page){
                    case 0:
                        if (spinnerMaquina.getSelectedItemPosition() != 0) {

                            int pos = spinnerMaquina.getSelectedItemPosition();
                            int maquina_id = recordMaquinas.get(pos-1).getInt(OColumn.ROW_ID);
                            values.put("maquina_id", maquina_id);
//                            coordinatorLayout.setCurrentPage(page + 1, true);

                        } else {
                            Toast.makeText(this, getResources().getText(R.string.please_pick_one), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 1:
                        if(spinnerLugares.getSelectedItemPosition() !=0){
                            int pos = spinnerLugares.getSelectedItemPosition();
                            int lugar_id = recordLugares.get(pos-1).getInt(OColumn.ROW_ID);
                            values.put("trabajo_destino", lugar_id);
                            coordinatorLayout.setCurrentPage(page+1,true);
                        }
                        else {
                            Toast.makeText(this, getResources().getText(R.string.please_pick_one), Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 2:
                        if(isEmpty(entradaOdometro)){
                            entradaOdometro.setError(getString(R.string.entrada_required));
                        }
                        else if(newImage == null){
                            Toast.makeText(this, getResources().getString(R.string.take_odometro_pic), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            float odometro_inicial = Float.parseFloat(entradaOdometro.getText().toString());
                            values.put("odometro_inicial", odometro_inicial);
                            values.put("odometro_inicial_imagen", newImage);
                            coordinatorLayout.setCurrentPage(3,true);
//                            INICIALIZA INMEDIATAMENTE LA 3 PAGINA
                            findViewById(R.id.next).setVisibility(View.GONE);
                            findViewById(R.id.end).setVisibility(View.VISIBLE);
                            String info = getInfo();
                            infoRecopilada = (TextView) findViewById(R.id.info_recopilada);
                            infoRecopilada.setText(info);
                        }
                        break;
                }
                break;
            case R.id.back:
                page = coordinatorLayout.getPageSelected();

                switch (page) {
                    case 0:
                        finish();
                        break;
                    case 3:
                        coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() - 1, true);
                        findViewById(R.id.next).setVisibility(View.VISIBLE);
                        findViewById(R.id.end).setVisibility(View.GONE);
                        break;
                    default:
                        coordinatorLayout.setCurrentPage(coordinatorLayout.getPageSelected() - 1, true);
                        break;
                }
                break;
            case R.id.end:

                values.put("hora_inicio", ODateUtils.getUTCDate());
                final int row_id = maquinariaTrabajoLinea.insert(values);
                if( row_id != OModel.INVALID_ROW_ID) {
                    Toast.makeText(this, getResources().getString(R.string.msg_data_saved), Toast.LENGTH_SHORT).show();
                    maquinariaTrabajoLinea.sync().requestSync(Trabajo.AUTHORITY);
                }
                OValues maquinaValues = new OValues();
                maquinaValues.put("turno_estado", "open");
                maquinaValues.put("turno_abierto_id", row_id);
                maquinariaMaquina.update(rowId, maquinaValues);


                finish();
                break;
            case R.id.btn_registrar_odometro_img:
                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
                break;
            case R.id.label_lugar_perdido:
                IntentUtils.startActivity(this, NuevoLugar.class, null);
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
        }
        else if (values != null){
            Toast.makeText(this, getResources().getString(R.string.image_too_big), Toast.LENGTH_LONG).show();
        }
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private String getInfo(){
        String info = String.format("Maquina: %s \n Lugar: %s \n Odometro actual: %s",spinnerMaquina.getSelectedItem().toString(), spinnerLugares.getSelectedItem().toString(), entradaOdometro.getText().toString());
    return info;
    }

}
