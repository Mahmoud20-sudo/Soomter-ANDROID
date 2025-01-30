package com.soomter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import com.soomter.utils.SpacesItemDecoration;

public class RoundedDialog extends DialogFragment {

    public static RoundedDialog newInstance(int title) {
        RoundedDialog frag = new RoundedDialog();
        Bundle args = new Bundle();
        args.putInt("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rounded_dialog, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        TextView title = view.findViewById(R.id.dialog_title);
        TextView product_sismilar_tv = view.findViewById(R.id.product_sismilar_tv);
        TextView disadvntgs_tv = view.findViewById(R.id.disadvntgs_tv);
        TextView comment_tv = view.findViewById(R.id.comment_tv);

        EditText advntg_ed = view.findViewById(R.id.advntg_ed);
        EditText disadvntgs_ed = view.findViewById(R.id.disadvntgs_ed);
        EditText comment_ed = view.findViewById(R.id.comment_ed);

        Button send  = view.findViewById(R.id.button_send);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/SST-Arabic-Light.ttf");
        title.setTypeface(type);
        product_sismilar_tv.setTypeface(type);
        disadvntgs_tv.setTypeface(type);
        comment_tv.setTypeface(type);
        advntg_ed.setTypeface(type);
        disadvntgs_ed.setTypeface(type);
        comment_ed.setTypeface(type);
        send.setTypeface(type);

        return view;
    }
}