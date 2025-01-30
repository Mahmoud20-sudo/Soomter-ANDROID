package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

import com.soomter.utils.SpacesItemDecoration2;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyAdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyAdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyAdsFragment extends Fragment implements View.OnClickListener, CompanyAdsAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    Locale locale;
    ProgressBar determinateBar;
    private CompanyProfileData.Result resultObj;
    private OnFragmentInteractionListener mListener;
    private Typeface type;
    private RecyclerView recyclerView;

    public CompanyAdsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyAdsFragment newInstance(String jsonString) {
        CompanyAdsFragment fragment = new CompanyAdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            Type type = new TypeToken<CompanyProfileData.Result>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_ads, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        recyclerView = v.findViewById(R.id.ads_rv);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new
                SpacesItemDecoration2(getResources().getDimensionPixelSize(R.dimen.hspacing)));

        if (resultObj.listAds.isEmpty()) {
            View nodata_view = v.findViewById(R.id.nodata_view);
            nodata_view.setVisibility(View.VISIBLE);
            ImageView img = nodata_view.findViewById(R.id.nodata_img);
            img.setImageResource(R.mipmap.ads_off);
            TextView title = nodata_view.findViewById(R.id.nodata_txt);
            title.setText(R.string.no_ads);
            TextView breif = nodata_view.findViewById(R.id.nodata_brief_txt);
            title.setTypeface(type);
            breif.setTypeface(type);
            breif.setText(R.string.no_ads_brief);
        } else {
            CompanyAdsAdapter adsAdapter = new CompanyAdsAdapter(getContext(), resultObj.listAds);
            adsAdapter.setClickListener(this);
            recyclerView.setAdapter(adsAdapter);
        }
        return v;
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
    }

    @Override
    public void onItemClick(View view, int position) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)//data.id, data.Type, data.Title
                .add(R.id.frag_container, AdsDetailsFragment.newInstance(resultObj.listAds.get(position).Id
                        ,1, resultObj.listAds.get(position).Title)).hide(this).commit();
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
