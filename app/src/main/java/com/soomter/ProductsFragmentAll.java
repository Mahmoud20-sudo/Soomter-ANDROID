package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import com.soomter.utils.HozriontalSpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragmentAll.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragmentAll#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragmentAll extends Fragment implements View.OnClickListener, BestSellerAdapter.ItemClickListener
        , BestTradesAdapter.BestTradesItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Locale locale;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private RecyclerView recyclerView, tradesRv, bestCatgsRv;
    private ProgressBar progress, tradesProgress, moreProgress;
    private TextView bestPickedShow, vogueShow, exploreMoreShow;
    private List<BestSellingData.Datum> bestSellerList;
    private BestSellerAdapter bestSellerAdapter;
    private EditText searchED;
    private int productTo = 1;
    public int isFavourite = 0;

    public ProductsFragmentAll() {
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
    public static ProductsFragmentAll newInstance(String param1, String param2) {
        ProductsFragmentAll fragment = new ProductsFragmentAll();
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

    public void updateViews() {
        ProductsFragment.showView();
        ProductsFragment.setTitle(getString(R.string.product));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products_all, container, false);

        TextView bestPicked = (TextView) v.findViewById(R.id.best_seller_txt);
        bestPickedShow = (TextView) v.findViewById(R.id.best_seller_txt_show);
        TextView vogueTxt = (TextView) v.findViewById(R.id.special_trades_txt);
        vogueShow = (TextView) v.findViewById(R.id.special_trades_txt_show);
        TextView exploreMoreTxt = (TextView) v.findViewById(R.id.explore_more_txt);
        exploreMoreShow = (TextView) v.findViewById(R.id.explore_more_txt_show);

         searchED = (EditText) v.findViewById(R.id.serch_ed);
        progress = v.findViewById(R.id.progress);
        tradesProgress = v.findViewById(R.id.trads_progress);
        moreProgress = v.findViewById(R.id.more_progress);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        bestPicked.setTypeface(type);
        bestPickedShow.setTypeface(type);
        vogueTxt.setTypeface(type);
        vogueShow.setTypeface(type);
        exploreMoreTxt.setTypeface(type);
        exploreMoreShow.setTypeface(type);
        searchED.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.hspacing);

        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView = v.findViewById(R.id.best_seller_rv);
        tradesRv = v.findViewById(R.id.trads_rv);
        bestCatgsRv = v.findViewById(R.id.more_rv);

        tradesRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        tradesRv.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing_2dp)));

        bestCatgsRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        bestCatgsRv.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing_2dp)));

        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.addItemDecoration(new HozriontalSpacesItemDecoration(spacingInPixels));

        getBestSellingProducts();
        getBestTradeMarks();
        getBestCatgs();

        UserData.Datum user = sharedPref.getUser();

        if (user == null || !user.userType.equalsIgnoreCase("Company")){
            productTo = 1;
        }
        else
            productTo = 2;

        v.findViewById(R.id.settings_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.products_fragment_container,
                                ProductsSearchFragment.newInstance(null, null))
                        .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
            }
        });

        searchED.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    EventsMainFragment.hideKeyboardFrom(getActivity(), searchED);
                    //searchProducts();
                    getFragmentManager().beginTransaction()
                            .add(R.id.products_fragment_container,
                                    ProductsFragmentMain.newInstance(null, null , searchED.getText().toString()))
                            .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    searchED.setText("");
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
                    getBestSellingProducts();
                }
            }
        });


        ProductsFragment.productFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPref.getUser() == null) {
                    Toast.makeText(getContext(), getString(R.string.require_login), Toast.LENGTH_LONG).show();
                    return;
                }

                getFragmentManager().beginTransaction()
                        .add(R.id.products_fragment_container,
                                ProductsFragmentMain.newInstance(null, null , ""))
                        .addToBackStack(null).hide(ProductsFragmentAll.this).commit();

            }

        });

        return v;
    }

    private void searchProducts(String tradeId) {//String lang, String Title, int CategoryId, int ProductTo, int MinPrice, int MaxPrice
        final ProdsSearchRequest request = new ProdsSearchRequest(Locale.getDefault().getLanguage().toString() ,
                                                                        ""
                                                                    , 0 , -1 , 0, 0
                                                                    ,"", isFavourite ,tradeId);


        Call<BestSellingData> call1 = apiInterface.searchProducts(request);
        call1.enqueue(new Callback<BestSellingData>() {
            @Override
            public void onResponse(Call<BestSellingData> call, retrofit2.Response<BestSellingData> response) {
                BestSellingData resp = response.body();
                if (resp.result == null || resp.result.isEmpty()) {
                    Toast.makeText(getActivity(),getString(R.string.no_result),Toast.LENGTH_LONG).show();
                    return;
                }
                bestSellerAdapter = new BestSellerAdapter(getContext(), resp.result);
                bestSellerAdapter.setClickListener(ProductsFragmentAll.this);
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                recyclerView.setAdapter(bestSellerAdapter);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(resp.result);

                bestSellerList = resp.result;

                bestPickedShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.products_fragment_container,
                                        ProductsFragmentMain.newInstance(jsonString, null , ""))
                                .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<BestSellingData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    /*public void getFavProducts() {//String lang, String Title, int CategoryId, int ProductTo, int MinPrice, int MaxPrice
        final ProdsSearchRequest request = new ProdsSearchRequest(Locale.getDefault().getLanguage().toString() ,
                searchED.getText().toString().trim()
                , 0 , productTo , 0, 0
                ,sharedPref.getUser().id , isFavourite ,"");


        Call<BestSellingData> call1 = apiInterface.searchProducts(request);
        call1.enqueue(new Callback<BestSellingData>() {
            @Override
            public void onResponse(Call<BestSellingData> call, retrofit2.Response<BestSellingData> response) {
                BestSellingData resp = response.body();
                if (resp.result == null || resp.result.isEmpty()) {
                    Toast.makeText(getActivity(),getString(R.string.no_result),Toast.LENGTH_LONG).show();
                    return;
                }
                bestSellerAdapter = new BestSellerAdapter(getContext(), resp.result);
                bestSellerAdapter.setClickListener(ProductsFragmentAll.this);
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                recyclerView.setAdapter(bestSellerAdapter);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(resp.result);

                bestSellerList = resp.result;

                bestPickedShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.products_fragment_container,
                                        ProductsFragmentMain.newInstance(jsonString, null))
                                .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<BestSellingData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }*/

    public void getBestCatgs() {
        final PortalCatgRequest request = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<ProductsCatgData> call1 = apiInterface.getBestCategories(request);
        call1.enqueue(new Callback<ProductsCatgData>() {
            @Override
            public void onResponse(Call<ProductsCatgData> call, retrofit2.Response<ProductsCatgData> response) {
                ProductsCatgData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                BestCatgsAdapter adapter = new BestCatgsAdapter(getContext(), resp.result.subList(0, 3));
                //adapter.setClickListener(ProductsFragmentAll.this);
                bestCatgsRv.setVisibility(View.VISIBLE);
                moreProgress.setVisibility(View.GONE);
                bestCatgsRv.setAdapter(adapter);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(resp.result);

                exploreMoreShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.products_fragment_container,
                                        MoreTradesFragment.newInstance(jsonString, null))
                                .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductsCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void getBestTradeMarks() {
        final PortalCatgRequest request = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<ProductsCatgData> call1 = apiInterface.getBestTradeMarks(request);
        call1.enqueue(new Callback<ProductsCatgData>() {
            @Override
            public void onResponse(Call<ProductsCatgData> call, retrofit2.Response<ProductsCatgData> response) {
                ProductsCatgData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                BestTradesAdapter adapter = new BestTradesAdapter(getContext(), resp.result);
                adapter.setClickListener(ProductsFragmentAll.this);
                tradesRv.setVisibility(View.VISIBLE);
                tradesProgress.setVisibility(View.GONE);
                tradesRv.setAdapter(adapter);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(resp.result);

                vogueShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.products_fragment_container,
                                        SpecialTradesFragment.newInstance(jsonString, null))
                                .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    }
                });

            }

            @Override
            public void onFailure(Call<ProductsCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void getBestSellingProducts() {
        final BestSellingProdsRequest request = new BestSellingProdsRequest(Locale.getDefault().getLanguage().toString(),
                30);
        Call<BestSellingData> call1 = apiInterface.getBestSellProds(request);
        call1.enqueue(new Callback<BestSellingData>() {
            @Override
            public void onResponse(Call<BestSellingData> call, retrofit2.Response<BestSellingData> response) {
                BestSellingData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                 bestSellerAdapter = new BestSellerAdapter(getContext(), resp.result);
                bestSellerAdapter.setClickListener(ProductsFragmentAll.this);
                recyclerView.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                recyclerView.setAdapter(bestSellerAdapter);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(resp.result);

                bestSellerList = resp.result;

                bestPickedShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .add(R.id.products_fragment_container,
                                        ProductsFragmentMain.newInstance(jsonString, null , "") , "ProductsFragmentMain")
                                .addToBackStack(null).hide(ProductsFragmentAll.this).commit();
                    }
                });

            }

            @Override
            public void onFailure(Call<BestSellingData> call, Throwable t) {
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

    public void updateProductList(int position ,BestSellingData.Datum obj){
        bestSellerList.set(position ,obj);
        bestSellerAdapter.notifyDataSetChanged();
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
                        .newInstance(ProductsFragmentAll.this, new Gson().toJson(bestSellerList
                                .get(position)), position)).commit();
    }

    @Override
    public void onBestTradesItemClick(int id) {
        searchProducts(id+"");
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
