package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODate;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.support.OUser;

public class CargaCombustible extends OModel {
    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_combustible_carga";
    public static final String TAG = CargaCombustible.class.getSimpleName();

    OColumn maquina_id = new OColumn("Maquina", Maquina.class, OColumn.RelationType.ManyToOne);
    OColumn cantidad = new OColumn("Cantidad", OFloat.class);
    OColumn fecha_carga = new OColumn("Fecha carga", ODate.class);
    OColumn fecha_hora = new OColumn("Hora", ODateTime.class);


    @Override
    public Uri uri(){ return buildURI(AUTHORITY);}
    public CargaCombustible(Context context, OUser user) {
        super(context, "maquinaria.combustible.carga", user);
    }
}
