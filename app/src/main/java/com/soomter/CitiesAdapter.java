package com.soomter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.MyViewHolder> {

    private final Typeface type;
    private final SharedPref sharePref;
    private final int catgID;
    private Context mContext;
    private List<CitiesData.Datum> citiesList;
    private RecyclerView mRecyclerView;

    private ItemClickListener mClickListener;

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public SmoothCheckBox cb;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_name);
            cb = (SmoothCheckBox) view.findViewById(R.id.item_cb);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    public CitiesAdapter(Context mContext, List<CitiesData.Datum> albumList , int catgID) {
        this.mContext = mContext;
        this.citiesList = albumList;
        type = Typeface.createFromAsset(mContext.getAssets(), "fonts/SST-Arabic-Roman.ttf");
        sharePref = new SharedPref(mContext);
        this.catgID = catgID;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CitiesData.Datum cities = citiesList.get(position);
        holder.title.setText(cities.name);
        holder.itemView.setId(cities.id);
        holder.setIsRecyclable(false);
        holder.cb.setChecked(cities.isChecked , false);

        if (position == 0) {
            holder.title.setTextColor(Color.parseColor("#5E5E5F"));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, 30);
            holder.itemView.setLayoutParams(lp);

        }

        holder.title.setTypeface(type);
        holder.cb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    holder.title.setTextColor(Color.parseColor("#5E5E5F"));
                } else {
                    holder.title.setTextColor(Color.parseColor("#B3B5B8"));
                }
            }
        });

       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*if (position == 0) {
                    for (int i = 0; i < citiesList.size(); i++) {
                        citiesList.get(i).isChecked = !(sharePref.getAllCitiesStatus());
                    }
                    notifyDataSetChanged();
                    sharePref.saveAllCitiesStatus(!sharePref.getAllCitiesStatus());
                    saveCities();
                    return;
                }

                if (holder.cb.isChecked()) {
                    holder.title.setTextColor(Color.parseColor("#B3B5B8"));
                    holder.cb.setChecked(false, true);
                    citiesList.get(position).isChecked = false;
                    saveCities();
                } else {
                    holder.title.setTextColor(Color.parseColor("#5E5E5F"));
                    holder.cb.setChecked(true, true);
                    citiesList.get(position).isChecked = true;
                    saveCities();
                }*//*
            }
        });*/

    }

    private void saveCities() {
        CitiesData.Datum obj = citiesList.get(0);
        citiesList.remove(0);
        sharePref.saveCities(citiesList);
        citiesList.add(0, obj);
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }
}
