package com.soomter.expand;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.soomter.R;


public class ChildViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder {

    public TextView childTextView;
    private ImageView childImage;
    private Context context;

    public ChildViewHolder(View itemView) {
        super(itemView);
        childTextView = (TextView) itemView.findViewById(R.id.list_item_artist_name);
        Typeface type = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/SST-Arabic-Light.ttf");
        childTextView.setTypeface(type);
        this.context = itemView.getContext();
    }

    public void setChildName(String name) {
        childTextView.setText(name);
    }
}
