/*
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
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

*/
/**
 * Created by Ravi Tamada on 18/05/16.
 *//*

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.MyViewHolder> {

    private Typeface type;
    private SharedPref sharePref;
    private Context mContext;
    private List<CompaniesData.Datum> companiesList;
    private RecyclerView mRecyclerView;
    private ItemClickListener mClickListener;
    private APIInterface apiInterface;

    public CompaniesAdapter(Context mContext, List<CompaniesData.Datum> albumList) {
        this.mContext = mContext;
        this.companiesList = albumList;
        if (mContext == null)
            return;
        type = Typeface.createFromAsset(mContext.getAssets(), "fonts/SST-Arabic-Light.ttf");
        sharePref = new SharedPref(mContext);
        apiInterface = APIClient.getClient().create(APIInterface.class);
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
        final CompaniesData.Datum company = companiesList.get(position);
        holder.name.setText(company.CompanyName);
        holder.itemView.setId(company.Id);
        holder.setIsRecyclable(false);

        holder.name.setTypeface(type);
        holder.viewsCount.setTypeface(type);
        holder.adsCount.setTypeface(type);
        holder.productCount.setTypeface(type);
        holder.eventsCount.setTypeface(type);

        holder.viewsCount.setText("" + company.ViewCounts);
        holder.adsCount.setText("" + company.CountADS);
        holder.productCount.setText("" + company.CountProduct);
        holder.eventsCount.setText("" + company.CountEvents);

        Glide.with(mContext).
                load("https://soomter.com/AttachmentFiles/" + company.LogoId + ".png").into(holder.compImg);

        if (company.IsFlowed > 0) {
            holder.follow.setVisibility(View.GONE);
            holder.catFollowTxt.setVisibility(View.GONE);
        }

        switch (company.AverageRating) {
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

        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followCompany(company.Id, holder);
            }
        });
    }

    private void followCompany(int comanyId, final MyViewHolder holder) {
        if (sharePref.getUser() == null) {
            Toast.makeText(mContext, mContext.getString(R.string.require_login), Toast.LENGTH_LONG).show();
            return;
        }

        Call<Void> call1 = apiInterface.followCompany(comanyId, sharePref.getUser().id);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(mContext, mContext.getString(R.string.success), Toast.LENGTH_LONG).show();
                    holder.follow.setVisibility(View.GONE);
                    holder.catFollowTxt.setVisibility(View.GONE);
                    return;
                }
                Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return companiesList.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, viewsCount, productCount, adsCount, eventsCount, catFollowTxt;
        public ImageView compImg, firstStar, secondStar, thirdStar, fourthStar, fifthStar, follow;

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

            follow = (ImageView) view.findViewById(R.id.follow_btn);
            catFollowTxt = (TextView) view.findViewById(R.id.cat_follow_txt);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
*/
