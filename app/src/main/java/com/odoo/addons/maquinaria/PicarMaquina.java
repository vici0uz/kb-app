package com.odoo.addons.maquinaria;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Maquina;
import com.odoo.addons.maquinaria.wizard.AsistenteCierre;
import com.odoo.addons.maquinaria.wizard.AsistenteNuevo;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.OControls;
import com.odoo.core.utils.OCursorUtils;

import java.util.ArrayList;
import java.util.List;

public class PicarMaquina extends BaseFragment implements OCursorListAdapter.OnViewBindListener,  SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, ISyncStatusObserverListener {
    public static final String KEY = PicarMaquina.class.getSimpleName();
    private View mView;
    private OCursorListAdapter mAdapter = null;
    private boolean syncRequested = false;
    private boolean turno_abierto_xp = false;
    private OValues new_values;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        setHasOptionsMenu(true);
        setHasSyncStatusObserver(KEY, this,db());
        return inflater.inflate(R.layout.common_listview, container,false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mView.findViewById(R.id.fabButton).setVisibility(View.GONE);
        ListView mListaMaquinas = (ListView) view.findViewById(R.id.listview);
        mAdapter = new OCursorListAdapter(getActivity(), null, R.layout.maquina_row_item);
        mAdapter.setOnViewBindListener(this);
        mListaMaquinas.setAdapter(mAdapter);
        mListaMaquinas.setOnItemClickListener(this);
        mListaMaquinas.setOnItemLongClickListener(this);

        getLoaderManager().initLoader(0, null, this);
    }
    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        return null;
    }

    @Override
    public Class<Maquina> database() {
        return Maquina.class;
    }


    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
        OControls.setText(view, R.id.name, row.getString("name"));
//        OControls.setText(view, R.id.turno_estado, row.getString("turno_estado"));
        if(row.getString("turno_estado").equals("open")){
            view.findViewById(R.id.linear_root).setBackgroundColor(getResources().getColor(R.color.android_orange));
            view.findViewById(R.id.turno_abierto_indicator).setVisibility(View.VISIBLE);
            turno_abierto_xp = true;
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle data) {
        String where = "";
        List<String> args = new ArrayList<>();
        String selection = (args.size() > 0) ? where : null;
        String[] selectionArgs = (args.size() > 0) ? args.toArray(new String[args.size()]) : null;
        return new CursorLoader(getActivity(), db().uri(), null, selection, selectionArgs, "name" );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);

        if (data.getCount() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OControls.setGone(mView, R.id.loadingProgress);
                    OControls.setVisible(mView, R.id.swipe_container);
                    OControls.setGone(mView, R.id.data_list_no_item);
//                    OControls.setGone(mView, R.id.fabButton);
                    setHasSwipeRefreshView(mView, R.id.swipe_container, PicarMaquina.this);
                }
            }, 500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OControls.setGone(mView, R.id.loadingProgress);
                    OControls.setGone(mView, R.id.swipe_container);
                    OControls.setVisible(mView, R.id.data_list_no_item);
                    setHasSwipeRefreshView(mView, R.id.data_list_no_item, PicarMaquina.this);
                    OControls.setImage(mView, R.id.icon, R.drawable.ic_action_customers);
                    OControls.setText(mView, R.id.title, _s(R.string.label_no_customer_found));
                    OControls.setText(mView, R.id.subTitle, "");
                }
            }, 500);
            if (db().isEmptyTable() && !syncRequested) {
                syncRequested = true;
                onRefresh();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.i("ALAN DEBUG: ", "volvi perras");
//        for ODataRow in
    }

    @Override
    public void onRefresh() {
        if (inNetwork()) {
            parent().sync().requestSync(Maquina.AUTHORITY);
            setSwipeRefreshing(true);
        } else {
            hideRefreshingProgress();
            Toast.makeText(getActivity(), _s(R.string.toast_network_required), Toast.LENGTH_LONG)
                    .show();
        }
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    private void loadActivity(ODataRow row) {
        Bundle data = new Bundle();
        data = row.getPrimaryBundleData();

        row.getString("turno_estado");
        if (row.getString("turno_estado").equals("close") || row.getString("turno_estado") == "false"){
//            if(turno_abierto_xp != true)
                IntentUtils.startActivity(getActivity(), AsistenteNuevo.class, data);
//            else
//                Toast.makeText(getActivity(), "Primero debe cerrar los turnos que ya haya abierto", Toast.LENGTH_LONG).show();
        }else {
            IntentUtils.startActivity(getActivity(), AsistenteCierre.class, data);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ODataRow row = OCursorUtils.toDatarow((Cursor) mAdapter.getItem(position));
        loadActivity(row);
    }

    @Override
    public void onStatusChange(Boolean refreshing) {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

//        Toast.makeText(getActivity(), "Joder alan", Toast.LENGTH_SHORT).show();
//        float cantidad;


        final ODataRow row = OCursorUtils.toDatarow((Cursor) mAdapter.getItem(position));

        LayoutInflater linf = LayoutInflater.from(getActivity());
        final View inflator = linf.inflate(R.layout.dialog_cargar_combustible, null);
        final EditText entrada = (EditText) inflator.findViewById(R.id.ingreso_combustible);

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(inflator);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Toast.makeText(getActivity(), entrada.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        final AlertDialog dialogoCombu = builder.create();
        dialogoCombu.show();
//        builder.show();
        dialogoCombu.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Boolean cerrarDialogo = false;
                    if(isEmpty(entrada)){
                        entrada.setError("Ingrese una cantidad valida");
                    }
                    else{

                    }
                    if (cerrarDialogo)
                        dialogoCombu.dismiss();
            }
        });


        return true;
    }

    public boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
