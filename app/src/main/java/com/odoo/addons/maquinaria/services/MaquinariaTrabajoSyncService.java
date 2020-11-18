package com.odoo.addons.maquinaria.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.maquinaria.models.Trabajo;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

public class MaquinariaTrabajoSyncService extends OSyncService {
    public static final String TAG = MaquinariaTrabajoSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(context, Trabajo.class, service, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}

