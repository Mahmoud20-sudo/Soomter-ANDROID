package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductPropsAdapter extends RecyclerView.Adapter<ProductPropsAdapter.ViewHolder> {

    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<ProductDetailData.ProductsProperties> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ProductPropsAdapter(Context context, List<ProductDetailData.ProductsProperties> items) {
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
        View view = mInflater.inflate(R.layout.product_prop_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textTitle.setText(items.get(position).Value);
        holder.productValue.setText(items.get(position).Text);
        if(position == items.size() - 1)
            holder.divider.setVisibility(View.INVISIBLE);
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
        public ImageView imageView, divider;
        public TextView textTitle, productValue;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView
                    .findViewById(R.id.divider);
            textTitle = (TextView) itemView
                    .findViewById(R.id.lbl);
            //textTitle.setTypeface(type);
            productValue = (TextView) itemView
                    .findViewById(R.id.lbl_value);
            //productPrice.setTypeface(typeMedium);
            divider = itemView.findViewById(R.id.divider);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}