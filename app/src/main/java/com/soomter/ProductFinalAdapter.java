package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
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

public class ProductFinalAdapter extends RecyclerView.Adapter<ProductFinalAdapter.ViewHolder> {

    private final SharedPref sharedPref;
    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<ProductCartData.Items> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private APIInterface apiInterface;

    // data is passed into the constructor
    ProductFinalAdapter(Context context, List<ProductCartData.Items> items) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.items = items;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Medium.ttf");
        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(context);
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
        View view = mInflater.inflate(R.layout.orders_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (items == null)
            return;

        final ProductCartData.Items obj = items.get(position);

        Glide.with(context).load("https://soomter.com/AttachmentFiles/" + obj.ImageId + ".png").into(holder.product_img);

        holder.product_name.setText(obj.Title);
        holder.quantity_value_txt.setText(obj.ItemQuantity + "");
        holder.price_tv.setText(obj.Price + "");

        if(position == items.size() - 1)
            holder.divider.setVisibility(View.GONE);
    }

    private void removeFromCart(int id, final int index) {

        Call<Void> call1 = apiInterface.removeFromCart(id, sharedPref.getUser().id);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(context, context.getString(R.string.success_delete), Toast.LENGTH_LONG).show();
                    items.remove(index);
                    notifyDataSetChanged();
                    return;
                }
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
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
        public ImageView product_img , divider;
        public TextView product_name, quantity_txt, price_tv, quantity_value_txt;

        ViewHolder(View itemView) {
            super(itemView);
            product_img = (ImageView) itemView
                    .findViewById(R.id.product_img);
            divider = (ImageView) itemView
                    .findViewById(R.id.divider);
            product_name = (TextView) itemView
                    .findViewById(R.id.product_name);
            quantity_txt = (TextView) itemView
                    .findViewById(R.id.quantity_txt);
            quantity_value_txt = (TextView) itemView
                    .findViewById(R.id.quantity_value_txt);
            price_tv = (TextView) itemView
                    .findViewById(R.id.price_tv);

            product_name.setTypeface(type);

            quantity_txt.setTypeface(typeRoman);
            price_tv.setTypeface(typeRoman);
            quantity_value_txt.setTypeface(typeRoman);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}