package com.odoo.addons.maquinaria.wizard;

import android.content.Intent;
import android.net.http.DelegatingSSLSession;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.OdooCompatActivity;
import com.odoo.core.utils.BitmapUtils;
import com.odoo.core.utils.ODateUtils;
import com.redbooth.WelcomeCoordinatorLayout;


public class AsistenteCierre extends OdooCompatActivity implements View.OnClickListener {
    private WelcomeCoordinatorLayout coordinatorLayout;

    private Trabajo turnoTrabajo;
    private Maquina maquinariaMaquina;
    private ODataRow record = null;
    private Bundle extras;
    private OFileManager fileManager;
    private OValues oValues = new OValues();
    private String newImage = null;
    private ImageView imgOdometro;
    private EditText entradaOdometro, entradaDescripcion, entradaObservacion, entradaCombustible;
    private TextView infoRecopilada;

    int maquinaId;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_root);

        turnoTrabajo = new Trabajo(this, null);
        maquinariaMaquina = new Maquina(this, null);

        fileManager = new OFileManager(this);
        extras = getIntent().getExtras();

        initializePages();

        setupMode();
    }

    private boolean hasRecordInExtra() {
        return extras != null && extras.containsKey(OColumn.ROW_ID);
    }

    private void setupMode(){
        if( hasRecordInExtra()){
            maquinaId = extras.getInt(OColumn.ROW_ID);
            record = maquinariaMaquina.browse(maquinaId);
        }
    }


    private void initializePages(){
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.showIndicators(true);
        coordinatorLayout.setScrollingEnabled(false);
        coordinatorLayout.addPage(R.layout.wizard_pagina_odometro, R.layout.wizard_pagina_descripcion_trabajo, R.layout.wizard_pagina_combustible, R.layout.wizard_info_confirmacion);

        entradaOdometro = (EditText) findViewById(R.id.entrada_odometro);
        entradaOdometro.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        entradaOdometro.setImeOptions(EditorInfo.IME_ACTION_DONE);

        imgOdometro = (ImageView) findViewById(R.id.odometro_img_view);

        entradaDescripcion =(EditText)findViewById(R.id.text_trabajo_edit);
        entradaObservacion = (EditText)findViewById(R.id.edit_observacion);

        entradaCombustible = (EditText)findViewById(R.id.entrada_combustible);
        entradaCombustible.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        entradaCombustible.setImeOptions(EditorInfo.IME_ACTION_DONE);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btn_registrar_odometro_img).setOnClickListener(this);
        findViewById(R.id.label_observacion).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int page = coordinatorLayout.getPageSelected();
        int pageNumber = (coordinatorLayout.getNumOfPages() +1);

        switch (view.getId()) {
            case R.id.next:
                switch (page){
                    case 0:
                        if(isEmpty(entradaOdometro)){
                            entradaOdometro.setError(getString(R.string.entrada_required));
                        }
                        else if(newImage == null){
                            Toast.makeText(this, getResources().getString(R.string.take_odometro_pic), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            float odometro_final = Float.parseFloat(entradaOdometro.getText().toString());
                            oValues.put("odometro_final", odometro_final);
                            oValues.put("odometro_final_imagen", newImage);
                            coordinatorLayout.setCurrentPage(page + 1, true);
                        }
                        break;
                    case 1:
                        if(isEmpty(entradaDescripcion)){
                            entradaDescripcion.setError(getString(R.string.entrada_required));

                        }
                        else{
                            oValues.put("descripcion", entradaDescripcion.getText().toString());
                            if (!isEmpty(entradaObservacion)){
                                oValues.put("observacion", entradaObservacion.getText().toString());
                                oValues.put("tiene_observacion", true);
                            }
                            coordinatorLayout.setCurrentPage(page + 1, true);
                            // INICIALIZA INMEDIATAMENTE LA 3 PAGINA

                        }
                        break;
                    case 2:
                        if(!isEmpty(entradaCombustible)){
                            oValues.put("combustible", Float.parseFloat(entradaCombustible.getText().toString()));
                        }
                        coordinatorLayout.setCurrentPage(page + 1, true);
                        findViewById(R.id.next).setVisibility(View.GONE);
                        findViewById(R.id.end).setVisibility(View.VISIBLE);
                        String info = getInfo();
                        infoRecopilada = (TextView) findViewById(R.id.info_recopilada);
                        infoRecopilada.setText(info);
                        break;
                    case 3:

                }
                break;
            case R.id.back:
                switch (page){
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
                if (record != null){
                    if(record.getInt("turno_abierto_id")!= null){
                        int turno_id = record.getInt("turno_abierto_id");
                        oValues.put("hora_final", ODateUtils.getUTCDate());
                        turnoTrabajo.update(turno_id, oValues);
                        OValues maquinaVals = new OValues();
                        maquinaVals.put("turno_estado", "close");
                        maquinaVals.put("turno_abierto_id", null);
                        maquinariaMaquina.update(maquinaId, maquinaVals);
                        maquinariaMaquina.sync().requestSync(Maquina.AUTHORITY);
                        turnoTrabajo.sync().requestSync(Trabajo.AUTHORITY);
                    }
                }
                else
                    Toast.makeText(this, "Algo salio mal!", Toast.LENGTH_SHORT).show();
                finish();

                break;
            case R.id.btn_registrar_odometro_img:
                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
                break;
            case R.id.label_observacion:
                findViewById(R.id.edit_observacion).setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        OValues values = fileManager.handleResult(requestCode, resultCode, data);
        if (values != null && !values.contains("size_limit_exceed")){
            newImage = values.getString("datas");
            imgOdometro.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imgOdometro.setColorFilter(null);
            imgOdometro.setImageBitmap(BitmapUtils.getBitmapImage(this, newImage));
            imgOdometro.setVisibility(View.VISIBLE);
        }
        else if (values != null){
            Toast.makeText(this, "Imagen muy grande", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private String getInfo(){
        String info = "";
        info = String.format("Odometro: %s\nTrabajo: %s\n ",oValues.getString("odometro_final"),oValues.getString("descripcion"));
        if(!isEmpty(entradaCombustible))
            info += String.format("Combustible: %s\n", oValues.getString("combustible"));
        if(!isEmpty(entradaObservacion))
            info += String.format("Observación: %s\n", oValues.getString("observación"));
        return info;
    }
}
