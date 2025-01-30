package com.soomter;

import android.app.Activity;
import android.content.Context;
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
 * {@link EventsMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsMainFragment extends Fragment implements View.OnClickListener, PaginationAdapterCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PAGE_START = 0;
    public static int currentPage = PAGE_START;
    public String FromDate, ToDate;
    public int CityId, CategoryId, EventTypeId;
    Locale locale;
    List<?>[] arrOfLists;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
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
    private PaginationAdapter adapter;
    private View progress;
    private ImageView adToolbar;
    private EditText serchEd;
    private ViewTreeObserver.OnScrollChangedListener observer;
    private LockableScrollView lockableScrollView;

    public EventsMainFragment() {
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
    public static EventsMainFragment newInstance(int param1, String param2) {
        EventsMainFragment fragment = new EventsMainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            catgId = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentPage = PAGE_START;
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_events_main, container, false);

        serchEd = (EditText) v.findViewById(R.id.serch_ed);

        progress = v.findViewById(R.id.event_progress);
        progress.setVisibility(View.VISIBLE);

        adToolbar = (ImageView) v.findViewById(R.id.ad_toolbar);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.events_rv);
        mRecyclerView.setNestedScrollingEnabled(false);

       /* RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());*/

        adapter = new PaginationAdapter(getContext(),
                this, new ArrayList<EventsData.Items>());

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        //adToolbar.setTypeface(type);
        serchEd.setTypeface(type);

        fm = getFragmentManager();

        // eventsRequest(serchEd.getText().toString().trim());
     /*   parentFrag = (EventsFragment)getActivity().getSupportFragmentManager()
                .findFragmentByTag("EventsFragment");*/

        v.findViewById(R.id.settings_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().addToBackStack(null)
                        .add(R.id.event_fragment_container, EventsSearchFragmentV2.newInstance(null, null))
                        .hide(EventsMainFragment.this).commit();
            }
        });

        getAdImage();

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    hideKeyboardFrom(getActivity(), serchEd);
                    //    currentPage = 1;
                    eventsRequest(serchEd.getText().toString().trim());
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
                    //   currentPage = 1;
                    hideKeyboardFrom(getActivity(), serchEd);
                    adapter.setFilter(new ArrayList<EventsData.Items>());
                    eventsRequest("");
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*if (EventsFragment.IsFavourite == 1)
            currentPage = -1;*/
        lockableScrollView = (LockableScrollView) mRecyclerView
                .getParent().getParent().getParent().getParent().getParent();

        observer = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
               /* if (VirtualStore.ayHyasWlahi == -1)
                    return;*/
                View view = (View) lockableScrollView.getChildAt(lockableScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (lockableScrollView.getHeight() + lockableScrollView
                        .getScrollY()));

                if (diff == 0) {
                    // your pagination code
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                    // VirtualStore.ayHyasWlahi = -1;
                    lockableScrollView.getViewTreeObserver().removeOnScrollChangedListener(this);

                }
            }
        };
        lockableScrollView.getViewTreeObserver()
                .addOnScrollChangedListener(observer);
    }

    private void getAdImage() {
        Call<TopDownAdsData> call1 = apiInterface.getTopDownAds(2);
        call1.enqueue(new Callback<TopDownAdsData>() {
            @Override
            public void onResponse(Call<TopDownAdsData> call, Response<TopDownAdsData> response) {
                final TopDownAdsData adsData = response.body();
                if (adsData.result == null) {
                    //  Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                adToolbar.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getContext()).load(adsData.result.Image).into(adToolbar);
            }

            @Override
            public void onFailure(Call<TopDownAdsData> call, Throwable t) {

            }
        });
    }

    public void clearData() {
        adapter.setFilter(new ArrayList<EventsData.Items>());
    }

    public void loadNextPage() {
        //if (EventsFragment.IsFavourite == 0) {
        eventsRequest(serchEd.getText().toString().trim());
        //} else {
        //eventsFavRequest(serchEd.getText().toString().trim());
        // }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //int ComapnyId, String lang, String UserId, int CategoryId, int EventTypeId
    //            , int CityId, String FromDate, String ToDate, String SearchText, int PageSize, int PageNumber
    private void eventsRequest(final String SearchText) {
        final EventsRequest eventsRequest = new EventsRequest(catgId,
                Locale.getDefault().getLanguage().toString(), "", CategoryId,
                EventTypeId, CityId, FromDate, ToDate, 10, currentPage, SearchText);
        Call<EventsData> call1 = apiInterface.getEvents(eventsRequest);
        call1.enqueue(new Callback<EventsData>() {
            @Override
            public void onResponse(Call<EventsData> call, Response<EventsData> response) {
                //VirtualStore.ayHyasWlahi = 0;
                final EventsData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                progress.setVisibility(View.GONE);

                adapter.removeLoadingFooter();
                isLoading = false;

                if (SearchText.length() > 0 && currentPage == 1) {
                    adapter.setFilter(adsData.result.Items);
                } else
                    adapter.addAll(adsData.result.Items);

                if (currentPage != TOTAL_PAGES) {
                    if (currentPage == 1 && adsData.result.Items.size() < 10) {
                        isLastPage = true;
                        progress.setVisibility(View.GONE);
                        adapter.removeLoadingFooter();
                        lockableScrollView.getViewTreeObserver().removeOnScrollChangedListener(observer);
                    } else {
                        adapter.addLoadingFooter();
                        lockableScrollView.getViewTreeObserver()
                                .addOnScrollChangedListener(observer);
                    }
                } else isLastPage = true;
            }

            @Override
            public void onFailure(Call<EventsData> call, Throwable t) {
                // VirtualStore.ayHyasWlahi = 0;
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                adapter.showRetry(true, t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });
    }

  /*  private void eventsFavRequest(final String SearchText) {
        final EventsRequest eventsRequest = new EventsRequest(catgId,
                Locale.getDefault().getLanguage().toString(), prefObj.getUser().id, CategoryId,
                EventTypeId, CityId, FromDate, ToDate, 10, currentPage, SearchText, 1);
        Call<EventsData> call1 = apiInterface.getEvents(eventsRequest);
        call1.enqueue(new Callback<EventsData>() {
            @Override
            public void onResponse(Call<EventsData> call, Response<EventsData> response) {
               // VirtualStore.ayHyasWlahi = 0;
                final EventsData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                progress.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                isLoading = false;

                if (SearchText.length() > 0 && currentPage == 1)
                    adapter.setFilter(adsData.result.Items);
                else {
                    adapter.addAll(adsData.result.Items);
                    adapter.notifyDataSetChanged();
                }
                if (currentPage != TOTAL_PAGES) {
                    if (adsData.result.Items.size() < 10) {
                        isLastPage = true;
                        progress.setVisibility(View.GONE);
                        adapter.removeLoadingFooter();
                    } else {
                        adapter.addLoadingFooter();
                        lockableScrollView.getViewTreeObserver()
                                .addOnScrollChangedListener(observer);
                    }
                } else isLastPage = true;

            }

            @Override
            public void onFailure(Call<EventsData> call, Throwable t) {
                //VirtualStore.ayHyasWlahi = 0;
               // Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                adapter.showRetry(true, t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });
    }
*/

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
