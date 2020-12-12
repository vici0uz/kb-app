package com.odoo.addons.maquinaria;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.drawer.ODrawerItem;

import java.util.ArrayList;
import java.util.List;

public class PicarMaquina extends BaseFragment {
    public static final String KEY = PicarMaquina.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
//        setHasSyncStatusObserver(KEY, this,db());
        return inflater.inflate(R.layout.common_listview, container,false);
    }

    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        return null;
    }

    @Override
    public Class<Maquina> database() {
        return Maquina.class;
    }


}
