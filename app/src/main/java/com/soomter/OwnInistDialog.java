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
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;

public class OwnInistDialog extends DialogFragment {

    public ClickEventListener clickEventListener;
    public ProgressBar progressBar;
    public Button send;
    boolean isValid = true;
    private APIInterface apiInterface;

    private SharedPref sharedPref;
    private int companyID;

    public static OwnInistDialog newInstance(int companyID) {
        OwnInistDialog frag = new OwnInistDialog();
        Bundle args = new Bundle();
        args.putInt("companyID", companyID);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        sharedPref = new SharedPref(getActivity());
        companyID = getArguments().getInt("companyID");
        View view = inflater.inflate(R.layout.own_inist_dialog, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        progressBar = view.findViewById(R.id.loadmore_progress);
        TextView title = view.findViewById(R.id.dialog_title);
        TextView name_tv = view.findViewById(R.id.name_tv);
        TextView address_tv = view.findViewById(R.id.address_tv);
        TextView mob_tv = view.findViewById(R.id.mobile_tv);
        TextView details_tv = view.findViewById(R.id.details_tv);

        final EditText name_ed = view.findViewById(R.id.name_ed);
        final EditText email_ed = view.findViewById(R.id.address_ed);
        final EditText mobile_ed = view.findViewById(R.id.mobile_ed);
        final EditText details_ed = view.findViewById(R.id.details_ed);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        send = view.findViewById(R.id.button_send);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/SST-Arabic-Light.ttf");
        title.setTypeface(type);
        name_tv.setTypeface(type);
        address_tv.setTypeface(type);
        mob_tv.setTypeface(type);
        details_tv.setTypeface(type);
        name_ed.setTypeface(type);
        email_ed.setTypeface(type);
        mobile_ed.setTypeface(type);
        details_ed.setTypeface(type);
        send.setTypeface(type);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isValid = true;
                if (name_ed.getText().length() == 0) {
                    name_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (email_ed.getText().length() == 0) {
                    email_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (!email_ed.getText().toString().contains("@")){
                    email_ed.setError(getString(R.string.error_invalid_email));
                    isValid = false;
                }
                if (mobile_ed.getText().length() == 0) {
                    mobile_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (details_ed.getText().length() == 0) {
                    details_ed.setError(getString(R.string.error_field_required));
                    isValid = false;
                }
                if (isValid) {
                    progressBar.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                    ownCompany(name_ed.getText().toString(), email_ed.getText().toString(),
                            mobile_ed.getText().toString(), details_ed.getText().toString() ,sharedPref.getUser().id);
                }
            }
        });

        return view;
    }

    private void ownCompany(String name, String email, String mobiel, String requestDets, String userId) {
        //Integer Id, String Name,String Email,  String Mobile, String RequestDetails ,String UserId
        GeneralCompRequest generalCompRequest = new GeneralCompRequest(companyID, name, email, mobiel, requestDets, userId);
        Call<Void> call1 = apiInterface.sendGeneralCompanyRequest(generalCompRequest);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    dismiss();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    public interface ClickEventListener {
        void click(String name, String address, String mobile, String details, OwnInistDialog dialog);
    }
}