package com.odoo.addons.maquinaria.models;

import android.content.Context;

import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.support.OUser;

public class Combustible extends OModel {

    OColumn cantidad = new OColumn("Cantidad", OFloat.class);

    public Combustible(Context context,  OUser user) {
        super(context, "maquinaria.combustible.carga", user);
    }
}
