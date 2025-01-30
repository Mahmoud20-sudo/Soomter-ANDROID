package com.soomter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.List;
import java.util.Locale;

import com.soomter.utils.SpacesItemDecoration;

@SuppressLint("ValidFragment")
public class CatgsFragmentV2 extends Fragment implements GridRvAdapter.ItemClickListener{

    private RecyclerView mGridView;
    private Activity activity;
    private List<?> list;
    private int type;
    private GridRvAdapter adapter;

    public CatgsFragmentV2(Activity activity, List<?> list, int type) {
        this.activity = activity;
        this.list = list;
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.app_grid_rv, container, false);
        mGridView = (RecyclerView) view.findViewById(R.id.grid_rv);
        mGridView.setNestedScrollingEnabled(true);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mGridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null) {
            mGridView.setLayoutManager(new GridLayoutManager(activity, 3));
            adapter = new GridRvAdapter(getContext(), list, type);
            adapter.setClickListener(this);
            mGridView.setAdapter(adapter);
        }
    }

    public void onGridItemClick(GridView g, View v, int position, long id) {
       /* Toast.makeText(
                activity,
                "Position Clicked: - " + position + " & " + "Text is: - "
                        + gridItems[position].title, Toast.LENGTH_LONG).show();
       */
        Log.e("TAG", "POSITION CLICKED " + position);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (type == 1)
            /*getFragmentManager().beginTransaction().addToBackStack(null).
                    replace(R.id.frag_container,
                            BusinessCatgsFragment.newInstance(((PortalCatgData.Datum) list.get(position)).id,
                                    Locale.getDefault().getLanguage().toString().equals("ar") ?
                                            ((PortalCatgData.Datum) list.get(position)).name :
                                            ((PortalCatgData.Datum) list.get(position)).nameEN)).commit();*/
            getFragmentManager()
                    .beginTransaction().replace(R.id.frag_container,
                    //categID, 0, list.get(position).id) , mParam2
                    CompaniesFragment.newInstance(((PortalCatgData.Datum) list.get(position)).id
                            , 0, 0, Locale.getDefault().getLanguage().toString().equals("ar") ?
                            ((PortalCatgData.Datum) list.get(position)).name :
                            ((PortalCatgData.Datum) list.get(position)).nameEN , "")).addToBackStack(null).commit();

    }
}