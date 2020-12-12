package com.odoo.addons.maquinaria.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.maquinaria.models.Destino;
import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class DestinoSyncService extends OSyncService {
    public static final String TAG = DestinoSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, Destino.class, service, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}

