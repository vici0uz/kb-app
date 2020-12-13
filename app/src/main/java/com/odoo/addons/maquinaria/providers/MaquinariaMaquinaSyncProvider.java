package com.odoo.addons.maquinaria.providers;

import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.orm.provider.BaseModelProvider;

public class MaquinariaMaquinaSyncProvider extends BaseModelProvider {

        public static final String TAG = MaquinariaMaquinaSyncProvider.class.getSimpleName();

        @Override
        public String authority(){
            return Maquina.AUTHORITY;
        }
    }

