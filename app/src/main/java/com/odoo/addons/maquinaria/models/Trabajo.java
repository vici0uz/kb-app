package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.support.OUser;

public class Trabajo extends OModel {

    public final static String  AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_trabajo_linea";
    OColumn maquina_id = new OColumn("Maquina", Maquina.class, OColumn.RelationType.ManyToOne);

    public Trabajo(Context context, OUser user) {
        super(context, "maquinaria.trabajo.linea", user);

    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
