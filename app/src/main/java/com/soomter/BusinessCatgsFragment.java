package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BusinessCatgsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BusinessCatgsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessCatgsFragment extends Fragment implements View.OnClickListener, BusenissCatgsAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;
    Locale locale;
    CompaniesFragment companiesFragment;
    // TODO: Rename and change types of parameters
    private int categID;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private RecyclerView recyclerView;
    private BusenissCatgsAdapter cardViewAdapter;
    private List<BussinesCatgData.Datum> list;
    private View progress;

    public BusinessCatgsFragment() {
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
    public static BusinessCatgsFragment newInstance(int categID, String param2, CompaniesFragment companiesFragment) {
        BusinessCatgsFragment fragment = new BusinessCatgsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, categID);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.companiesFragment = companiesFragment;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categID = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_cities, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);

        final TextView title = (TextView) v.findViewById(R.id.title);
        EditText searchED = (EditText) v.findViewById(R.id.serch_ed);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        if (mParam2 != null)
            title.setText(mParam2);
        title.setTypeface(type);
        searchED.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();
        list = sharedPref.getBusinessCatg();

        progress = v.findViewById(R.id.progress);

        //if (list == null || list.isEmpty())
        bussinesCatgReq();

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setVisibility(View.GONE);


        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return v;
    }

    private void bussinesCatgReq() {
        progress.setVisibility(View.VISIBLE);
        final BussinessCatgsRequest bussinesCatgReq = new BussinessCatgsRequest(Locale.getDefault().getLanguage().toString()
                , categID);
        Call<BussinesCatgData> call1 = apiInterface.getBussinesCatg(bussinesCatgReq);
        call1.enqueue(new Callback<BussinesCatgData>() {
            @Override
            public void onResponse(Call<BussinesCatgData> call, retrofit2.Response<BussinesCatgData> response) {
                progress.setVisibility(View.GONE);
                BussinesCatgData bussinesCatgData = response.body();
                if (bussinesCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }

                list = bussinesCatgData.result;
                sharedPref.saveBusinessCatg(bussinesCatgData.result);
                BussinesCatgData.Datum obj = new BussinesCatgData.Datum();
                /*obj.id = 0;
                obj.name = getString(R.string.all_activities);
                obj.isChecked = sharedPref.getAllBussCatgsStatus();
                list.add(0, obj);*/
                cardViewAdapter = new BusenissCatgsAdapter(getContext(), bussinesCatgData.result, categID);
                cardViewAdapter.setClickListener(BusinessCatgsFragment.this);
                recyclerView.setAdapter(cardViewAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<BussinesCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });

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
        companiesFragment.subCategoryId = list.get(position).id;
        companiesFragment.currentPage = 1;
        companiesFragment.loadNextPage();
        getFragmentManager().popBackStack();
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
