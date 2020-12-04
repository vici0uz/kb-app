package com.odoo.addons.maquinaria.wizard;

import android.os.Bundle;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.support.OdooCompatActivity;
import com.redbooth.WelcomeCoordinatorLayout;

public class AsistenteNuevo extends OdooCompatActivity {
    private WelcomeCoordinatorLayout coordinatorLayout;
    private Trabajo turnoTrabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard);

        turnoTrabajo = new Trabajo(this, null);

        initializePages();


    }

    private void initializePages() {
        coordinatorLayout = (WelcomeCoordinatorLayout) findViewById(R.id.coordinator);
        coordinatorLayout.showIndicators(true);
        coordinatorLayout.setScrollingEnabled(true);
        coordinatorLayout.addPage(R.layout.wizard_inicial1,R.layout.wizard_inicial2, R.layout.wizard_inicial3);
    }
}
