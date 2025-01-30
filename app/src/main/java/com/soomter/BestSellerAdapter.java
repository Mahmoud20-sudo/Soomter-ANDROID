package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;

public class BestSellerAdapter extends RecyclerView.Adapter<BestSellerAdapter.ViewHolder> {

    private final SharedPref sharedPref;
    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<BestSellingData.Datum> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private APIInterface apiInterface;

    // data is passed into the constructor
    BestSellerAdapter(Context context, List<BestSellingData.Datum> items) {
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
        View view = mInflater.inflate(R.layout.best_seller_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (items == null)
            return;

        final BestSellingData.Datum obj = items.get(position);

        Glide.with(context)
                .load(obj.Image)
                .into(holder.imageView);

        holder.textTitle.setText(obj.Title);
        holder.textTitle.setTypeface(type);
        holder.productPrice.setText(obj.Price + " " + context.getString(R.string.rial));
        holder.productPrice.setTypeface(typeMedium);
        holder.favIcon.setImageResource(obj.IsFavorite > 0 ? R.mipmap.ad_details_star : R.mipmap.fav_star);

        if (obj.Discount != null) {
            holder.productLabel.setVisibility(View.VISIBLE);
            holder.productLabel.setText(context.getString(R.string.discount) + " " + obj.Discount);
            holder.productLabel.setTypeface(typeRoman);
        }

        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPref.getUser() == null) {
                    Toast.makeText(context, context.getString(R.string.need_login), Toast.LENGTH_LONG).show();
                    return;
                }
                favouriteReq(obj, holder, position);
            }
        });

        holder.addtocart_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPref.getUser() == null) {
                    Toast.makeText(context, context.getString(R.string.need_login), Toast.LENGTH_LONG).show();
                    return;
                }
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.addtocart_img.setVisibility(View.GONE);
                addToCart(obj,holder ,position);
            }
        });
    }

    private void favouriteReq(final BestSellingData.Datum obj, final ViewHolder holder, final int position) {//  String UserId, int ProductId, int CompanyId, int ProductFieldValueId
        final ProductFavRequest request = new ProductFavRequest("", obj.id, obj.CompanyId, obj.ProductFieldValueId);
        Call<ProductFavData> call1 = obj.IsFavorite > 0 ? apiInterface.removeFavProduct(request)
                : apiInterface.addFavProduct(request);
        call1.enqueue(new Callback<ProductFavData>() {
            @Override
            public void onResponse(Call<ProductFavData> call, retrofit2.Response<ProductFavData> response) {
                ProductFavData resp = response.body();
                if (resp.result == false) {
                    Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (obj.IsFavorite > 0) {
                    obj.IsFavorite = 0;
                } else {
                    obj.IsFavorite = Math.abs(new Random().nextInt());
                }
                items.set(position, obj);
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductFavData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void addToCart(BestSellingData.Datum obj , final ViewHolder holder, int quantity) {
        //  String UserId, int ProductId, int CompanyId, int ProductFieldValueId
        final ProductCartRequest request = new ProductCartRequest(sharedPref.getUser().id, obj.id,
                quantity, obj.ProductFieldValueId);
        Call<Void> call1 = apiInterface.addToCart(request);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                holder.progressBar.setVisibility(View.GONE);
                holder.addtocart_img.setVisibility(View.VISIBLE);
                if (response.message().equals("OK")) {
                    Toast.makeText(context, context.getString(R.string.added_success), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                /*if (obj.IsFavorite > 0) {
                    obj.IsFavorite = 0;
                    favIcon.setImageResource(R.mipmap.fav_star);
                } else {
                    obj.IsFavorite = Math.abs(new Random().nextInt());
                    favIcon.setImageResource(R.mipmap.ad_details_star);
                }
                if (productsFragmentAll != null)
                    productsFragmentAll.updateProductList(position, obj);
                else
                    productsFragmentMain.updateProductList(position, obj);*/
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                holder.progressBar.setVisibility(View.GONE);
                holder.addtocart_img.setVisibility(View.VISIBLE);
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
        public ImageView imageView, favIcon ,addtocart_img;
        public TextView textTitle, productPrice, productLabel;
        public ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView
                    .findViewById(R.id.product_img);
            favIcon = (ImageView) itemView
                    .findViewById(R.id.fav_img);
            progressBar = (ProgressBar) itemView
                    .findViewById(R.id.loadmore_progress);
            addtocart_img = (ImageView) itemView
                    .findViewById(R.id.addtocart_img);
            textTitle = (TextView) itemView
                    .findViewById(R.id.product_name);
            textTitle.setTypeface(type);
            productPrice = (TextView) itemView
                    .findViewById(R.id.product_price);
            productPrice.setTypeface(typeMedium);
            productLabel = (TextView) itemView
                    .findViewById(R.id.product_label);
            productLabel.setTypeface(typeRoman);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
}