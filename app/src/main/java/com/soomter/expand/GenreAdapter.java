package com.soomter.expand;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import com.soomter.Artist;
import com.soomter.Genre;
import com.soomter.ProductCatgsTreeData;
import com.soomter.R;

public class GenreAdapter extends ExpandableRecyclerViewAdapter<MainViewHolder, ChildViewHolder> {

    private final Typeface type;
    private final Context context;

    public GenreAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
        this.context  =context;
    }

    @Override
    public MainViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_main, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_sub, parent, false);
        return new ChildViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {

        final Artist artist = ((Genre) group).getItems().get(childIndex);
        holder.setChildName(artist.getName());
        //holder.setChildImage(artist.getImage());
    }

    @Override
    public void onBindGroupViewHolder(MainViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {

       holder.setGenre(group);
    }
}
