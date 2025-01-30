package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecoverAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecoverAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecoverAccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Locale locale;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private SharedPref prefObj;
    private Typeface type, typeRoman, typeMedium;
    private int method = 2 ,typeInt = 1;
    private EditText inputEd;
    private MySpinnerDialog myInstance;

    public RecoverAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecoverAccountFragment newInstance(String param1, String param2) {
        RecoverAccountFragment fragment = new RecoverAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");

        final View v = inflater.inflate(R.layout.fragment_account_recover, container, false);

        TextView titleTv = v.findViewById(R.id.recover_title);
        TextView chooseTv = v.findViewById(R.id.choose_txt);

        RadioGroup chooseRgb = v.findViewById(R.id.rgp);
        RadioGroup sendRgb = v.findViewById(R.id.send_rgp);

        titleTv.setTypeface(type);
        chooseTv.setTypeface(typeRoman);
        ((Button)v.findViewById(R.id.button_send)).setTypeface(type);
        ((RadioButton)v.findViewById(R.id.forget_pass_rb)).setTypeface(type);
        ((RadioButton)v.findViewById(R.id.forget_user_rb)).setTypeface(type);
        ((RadioButton)v.findViewById(R.id.mobile_sms_rb)).setTypeface(type);
        ((RadioButton)v.findViewById(R.id.mail_verify_rb)).setTypeface(type);

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        inputEd = v.findViewById(R.id.input_verify);
        inputEd.setTypeface(type);
        //inputEd.setInputType(InputType.Ema);
        //TYPE //1: USERNAME 2:PASSWORDS

        chooseRgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.forget_pass_rb:
                        typeInt = 2;
                        break;
                    case R.id.forget_user_rb:
                        typeInt = 1;
                        break;
                }
            }
        });

        sendRgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.mobile_sms_rb:
                        method = 2;
                        inputEd.setText("");
                        inputEd.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                        inputEd.setHint(getString(R.string.mobile));
                        break;
                    case R.id.mail_verify_rb:
                        method = 1;
                        inputEd.setText("");
                        inputEd.setHint(getString(R.string.email));
                        inputEd.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        break;
                }
            }
        });

        v.findViewById(R.id.button_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputEd.getText().toString().trim().length() == 0){
                    inputEd.setError(getString(R.string.error_field_required));
                    return;
                }
                if(method == 1){
                    if(!isValidEmail(inputEd.getText().toString())){
                        inputEd.setError(getString(R.string.error_invalid_email));
                        return;
                    }
                    sendRequest();
                }
                else
                    sendRequest();
            }
        });

        return v;
    }

    private void sendRequest(){

        myInstance = new MySpinnerDialog();
        myInstance.setCancelable(false);

        myInstance.show(getFragmentManager(), "some_tag");

        final AccountRecover user = new AccountRecover(inputEd.getText().toString(), method, typeInt);
        Call<AccountRecoverData> call1 = apiInterface.recoverAccount(user);
        call1.enqueue(new Callback<AccountRecoverData>() {
            @Override
            public void onResponse(Call<AccountRecoverData> call, retrofit2.Response<AccountRecoverData> response) {
                myInstance.dismiss();
                AccountRecoverData user1 = response.body();
                if(user1.result == null){
                    Toast.makeText(getContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                    return;
                }
                if(user1.result.IsSuccess)
                    Toast.makeText(getContext(),getString(R.string.recover_success),Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<AccountRecoverData> call, Throwable t) {
                myInstance.dismiss();
                Toast.makeText(getContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
/*
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frag_container, LoginFragment.newInstance(null, null)).commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}