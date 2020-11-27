package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBlob;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.ODate;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.support.OUser;

public class Trabajo extends OModel {

    public final static String  AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_trabajo_linea";
    OColumn maquina_id = new OColumn("Maquina", Maquina.class, OColumn.RelationType.ManyToOne);
    OColumn fecha_trabajo = new OColumn("Fecha", ODate.class);
    OColumn cerrado = new OColumn("Turno cerrado", OBoolean.class);

    OColumn odometro_inicial = new OColumn("Odometro inicial", OInteger.class);
    OColumn odometro_inicial_imagen = new OColumn("Imagen del Odometro", OBlob.class);

    OColumn odometro_final = new OColumn("Odometro final", OInteger.class);
    OColumn odometro_final_imagen = new OColumn("Odometro final imagen", OBlob.class);
    OColumn status = new OColumn("Estado", OSelection.class)
            .addSelection("abierto","Abierto")
            .addSelection("mitad", "Mitad")
            .addSelection("cerrado", "Cerrado");

    public Trabajo(Context context, OUser user) {
        super(context, "maquinaria.trabajo.linea", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
