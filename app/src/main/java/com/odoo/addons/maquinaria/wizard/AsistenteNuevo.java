package com.odoo.addons.maquinaria.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.odoo.core.orm.fields.OColumn;
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
    private List<ODataRow> recordMaquinas = null;
    private List<ODataRow> recordLugares = null;
    private Spinner spinnerMaquina;
    private Spinner spinnerLugares;
    private EditText entradaOdometro;
    private TextView infoRecopilada;
    private OFileManager fileManager;
    private String newImage = null;
    private ImageView imgOdometroInicial;
    private OValues values = new OValues();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wizard_root);

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

        entradaOdometro = (EditText) findViewById(R.id.entrada_odometro_inicial);
        imgOdometroInicial = (ImageView) findViewById(R.id.odometro_inicial_img_view);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btn_registrar_odometro_img).setOnClickListener(this);

        getRecords();
    }

    private void getRecords(){
        OModel modelMaquina = new OModel(this, "maquinaria.maquina", OUser.current(this));
        OModel modelLugar = new OModel(this, "maquinaria.destino", OUser.current(this));
        recordMaquinas =modelMaquina.select();
        recordLugares = modelLugar.select();
        for (ODataRow row: recordMaquinas) {
            String maquinaName = maquina.browse(row.getInt("id")).getString("name");
            listaMaquinas.add(maquinaName);
        }
        for (ODataRow row: recordLugares){
            String lugarName = lugarTrabajo.browse(row.getInt("id")).getString("name");
            listaLugares.add(lugarName);
        }

        int hidingItemIndex = 0;
        listaMaquinas.add(0,getString(R.string.pick_one_item) );
        listaLugares.add(0, getString(R.string.pick_one_item));

        CustomAdapter adapterSpinnerMaquinas = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaMaquinas, hidingItemIndex);
        CustomAdapter adapterSpinnerLugares = new CustomAdapter(this, android.R.layout.simple_spinner_dropdown_item, listaLugares, hidingItemIndex);

        spinnerMaquina.setAdapter(adapterSpinnerMaquinas);
        spinnerLugares.setAdapter(adapterSpinnerLugares);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next:
                int page = coordinatorLayout.getPageSelected();
                Log.i("ALAN DEBUG pagina", String.valueOf(page));
                switch (page){
                    case 0:
                        if (spinnerMaquina.getSelectedItemPosition() != 0) {

                            int pos = spinnerMaquina.getSelectedItemPosition();
                            int maquina_id = recordMaquinas.get(pos-1).getInt(OColumn.ROW_ID);
                            values.put("maquina_id", maquina_id);
                            coordinatorLayout.setCurrentPage(page + 1, true);

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
                            entradaOdometro.setError("Requerido");
                        }
                        else if(newImage == null){
                            Toast.makeText(this, getResources().getString(R.string.take_odometro_pic), Toast.LENGTH_SHORT).show();
                        }
                        else {
                            float odometro_inicial = Float.parseFloat(entradaOdometro.getText().toString());
                            values.put("odometro_inicial", odometro_inicial);
                            values.put("odometro_inicial_imagen", newImage);
                            coordinatorLayout.setCurrentPage(3,true);
//                            INICIALIZAR 3 PAGINA
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
                final int row_id = turnoTrabajo.insert(values);
                if( row_id != OModel.INVALID_ROW_ID) {
                    Toast.makeText(this, getResources().getString(R.string.msg_data_saved), Toast.LENGTH_SHORT).show();
                    turnoTrabajo.sync().requestSync(Trabajo.AUTHORITY);
                }
                finish();
                break;
            case R.id.btn_registrar_odometro_img:
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
//            Toast.makeText(this, "joder hay foto",Toast.LENGTH_LONG).show();

        }
        else if (values != null){
            Toast.makeText(this, getResources().getString(R.string.image_too_big), Toast.LENGTH_LONG).show();
        }
    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private String getInfo(){
        String info = String.format("Maquína: %s \n Lugar: %s \n Odometro actual: %s",spinnerMaquina.getSelectedItem().toString(), spinnerLugares.getSelectedItem().toString(), entradaOdometro.getText().toString());
    return info;
    }

}