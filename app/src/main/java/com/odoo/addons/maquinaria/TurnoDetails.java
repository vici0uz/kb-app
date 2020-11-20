package com.odoo.addons.maquinaria;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.odoo.App;
import com.odoo.R;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.OdooCompatActivity;
import com.odoo.core.utils.OStringColorUtil;
import android.support.v7.widget.Toolbar;


import odoo.controls.OField;
import odoo.controls.OForm;

public class TurnoDetails extends OdooCompatActivity  {
    public static final String TAG = TurnoDetails.class.getSimpleName();
    private final String KEY_MODE = "key_edit_mode";
    private Trabajo turnoTrabajo;
    private App app;
    private OForm mForm;
    private Boolean mEditMode = false;
    private Bundle extras;
    private ODataRow record = null;
    private Menu mMenu;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.turno_detail);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.turno_collapsing_toolbar);

        toolbar = (Toolbar)  findViewById(R.id.turno_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (toolbar != null)
            collapsingToolbarLayout.setTitle("");
        if (savedInstanceState != null) {
            mEditMode = savedInstanceState.getBoolean(KEY_MODE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_detail, menu);
        mMenu = menu;
        setMode(mEditMode);
        return true;
    }

    private void setMode(Boolean edit) {
//        findViewById(R.id.captureImage).setVisibility(edit ? View.VISIBLE : View.GONE);
        if (mMenu != null) {
            mMenu.findItem(R.id.menu_customer_detail_more).setVisible(!edit);
            mMenu.findItem(R.id.menu_customer_edit).setVisible(!edit);
            mMenu.findItem(R.id.menu_customer_save).setVisible(edit);
            mMenu.findItem(R.id.menu_customer_cancel).setVisible(edit);
        }
        int color = Color.DKGRAY;
        if (record != null) {
            color = OStringColorUtil.getStringColor(this, record.getString("maquina_id"));
        }
        if (edit) {
            if (!hasRecordInExtra()) {
                collapsingToolbarLayout.setTitle("New");
            }
            mForm = (OForm) findViewById(R.id.turnoFormEdit);
            findViewById(R.id.turno_view_layout).setVisibility(View.GONE);
            findViewById(R.id.turno_edit_layout).setVisibility(View.VISIBLE);
//            OField is_company = (OField) findViewById(R.id.is_company_edit);
//            is_company.setOnValueChangeListener(this);
        } else {
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
//            record.put("full_address", resPartner.getAddress(record));
            checkControls();
            setMode(mEditMode);
            mForm.setEditable(mEditMode);
            mForm.initForm(record);
//            collapsingToolbarLayout.setTitle(record.getString("name"));
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
            case R.id.menu_customer_cancel:
            case R.id.menu_customer_edit:
                if(hasRecordInExtra()){
                    mEditMode = !mEditMode;
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
}
