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
public class SubCatgsAdapter extends RecyclerView.Adapter<SubCatgsAdapter.MyViewHolder> {

    private final Typeface type;
    private final SharedPref sharePref;
    private Context mContext;
    private List<ProductSubCatgsData.Datum> citiesList;
    private RecyclerView mRecyclerView;
    private ItemClickListener mClickListener;
    public int currentCheckedSubCatgID;

    public SubCatgsAdapter(Context mContext, List<ProductSubCatgsData.Datum> albumList) {
        this.mContext = mContext;
        this.citiesList = albumList;
        type = Typeface.createFromAsset(mContext.getAssets(), "fonts/SST-Arabic-Roman.ttf");
        sharePref = new SharedPref(mContext);
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
        ProductSubCatgsData.Datum cities = citiesList.get(position);
        holder.title.setText(cities.name.trim());
        holder.title.setTextColor(Color.parseColor("#5e5e5f"));
        holder.itemView.setId(cities.id);
        holder.setIsRecyclable(false);
        holder.cb.setChecked(cities.isChecked, false);

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
                if(position == 0)
                    setAllChecked(isChecked);
                else
                    setChecked(position);

                if (isChecked) {
                    holder.title.setTextColor(Color.parseColor("#5E5E5F"));
                } else {
                    holder.title.setTextColor(Color.parseColor("#B3B5B8"));
                }
            }
        });
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int id);
    }

    private void setChecked(int pos){
        for (ProductSubCatgsData.Datum td: citiesList) {
            td.isChecked = false;
        }
        citiesList.get(pos).isChecked = true;
        notifyDataSetChanged();

        currentCheckedSubCatgID = citiesList.get(pos).id;
    }

    private void setAllChecked(boolean checked){
        for (ProductSubCatgsData.Datum td: citiesList) {
            td.isChecked = checked;
        }
        currentCheckedSubCatgID = 0;

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public SmoothCheckBox cb;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_name);
            cb = (SmoothCheckBox) view.findViewById(R.id.item_cb);
        }
    }
}
