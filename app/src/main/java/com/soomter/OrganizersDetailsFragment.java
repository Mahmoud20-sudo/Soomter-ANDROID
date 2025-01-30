package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class OrganizersDetailsFragment extends Fragment implements View.OnClickListener {
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
    private EventDetailData.Datum resultObj;
    private View child;
    private LayoutInflater layoutInflater;
    private Typeface type;
    private LinearLayout parentView;

    public OrganizersDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrganizersDetailsFragment newInstance(String jsonString) {
        OrganizersDetailsFragment fragment = new OrganizersDetailsFragment();
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
            Type type = new TypeToken<EventDetailData.Datum>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_organizers_details, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        layoutInflater = LayoutInflater.from(getContext());
        parentView = v.findViewById(R.id.container);

        Button regEvent = v.findViewById(R.id.reg_eve_btn);
        Button inivte = v.findViewById(R.id.invite_btn);

        inivte.setTypeface(type);
        regEvent.setTypeface(type);

        View innerChild = layoutInflater.inflate(R.layout.organizer_detail_item, null);
        TextView title = innerChild.findViewById(R.id.title);
        ImageView img = innerChild.findViewById(R.id.img);
        title.setText(resultObj.Telephone != null && resultObj.Telephone.length() > 0 ? resultObj.Telephone :
                getString(R.string.no_inform));
        title.setTypeface(type);
        img.setImageResource(R.mipmap.phone_off);
        innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        parentView.addView(innerChild);


        innerChild = layoutInflater.inflate(R.layout.organizer_detail_item, null);
        title = innerChild.findViewById(R.id.title);
        img = innerChild.findViewById(R.id.img);
        title.setText(resultObj.Mobile != null && resultObj.Mobile.length() > 0 ? resultObj.Mobile :
                getString(R.string.no_inform));
        title.setTypeface(type);
        img.setImageResource(R.mipmap.mobile);
        innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        parentView.addView(innerChild);


        innerChild = layoutInflater.inflate(R.layout.organizer_detail_item, null);
        title = innerChild.findViewById(R.id.title);
        img = innerChild.findViewById(R.id.img);
        title.setText(resultObj.Email != null && resultObj.Email.length() > 0 ? resultObj.Email :
                getString(R.string.no_inform));
        title.setTypeface(type);
        img.setImageResource(R.mipmap.email_off);
        innerChild.findViewById(R.id.divider).setVisibility(View.VISIBLE);
        parentView.addView(innerChild);

        innerChild = layoutInflater.inflate(R.layout.organizer_detail_item, null);
        title = innerChild.findViewById(R.id.title);
        img = innerChild.findViewById(R.id.img);
        title.setTypeface(type);
        title.setText(resultObj.EventUrl != null && resultObj.EventUrl.length() > 0 ? resultObj.EventUrl :
                getString(R.string.no_inform));
        img.setImageResource(R.mipmap.website);
        innerChild.findViewById(R.id.divider).setVisibility(View.GONE);
        parentView.addView(innerChild);

        regEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                RegisterEventDialog editNameDialogFragment = RegisterEventDialog.newInstance(resultObj.Id);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        inivte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                EventInviteDialog editNameDialogFragment = EventInviteDialog.newInstance(1);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });

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
