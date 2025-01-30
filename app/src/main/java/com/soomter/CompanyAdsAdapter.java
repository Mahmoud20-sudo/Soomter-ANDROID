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

public class CompanyAdsAdapter extends RecyclerView.Adapter<CompanyAdsAdapter.ViewHolder> {

    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<CompanyProfileData.ListAds> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    CompanyAdsAdapter(Context context, List<CompanyProfileData.ListAds> items) {
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
        View view = mInflater.inflate(R.layout.ad_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CompanyProfileData.ListAds obj = items.get(position);

        /*Glide.with(context).load(obj.Image).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    holder.layout.setBackground(resource);
                }
            }
        });*/

        holder.adImg.setVisibility(View.VISIBLE);
        Glide.with(context).load("https://soomter.com/AttachmentFiles/" + obj.ImageId + ".png").into(holder.adImg);

        if (obj.IsVedioAds) {
            holder.playIcon.setVisibility(View.VISIBLE);
            holder.playIcon.bringToFront();
        }

        holder.favIcon.setImageResource(obj.IsFavorite > 0 ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);
        holder.textTitle.setText(obj.Title);
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
        public TextView textTitle;
        public ImageView adImg, favIcon, playIcon;

        ViewHolder(View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.ad_desc);
            adImg = itemView.findViewById(R.id.ad_img);
            playIcon = itemView.findViewById(R.id.play_icon);
            favIcon = itemView.findViewById(R.id.special_ads_fav_icon);
            itemView.setOnClickListener(this);
            textTitle.setTypeface(typeRoman);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}