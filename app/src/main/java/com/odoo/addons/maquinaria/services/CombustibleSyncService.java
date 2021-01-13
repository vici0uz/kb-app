package com.odoo.addons.maquinaria.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.maquinaria.models.Combustible;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class CombustibleSyncService extends OSyncService {
    public static final String TAG = CombustibleSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context){
        return new OSyncAdapter(context, Combustible.class, service, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}
