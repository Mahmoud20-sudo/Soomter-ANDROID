package com.soomter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MostWatchedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MostWatchedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MostWatchedFragment extends Fragment implements View.OnClickListener , MostWatchedAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;

    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private RecyclerView recyclerView;
    private MostWatchedAdapter companiesAdapter;
    List<MostWatchedData.Datum> list;

    public MostWatchedFragment() {
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
    public static MostWatchedFragment newInstance(String mParam1) {
        MostWatchedFragment fragment = new MostWatchedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam2 = getArguments().getString(ARG_PARAM1);

            //companiesData.result.Items
            Gson gson = new Gson();
            Type listOfdoctorType = new TypeToken<List<MostWatchedData.Datum>>() {
            }.getType();
            list = gson.fromJson(mParam2, listOfdoctorType);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_companies, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        TextView titleTv = (TextView) v.findViewById(R.id.title);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        titleTv.setText(getString(R.string.most_view));
        titleTv.setTypeface(type);

        v.findViewById(R.id.top_img).setVisibility(View.GONE);
        v.findViewById(R.id.search_view).setVisibility(View.GONE);
        v.findViewById(R.id.show_cont).setVisibility(View.GONE);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();
        determinateBar = v.findViewById(R.id.determinateBar);

        determinateBar.setVisibility(View.GONE);

        recyclerView.setPadding(0, 20, 0, 0);

        companiesAdapter = new MostWatchedAdapter(getContext(), list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        companiesAdapter.setClickListener(this);
        // recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(companiesAdapter);

        TextView showByCat = (TextView) v.findViewById(R.id.show_by_cat);
        TextView showByCity = (TextView) v.findViewById(R.id.show_by_city);

        showByCat.setTypeface(type);
        showByCity.setTypeface(type);

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
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
        getFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frag_container, LoginFragment.newInstance(null, null)).commit();
    }

    @Override
    public void onItemClick(View view, int position) {
        getFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.frag_container,
                        CompanyProfileFragment.newInstance(list.get(position).Id ,
                                list.get(position).CompanyName)).hide(this).addToBackStack(null).commit();
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
