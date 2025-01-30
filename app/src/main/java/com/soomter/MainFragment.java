package com.soomter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView searchSrcText, adsTxt, prodctsTxt, catTxt, aucTxt, tendTxt, eventsTxt;
    private Button serchTextView;
    private EditText serchEd;

    private OnFragmentInteractionListener mListener;
    private NestedScrollView scrollView;
    private SharedPref prefObj;
    private APIInterface apiInterface;


    public MainFragment() {
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
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        }
        else
        {
            ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).bottomNavigationViewBehavior.slideUp(((MainActivity)getActivity()).bottomNavigationView);
        final View v = inflater.inflate(R.layout.fragment_main, container, false);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        Typeface typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        searchSrcText = (TextView) v.findViewById(R.id.search_src_text);
        TextView serchTextView = (TextView) v.findViewById(R.id.serch_txt);
        searchSrcText.setTypeface(type);
        serchTextView.setTypeface(type);

        serchEd = (EditText) v.findViewById(R.id.serch_ed);
        serchEd.setTypeface(type);

//        v.findViewById(R.id.shadow_view).bringToFront();
        catTxt = (TextView) v.findViewById(R.id.cat_txt);
        catTxt.bringToFront();
        catTxt.setTypeface(type);
        prodctsTxt = (TextView) v.findViewById(R.id.prod_txt);
        prodctsTxt.bringToFront();
        prodctsTxt.setTypeface(type);
        adsTxt = (TextView) v.findViewById(R.id.ads_txt);
        adsTxt.bringToFront();
        adsTxt.setTypeface(type);

        aucTxt = (TextView) v.findViewById(R.id.auctions_txt);
        aucTxt.bringToFront();
        aucTxt.setTypeface(type);
        tendTxt = (TextView) v.findViewById(R.id.tenders_txt);
        tendTxt.bringToFront();
        tendTxt.setTypeface(type);
        eventsTxt = (TextView) v.findViewById(R.id.events_txt);
        eventsTxt.bringToFront();
        eventsTxt.setTypeface(type);

        v.findViewById(R.id.cat_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager()
                        .beginTransaction().replace(R.id.frag_container, CategoriesFragment.newInstance(null, null))
                        .addToBackStack(null).commit();
            }
        });

        v.findViewById(R.id.ads_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (APIClient.isInternetAvailable(getActivity()) == false) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    return;
                }

                getFragmentManager()
                        .beginTransaction().replace(R.id.frag_container, AdsFragment.newInstance(null, null))
                        .addToBackStack(null).commit();
            }
        });

        v.findViewById(R.id.events_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (APIClient.isInternetAvailable(getActivity()) == false) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    return;
                }

                getFragmentManager()
                        .beginTransaction().replace(R.id.frag_container, EventsFragment.newInstance(null, null) , "EventsFragment")
                        .addToBackStack(null).commit();
            }
        });

        v.findViewById(R.id.products_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (APIClient.isInternetAvailable(getActivity()) == false) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    return;
                }
                getFragmentManager()
                        .beginTransaction().replace(R.id.frag_container,
                        ProductsFragment.newInstance(null, null))
                        .addToBackStack(null).commit();
            }
        });

        scrollView = v.findViewById(R.id.scrollView2);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        prefObj = new SharedPref(getContext());

        if (prefObj.getCities() == null)
            citiesReq();
        if (prefObj.getPortalCatg() == null)
            getPortalCategories();
        /*if (prefObj.getBusinessCatg() == null) {
            bussinesCatgReq();
        }
        */if (prefObj.getProductsCatg() == null) {
            getProductsCategories();
        }
        if (prefObj.getProductTradeMarks() == null) {
            getProductTradeMarks();
        }
        if (prefObj.getTree() == null) {
            getProductCatgsTree();
        }

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    hideKeyboardFrom(getActivity(), serchEd);
                    //    currentPage = 1;
                    getFragmentManager()
                            .beginTransaction().add(R.id.frag_container,
                            //categID, 0, list.get(position).id) , mParam2
                            CompaniesFragment.newInstance(0, 0, 0,getString(R.string.serch_result) ,
                                    serchEd.getText().toString().trim()))
                            .hide(MainFragment.this).addToBackStack(null).commit();
                    serchEd.setText("");
                    return true;
                } else {
                    return false;
                }
            }
        });

        serchEd.addTextChangedListener(new TextWatcher() {

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

                }
            }
        });

        return v;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*private void bussinesCatgReq() {
        //view.setEnabled(false);
        final BussinessCatgsRequest bussinesCatgReq = new BussinessCatgsRequest(Locale.getDefault().getLanguage().toString());
        Call<BussinesCatgData> call1 = apiInterface.getBussinesCatg(bussinesCatgReq);
        call1.enqueue(new Callback<BussinesCatgData>() {
            @Override
            public void onResponse(Call<BussinesCatgData> call, retrofit2.Response<BussinesCatgData> response) {
                BussinesCatgData bussinesCatgData = response.body();
                if (bussinesCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveBusinessCatg(bussinesCatgData.result);
            }

            @Override
            public void onFailure(Call<BussinesCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }*/

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
                prefObj.saveCities(cities.result);
            }

            @Override
            public void onFailure(Call<CitiesData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    //GetProductCategoriesTree.

    private void getProductCatgsTree() {
        Call<ProductCatgsTreeData> call1 = apiInterface.getProductCatgsTree(Locale.getDefault().toString());
        call1.enqueue(new Callback<ProductCatgsTreeData>() {
            @Override
            public void onResponse(Call<ProductCatgsTreeData> call, retrofit2.Response<ProductCatgsTreeData> response) {
                ProductCatgsTreeData productSubCatgsData = response.body();
                if (productSubCatgsData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveProductCatgsTree(productSubCatgsData.result);
            }

            @Override
            public void onFailure(Call<ProductCatgsTreeData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void getProductSubCatgs() {
        //view.setEnabled(false);
        final ProductSubCatgRequest productSubCatgRequest
                = new ProductSubCatgRequest(2, Locale.getDefault().getLanguage().toString());
        Call<ProductSubCatgsData> call1 = apiInterface.getProductsSubCatgs(productSubCatgRequest);
        call1.enqueue(new Callback<ProductSubCatgsData>() {
            @Override
            public void onResponse(Call<ProductSubCatgsData> call, retrofit2.Response<ProductSubCatgsData> response) {
                ProductSubCatgsData productSubCatgsData = response.body();
                if (productSubCatgsData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveProductSubCatgs(productSubCatgsData.result);
            }

            @Override
            public void onFailure(Call<ProductSubCatgsData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void getProductTradeMarks() {
        //view.setEnabled(false);
        final PortalCatgRequest portalCatgRequest = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<ProductTradeMarksData> call1 = apiInterface.getProductTrademarks(portalCatgRequest);
        call1.enqueue(new Callback<ProductTradeMarksData>() {
            @Override
            public void onResponse(Call<ProductTradeMarksData> call, retrofit2.Response<ProductTradeMarksData> response) {
                ProductTradeMarksData productTradeMarksData = response.body();
                if (productTradeMarksData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveTradeMarks(productTradeMarksData.result);
            }

            @Override
            public void onFailure(Call<ProductTradeMarksData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void getPortalCategories() {
        //view.setEnabled(false);
        final PortalCatgRequest cities = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<PortalCatgData> call1 = apiInterface.getPortalCatg(cities);
        call1.enqueue(new Callback<PortalCatgData>() {
            @Override
            public void onResponse(Call<PortalCatgData> call, retrofit2.Response<PortalCatgData> response) {
                PortalCatgData portalCatgData = response.body();
                if (portalCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.savePortalCatg(portalCatgData.result);
            }

            @Override
            public void onFailure(Call<PortalCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void getProductsCategories() {
        //view.setEnabled(false);
        final PortalCatgRequest catgs = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<ProductsCatgData> call1 = apiInterface.getProductsCatgs(catgs);
        call1.enqueue(new Callback<ProductsCatgData>() {
            @Override
            public void onResponse(Call<ProductsCatgData> call, retrofit2.Response<ProductsCatgData> response) {
                ProductsCatgData catgData = response.body();
                if (catgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveProductsCatg(catgData.result);
            }

            @Override
            public void onFailure(Call<ProductsCatgData> call, Throwable t) {
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
        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().addToBackStack("login")
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