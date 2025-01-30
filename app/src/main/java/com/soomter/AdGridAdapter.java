package com.soomter;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

public class AdGridAdapter extends BaseAdapter {

    private final Typeface typeRoman;
    Context context;

    public class ViewHolder {
        public TextView textDetails;
        public ImageView adImg  ;
        public ImageView playIcon ;
        public ImageView favIcon ;

    }

    private List<AdsData.ImagesAds> items;
    private LayoutInflater mInflater;

    public AdGridAdapter(Context context, List<AdsData.ImagesAds> items) {

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;
        typeRoman = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Roman.ttf");
    }

    public List<AdsData.ImagesAds> getItems() {
        return items;
    }

    public void setItems(List<AdsData.ImagesAds> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public AdsData.ImagesAds getItem(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            return items.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            //return items.get(position).imgsAds.;
            return 0;
        }
        return 0;
    }

    public void setItemsList(List<AdsData.ImagesAds> locations) {
        this.items = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {

            view = mInflater.inflate(R.layout.ad_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textDetails = view.findViewById(R.id.ad_desc);
            viewHolder.textDetails.setTypeface(typeRoman);
            viewHolder.adImg = view.findViewById(R.id.ad_img);
            viewHolder.playIcon = view.findViewById(R.id.play_icon);
            viewHolder.favIcon = view.findViewById(R.id.special_ads_fav_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final AdsData.ImagesAds obj = items.get(position);

        if (obj.Type == 3) {
            viewHolder.playIcon.setVisibility(View.VISIBLE);
            viewHolder.playIcon.bringToFront();

            viewHolder.playIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
                    VideoDialog editNameDialogFragment = VideoDialog.newInstance(obj.VedioUrl);

                    editNameDialogFragment.show(fm, "fragment_edit_name");
                }
            });
        }

        viewHolder.textDetails.setText(obj.Title);

        /*Picasso.get().load(obj.Image)
                .into(viewHolder.adImg);*/

        Glide.with(context).load(obj.Image)
                .into(viewHolder.adImg);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                AdsDetailsFragment fragOne = AdsDetailsFragment.newInstance(obj.id, obj.Type, obj.Title);
                ft.replace(R.id.ads_fragment_container, fragOne);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        viewHolder.favIcon.setImageResource(obj.IsIdentified > 0 ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);

        /*PortalCatgData.Datum gridItems = items.get(position);
        setCatImage(position, viewHolder, Locale.getDefault().getLanguage().toString().equalsIgnoreCase("ar") ?
                gridItems.name : gridItems.nameEN);
        //Picasso.get().load(items.get(position).businessIcon64).into(viewHolder.imageView);
        Glide.with(context)
                .load(items.get(position).businessIcon64)
                .into(viewHolder.imageView);
*/
        return view;
    }
}
