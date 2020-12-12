package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

public class Destino extends OModel {
    public static final String TAG = Destino.class.getSimpleName();
    public final static String  AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_destino";

    OColumn name = new OColumn("Name", OVarchar.class).setRequired();

    public Destino(Context context,  OUser user) {
        super(context, "maquinaria.destino", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

    @Override
    public boolean allowCreateRecordOnServer() {
        return true;
    }



}
