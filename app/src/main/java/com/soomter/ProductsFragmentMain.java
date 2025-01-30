package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.soomter.utils.SpacesItemDecoration2;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragmentMain.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragmentMain#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragmentMain extends Fragment implements View.OnClickListener, BestSellerAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    public int isFavourite = 0;
    Locale locale;
    String SearchText = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private RecyclerView mRecyclerView;
    private ProgressBar progress;
    private List<BestSellingData.Datum> list;
    private EditText searchED;
    private BestSellerAdapter bestSellerAdapter;
    private int productTo = 1;

    public ProductsFragmentMain() {
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
    public static ProductsFragmentMain newInstance(String param1, String param2, String param3) {
        ProductsFragmentMain fragment = new ProductsFragmentMain();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            Gson gson = new Gson();
            Type listOfdoctorType = new TypeToken<List<BestSellingData.Datum>>() {
            }.getType();
            list = gson.fromJson(mParam1, listOfdoctorType);

            mParam2 = getArguments().getString(ARG_PARAM2);
            SearchText = getArguments().getString(ARG_PARAM3);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products_main2, container, false);

        searchED = (EditText) v.findViewById(R.id.serch_ed);
        searchED.setText(SearchText);

        progress = v.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);

        final Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        searchED.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        mRecyclerView = v.findViewById(R.id.products_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.addItemDecoration(new
                SpacesItemDecoration2(getResources().getDimensionPixelSize(R.dimen.hspacing)));

        getBestCatgs();

        UserData.Datum user = sharedPref.getUser();
        if (user == null || !user.userType.equalsIgnoreCase("Company")) {
            productTo = 1;
        } else
            productTo = 2;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ProductsFragment.scrollView.scrollTo(0, 0);
            }
        }, 100);

        searchED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    EventsMainFragment.hideKeyboardFrom(getActivity(), searchED);
                    SearchText = searchED.getText().toString();
                    searchProducts();
                    return true;
                } else {
                    return false;
                }
            }
        });

        searchED.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null && s.toString().trim().equals("") == false) {
                } else {
                    new BestSellerAdapter(getContext(), list);
                    mRecyclerView.setAdapter(bestSellerAdapter);
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProductsFragment.hideView();
        ProductsFragment.setTitle(getString(R.string.products));
    }

    private void searchProducts() {//String lang, String Title, int CategoryId, int ProductTo, int MinPrice, int MaxPrice
        final ProdsSearchRequest request = new ProdsSearchRequest(Locale.getDefault().getLanguage().toString(),
                SearchText
                , 0, productTo, 0, 0, sharedPref.getUser() != null ?
                sharedPref.getUser().id : "", 0, "");
        Call<BestSellingData> call1 = apiInterface.searchProducts(request);
        call1.enqueue(new Callback<BestSellingData>() {
            @Override
            public void onResponse(Call<BestSellingData> call, retrofit2.Response<BestSellingData> response) {
                BestSellingData resp = response.body();
                if (resp.result == null || resp.result.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                    return;
                }
                bestSellerAdapter = new BestSellerAdapter(getContext(), resp.result);
                bestSellerAdapter.setClickListener(ProductsFragmentMain.this);
                mRecyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                mRecyclerView.setAdapter(bestSellerAdapter);
            }

            @Override
            public void onFailure(Call<BestSellingData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getFavProducts() {//String lang, String Title, int CategoryId, int ProductTo, int MinPrice, int MaxPrice
        final ProdsSearchRequest request = new ProdsSearchRequest(Locale.getDefault().getLanguage().toString(),
                searchED.getText().toString().trim()
                , 0, productTo, 0, 0
                , sharedPref.getUser().id, 1, "");


        Call<BestSellingData> call1 = apiInterface.searchProducts(request);
        call1.enqueue(new Callback<BestSellingData>() {
            @Override
            public void onResponse(Call<BestSellingData> call, retrofit2.Response<BestSellingData> response) {
                BestSellingData resp = response.body();
                if (resp.result == null || resp.result.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.no_result), Toast.LENGTH_LONG).show();
                    return;
                }
                bestSellerAdapter = new BestSellerAdapter(getContext(), resp.result);
                bestSellerAdapter.setClickListener(ProductsFragmentMain.this);
                mRecyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                mRecyclerView.setAdapter(bestSellerAdapter);
            }

            @Override
            public void onFailure(Call<BestSellingData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    public void getBestCatgs() {
        if (list == null && SearchText.length() == 0) {
            getFavProducts();
            return;
        }
        if(SearchText.length() > 0){
            searchProducts();
            return;
        }
        bestSellerAdapter = new BestSellerAdapter(getContext(), list);
        bestSellerAdapter.setClickListener(this);
        mRecyclerView.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        mRecyclerView.setAdapter(bestSellerAdapter);
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

        getFragmentManager().beginTransaction().addToBackStack(null).hide(this)
                .add(R.id.products_fragment_container, ProductDetailsFragment
                        .newInstance(ProductsFragmentMain.this, new Gson().toJson(list
                                .get(position)), position)).commit();
    }

    public void updateProductList(int position, BestSellingData.Datum obj) {
        list.set(position, obj);
        bestSellerAdapter.notifyDataSetChanged();
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
