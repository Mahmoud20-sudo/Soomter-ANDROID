package com.soomter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterEventDialog extends DialogFragment {

    View focusView = null;
    int eventId;
    private SharedPref sharedPref;
    private APIInterface apiInterface;
    private View progress;
    private Button send;

    public static RegisterEventDialog newInstance(int id) {
        RegisterEventDialog frag = new RegisterEventDialog();
        Bundle args = new Bundle();
        args.putInt("id", id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPref = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View view = inflater.inflate(R.layout.reg_event_dialog, container, false);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        eventId = getArguments().getInt("id");
        TextView title = view.findViewById(R.id.dialog_title);
        TextView nameTv = view.findViewById(R.id.name_tv);
        TextView mobTv = view.findViewById(R.id.mob_tv);

        final EditText nameEd = view.findViewById(R.id.name_ed);
        final EditText mobEd = view.findViewById(R.id.mob_ed);

         send = (Button) view.findViewById(R.id.button_send);

        progress = view.findViewById(R.id.progress);

        Typeface type = Typeface.createFromAsset(getContext().getAssets(), "fonts/SST-Arabic-Light.ttf");
        title.setTypeface(type);
        nameTv.setTypeface(type);
        mobTv.setTypeface(type);

        nameEd.setTypeface(type);
        mobEd.setTypeface(type);
        send.setTypeface(type);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean cancel = false;
                if (TextUtils.isEmpty(nameEd.getText().toString())) {
                    nameEd.setError(getString(R.string.error_field_required));
                    focusView = nameEd;
                    cancel = true;
                }
                if (TextUtils.isEmpty(mobEd.getText().toString())) {
                    mobEd.setError(getString(R.string.error_field_required));
                    focusView = nameEd;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    register(nameEd.getText().toString(), mobEd.getText().toString());
                }
            }
        });

        return view;
    }

    private void register(String name, String phone) {
        if (sharedPref.getUser() == null) {
            Toast.makeText(getContext(), getString(R.string.require_login), Toast.LENGTH_LONG).show();
            return;
        }

        send.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        Call<Void> call1 = apiInterface.registerToEvent(eventId, name, phone);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                dismiss();
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
                send.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        });

    }

}