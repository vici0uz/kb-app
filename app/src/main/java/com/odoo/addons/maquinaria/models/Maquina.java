package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

public class Maquina extends OModel {
    public final static String AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_maquina";
    public static final String TAG = Maquina.class.getSimpleName();

    OColumn name = new OColumn("Name", OVarchar.class);
    OColumn no_serie = new OColumn("Serial", OVarchar.class);
    OColumn turno_estado = new OColumn("Turno", OSelection.class)
            .addSelection("open", "Abierto")
            .addSelection("close", "Cerrado")
            .setDefaultValue("close")
            .setLocalColumn();


    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

    public Maquina(Context context, OUser user)
    {
        super(context, "maquinaria.maquina", user);
    }
}
