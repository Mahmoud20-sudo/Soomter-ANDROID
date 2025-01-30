package com.soomter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.msarhan.ummalqura.calendar.UmmalquraCalendar;

import net.alhazmy13.hijridatepicker.date.gregorian.GregorianDatePickerDialog;
import net.alhazmy13.hijridatepicker.date.hijri.HijriDatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsSearchFragmentV2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsSearchFragmentV2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsSearchFragmentV2 extends Fragment implements View.OnClickListener, CitiesAdapter.ItemClickListener,
        BusenissCatgsAdapter.ItemClickListener,
        GregorianDatePickerDialog.OnDateSetListener, HijriDatePickerDialog.OnDateSetListener, TradeMarksAdapter.ItemClickListener {
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
    private CitiesAdapter citiesAdapter, adsAdapter;
    private RecyclerView catgsRv, eventTypesRv, citiesRv;
    private LinearLayoutManager mLayoutManager;
    private TextView adTxt, catTxt, cityTxt;
    private SubCatgsAdapter subCatgsAdapter;
    private List<BussinesCatgData.Datum> list;
    private List<CitiesData.Datum> citiesList;
    private List<CitiesData.Datum> adsList;
    private Typeface type, typeRoman;
    private AutoCompleteTextView fromDateEd, toDateEd;
    private boolean isFrom;

    public EventsSearchFragmentV2() {
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
    public static EventsSearchFragmentV2 newInstance(String param1, String param2) {
        EventsSearchFragmentV2 fragment = new EventsSearchFragmentV2();
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
        EventsFragment.setView(getString(R.string.advanced_search));
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
                        EventsFragment.setTitle(getString(R.string.events));
                        EventsFragment.showView();
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
        eventTypesRv = (RecyclerView) v.findViewById(R.id.ads_recycler_view);

        adTxt = (TextView) v.findViewById(R.id.ads_txt);
        cityTxt = (TextView) v.findViewById(R.id.cities_txt);
        catTxt = (TextView) v.findViewById(R.id.catgs_txt);

        adTxt.setTypeface(typeRoman);
        cityTxt.setTypeface(typeRoman);
        catTxt.setTypeface(typeRoman);

        adTxt.setText(getString(R.string.the_events));

        RadioGroup dgp = v.findViewById(R.id.rgp);
        RadioButton hijriRp = v.findViewById(R.id.hijri_rb);
        RadioButton georgianRp = v.findViewById(R.id.georgian_rb);
        dgp.setVisibility(View.VISIBLE);
        v.findViewById(R.id.fromdate_ed_prnt).setVisibility(View.VISIBLE);
        v.findViewById(R.id.todate_ed_prnt).setVisibility(View.VISIBLE);

        hijriRp.setTypeface(type);
        georgianRp.setTypeface(type);

        hijriRp.setTextColor(Color.parseColor("#5e5e5f"));
        georgianRp.setTextColor(Color.parseColor("#5e5e5f"));

        fromDateEd = v.findViewById(R.id.fromdate_ed);
        toDateEd = v.findViewById(R.id.todate_ed);

        fromDateEd.setTypeface(typeRoman);
        toDateEd.setTypeface(typeRoman);

        fromDateEd.setFocusable(false);
        toDateEd.setFocusable(false);

        final Button saveBtn = (Button) v.findViewById(R.id.serch_save);

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

        //list = sharedPref.getBusinessCatg();
        citiesList = sharedPref.getCities();

        bussinesCatgReq();
        getEventTypes();

        if (citiesList == null || citiesList.isEmpty())
            citiesReq();
        else {
            CitiesData.Datum obj = new CitiesData.Datum();
            obj.id = 0;
            obj.name = getString(R.string.all_cities);
            obj.isChecked = sharedPref.getAllCitiesStatus();
            citiesList.add(0, obj);
            citiesAdapter = new CitiesAdapter(getContext(), citiesList, 0);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            citiesRv.setLayoutManager(mLayoutManager);
            citiesRv.setItemAnimator(new DefaultItemAnimator());
            citiesAdapter.setClickListener(EventsSearchFragmentV2.this);
            citiesRv.setAdapter(citiesAdapter);
        }

        adsList = new ArrayList<>();
        String[] adsArray = getResources().getStringArray(R.array.ads_list);

        for (int i = 0; i < adsArray.length; i++) {
            CitiesData.Datum obj = new CitiesData.Datum();
            obj.id = i;
            obj.name = adsArray[i];
            obj.isChecked = false;
            adsList.add(obj);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        eventTypesRv.setLayoutManager(mLayoutManager);
        eventTypesRv.setItemAnimator(new DefaultItemAnimator());
        adsAdapter = new CitiesAdapter(getContext(), adsList, 0);
        adsAdapter.setClickListener(EventsSearchFragmentV2.this);
        eventTypesRv.setAdapter(adsAdapter);

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
        eventTypesRv.setNestedScrollingEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventsFragment.resetScroll();
            }
        }, 100);

        dgp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.hijri_rb:
                        setHijriDate();
                        break;
                    case R.id.georgian_rb:
                        setGrorgianDate();
                        break;
                }
            }
        });

        setGrorgianDate();

        return v;
    }

    private void setGrorgianDate() {
        fromDateEd.setText("");
        toDateEd.setText("");
        Calendar now = Calendar.getInstance();
        final GregorianDatePickerDialog fromDp = GregorianDatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        final GregorianDatePickerDialog toDp = GregorianDatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH));

        fromDateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrom = true;
                fromDp.show(getActivity().getFragmentManager(), "");
            }
        });

        toDateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromDateEd.getText().length() == 0) {
                    Toast.makeText(getContext(), getString(R.string.date_validate), Toast.LENGTH_SHORT).show();
                    return;
                }
                isFrom = false;
                toDp.show(getActivity().getFragmentManager(), "");
            }
        });
    }

    private void setHijriDate() {
        fromDateEd.setText("");
        toDateEd.setText("");
        UmmalquraCalendar now = new UmmalquraCalendar();
        final HijriDatePickerDialog fromDp = HijriDatePickerDialog.newInstance(
                this,
                now.get(UmmalquraCalendar.YEAR),
                now.get(UmmalquraCalendar.MONTH),
                now.get(UmmalquraCalendar.DAY_OF_MONTH));

        fromDp.show(getActivity().getFragmentManager(), "HijriDatePickerDialog");

        final HijriDatePickerDialog toDp = HijriDatePickerDialog.newInstance(
                this,
                now.get(UmmalquraCalendar.YEAR),
                now.get(UmmalquraCalendar.MONTH),
                now.get(UmmalquraCalendar.DAY_OF_MONTH));


        fromDateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFrom = true;
                fromDp.show(getActivity().getFragmentManager(), "");
            }
        });

        toDateEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromDateEd.getText().length() == 0) {
                    Toast.makeText(getContext(), getString(R.string.date_validate), Toast.LENGTH_SHORT).show();
                    return;
                }
                isFrom = false;
                toDp.show(getActivity().getFragmentManager(), "");
            }
        });
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
                citiesAdapter = new CitiesAdapter(getContext(), cities.result, 0);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                citiesRv.setLayoutManager(mLayoutManager);
                citiesRv.setItemAnimator(new DefaultItemAnimator());
                citiesAdapter.setClickListener(EventsSearchFragmentV2.this);
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
                , 0);
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
                cardViewAdapter = new BusenissCatgsAdapter(getContext(), bussinesCatgData.result, 0);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                catgsRv.setLayoutManager(mLayoutManager);
                catgsRv.setItemAnimator(new DefaultItemAnimator());
                cardViewAdapter.setClickListener(EventsSearchFragmentV2.this);
                catgsRv.setAdapter(cardViewAdapter);
            }

            @Override
            public void onFailure(Call<BussinesCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void getEventTypes() {
        Call<ProductTradeMarksData> call1 = apiInterface.getEventTyps(Locale.getDefault().getLanguage().toString());
        call1.enqueue(new Callback<ProductTradeMarksData>() {
            @Override
            public void onResponse(Call<ProductTradeMarksData> call, retrofit2.Response<ProductTradeMarksData> response) {
                ProductTradeMarksData objectReposne = response.body();
                if (objectReposne.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                ProductTradeMarksData.Datum obj = new ProductTradeMarksData.Datum();
                obj.id = 0;
                obj.name = getString(R.string.all_trads);
                obj.isChecked = sharedPref.getAllTradsStatus();
                // list.add(0, obj);
                TradeMarksAdapter cardViewAdapter = new TradeMarksAdapter(getContext(), objectReposne.result);
                cardViewAdapter.setClickListener(EventsSearchFragmentV2.this);
                mLayoutManager = new LinearLayoutManager(getContext());
                eventTypesRv.setLayoutManager(mLayoutManager);
                eventTypesRv.setItemAnimator(new DefaultItemAnimator());
                eventTypesRv.setAdapter(cardViewAdapter);
            }

            @Override
            public void onFailure(Call<ProductTradeMarksData> call, Throwable t) {
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

    @Override
    public void onItemClick(int id) {
        //event types click
    }

    @Override
    public void onDateSet(GregorianDatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + monthOfYear + "/" + year;

        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
        Date date3 = null;
        try {
            date3 = sdf.parse(date);
        } catch (Exception e) {

        }
        sdf = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
        String format = sdf.format(date3);
        System.out.print("Result: " + format);
        date = format;

        if (isFrom)
            fromDateEd.setText(date);
        else
            toDateEd.setText(date);
    }

    @Override
    public void onDateSet(HijriDatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + monthOfYear + "/" + year;

        SimpleDateFormat sdf = new SimpleDateFormat("d/MM/yyyy");
        Date date3 = null;
        try {
            date3 = sdf.parse(date);
        } catch (Exception e) {

        }
        sdf = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
        String format = sdf.format(date3);
        System.out.print("Result: " + format);
        date = format;

        if (isFrom)
            fromDateEd.setText(date);
        else
            toDateEd.setText(date);
    }

    private void validateDates() {
        String fromDate = fromDateEd.getText().toString();
        String toDate = toDateEd.getText().toString();

        if (fromDate.length() > 0 && toDate.length() > 0) {


        }
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
