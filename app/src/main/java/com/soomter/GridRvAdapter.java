package com.soomter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

public class GridRvAdapter extends RecyclerView.Adapter<GridRvAdapter.ViewHolder> {

    private List<?> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    private int type;

    // data is passed into the constructor
    GridRvAdapter(Context context, List<?> items , int type) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        this.type = type;
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (items != null && position >= 0 && position < getItemCount()) {
            return type == 1 ? ((PortalCatgData.Datum)items.get(position)).id:
                    ((ProductsCatgData.Datum)items.get(position)).id;
        }
        return 0;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cat_orders_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(type == 1) {
            PortalCatgData.Datum gridItem = (PortalCatgData.Datum)items.get(position);
            holder.textTitle.setText(Locale.getDefault().getLanguage().toString().equalsIgnoreCase("ar") ?
                    gridItem.name : gridItem.nameEN);
            //Picasso.get().load(items.get(position).businessIcon64).into(viewHolder.imageView);
            Glide.with(context)
                    .load(gridItem.businessIcon64)
                    .into(holder.imageView);
        }
        else{
            ProductsCatgData.Datum gridItem = (ProductsCatgData.Datum)items.get(position);
            holder.textTitle.setText(gridItem.Name);
            Glide.with(context)
                    .load(gridItem.Image)
                    .into(holder.imageView);
        }
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;
        public TextView textTitle;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView
                    .findViewById(R.id.portalcatg_img);
            textTitle = (TextView) itemView
                    .findViewById(R.id.portalcatg_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}