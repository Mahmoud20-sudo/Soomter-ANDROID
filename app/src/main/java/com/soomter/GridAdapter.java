package com.soomter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Locale;

public class GridAdapter extends BaseAdapter {

    Context context;
    private int type;

    public class ViewHolder {
        public ImageView imageView;
        public TextView textTitle;
    }

    private List<?> items;
    private LayoutInflater mInflater;

    public GridAdapter(Context context, List<?> items , int type) {

        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;
        this.type = type;
    }

    public List<?> getItems() {
        return items;
    }

    public void setItems(List<PortalCatgData.Datum> items) {
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
    public Object getItem(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            return type == 1 ? ((PortalCatgData.Datum)items.get(position)) :
                    ((ProductsCatgData.Datum)items.get(position));
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (items != null && position >= 0 && position < getCount()) {
            return type == 1 ? ((PortalCatgData.Datum)items.get(position)).id:
                    ((ProductsCatgData.Datum)items.get(position)).id;
        }
        return 0;
    }

    public void setItemsList(List<PortalCatgData.Datum> locations) {
        this.items = locations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {

            view = mInflater.inflate(R.layout.cat_orders_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view
                    .findViewById(R.id.portalcatg_img);
            viewHolder.textTitle = (TextView) view
                    .findViewById(R.id.portalcatg_txt);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if(type == 1) {
            PortalCatgData.Datum gridItem = (PortalCatgData.Datum)items.get(position);
            setCatImage(position, viewHolder, Locale.getDefault().getLanguage().toString().equalsIgnoreCase("ar") ?
                    gridItem.name : gridItem.nameEN);
            //Picasso.get().load(items.get(position).businessIcon64).into(viewHolder.imageView);
            Glide.with(context)
                    .load(gridItem.businessIcon64)
                    .into(viewHolder.imageView);
        }
        else{
            ProductsCatgData.Datum gridItem = (ProductsCatgData.Datum)items.get(position);
            setCatImage(position, viewHolder, gridItem.Name);
            //Picasso.get().load(items.get(position).businessIcon64).into(viewHolder.imageView);
            Glide.with(context)
                    .load(gridItem.Image)
                    .into(viewHolder.imageView);
        }
        return view;
    }

    private void setCatImage(int pos, ViewHolder viewHolder, String catTitle) {
        viewHolder.textTitle.setText(catTitle);
    }
}
