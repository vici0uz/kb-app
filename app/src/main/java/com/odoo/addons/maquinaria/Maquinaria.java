package com.odoo.addons.maquinaria;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.odoo.R;
import com.odoo.addons.maquinaria.models.Trabajo;
//import com.odoo.base.addons.res.ResPartner;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.IOnSearchViewChangeListener;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.OControls;
import com.odoo.core.utils.OCursorUtils;

import java.util.ArrayList;
import java.util.List;

public class Maquinaria extends BaseFragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener, OCursorListAdapter.OnViewBindListener, AdapterView.OnItemClickListener, IOnSearchViewChangeListener, ISyncStatusObserverListener {
    public static final String KEY = Maquinaria.class.getSimpleName();
    private View mView;
    private OCursorListAdapter mAdapter = null;
    private boolean syncRequested = false;

    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> items = new ArrayList<>();
        items.add(new ODrawerItem(KEY).setTitle("Turnos").setIcon(R.drawable.ic_baseline_history_toggle_off_24).setInstance(new Maquinaria()));
        return items;
    }

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
        ListView mListaTurnos = (ListView) view.findViewById(R.id.listview);
        mAdapter = new OCursorListAdapter(getActivity(), null, R.layout.turno_row_item);
        mAdapter.setOnViewBindListener(this);
        mListaTurnos.setAdapter(mAdapter);
        mListaTurnos.setOnItemClickListener(this);
        setHasFloatingButton(view,R.id.fabButton, mListaTurnos, this);
        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
//        Bitmap img;
//        if (row.getString("image_small").equals("false")) {
//            img = BitmapUtils.getAlphabetImage(getActivity(), row.getString("name"));
//        } else {
//            img = BitmapUtils.getBitmapImage(getActivity(), row.getString("image_small"));
//        }
//        OControls.setImage(view, R.id.image_small, img);
        OControls.setText(view, R.id.maquina_id, row.getString("maquina_id"));
//        OControls.setText(view, R.id.company_name, (row.getString("company_name").equals("false"))
//                ? "" : row.getString("company_name"));
//        OControls.setText(view, R.id.email, (row.getString("email").equals("false") ? " "
//                : row.getString("email")));
    }

    @Override
    public Class<Trabajo> database() {
        return Trabajo.class;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabButton:
                loadActivity(null);
                break;
        }
    }

    private void loadActivity(ODataRow row) {
        Bundle data = new Bundle();
        if (row != null) {
            data = row.getPrimaryBundleData();
        }
//        data.putString(CustomerDetails.KEY_PARTNER_TYPE, mType.toString());
        IntentUtils.startActivity(getActivity(), TurnoDetails.class, data);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle data) {
        String where = "";
        List<String> args = new ArrayList<>();
        String selection = (args.size() > 0) ? where : null;
        String[] selectionArgs = (args.size() > 0) ? args.toArray(new String[args.size()]) : null;

        return new CursorLoader(getActivity(), db().uri(), null, selection, selectionArgs, "maquina_id" );
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
                    setHasSwipeRefreshView(mView, R.id.swipe_container, Maquinaria.this);
                }
            }, 500);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    OControls.setGone(mView, R.id.loadingProgress);
                    OControls.setGone(mView, R.id.swipe_container);
                    OControls.setVisible(mView, R.id.data_list_no_item);
                    setHasSwipeRefreshView(mView, R.id.data_list_no_item, Maquinaria.this);
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
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        if (inNetwork()) {
            parent().sync().requestSync(Trabajo.AUTHORITY);
            setSwipeRefreshing(true);
        } else {
            hideRefreshingProgress();
            Toast.makeText(getActivity(), _s(R.string.toast_network_required), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        ODataRow row = OCursorUtils.toDatarow((Cursor) mAdapter.getItem(position));
        loadActivity(row);
    }

    @Override
    public boolean onSearchViewTextChange(String newFilter) {
        return false;
    }

    @Override
    public void onSearchViewClose() {

    }

    @Override
    public void onStatusChange(Boolean refreshing) {
        getLoaderManager().restartLoader(0, null, this);

    }
}
