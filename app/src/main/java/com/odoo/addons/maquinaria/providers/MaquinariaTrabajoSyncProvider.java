package com.odoo.addons.maquinaria.providers;

import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.provider.BaseModelProvider;

public class MaquinariaTrabajoSyncProvider extends BaseModelProvider {

        public static final String TAG = MaquinariaTrabajoSyncProvider.class.getSimpleName();

        @Override
        public String authority(){
            return Trabajo.AUTHORITY;
        }
    }

