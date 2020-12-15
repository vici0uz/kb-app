package com.odoo.addons.maquinaria.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.BuildConfig;
import com.odoo.base.addons.res.ResPartner;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.annotation.Odoo;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OBlob;
import com.odoo.core.orm.fields.types.OBoolean;
import com.odoo.core.orm.fields.types.ODate;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OSelection;
import com.odoo.core.orm.fields.types.OText;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

import java.util.ArrayList;
import java.util.List;

public class Trabajo extends OModel {
    public final static String  AUTHORITY = BuildConfig.APPLICATION_ID + ".addons.maquinaria.content.sync.maquinaria_trabajo_linea";

    OColumn maquina_id = new OColumn("Maquina", Maquina.class, OColumn.RelationType.ManyToOne);
    @Odoo.Functional(store=true, depends = {"maquina_id"}, method = "guardarNombreMaquina")
    OColumn maquina = new OColumn("Maquina", OVarchar.class).setLocalColumn().setSize(100);
    OColumn fecha_trabajo = new OColumn("Fecha", ODate.class);
    OColumn cerrado = new OColumn("Turno cerrado", OBoolean.class);

    OColumn odometro_inicial = new OColumn("Odometro inicial", OFloat.class);
    OColumn odometro_inicial_imagen = new OColumn("Imagen del Odometro", OBlob.class);

    OColumn odometro_final = new OColumn("Odometro final", OFloat.class);
    OColumn odometro_final_imagen = new OColumn("Odometro final imagen", OBlob.class);
    OColumn status = new OColumn("Estado", OSelection.class)
            .addSelection("abierto","Abierto")
            .addSelection("mitad", "Mitad")
            .addSelection("cerrado", "Cerrado");
    OColumn trabajo_destino = new OColumn("Lugar de trabajo", Destino.class, OColumn.RelationType.ManyToOne);

    OColumn operador = new OColumn("Operador", ResPartner.class, OColumn.RelationType.ManyToOne);

    /* CREAR */
    OColumn descripcion = new OColumn("Descripción del trabajo", OText.class);
    OColumn observacion = new OColumn("Observación del trabajo", OText.class).setSize(256);
    OColumn tiene_observacion = new OColumn("Tiene observación", OBoolean.class).setDefaultValue(false);

    OColumn combustible = new OColumn("Combustible", OFloat.class);

    public Trabajo(Context context, OUser user) {
        super(context, "maquinaria.trabajo.linea", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

    public String guardarNombreMaquina(OValues value){
        try {
            if(!value.getString("maquina_id").equals("false")){
                List<Object> maquina_id = (ArrayList<Object>) value.get("maquina_id");
                return maquina_id.get(1)+"";
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}
