package com.soomter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdsTypesAdapter extends RecyclerView.Adapter<AdsTypesAdapter.ViewHolder> {

    private final Context context;
    private List<AdsData.ImagesAds> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    AdsTypesAdapter(Context context, List<AdsData.ImagesAds> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ad_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final AdsData.ImagesAds obj = mData.get(position);

        if (obj.Type == 3) {
            viewHolder.playIcon.setVisibility(View.VISIBLE);
            viewHolder.playIcon.bringToFront();
        }

        viewHolder.textDetails.setText(obj.Title);

     /*   Picasso.get().load(obj.Image)
                .into(viewHolder.adImg);*/

        Glide.with(context).load(obj.Image)
                .into(viewHolder.adImg);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) viewHolder.itemView.getContext())
                        .getSupportFragmentManager().beginTransaction();
                AdsDetailsFragment fragOne = AdsDetailsFragment.newInstance(obj.id, obj.Type, obj.Title);
                ft.add(R.id.ads_fragment_container, fragOne);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    AdsData.ImagesAds getItem(int id) {
        return mData.get(id);
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
        public TextView textDetails;
        public ImageView adImg;
        public ImageView playIcon;
        public ImageView favIcon;


        ViewHolder(View view) {
            super(view);
            textDetails = view.findViewById(R.id.ad_desc);
            //textDetails.setTypeface(typeRoman);
            adImg = view.findViewById(R.id.ad_img);
            playIcon = view.findViewById(R.id.play_icon);
            favIcon = view.findViewById(R.id.special_ads_fav_icon);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
