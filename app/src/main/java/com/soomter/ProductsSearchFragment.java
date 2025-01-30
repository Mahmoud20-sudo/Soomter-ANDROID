package com.soomter;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import com.soomter.utils.InputFilterMinMax;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsSearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsSearchFragment extends Fragment implements View.OnClickListener, TradeMarksAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public int tradeMarksId;
    Locale locale;
    List<ProductTradeMarksData.Datum> list = new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private float currentMinVal, currentMaxVal;
    private TradeMarksAdapter cardViewAdapter;
    private RecyclerView tradeRv, typeRv;
    private LinearLayoutManager mLayoutManager;
    private TextView typeTxt, tradeTxt;
    private SubCatgsAdapter subCatgsAdapter;

    public ProductsSearchFragment() {
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
    public static ProductsSearchFragment newInstance(String param1, String param2) {
        ProductsSearchFragment fragment = new ProductsSearchFragment();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        ProductsFragment.setTitle(getString(R.string.product));
                        ProductsFragment.showView();
                        getFragmentManager().popBackStack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProductsFragment.hideView();
        ProductsFragment.setTitle(getString(R.string.advanced_search));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_search, container, false);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        Typeface typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        // get seekbar from view
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) v.findViewById(R.id.rangeSeekbar1);

        final EditText fromEd = v.findViewById(R.id.from_ed);
        final EditText toEd = v.findViewById(R.id.to_ed);

        fromEd.setFilters(new InputFilter[]{new InputFilterMinMax("0", "10000")});
        toEd.setFilters(new InputFilter[]{new InputFilterMinMax("0", "10000")});

        tradeRv = (RecyclerView) v.findViewById(R.id.trade_recycler_view);
        typeRv = (RecyclerView) v.findViewById(R.id.type_recycler_view);
        typeTxt = (TextView) v.findViewById(R.id.type_txt);
        tradeTxt = (TextView) v.findViewById(R.id.trad_txt);

        // get min and max text view
        final TextView tvMin = (TextView) v.findViewById(R.id.textMin);
        final TextView tvMax = (TextView) v.findViewById(R.id.textMax);
        final TextView tvPrice = (TextView) v.findViewById(R.id.product_price_txt);
        final TextView tvRial = (TextView) v.findViewById(R.id.product_rial_txt);
        final TextView tvFrom = (TextView) v.findViewById(R.id.from_txt);
        final TextView tvTo = (TextView) v.findViewById(R.id.to_txt);

        final Button saveBtn = (Button) v.findViewById(R.id.serch_save);

        tvPrice.setTypeface(typeRoman);
        tvRial.setTypeface(typeRoman);
        tvMin.setTypeface(type);
        tvMax.setTypeface(type);
        tvFrom.setTypeface(type);
        tvTo.setTypeface(type);
        typeTxt.setTypeface(typeRoman);
        tradeTxt.setTypeface(typeRoman);
        fromEd.setTypeface(type);
        toEd.setTypeface(type);
        saveBtn.setTypeface(type);

        // set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue) + " " + getString(R.string.rial));
                tvMax.setText(String.valueOf(maxValue) + " " + getString(R.string.rial));
            }
        });

        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });

        currentMaxVal = 8000;
        currentMinVal = 7;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rangeSeekbar.setMinValue(0).setMaxValue(10000).setMinStartValue(currentMinVal)
                        .setMaxStartValue(currentMaxVal).apply();
                ProductsFragment.scrollView.scrollTo(0, 0);
            }
        }, 100);

        fromEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentMinVal = fromEd.getText().toString().length() == 0 ? 0 : Integer.parseInt(fromEd.getText().toString());
                rangeSeekbar.setMinStartValue(currentMinVal)
                        .setMaxStartValue(currentMaxVal).apply();
            }
        });

        toEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                currentMaxVal = toEd.getText().toString().length() == 0 ? 10000 : Integer.parseInt(toEd.getText().toString());
                rangeSeekbar.setMaxStartValue(currentMaxVal)
                        .setMinValue(currentMinVal).apply();
            }
        });

        list = sharedPref.getProductTradeMarks();
        ProductTradeMarksData.Datum obj = new ProductTradeMarksData.Datum();
        obj.id = 0;
        obj.name = getString(R.string.all_trads);
        obj.isChecked = sharedPref.getAllTradsStatus();
        list.add(0, obj);
        cardViewAdapter = new TradeMarksAdapter(getContext(), list);
        cardViewAdapter.setClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        tradeRv.setLayoutManager(mLayoutManager);
        tradeRv.setItemAnimator(new DefaultItemAnimator());
        tradeRv.setAdapter(cardViewAdapter);

       /* mLayoutManager = new LinearLayoutManager(getContext());
        typeRv.setLayoutManager(mLayoutManager);
        typeRv.setItemAnimator(new DefaultItemAnimator());
        typeRv.setAdapter(cardViewAdapter);*/

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*currentMaxVal,
                        currentMinVal,cardViewAdapter.currentCheckedTradeID,
                if(subCatgsAdapter ! = null)
                    subCatgsAdapter.currentCheckedSubCatgID*/
            }
        });

        return v;
    }

    private void getSubCatgs(int catgId) {
        //view.setEnabled(false);
        final ProductSubCatgRequest request = new ProductSubCatgRequest(catgId, Locale.getDefault().getLanguage().toString());
        Call<ProductSubCatgsData> call1 = apiInterface.getProductsSubCatgs(request);
        call1.enqueue(new Callback<ProductSubCatgsData>() {
            @Override
            public void onResponse(Call<ProductSubCatgsData> call, retrofit2.Response<ProductSubCatgsData> response) {
                ProductSubCatgsData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }

                if (!resp.result.isEmpty()) {
                    typeTxt.setVisibility(View.VISIBLE);
                    typeRv.setVisibility(View.VISIBLE);
                } else {
                    typeTxt.setVisibility(View.GONE);
                    typeRv.setVisibility(View.GONE);
                }

                ProductSubCatgsData.Datum obj = new ProductSubCatgsData.Datum();
                obj.id = 0;
                obj.name = getString(R.string.all_types);
                resp.result.add(0, obj);
                subCatgsAdapter = new SubCatgsAdapter(getContext(),
                        resp.result);
                mLayoutManager = new LinearLayoutManager(getContext());
                typeRv.setLayoutManager(mLayoutManager);
                typeRv.setItemAnimator(new DefaultItemAnimator());
                typeRv.setAdapter(subCatgsAdapter);
            }

            @Override
            public void onFailure(Call<ProductSubCatgsData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
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
    public void onItemClick(int tradeMarksId) {
        getSubCatgs(tradeMarksId);
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
