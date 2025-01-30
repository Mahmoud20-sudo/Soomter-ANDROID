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

public class BestTradesAdapter extends RecyclerView.Adapter<BestTradesAdapter.ViewHolder> {

    Context context;
    private List<ProductsCatgData.Datum> items;
    private LayoutInflater mInflater;
    private BestTradesItemClickListener mClickListener;

    // data is passed into the constructor
    BestTradesAdapter(Context context, List<ProductsCatgData.Datum> items) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
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
            return items.get(position).id ;
        }
        return 0;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.best_trades_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ProductsCatgData.Datum gridItem = (ProductsCatgData.Datum) items.get(position);
        Glide.with(context)
                .load(gridItem.Image)
                .into(holder.imageView);
    }

    // allows clicks events to be caught
    void setClickListener(BestTradesItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface BestTradesItemClickListener {
        void onBestTradesItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView
                    .findViewById(R.id.trades_img);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onBestTradesItemClick(items.get(getAdapterPosition()).id);
        }
    }
}