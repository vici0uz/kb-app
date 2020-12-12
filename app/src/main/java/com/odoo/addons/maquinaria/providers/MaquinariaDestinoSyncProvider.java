package com.odoo.addons.maquinaria.providers;

import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.provider.BaseModelProvider;

public class MaquinariaDestinoSyncProvider extends BaseModelProvider {

        public static final String TAG = MaquinariaDestinoSyncProvider.class.getSimpleName();

        @Override
        public String authority(){
            return Destino.AUTHORITY;
        }
    }

