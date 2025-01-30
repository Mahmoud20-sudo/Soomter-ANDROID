package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.ViewHolder> {

    private final SharedPref sharedPref;
    Context context;
    Typeface type, typeRoman, typeMedium;
    private List<ProductAddreseData.Result> items;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private APIInterface apiInterface;
    private int csSelectedAddressPos = 0;

    // data is passed into the constructor
    AddressesAdapter(Context context, List<ProductAddreseData.Result> items) {
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
        View view = mInflater.inflate(R.layout.product_addresses_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (items == null)
            return;

        final ProductAddreseData.Result obj = items.get(position);

        holder.address_name.setText(obj.ReceiverName);
        holder.address_details.setText(obj.Address + " - " + obj.Region + " - " + obj.StreetNo);

        holder.mobile_value_tv.setText(obj.ContactTel);

        if(position == csSelectedAddressPos)
            holder.address_rb.setChecked(true);
        else
            holder.address_rb.setChecked(false);

        holder.address_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set object isChecked here
                csSelectedAddressPos = position;
                notifyDataSetChanged();
            }
        });

        holder.address_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(obj);
            }
        });

    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }


    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(ProductAddreseData.Result obj);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView address_edit_btn;
        public RadioButton address_rb;
        public TextView address_name, address_details, mobile_tv, mobile_value_tv;
        //address_name address_details mobile_tv mobile_value_tv address_rb address_edit_btn

        ViewHolder(View itemView) {
            super(itemView);
            address_edit_btn = (ImageView) itemView
                    .findViewById(R.id.address_edit_btn);
            address_rb = (RadioButton) itemView
                    .findViewById(R.id.address_rb);
            address_name = (TextView) itemView
                    .findViewById(R.id.address_name);
            address_details = (TextView) itemView
                    .findViewById(R.id.address_details);
            mobile_tv = (TextView) itemView
                    .findViewById(R.id.mobile_tv);
            mobile_value_tv = (TextView) itemView
                    .findViewById(R.id.mobile_value_tv);

            address_name.setTypeface(typeMedium);

            address_details.setTypeface(typeRoman);
            mobile_tv.setTypeface(typeRoman);
            mobile_value_tv.setTypeface(typeRoman);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(items.get(getAdapterPosition()));
        }
    }
}