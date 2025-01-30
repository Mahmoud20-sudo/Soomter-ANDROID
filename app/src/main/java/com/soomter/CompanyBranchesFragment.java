package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyBranchesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyBranchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyBranchesFragment extends Fragment implements View.OnClickListener {
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
    private Typeface type;
    private LinearLayout parentView;

    public CompanyBranchesFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyBranchesFragment newInstance(String jsonString) {
        CompanyBranchesFragment fragment = new CompanyBranchesFragment();
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
        View v = inflater.inflate(R.layout.fragment_company_branches, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        layoutInflater = LayoutInflater.from(getContext());

        parentView = v.findViewById(R.id.branches_cont);

        if (resultObj.branchsCompany.isEmpty()) {
            View nodata_view = v.findViewById(R.id.nodata_view);
            nodata_view.setVisibility(View.VISIBLE);
            ImageView img = nodata_view.findViewById(R.id.nodata_img);
            img.setImageResource(R.mipmap.branches_off);
            TextView title = nodata_view.findViewById(R.id.nodata_txt);
            title.setText(R.string.no_branches);
            TextView breif = nodata_view.findViewById(R.id.nodata_brief_txt);
            title.setTypeface(type);
            breif.setTypeface(type);
            breif.setText(R.string.no_branches_brief);
        } else
            for (int i = 0; i < resultObj.branchsCompany.size(); i++) {
                CompanyProfileData.BranchsCompany obj = resultObj.branchsCompany.get(i);

                View child = layoutInflater.inflate(R.layout.branch_item, null);
                TextView branchTitle = child.findViewById(R.id.branch_title);
                LinearLayout dataCont = child.findViewById(R.id.branch_cont);

                branchTitle.setText(obj.BranchName);
                parentView.addView(child);

                if (obj.BranchAddress != null && obj.BranchAddress.length() > 0) {
                    View innerChild = layoutInflater.inflate(R.layout.company_detail_item, null);
                    TextView title = innerChild.findViewById(R.id.title);
                    TextView num = innerChild.findViewById(R.id.num);

                    title.setText(getString(R.string.address));
                    num.setText(obj.BranchAddress);
                    innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                    dataCont.addView(innerChild);

                }

                if (obj.BranchMobile != null && obj.BranchMobile.length() > 0) {
                    View innerChild = layoutInflater.inflate(R.layout.company_detail_item, null);
                    TextView title = innerChild.findViewById(R.id.title);
                    TextView num = innerChild.findViewById(R.id.num);

                    title.setText(getString(R.string.mobile));
                    num.setText(obj.BranchMobile);
                    innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                    dataCont.addView(innerChild);
                }

                if (obj.BranchPhone != null && obj.BranchPhone.length() > 0) {
                    View innerChild = layoutInflater.inflate(R.layout.company_detail_item, null);
                    TextView title = innerChild.findViewById(R.id.title);
                    TextView num = innerChild.findViewById(R.id.num);

                    title.setText(getString(R.string.phone));
                    num.setText(obj.BranchPhone);
                    innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                    dataCont.addView(innerChild);
                }

                if (obj.BranchFax != null && obj.BranchFax.length() > 0) {
                    View innerChild = layoutInflater.inflate(R.layout.company_detail_item, null);
                    TextView title = innerChild.findViewById(R.id.title);
                    TextView num = innerChild.findViewById(R.id.num);

                    title.setText(getString(R.string.fax));
                    num.setText(obj.BranchFax);
                    innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                    dataCont.addView(innerChild);
                }

           /* if(obj. != null && obj.BranchFax.length() > 0){
                View innerChild = layoutInflater.inflate(R.layout.company_detail_item , null);
                TextView title = innerChild.findViewById(R.id.title);
                TextView num = innerChild.findViewById(R.id.num);

                title.setText(getString(R.string.fax));
                num.setText(obj.BranchFax);
                innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            }*/

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
