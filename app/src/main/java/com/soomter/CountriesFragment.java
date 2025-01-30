package com.soomter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CountriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CountriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CountriesFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView chooseTxt, ksaTxt, uaeTxt, kuwaitTxt;
    private View ksaBtn, uaeBtn,kuwaitBtn;

    Locale locale;
    private OnFragmentInteractionListener mListener;

    public CountriesFragment() {
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
    public static CountriesFragment newInstance(String param1, String param2) {
        CountriesFragment fragment = new CountriesFragment();
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
        View v = inflater.inflate(R.layout.fragment_countries, container, false);

        ksaTxt = (TextView)v.findViewById(R.id.ksa_txt);
        uaeTxt = (TextView)v.findViewById(R.id.uae_txt);
        kuwaitTxt = (TextView)v.findViewById(R.id.kuwait_txt);
        chooseTxt = (TextView)v.findViewById(R.id.choose_txt);

        ksaBtn = v.findViewById(R.id.ksa_btn);
        uaeBtn = v.findViewById(R.id.uae_btn);
        kuwaitBtn = v.findViewById(R.id.kuwait_btn);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SST-Arabic-Light.ttf");

        ksaTxt.setTypeface(type);
        uaeTxt.setTypeface(type);
        chooseTxt.setTypeface(type);
        kuwaitTxt.setTypeface(type);

        kuwaitBtn.setOnClickListener(this);
        uaeBtn.setOnClickListener(this);
        ksaBtn.setOnClickListener(this);

        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.GONE);

        final SharedPreferences languagepref = getActivity()
                .getSharedPreferences("language",getActivity().MODE_PRIVATE);
        final SharedPreferences.Editor editor = languagepref.edit();

        v.findViewById(R.id.lang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (languagepref.getString("languageToLoad","en_US").equals("en_US")) {
                    locale = new Locale("ar");
                    editor.putString("languageToLoad","ar" );
                    editor.commit();

                } else {
                    locale = new Locale("en");
                    editor.putString("languageToLoad","en_US" );
                    editor.commit();
                }
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getResources().updateConfiguration(config,
                        getActivity().getResources().getDisplayMetrics());
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
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
        getFragmentManager().beginTransaction()
                .replace(R.id.frag_container , LoginFragment.newInstance(null,null)).commit();
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
