package com.odoo.addons.maquinaria.models;

import android.content.Context;

import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

public class Destino extends OModel {
    public static final String TAG = Destino.class.getSimpleName();

    OColumn name = new OColumn("Name", OVarchar.class);

    public Destino(Context context,  OUser user) {
        super(context, "maquinaria.destino", user);
    }
}
