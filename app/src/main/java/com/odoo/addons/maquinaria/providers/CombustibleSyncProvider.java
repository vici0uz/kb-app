package com.odoo.addons.maquinaria.providers;

import com.odoo.addons.maquinaria.models.Combustible;
import com.odoo.core.orm.provider.BaseModelProvider;

public class CombustibleSyncProvider extends BaseModelProvider {
    public static final String TAG = CombustibleSyncProvider.class.getSimpleName();

    @Override
    public String authority(){
        return Combustible.AUTHORITY;
    }
}
