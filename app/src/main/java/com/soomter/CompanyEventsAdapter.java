package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class CompanyEventsAdapter extends RecyclerView.Adapter<CompanyEventsAdapter.MyViewHolder> {

    private  Typeface type;
    private  SharedPref sharePref;
    private Context mContext;
    private List<CompanyProfileData.ListEvents> companiesList;
    private RecyclerView mRecyclerView;
    private ItemClickListener mClickListener;

    public CompanyEventsAdapter(Context mContext, List<CompanyProfileData.ListEvents> albumList) {
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
                .inflate(R.layout.event_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CompanyProfileData.ListEvents result = companiesList.get(position);

        holder.eventNme.setText(result.Title);
       // holder.eventDate.setText(result.Period);

        holder.eventLoc.setText(mContext.getString(R.string.bank_loc) + " - " + result.CityName);
        holder.favIcon.setImageResource(result.IsStar ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);

        holder.eventNme.setTypeface(type);
        holder.eventDate.setTypeface(type);
        holder.eventLoc.setTypeface(type);

        Glide.with(mContext).load("https://soomter.com/AttachmentFiles/" + result.ImageId + ".png").into(holder.eventImg);

        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) context)
                        .getSupportFragmentManager().beginTransaction();
                EventsDetailsFragment fragOne = EventsDetailsFragment.newInstance(result.Id, result.Image);
                ft.add(R.id.event_fragment_container, fragOne);
                ft.hide(eventsMainFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });*/
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

    public class MyViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        private TextView eventNme;
        private TextView eventDate;
        private TextView eventLoc; // displays "year | language"
        private ImageView favIcon , eventImg;


        public MyViewHolder(View view) {
            super(view);
            eventImg = (ImageView) view.findViewById(R.id.event_img);
            favIcon = (ImageView) view.findViewById(R.id.event_fav_icon);
            eventNme = (TextView) view.findViewById(R.id.event_name);
            eventDate = (TextView) view.findViewById(R.id.event_date);
            eventLoc = (TextView) view.findViewById(R.id.event_location);

            eventNme.setTypeface(type);
            eventDate.setTypeface(type);
            eventLoc.setTypeface(type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}
