package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyEventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyEventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyEventsFragment extends Fragment implements View.OnClickListener, CompanyEventsAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private CompanyProfileData.Result resultObj;
    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private Typeface type;
    private RecyclerView recyclerView;

    public CompanyEventsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyEventsFragment newInstance(String jsonString) {
        CompanyEventsFragment fragment = new CompanyEventsFragment();
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

        if (resultObj.listEvents.isEmpty()) {
            View nodata_view = v.findViewById(R.id.nodata_view);
            nodata_view.setVisibility(View.VISIBLE);
            ImageView img = nodata_view.findViewById(R.id.nodata_img);
            img.setImageResource(R.mipmap.events_off);
            TextView title = nodata_view.findViewById(R.id.nodata_txt);
            title.setText(R.string.no_events);
            TextView breif = nodata_view.findViewById(R.id.nodata_brief_txt);
            title.setTypeface(type);
            breif.setTypeface(type);
            breif.setText(R.string.no_events_brief);
        }else {
            recyclerView = v.findViewById(R.id.ads_rv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            CompanyEventsAdapter adsAdapter = new CompanyEventsAdapter(getContext(), resultObj.listEvents);
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
        EventsDetailsFragment fragOne = EventsDetailsFragment.newInstance(resultObj.listEvents.get(position).Id,
                resultObj.listEvents.get(position).ImageId + "");
        getActivity().getSupportFragmentManager()
                .beginTransaction().add(R.id.frag_container, fragOne)
                .hide(this).addToBackStack(null).commit();
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
