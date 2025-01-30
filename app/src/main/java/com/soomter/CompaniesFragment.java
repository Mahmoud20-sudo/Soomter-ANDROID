package com.soomter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soomter.utils.PaginationAdapterCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompaniesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompaniesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompaniesFragment extends Fragment implements View.OnClickListener, PaginationAdapterCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int PAGE_START = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    public static int currentPage = PAGE_START;
    public String FromDate, ToDate;
    public int CityId, CategoryId, EventTypeId;
    public int categoryId, subCategoryId, cityId;
    List<?>[] arrOfLists;
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;
    Locale locale;
    ProgressBar determinateBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SharedPref prefObj;
    private LinearLayout navDrawer;
    private Typeface type;
    private LinearLayout cont;
    private Typeface typeRoman;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int catgId;
    private RecyclerView mRecyclerView;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 500;
    private LinearLayoutManager linearLayoutManager;
    private CompaniesAdapterV2 adapter;
    private EditText serchEd;
    private ViewTreeObserver.OnScrollChangedListener observer;
    private CompaniesFragment.OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private CompaniesAdapterV2 companiesAdapter;
    private String title;
    private List<CompaniesData.Items> arrayList;
    private String SearchText = "";

    public CompaniesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CountriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CompaniesFragment newInstance(int categoryId, int cityId, int subCategoryId, String title , String searchText) {
        CompaniesFragment fragment = new CompaniesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, categoryId);
        args.putInt(ARG_PARAM2, cityId);
        args.putInt(ARG_PARAM3, subCategoryId);
        args.putString(ARG_PARAM4, title);
        args.putString(ARG_PARAM5, searchText);
        fragment.setArguments(args);
        return fragment;
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getInt(ARG_PARAM1);
            cityId = getArguments().getInt(ARG_PARAM2);
            subCategoryId = getArguments().getInt(ARG_PARAM3);
            title = getArguments().getString(ARG_PARAM4);
            SearchText = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        currentPage = PAGE_START;
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_companies, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.comps_recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);

       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());*/

        adapter = new CompaniesAdapterV2(getContext(),
                this, new ArrayList<CompaniesData.Items>());

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);

        TextView titleTv = (TextView) v.findViewById(R.id.title);
        serchEd = (EditText) v.findViewById(R.id.serch_ed);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        if (title != null)
            titleTv.setText(title);
        serchEd.setText(SearchText);

        titleTv.setTypeface(type);
        serchEd.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();
        v.findViewById(R.id.show_cont).setVisibility(View.VISIBLE);
        determinateBar = v.findViewById(R.id.determinateBar);

        determinateBar.getIndeterminateDrawable().
                setColorFilter(Color.parseColor("#D9B878"), android.graphics.PorterDuff.Mode.MULTIPLY);

        mRecyclerView.setPadding(0, 20, 0, 0);

        // companiesRequest();

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

        showByCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        replace(R.id.frag_container,
                                BusinessCatgsFragment.newInstance(categoryId, title,
                                        CompaniesFragment.this)).addToBackStack(null).commit();
            }
        });

        showByCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().
                        replace(R.id.frag_container,
                                CitiesFragmentV2.newInstance(categoryId, title,
                                        CompaniesFragment.this)).addToBackStack(null).commit();
            }
        });

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    //Perform your Actions here.
                    hideKeyboardFrom(getActivity(), serchEd);
                    currentPage = 1;
                    SearchText = serchEd.getText().toString().trim();
                    companiesSearchRequest();
                    handled = true;
                }
                return handled;
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
                    currentPage = 1;
                    SearchText = "";
                    adapter.setFilter(new ArrayList<CompaniesData.Items>());
                    hideKeyboardFrom(getActivity(), serchEd);
                    companiesRequest();
                }
            }
        });
        loadNextPage();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (EventsFragment.IsFavourite == 1)
            currentPage = -1;*/
        /*lockableScrollView = (LockableScrollView) mRecyclerView
                .getParent().getParent().getParent().getParent();*/

        observer = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
               /* if (VirtualStore.ayHyasWlahi == -1)
                    return;*/
                View view = (View) mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);

                if (view == null) {
                    return;
                }

                int diff = (view.getBottom() - (mRecyclerView.getHeight() + mRecyclerView
                        .getScrollY()));

                if (diff == 0) {
                    // your pagination code
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                    // VirtualStore.ayHyasWlahi = -1;
                    mRecyclerView.getViewTreeObserver().removeOnScrollChangedListener(this);

                }
            }
        };
        mRecyclerView.getViewTreeObserver()
                .addOnScrollChangedListener(observer);
    }

    public void clearData() {
        adapter.setFilter(new ArrayList<CompaniesData.Items>());
    }

    public void loadNextPage() {
        if (SearchText.length() == 0) {
            companiesRequest();
        } else {
            companiesSearchRequest();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //int ComapnyId, String lang, String UserId, int CategoryId, int EventTypeId
    //            , int CityId, String FromDate, String ToDate, String SearchText, int PageSize, int PageNumber
    private void companiesRequest() {
        final CompaniesRequest companiesRequest = new CompaniesRequest(
                Locale.getDefault().getLanguage().toString(), 0, categoryId, subCategoryId,
                cityId, 0, 0, 10, currentPage);
        Call<CompaniesData> call1 = apiInterface.getCompanies(companiesRequest);
        call1.enqueue(new Callback<CompaniesData>() {
            @Override
            public void onResponse(Call<CompaniesData> call, Response<CompaniesData> response) {
                //VirtualStore.ayHyasWlahi = 0;
                determinateBar.setVisibility(View.GONE);
                final CompaniesData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                determinateBar.setVisibility(View.GONE);

                adapter.removeLoadingFooter();
                isLoading = false;

                adapter.addAll(adsData.result.Items);

                if (currentPage != TOTAL_PAGES) {
                    if (currentPage == 1 && adsData.result.Items.size() < 10) {
                        isLastPage = true;
                        determinateBar.setVisibility(View.GONE);
                        adapter.removeLoadingFooter();
                        mRecyclerView.getViewTreeObserver().removeOnScrollChangedListener(observer);
                    } else {
                        adapter.addLoadingFooter();
                        mRecyclerView.getViewTreeObserver()
                                .addOnScrollChangedListener(observer);
                    }
                } else isLastPage = true;
            }

            @Override
            public void onFailure(Call<CompaniesData> call, Throwable t) {
                // VirtualStore.ayHyasWlahi = 0;
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                adapter.showRetry(true, t.getMessage());
                determinateBar.setVisibility(View.GONE);
            }
        });
    }

    private void companiesSearchRequest() {
        final CompaniesSearchRequest companiesRequest = new CompaniesSearchRequest(
                Locale.getDefault().getLanguage().toString(), 0, categoryId, subCategoryId,
                cityId, 1, 0, 10, currentPage, SearchText);
        Call<CompaniesData> call1 = apiInterface.searchCompanies(companiesRequest);
        call1.enqueue(new Callback<CompaniesData>() {
            @Override
            public void onResponse(Call<CompaniesData> call, Response<CompaniesData> response) {
                //VirtualStore.ayHyasWlahi = 0;
                determinateBar.setVisibility(View.GONE);
                final CompaniesData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                if (adsData.result.Items.size() == 0) {
                    return;
                }
                determinateBar.setVisibility(View.GONE);

                adapter.removeLoadingFooter();
                isLoading = false;

                if (SearchText.length() > 0 && currentPage == 1)
                    adapter.setFilter(adsData.result.Items);
                else {
                    adapter.addAll(adsData.result.Items);
                }

                if (currentPage != TOTAL_PAGES) {
                    if (currentPage == 1 && adsData.result.Items.size() < 10) {
                        isLastPage = true;
                        determinateBar.setVisibility(View.GONE);
                        adapter.removeLoadingFooter();
                        mRecyclerView.getViewTreeObserver().removeOnScrollChangedListener(observer);
                    } else {
                        adapter.addLoadingFooter();
                        mRecyclerView.getViewTreeObserver()
                                .addOnScrollChangedListener(observer);
                    }
                } else isLastPage = true;
            }

            @Override
            public void onFailure(Call<CompaniesData> call, Throwable t) {
                // VirtualStore.ayHyasWlahi = 0;
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                adapter.showRetry(true, t.getMessage());
                determinateBar.setVisibility(View.GONE);
            }
        });
    }

    private void setAdsData(View v, int id, final List<AdsData.ImagesAds> adsList) {
        cont = (LinearLayout) v.findViewById(id);
        for (int i = 0; i < adsList.size(); i++) {
            final AdsData.ImagesAds data = adsList.get(i);
            View view = LayoutInflater.from(getContext()).inflate(R.layout.ad_item, null);
            cont.addView(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ft = fm.beginTransaction();
                    AdsDetailsFragment fragOne = AdsDetailsFragment.newInstance(data.id, data.Type, data.Title);
                    ft.add(R.id.ads_fragment_container, fragOne);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });

            TextView adDesc = view.findViewById(R.id.ad_desc);
            adDesc.setText(adsList.get(i).Title);
            adDesc.setTypeface(typeRoman);
            ImageView adImg = view.findViewById(R.id.ad_img);
            ImageView playIcon = view.findViewById(R.id.play_icon);
            adImg.setVisibility(View.VISIBLE);

            if (adsList.get(i).Type == 3) {
                playIcon.setVisibility(View.VISIBLE);
                playIcon.bringToFront();
            }

            /*Picasso.get().load(adsList.get(i).Image)
                    .into(adImg);*/

            Glide.with(getContext()).load(adsList.get(i).Image)
                    .into(adImg);

            if (i == 1)
                setMargins(view, 15, 10, 15, 10);
            else if (i == 0)
                setMargins(view, 0, 10, 5, 10);
            else
                setMargins(view, 5, 10, 0, 10);
        }

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
        /*getFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.ads_fragment_container,
                        AdPagerMainFagment.newInstance(Integer.parseInt(view.getTag().toString()), null,
                                arrOfLists)).hide(this).addToBackStack(null).commit();*/
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
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
