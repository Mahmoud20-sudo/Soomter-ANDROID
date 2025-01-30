package com.soomter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import com.soomter.utils.InputFilterMinMax;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdsSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdsSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsSearchFragment extends Fragment implements View.OnClickListener, CitiesAdapter.ItemClickListener,
        BusenissCatgsAdapter.ItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int tradeMarksId;
    Locale locale;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private float currentMinVal, currentMaxVal;
    private BusenissCatgsAdapter cardViewAdapter;
    private CitiesAdapter citiesAdapter , adsAdapter;
    private RecyclerView catgsRv, adsRv , citiesRv;
    private LinearLayoutManager mLayoutManager;
    private TextView adTxt, catTxt , cityTxt;
    private SubCatgsAdapter subCatgsAdapter;
    private List<BussinesCatgData.Datum> list;
    private List<CitiesData.Datum> citiesList;
    private List<CitiesData.Datum> adsList;
    private Typeface type , typeRoman;

    public AdsSearchFragment() {
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
    public static AdsSearchFragment newInstance(String param1, String param2) {
        AdsSearchFragment fragment = new AdsSearchFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AdsFragment.setView(getString(R.string.advanced_search));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AdsFragment.setTitle(getString(R.string.ads));
                        AdsFragment.showView();
                        getFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ads_search, container, false);

         type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
         typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        catgsRv = (RecyclerView) v.findViewById(R.id.catgs_recycler_view);
        citiesRv = (RecyclerView) v.findViewById(R.id.cities_recycler_view);
        adsRv = (RecyclerView) v.findViewById(R.id.ads_recycler_view);

        adTxt = (TextView) v.findViewById(R.id.ads_txt);
        cityTxt = (TextView) v.findViewById(R.id.cities_txt);
        catTxt = (TextView) v.findViewById(R.id.catgs_txt);

        adTxt.setTypeface(typeRoman);
        cityTxt.setTypeface(typeRoman);
        catTxt.setTypeface(typeRoman);

        final Button saveBtn = (Button) v.findViewById(R.id.serch_save);

        //list = sharedPref.getBusinessCatg();
        citiesList = sharedPref.getCities();

        //if (list == null || list.isEmpty())
            bussinesCatgReq();
        /*else {
           *//* BussinesCatgData.Datum obj = new BussinesCatgData.Datum();
            obj.id = 0;
            obj.name = getString(R.string.all_activities);
            //obj.isChecked = sharedPref.getAllBussCatgsStatus();
            list.add(0, obj);*//*
            cardViewAdapter = new BusenissCatgsAdapter(getContext(), list,0);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            catgsRv.setLayoutManager(mLayoutManager);
            catgsRv.setItemAnimator(new DefaultItemAnimator());
            catgsRv.setAdapter(cardViewAdapter);
        }*/

        if (citiesList == null || citiesList.isEmpty())
            citiesReq();
        else {
            CitiesData.Datum obj = new CitiesData.Datum();
            obj.id = 0;
            obj.name = getString(R.string.all_cities);
            obj.isChecked = sharedPref.getAllCitiesStatus();
            citiesList.add(0, obj);
            citiesAdapter = new CitiesAdapter(getContext(), citiesList ,0);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            citiesRv.setLayoutManager(mLayoutManager);
            citiesRv.setItemAnimator(new DefaultItemAnimator());
            citiesAdapter.setClickListener(AdsSearchFragment.this);
            citiesRv.setAdapter(citiesAdapter);
        }

        adsList = new ArrayList<>();
        String[] adsArray = getResources().getStringArray(R.array.ads_list);

        for(int i = 0 ; i < adsArray.length ; i++){
            CitiesData.Datum obj = new CitiesData.Datum();
            obj.id = i;
            obj.name = adsArray[i];
            obj.isChecked = false;
            adsList.add(obj);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        adsRv.setLayoutManager(mLayoutManager);
        adsRv.setItemAnimator(new DefaultItemAnimator());
        adsAdapter = new CitiesAdapter(getContext(), adsList ,0);
        adsAdapter.setClickListener(AdsSearchFragment.this);
        adsRv.setAdapter(adsAdapter);

        saveBtn.setTypeface(type);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*currentMaxVal,
                        currentMinVal,cardViewAdapter.currentCheckedTradeID,
                if(subCatgsAdapter ! = null)
                    subCatgsAdapter.currentCheckedSubCatgID*/
            }
        });

        catgsRv.setNestedScrollingEnabled(false);
        citiesRv.setNestedScrollingEnabled(false);
        adsRv.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AdsFragment.resetScroll();
            }
        }, 100);

        return v;
    }

    private void citiesReq() {
        //view.setEnabled(false);
        final CitiesRequest cities = new CitiesRequest(2, Locale.getDefault().getLanguage().toString());
        Call<CitiesData> call1 = apiInterface.getCitites(cities);
        call1.enqueue(new Callback<CitiesData>() {
            @Override
            public void onResponse(Call<CitiesData> call, retrofit2.Response<CitiesData> response) {
                CitiesData cities = response.body();
                if (cities.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                sharedPref.saveCities(cities.result);
                CitiesData.Datum obj = new CitiesData.Datum();
                obj.id = 0;
                obj.name = getString(R.string.all_cities);
                obj.isChecked = sharedPref.getAllCitiesStatus();
                cities.result.add(0, obj);
                citiesAdapter = new CitiesAdapter(getContext(), cities.result ,0);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                citiesRv.setLayoutManager(mLayoutManager);
                citiesRv.setItemAnimator(new DefaultItemAnimator());
                citiesAdapter.setClickListener(AdsSearchFragment.this);
                citiesRv.setAdapter(cardViewAdapter);
            }

            @Override
            public void onFailure(Call<CitiesData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void bussinesCatgReq() {
        //view.setEnabled(false);
        final BussinessCatgsRequest bussinesCatgReq = new BussinessCatgsRequest(Locale.getDefault().getLanguage().toString()
                ,0);
        Call<BussinesCatgData> call1 = apiInterface.getBussinesCatg(bussinesCatgReq);
        call1.enqueue(new Callback<BussinesCatgData>() {
            @Override
            public void onResponse(Call<BussinesCatgData> call, retrofit2.Response<BussinesCatgData> response) {
                BussinesCatgData bussinesCatgData = response.body();
                if (bussinesCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                //sharedPref.saveBusinessCatg(bussinesCatgData.result);
                /* BussinesCatgData.Datum obj = new BussinesCatgData.Datum();
                obj.id = 0;
                obj.name = getString(R.string.all_activities);
                //obj.isChecked = sharedPref.getAllBussCatgsStatus();
                list.add(0, obj);*/
                cardViewAdapter = new BusenissCatgsAdapter(getContext(), bussinesCatgData.result,0);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                catgsRv.setLayoutManager(mLayoutManager);
                catgsRv.setItemAnimator(new DefaultItemAnimator());
                cardViewAdapter.setClickListener(AdsSearchFragment.this);
                catgsRv.setAdapter(cardViewAdapter);
            }

            @Override
            public void onFailure(Call<BussinesCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
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

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
}
