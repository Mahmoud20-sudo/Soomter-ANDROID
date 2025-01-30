package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

import com.soomter.utils.HozriontalSpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyDetailsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;

    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private CompanyProfileData.Result resultObj;
    private View child;
    private LayoutInflater layoutInflater;
    private Typeface typeRoman, type;
    private LinearLayout detailsCont, informCont;

    public CompanyDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyDetailsFragment newInstance(String jsonString) {
        CompanyDetailsFragment fragment = new CompanyDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_company_details, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        layoutInflater = LayoutInflater.from(getContext());

        detailsCont = v.findViewById(R.id.details_cont);
        informCont = v.findViewById(R.id.inform_cont);

        TextView dataTitle = v.findViewById(R.id.data_title);
        TextView informTitle = v.findViewById(R.id.inform_title);
        TextView pickedTitle = v.findViewById(R.id.picked_fc_title);

        dataTitle.setTypeface(typeRoman);
        informTitle.setTypeface(typeRoman);
        pickedTitle.setTypeface(typeRoman);

        initatieDetails();
        initiateInformation();

        if (resultObj.similarCompanies == null || resultObj.similarCompanies.isEmpty()) {
            v.findViewById(R.id.picked_facilities_cont).setVisibility(View.GONE);
        } else {
            initiateRecyclerView(v);
        }

        return v;
    }

    private void initatieDetails() {
        if (resultObj.UnifiedNumber != null && resultObj.UnifiedNumber.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.unified));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.UnifiedNumber);
            detailsCont.addView(child);
        }

        if (resultObj.Mobile != null && resultObj.Mobile.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.emobile));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Mobile);
            detailsCont.addView(child);
        }

        if (resultObj.Phone != null && resultObj.Phone.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.phone));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Phone);
            detailsCont.addView(child);
        }

        if (resultObj.Fax != null && resultObj.Fax.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fax));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Fax);
            detailsCont.addView(child);
        }

        if (resultObj.MailBox != null && resultObj.MailBox.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.post_code));

            title.setTypeface(type);
            num.setTypeface(type);

            num.setText(resultObj.MailBox);
            child.findViewById(R.id.divider).setVisibility(View.GONE);

            detailsCont.addView(child);
        }

        if (resultObj.Website != null && resultObj.Website.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.site));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Website);
            detailsCont.addView(child);
        }
    }

    private void initiateInformation() {
        if (resultObj.CommercialRegistrationNo != null && resultObj.CommercialRegistrationNo.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.postal));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.CommercialRegistrationNo);
            informCont.addView(child);
        }

        if (resultObj.CatgoryName != null && resultObj.CatgoryName.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fc_category));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.CatgoryName);
            informCont.addView(child);
        }

        if (resultObj.FieldName != null && resultObj.FieldName.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fc_activity));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.FieldName);
            informCont.addView(child);
        }

        if (resultObj.Address != null && resultObj.Address.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fc_address));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Address);
            informCont.addView(child);
        }

        if (resultObj.CompanyBanchesCount > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fc_branches));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.CompanyBanchesCount + "");
            informCont.addView(child);
        }

        if (resultObj.Sunday_Thursday != null && resultObj.Sunday_Thursday.length() > 0) {
            child = layoutInflater.inflate(R.layout.company_detail_item, null);
            TextView title = child.findViewById(R.id.title);
            TextView num = child.findViewById(R.id.num);
            title.setText(getString(R.string.fc_timeline));

            title.setTypeface(type);
            num.setTypeface(type);

            child.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            num.setText(resultObj.Sunday_Thursday);
            informCont.addView(child);
        }
    }

    private void initiateRecyclerView(View v) {
        final RecyclerView recyclerView = v.findViewById(R.id.picked_fc_rv);

        PickedFacilitiesAdapter pickedFacilitiesAdapter = new PickedFacilitiesAdapter(getContext()
                , resultObj.similarCompanies);

        final LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setAdapter(pickedFacilitiesAdapter);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(0);
            }
        }, 200);


        ImageView img_LeftScroll = v.findViewById(R.id.left_arrow);
        ImageView img_right_scroll = v.findViewById(R.id.right_arrow);

        img_right_scroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (horizontalLayoutManagaer.findFirstVisibleItemPosition() > 0) {
                    recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findFirstVisibleItemPosition() - 1);
                } else {
                    recyclerView.smoothScrollToPosition(0);
                }

            }
        });

        img_LeftScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(horizontalLayoutManagaer.findLastVisibleItemPosition() + 1);
            }
        });
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
