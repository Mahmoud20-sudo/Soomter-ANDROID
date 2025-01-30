package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PickedFacilitiesAdapter extends RecyclerView.Adapter<PickedFacilitiesAdapter.ViewHolder> {

    private final Context context;
    private List<CompanyProfileData.SimilarCompanies> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Typeface typeRoman, type;

    // data is passed into the constructor
    PickedFacilitiesAdapter(Context context, List<CompanyProfileData.SimilarCompanies> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Roman.ttf");
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.similar_company_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final CompanyProfileData.SimilarCompanies obj = mData.get(position);

        viewHolder.title.setText(obj.CompanyName);
        viewHolder.textDetails.setText(obj.Summary);



        /*Glide.with(context).load(obj.Image)
                .into(viewHolder.adImg);*/

        /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) viewHolder.itemView.getContext())
                        .getSupportFragmentManager().beginTransaction();
                AdsDetailsFragment fragOne = AdsDetailsFragment.newInstance(obj.id, obj.Type, obj.Title);
                ft.add(R.id.ads_fragment_container, fragOne);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // convenience method for getting data at click position
    CompanyProfileData.SimilarCompanies getItem(int id) {
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
        public TextView title, textDetails , textMore;
        public ImageView adImg;
        public ImageView favIcon;


        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.cp_name);
            adImg = view.findViewById(R.id.prof_img);
            favIcon = view.findViewById(R.id.special_fav_icon);
            textDetails = view.findViewById(R.id.cp_desc);
            textMore = view.findViewById(R.id.cp_more);

            textMore.setTypeface(type);
            title.setTypeface(typeRoman);
            textDetails.setTypeface(typeRoman);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
