package com.odoo.addons.maquinaria.providers;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.customers.services.CustomerSyncService;
import com.odoo.addons.maquinaria.models.CargaCombustible;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.base.addons.res.ResPartner;
import com.odoo.core.orm.provider.BaseModelProvider;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class MaquinariaTrabajoSyncProvider extends BaseModelProvider {

        public static final String TAG = MaquinariaTrabajoSyncProvider.class.getSimpleName();

        @Override
        public String authority(){
            return CargaCombustible.AUTHORITY;
        }
    }

