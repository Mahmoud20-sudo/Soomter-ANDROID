package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class MostWatchedAdapter extends RecyclerView.Adapter<MostWatchedAdapter.MyViewHolder> {

    private  Typeface type;
    private  SharedPref sharePref;
    private Context mContext;
    private List<MostWatchedData.Datum> companiesList;
    private RecyclerView mRecyclerView;
    private ItemClickListener mClickListener;

    public MostWatchedAdapter(Context mContext, List<MostWatchedData.Datum> albumList) {
        this.mContext = mContext;
        this.companiesList = albumList;
        if(mContext == null)
            return;
        type = Typeface.createFromAsset(mContext.getAssets(), "fonts/SST-Arabic-Light.ttf");
        sharePref = new SharedPref(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.most_views_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MostWatchedData.Datum company = companiesList.get(position);
        holder.name.setText(company.CompanyName);
        holder.itemView.setId(company.Id);
        holder.setIsRecyclable(false);


        Glide.with(mContext).
                load("https://soomter.com/AttachmentFiles/" + company.LogoId + ".png").into(holder.compImg);

        holder.name.setTypeface(type);
        holder.viewsCount.setTypeface(type);
        holder.adsCount.setTypeface(type);
        holder.productCount.setTypeface(type);
        holder.eventsCount.setTypeface(type);

        holder.viewsCount.setText("" + company.ViewCounts);
        holder.adsCount.setText("" + company.CountADS);
        holder.productCount.setText("" + company.CountProduct);
        holder.eventsCount.setText("" + company.CountEvents);

        switch (Integer.valueOf(company.AverageRating.intValue())) {
            case 1 :
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                break;
            case 2 :
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                break;
            case 3 :
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                break;
            case 4 :
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                holder.fourthStar.setImageResource(R.mipmap.gold_star);
                break;
            case 5 :
                holder.firstStar.setImageResource(R.mipmap.gold_star);
                holder.secondStar.setImageResource(R.mipmap.gold_star);
                holder.thirdStar.setImageResource(R.mipmap.gold_star);
                holder.fourthStar.setImageResource(R.mipmap.gold_star);
                holder.fifthStar.setImageResource(R.mipmap.gold_star);
                break;
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

    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, viewsCount, productCount, adsCount, eventsCount;
        public ImageView compImg, firstStar, secondStar, thirdStar, fourthStar, fifthStar;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            viewsCount = (TextView) view.findViewById(R.id.views_num);
            productCount = (TextView) view.findViewById(R.id.prods_num);
            adsCount = (TextView) view.findViewById(R.id.ads_num);
            eventsCount = (TextView) view.findViewById(R.id.events_num);

            compImg = (ImageView) view.findViewById(R.id.comp_img);
            firstStar = (ImageView) view.findViewById(R.id.first_start);
            secondStar = (ImageView) view.findViewById(R.id.second_start);
            thirdStar = (ImageView) view.findViewById(R.id.third_start);
            fourthStar = (ImageView) view.findViewById(R.id.fourth_start);
            fifthStar = (ImageView) view.findViewById(R.id.fifth_start);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
