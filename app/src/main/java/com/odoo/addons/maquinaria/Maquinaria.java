package com.odoo.addons.maquinaria;

import android.content.Context;

import com.odoo.R;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.drawer.ODrawerItem;

import java.util.ArrayList;
import java.util.List;

public class Maquinaria extends BaseFragment {
    public static final String KEY = Maquinaria.class.getSimpleName();
    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> items = new ArrayList<>();
        items.add(new ODrawerItem(KEY).setTitle("Turnos").setIcon(R.drawable.ic_baseline_history_toggle_off_24));
        return items;
    }

    @Override
    public <T> Class<T> database() {
        return null;
    }
}
