package com.odoo.addons.maquinaria.models;

import android.content.Context;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODate;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.support.OUser;

public class Combustible extends OModel {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID+ ".addons.maquinaria.content.sync.maquinaria_combustible_carga";

    OColumn cantidad = new OColumn("Cantidad", OFloat.class);
    OColumn maquina_id = new OColumn("Maquina", Maquina.class, OColumn.RelationType.ManyToOne);
    OColumn fecha_carga = new OColumn("Fecha", ODate.class);
    OColumn fecha_hora = new OColumn("Fecha y hora", ODateTime.class);

    public Combustible(Context context,  OUser user) {
        super(context, "maquinaria.combustible.carga", user);
    }


}
