package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ViewHolder> {

    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<ProductDetailData.ListSelectedProducts> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    SimilarProductsAdapter(Context context, List<ProductDetailData.ListSelectedProducts> items) {
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
        return 5;
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
        View view = mInflater.inflate(R.layout.smilar_product_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(items == null)
            return;

        ProductDetailData.ListSelectedProducts obj = items.get(position);

        Glide.with(context)
                .load(obj.Image)
                .into(holder.imageView);

        holder.textTitle.setText(obj.Title);
        holder.textTitle.setTypeface(type);
        holder.productPrice.setText(obj.Price + " " + context.getString(R.string.rial));
        holder.productPrice.setTypeface(typeMedium);
        holder.favIcon.setImageResource(obj.IsFavorite > 0 ? R.mipmap.ad_details_star : R.mipmap.fav_star);

        switch (Integer.valueOf(obj.Rating.intValue())) {
            case 1:
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                break;
            case 2:
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                break;
            case 3:
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                break;
            case 4:
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                holder.fourthStar.setImageResource(R.mipmap.gold_star);
                break;
            case 5:
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                holder.fourthStar.setImageResource(R.mipmap.gold_star);
                holder.fifthStar.setImageResource(R.mipmap.gold_star);
                break;
        }

        holder.ratingNumTv.setText(obj.Rating + "");

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
        public ImageView imageView, favIcon , firstStar , secondStar , thirdStar , fourthStar , fifthStar;
        public TextView textTitle, productPrice, productLabel ,ratingNumTv;

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
            ratingNumTv = (TextView) itemView
                    .findViewById(R.id.rating_num);
            ratingNumTv.setTypeface(type);

            firstStar = (ImageView) itemView
                    .findViewById(R.id.first_start);
            secondStar = (ImageView) itemView
                    .findViewById(R.id.second_start);
            thirdStar = (ImageView) itemView
                    .findViewById(R.id.third_start);
            fourthStar = (ImageView) itemView
                    .findViewById(R.id.fourth_start);
            fifthStar = (ImageView) itemView
                    .findViewById(R.id.fifth_start);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}