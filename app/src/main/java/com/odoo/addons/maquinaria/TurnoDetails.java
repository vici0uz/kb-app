package com.odoo.addons.maquinaria;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.odoo.App;
import com.odoo.R;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.ir.feature.OFileManager;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.OdooCompatActivity;
import com.odoo.core.utils.OStringColorUtil;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import odoo.controls.OField;
import odoo.controls.OForm;

public class TurnoDetails extends OdooCompatActivity implements View.OnClickListener {
    public static final String TAG = TurnoDetails.class.getSimpleName();
    private final String KEY_MODE = "key_edit_mode";
    private final String KEY_NEW_IMAGE = "key_new_image";

    private Trabajo turnoTrabajo;
    private App app;
    private OForm mForm;
    private Boolean mEditMode = false;
    private Bundle extras;
    private ODataRow record = null;
    private Menu mMenu;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private OFileManager fileManager;
    private String newImage = null;

    private ImageView primerOdometro = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turno_detail);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.turno_collapsing_toolbar);

        toolbar = (Toolbar)  findViewById(R.id.turno_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        findViewById(R.id.turnoCaptureImage).setOnClickListener(this);

        fileManager = new OFileManager(this);
        if (toolbar != null)
            collapsingToolbarLayout.setTitle("");
        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(KEY_MODE);
            newImage = savedInstanceState.getString(KEY_NEW_IMAGE);
        }
        app = (App) getApplicationContext();
        turnoTrabajo = new Trabajo(this, null);
        extras = getIntent().getExtras();
        if (!hasRecordInExtra())
            mEditMode = true;
        setupToolbar();
    }

    private boolean hasRecordInExtra() {
        return extras != null && extras.containsKey(OColumn.ROW_ID);
    }



    private void setMode(Boolean edit) {
        Log.i(TAG, "ALAN DEBUG el valor de edit"+ edit.toString());
        findViewById(R.id.turnoCaptureImage).setVisibility(edit ? View.VISIBLE : View.GONE);
        if (mMenu != null) {
//            mMenu.findItem(R.id.menu_t_detail_more).setVisible(!edit);
            mMenu.findItem(R.id.menu_turno_edit).setVisible(!edit);
            mMenu.findItem(R.id.menu_turno_save).setVisible(edit);
            mMenu.findItem(R.id.menu_turno_cancel).setVisible(edit);
        }
        int color = Color.DKGRAY;
        if (record != null) {
            color = OStringColorUtil.getStringColor(this, record.getString("maquina_id"));
        }
        if (edit) {
            Log.i(TAG, "ALAN DEBUG: entra al edit");
            if (!hasRecordInExtra()) {
                collapsingToolbarLayout.setTitle("New");
            }
            mForm = (OForm) findViewById(R.id.turnoFormEdit);
            findViewById(R.id.turno_view_layout).setVisibility(View.GONE);
            findViewById(R.id.turno_edit_layout).setVisibility(View.VISIBLE);
//            OField is_company = (OField) findViewById(R.id.is_company_edit);
//            is_company.setOnValueChangeListener(this);
        } else {
            Log.i(TAG, "ALAN DEBUG: NO entra al edit");

            mForm = (OForm) findViewById(R.id.turnoForm);
            findViewById(R.id.turno_edit_layout).setVisibility(View.GONE);
            findViewById(R.id.turno_view_layout).setVisibility(View.VISIBLE);
        }
        setColor(color);
    }
    private void setupToolbar() {
        if (!hasRecordInExtra()) {
            setMode(mEditMode);
//            userImage.setColorFilter(Color.parseColor("#ffffff"));
//            userImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            mForm.setEditable(mEditMode);
            mForm.initForm(null);
        } else {
            int rowId = extras.getInt(OColumn.ROW_ID);
            record = turnoTrabajo.browse(rowId);
            Log.i(TAG, "ALAN DEBUG: el valor de record"+ record.getString(OColumn.ROW_ID));
//            record.put("full_address", resPartner.getAddress(record));
            checkControls();
            setMode(mEditMode);
            mForm.setEditable(mEditMode);
            mForm.initForm(record);
            collapsingToolbarLayout.setTitle(record.getString("maquina_id"));
//            setCustomerImage();
//            if (record.getInt("id") != 0 && record.getString("large_image").equals("false")) {
//                CustomerDetails.BigImageLoader bigImageLoader = new CustomerDetails.BigImageLoader();
//                bigImageLoader.execute(record.getInt("id"));
//            }
        }
    }
    private void setColor(int color) {
        mForm.setIconTintColor(color);
    }

    private void checkControls() {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_turno_save:
                OValues values = mForm.getValues();
                if(values != null){
                    if (newImage != null){
                        values.put("odometro_inicial_imagen", newImage);
                    }
                    if (record != null){
                        turnoTrabajo.update(record.getInt(OColumn.ROW_ID), values);
                        Toast.makeText(this, "Information saved", Toast.LENGTH_LONG).show();
                        mEditMode = !mEditMode;
                        setupToolbar();
                        turnoTrabajo.sync().requestSync(Trabajo.AUTHORITY);

//                        if (inNetwork()) {
//                            parent().sync().requestSync(Trabajo.AUTHORITY);
//                            setSwipeRefreshing(true);
//                        }
                    } else {
                        final int row_id = turnoTrabajo.insert(values);
                        if (row_id != OModel.INVALID_ROW_ID){
                            finish();
                        }
                    }
                }
                break;
            case R.id.menu_turno_cancel:
            case R.id.menu_turno_edit:
                if(hasRecordInExtra()){
                    mEditMode = !mEditMode;
                    Log.i(TAG, "ALAN DEBUG: el valor de mEditMode "+ mEditMode.toString());
                    setMode(mEditMode);
                    mForm.setEditable(mEditMode);
                    mForm.initForm(record);

                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_turno_detail, menu);
        mMenu = menu;
        setMode(mEditMode);
        return true;
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.turnoCaptureImage:
                fileManager.requestForFile(OFileManager.RequestType.CAPTURE_IMAGE);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_MODE, mEditMode);
        outState.putString(KEY_NEW_IMAGE, newImage);
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
