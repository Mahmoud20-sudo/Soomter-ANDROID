package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CompanyProductsAdapter extends RecyclerView.Adapter<CompanyProductsAdapter.ViewHolder> {

    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<CompanyProfileData.ListProducts> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    CompanyProductsAdapter(Context context, List<CompanyProfileData.ListProducts> items) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Medium.ttf");
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    /*@Override
    public long getItemId(int position) {
        if (items != null && position >= 0 && position < getItemCount()) {
            items[position];
        }
        return 0;
    }*/

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.best_seller_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CompanyProfileData.ListProducts obj = items.get(position);

       /* Picasso.get().load(obj.Image)
                .into(holder.imageView);*/

        Glide.with(context)
                .load(obj.Image)
                .into(holder.imageView);

        holder.textTitle.setText(obj.Title);
        holder.textTitle.setTypeface(type);
        holder.productPrice.setText(obj.Price + " " + context.getString(R.string.rial));
        holder.productPrice.setTypeface(typeMedium);
        holder.favIcon.setImageResource(obj.IsFavorite > 0 ? R.mipmap.ad_details_star : R.mipmap.fav_star);

        if (obj.Discount != 0) {
            holder.productLabel.setVisibility(View.VISIBLE);
            holder.productLabel.setText(context.getString(R.string.discount) + " " + obj.Discount);
            holder.productLabel.setTypeface(typeRoman);
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

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imageView, favIcon;
        public TextView textTitle, productPrice, productLabel;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView
                    .findViewById(R.id.product_img);
            favIcon = (ImageView) itemView
                    .findViewById(R.id.fav_img);
            textTitle = (TextView) itemView
                    .findViewById(R.id.product_name);
            textTitle.setTypeface(type);
            productPrice = (TextView) itemView
                    .findViewById(R.id.product_price);
            productPrice.setTypeface(typeMedium);
            productLabel = (TextView) itemView
                    .findViewById(R.id.product_label);
            productLabel.setTypeface(typeRoman);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}