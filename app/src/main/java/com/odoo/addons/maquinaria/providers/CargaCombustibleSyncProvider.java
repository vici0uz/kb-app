package com.odoo.addons.maquinaria.providers;

import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.provider.BaseModelProvider;

public class CargaCombustibleSyncProvider extends BaseModelProvider {

        public static final String TAG = CargaCombustibleSyncProvider.class.getSimpleName();

        @Override
        public String authority(){
            return Trabajo.AUTHORITY;
        }
    }

