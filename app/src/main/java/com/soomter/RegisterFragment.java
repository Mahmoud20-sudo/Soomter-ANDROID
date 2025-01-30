package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView reg_name_txt;
    private EditText name, name2, lastName, lastName2, mobile, password, email, username;
    private Button register;

    private OnFragmentInteractionListener mListener;
    private ImageView regImg;
    private String passString, mobileSting, nameString, nameString2, emailString, lastNameString, lastNameString2, userNameString;
    private ProgressBar progress;
    private APIInterface apiInterface;
    private SharedPref prefObj;

    public RegisterFragment() {
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
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefObj = new SharedPref(getActivity());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        name = (EditText) v.findViewById(R.id.name);
        name2 = (EditText) v.findViewById(R.id.name2);
        lastName = (EditText) v.findViewById(R.id.last_name);
        lastName2 = (EditText) v.findViewById(R.id.last_name2);
        mobile = (EditText) v.findViewById(R.id.mobile_num);
        email = (EditText) v.findViewById(R.id.email);
        username = (EditText) v.findViewById(R.id.username);
        password = (EditText) v.findViewById(R.id.password);
        reg_name_txt = (TextView) v.findViewById(R.id.reg_name_txt);
        reg_name_txt.setTypeface(type);

        register = (Button) v.findViewById(R.id.rigester_btn);
        register.setOnClickListener(this);
        register.setTypeface(type);

        regImg = (ImageView) v.findViewById(R.id.reg_img);

        progress = (ProgressBar) v.findViewById(R.id.reg_progress);

        if (mParam1.equals("2")) {
            reg_name_txt.setText(R.string.person_reg);
            name.setHint(R.string.first_name);
            name2.setHint(Locale.getDefault().getLanguage().toString().equals("ar")
                    ? R.string.first_name_en : R.string.first_name_ar);
            lastName.setVisibility(View.VISIBLE);
            lastName2.setVisibility(View.VISIBLE);
            lastName2.setHint(Locale.getDefault().getLanguage().toString().equals("ar")
                    ? R.string.last_name_en : R.string.last_name_ar);
            regImg.setImageResource(R.mipmap.person_logo);
            v.findViewById(R.id.last_name_view).setVisibility(View.VISIBLE);
            v.findViewById(R.id.last_name_view_2).setVisibility(View.VISIBLE);
        } else {
            reg_name_txt.setText(R.string.institue_reg);
            name.setHint(R.string.build_name);
            name2.setHint(Locale.getDefault().getLanguage().toString().equals("ar")
                    ? R.string.build_name_en : R.string.build_name_ar);
            regImg.setImageResource(R.mipmap.inistute_logo);
        }

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        return v;
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
        getFragmentManager().beginTransaction().addToBackStack("login")
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

    private void attemptRegister() {

        // Reset errors.
        email.setError(null);
        password.setError(null);
        mobile.setError(null);
        name.setError(null);
        name2.setError(null);
        username.setError(null);
        lastName.setError(null);
        lastName2.setError(null);

        // Store values at the time of the login attempt.
        emailString = email.getText().toString();
        passString = password.getText().toString();
        nameString = name.getText().toString();
        nameString2 = name2.getText().toString();
        userNameString = username.getText().toString();
        mobileSting = mobile.getText().toString();
        lastNameString = lastName.getText().toString();
        lastNameString2 = lastName2.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passString) || !isPasswordValid(passString)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailString)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailString)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        } else if (TextUtils.isEmpty(mobileSting)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        } else if (TextUtils.isEmpty(nameString)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        } else if (TextUtils.isEmpty(nameString2)) {
            name2.setError(getString(R.string.error_field_required));
            focusView = name2;
            cancel = true;
        } else if (lastName.isShown() && TextUtils.isEmpty(lastNameString)) {
            lastName.setError(getString(R.string.error_field_required));
            focusView = lastName;
            cancel = true;
        } else if (lastName2.isShown() && TextUtils.isEmpty(lastNameString2)) {
            lastName2.setError(getString(R.string.error_field_required));
            focusView = lastName2;
            cancel = true;
        } else if (TextUtils.isEmpty(userNameString)) {
            username.setError(getString(R.string.error_field_required));
            focusView = username;
            cancel = true;
        } else if (TextUtils.isEmpty(mobileSting)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask.execute((Void) null);

            if (mParam1.equals("1"))
                registerCompanyRequest();
            else
                registerProfileRequest();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    private void registerCompanyRequest() {
        //view.setEnabled(false);
        CompanyReg user;
        progress.setVisibility(View.VISIBLE);
        if (Locale.getDefault().getLanguage().toString().equals("ar"))
            user = new CompanyReg(nameString, nameString2, emailString, mobileSting,
                    passString, userNameString, Locale.getDefault().getLanguage().toString());
        else
            user = new CompanyReg(nameString2, nameString, emailString, mobileSting,
                    passString, userNameString, Locale.getDefault().getLanguage().toString());

        Call<CompanyRegData> call1 = apiInterface.createCompnay(user);
        call1.enqueue(new Callback<CompanyRegData>() {
            @Override
            public void onResponse(Call<CompanyRegData> call, retrofit2.Response<CompanyRegData> response) {
                progress.setVisibility(View.INVISIBLE);
                CompanyRegData companyData = response.body();
                if (companyData.result == null) {
                    if (companyData.error.toString().equals("2"))
                        Toast.makeText(getContext(), getString(R.string.existed_user), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
                //getFragmentManager().beginTransaction().replace(R.id.frag_container, MainFragment.newInstance(null, null)).commit();
            }

            @Override
            public void onFailure(Call<CompanyRegData> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void registerProfileRequest() {
        //view.setEnabled(false);
        progress.setVisibility(View.VISIBLE);
        ProfileReg profileReg;
        CompanyReg user;
        progress.setVisibility(View.VISIBLE);
        if (Locale.getDefault().getLanguage().toString().equals("ar"))
            profileReg = new ProfileReg(nameString, nameString2, lastNameString, lastNameString2, emailString, mobileSting,
                    passString, userNameString, Locale.getDefault().getLanguage().toString());
        else
            profileReg = new ProfileReg(nameString2, nameString, lastNameString2, lastNameString, emailString, mobileSting,
                    passString, userNameString, Locale.getDefault().getLanguage().toString());

        Call<CompanyRegData> call1 = apiInterface.createProfile(profileReg);
        call1.enqueue(new Callback<CompanyRegData>() {
            @Override
            public void onResponse(Call<CompanyRegData> call, retrofit2.Response<CompanyRegData> response) {
                progress.setVisibility(View.INVISIBLE);
                CompanyRegData companyData = response.body();
                if (companyData.result == null) {
                    if (companyData.error.toString().equals("2"))
                        Toast.makeText(getContext(), getString(R.string.existed_user), Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                getFragmentManager().popBackStack();
                //getFragmentManager().beginTransaction().replace(R.id.frag_container, MainFragment.newInstance(null, null)).commit();
            }

            @Override
            public void onFailure(Call<CompanyRegData> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }
}
