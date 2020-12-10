package com.odoo.addons.maquinaria.wizard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nicolkill.validator.NumberValidator;
import com.nicolkill.validator.Validator;
import com.odoo.R;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.OdooCompatActivity;
import com.redbooth.WelcomeCoordinatorLayout;


public class AsistenteCierre extends OdooCompatActivity implements View.OnClickListener {
    private WelcomeCoordinatorLayout coordinatorLayout;
    private Trabajo turnoTrabajo;
    private ODataRow record = null;
    private Bundle extras;
    private OFileManager fileManager;
    private OValues oValues;
    private String newImage = null;
    private ImageView imgHolder = null;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_root);

        turnoTrabajo = new Trabajo(this, null);
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
//            Log.i("ALAN DEBUG","hay extras" );
            int rowId = extras.getInt(OColumn.ROW_ID);
            record = turnoTrabajo.browse(rowId);

        }

    }


    private void initializePages(){
            coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
            coordinatorLayout.showIndicators(true);
            coordinatorLayout.setScrollingEnabled(true);
            coordinatorLayout.addPage(R.layout.wizard_inicial3, R.layout.wizard_layout4, R.layout.wizard_layout5);
//            TimePicker timePicker = (TimePicker) findViewById(R.id.time_picker_horas);
//            timePicker.setIs24HourView(true);

        findViewById(R.id.next).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        findViewById(R.id.btn_registrar_odometro_img).setOnClickListener(this);
        findViewById(R.id.label_observacion).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int page = coordinatorLayout.getPageSelected();

        switch (view.getId()) {
            case R.id.next:
                switch (page){
                    case 0:
                        coordinatorLayout.setCurrentPage(page + 1, true);
                        break;
                    case 1:
                        break;
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
            case R.id.end:
                break;
            case R.id.btn_registrar_odometro_img:
                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
                break;
            case R.id.label_observacion:
                findViewById(R.id.edit_observacion).setVisibility(View.VISIBLE);
                break;
//            case R.id.btn_registrar_odometro:
////                Toast.makeText(this, "Joder", Toast.LENGTH_SHORT).show();
//                Validator validator = new NumberValidator();
//                oValues = new OValues();
//                EditText entrada_odometro = (EditText) findViewById(R.id.entrada_odometro);
//                if(validator.isValid(entrada_odometro.getText().toString())){
//                    Toast.makeText(this, "Joder hay numero", Toast.LENGTH_SHORT).show();
//                    oValues.put("odometro_inicial", entrada_odometro.getText().toString());
////                    turnoTrabajo.update(record.getInt(OColumn.ROW_ID), oValues);
////                    turnoTrabajo.sync().requestSync(Trabajo.AUTHORITY);
//                    coordinatorLayout.setCurrentPage(coordinatorLayout.getNumOfPages() - 1, true);
//
//                }
//                else
//                    Toast.makeText(this, "La cagaste mal papu", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btn_registrar_odometro_imagen:
////                Log.i("ALAN DEBUG", "Img");
//                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
//                break;
//
//        }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        OValues values = fileManager.handleResult(requestCode, resultCode, data);
        if (values != null && !values.contains("size_limit_exceed")){
            newImage = values.getString("datas");
            Toast.makeText(this, "joder hay foto",Toast.LENGTH_LONG).show();
        }
        else if (values != null){
            Toast.makeText(this, "Imagen muy grande", Toast.LENGTH_LONG).show();
        }
    }
}
