package com.soomter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ProductAddressDialog extends DialogFragment {

    public ClickEventListener clickEventListener;
    public ProgressBar progressBar;
    public Button send;
    boolean isValid = true;
    private ProductAddreseData.Result resultObj;
    private int id;

    public static ProductAddressDialog newInstance(String jsonString) {
        ProductAddressDialog frag = new ProductAddressDialog();
        Bundle args = new Bundle();
        args.putString("jsonString", jsonString);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString("jsonString");
            Gson gson = new Gson();
            Type type = new TypeToken<ProductAddreseData.Result>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.address_dialog, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        progressBar = view.findViewById(R.id.loadmore_progress);
        TextView title = view.findViewById(R.id.dialog_title);
        TextView name_tv = view.findViewById(R.id.name_tv);
        TextView address_tv = view.findViewById(R.id.address_tv);
        TextView mob_tv = view.findViewById(R.id.num_tv);

        final EditText name_ed = view.findViewById(R.id.name_ed);
        final EditText address_ed = view.findViewById(R.id.address_ed);
        final EditText mobile_ed = view.findViewById(R.id.num_ed);

        if(resultObj != null){
            name_ed.setText(resultObj.ReceiverName);
            address_ed.setText(resultObj.Address);
            mobile_ed.setText(resultObj.ContactTel);
            id = resultObj.Id;
        }

        send = view.findViewById(R.id.button_send);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/SST-Arabic-Light.ttf");
        title.setTypeface(type);
        name_tv.setTypeface(type);
        address_tv.setTypeface(type);
        mob_tv.setTypeface(type);
        name_ed.setTypeface(type);
        address_ed.setTypeface(type);
        mobile_ed.setTypeface(type);
        send.setTypeface(type);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = true;
                if (name_ed.getText().length() == 0) {
                    name_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (address_ed.getText().length() == 0) {
                    address_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (mobile_ed.getText().length() == 0) {
                    mobile_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (isValid) {
                    progressBar.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                    clickEventListener.click(id, name_ed.getText().toString(), address_ed.getText().toString(),
                            mobile_ed.getText().toString(), ProductAddressDialog.this);
                }
            }
        });

        return view;
    }

    public interface ClickEventListener {
        void click(int id, String name, String address, String mobile, ProductAddressDialog dialog);
    }
}