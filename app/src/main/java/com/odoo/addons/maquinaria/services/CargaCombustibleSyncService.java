package com.odoo.addons.maquinaria.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.maquinaria.models.CargaCombustible;
import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class CargaCombustibleSyncService extends OSyncService {
    public static final String TAG = CargaCombustibleSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, CargaCombustible.class, service, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}

