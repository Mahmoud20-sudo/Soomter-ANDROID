package com.soomter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static LockableScrollView scrollView;
    private static TextView titleTv;
    private static View topMenu, topBar;
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;
    Locale locale;
    AdsMainFragment fragOne;
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
    private FragmentTransaction ft;
    private int animeValue;

    public AdsFragment() {
        // Required empty public constructor
    }

    public static void resetScroll() {
        scrollView.scrollTo(0, 0);
    }

    public static void setTitle(String title) {
        titleTv.setText(title);
    }

    public static void setView(String title) {
        titleTv.setText(title);
        titleTv.setTextColor(Color.parseColor("#323232"));
        topMenu.setVisibility(View.GONE);
        topBar.setBackgroundColor(Color.WHITE);
    }

    public static void showView() {
        titleTv.setTextColor(Color.parseColor("#f4f4f4"));
        topMenu.setVisibility(View.VISIBLE);
        topBar.setBackgroundColor(Color.BLACK);
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
    public static AdsFragment newInstance(String param1, String param2) {
        AdsFragment fragment = new AdsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.GONE);

        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_ads, container, false);

        titleTv = (TextView) v.findViewById(R.id.ads_title);
        TextView allAds = (TextView) v.findViewById(R.id.all_ads_txt);
        TextView addAd = (TextView) v.findViewById(R.id.add_ad_txt);

        topBar = v.findViewById(R.id.topbar_view);
        topMenu = v.findViewById(R.id.topmenu_view);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        titleTv.setTypeface(type);
        allAds.setTypeface(type);
        addAd.setTypeface(type);

        FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();

        fragOne = new AdsMainFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean("shouldYouCreateAChildFragment", true);
        fragOne.setArguments(arguments);
        ft.add(R.id.ads_fragment_container, fragOne , "AdsMainFragment");
        ft.commit();

        navDrawer = v.findViewById(R.id.ads_nav_drawer);//.animate().translationX(-500);
        navDrawer.bringToFront();

        animeValue = Locale.getDefault().toString().equalsIgnoreCase("ar") ? 1200 : -1200;

        navDrawer.animate().setDuration(0).translationX(animeValue);

        setNavData(new int[]{0, 0, 0, 0, 0});

        adsRequest();

        scrollView = (LockableScrollView) v.findViewById(R.id.nested_scroll_view);

        v.findViewById(R.id.menu_icon_ads).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navDrawer.getTranslationX() == 0) {
                    navDrawer.animate().setDuration(100).translationX(animeValue);
                    scrollView.setScrollingEnabled(true);
                } else {
                    navDrawer.animate().setDuration(100).translationX(0);
                    scrollView.setScrollingEnabled(false);
                }
            }
        });

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (navDrawer.getTranslationX() == 0) {
                    navDrawer.animate().setDuration(100).translationX(animeValue);
                    scrollView.setScrollingEnabled(true);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                    setTitle(getString(R.string.ads));
                    showView();
                }
            }
        });

       /* v.findViewById(R.id.ads_parent).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (navDrawer.getTranslationX() == 0) {
                    navDrawer.animate().setDuration(100).translationX(850);
                    scrollView.setScrollingEnabled(true);
                }
                return false;
            }
        });*/

        /*scrollView.setOnTouchListener(new View.OnTouchListener() {
            float y1 = 0;
            float y0 = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    y0 = motionEvent.getY();
                    navDrawer.animate().setDuration(100).translationX(animeValue);
                    Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    y1 = motionEvent.getY();
                    if (y0 - y1 > 0) {
                        ((MainActivity)getActivity()).bottomNavigationViewBehavior.slideDown(((MainActivity)getActivity()).
                                bottomNavigationView);
                        Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                    } else if (y0 - y1 < 0) {
                        ((MainActivity)getActivity()).bottomNavigationViewBehavior.slideUp(((MainActivity)getActivity())
                                .bottomNavigationView);
                        Log.e("Y", motionEvent.getAction() + " - " + (y0 - y1));
                    }
                }

                return false;
            }
        });*/

        return v;
    }

    private void setNavData(int nums[]) {
        navDrawer.removeAllViews();
        final String navNames[] = new String[]{getString(R.string.home_nav), getString(R.string.ad_special_nav)
                , getString(R.string.ad_photo_nav), getString(R.string.ad_video_nav), getString(R.string.ad_text_nav)};

        final int navImgs[] = new int[]{R.mipmap.side_menu_home, R.mipmap.side_menu_fav_ads
                , R.mipmap.side_menu_pics_ads, R.mipmap.side_menu_videos_ads, R.mipmap.side_menu_text_ads};

        for (int i = 0; i < navNames.length; i++) {
            View navItem = LayoutInflater.from(getContext()).inflate(R.layout.ads_statistics_item, null);
            TextView navTitle = navItem.findViewById(R.id.ads_nav_txt);
            TextView navNum = navItem.findViewById(R.id.ads_nav_num);
            ImageView navImg = navItem.findViewById(R.id.ads_nav_img);
            navTitle.setTypeface(type);
            navNum.setTypeface(type);
            navNum.setText("" + nums[i]);
            navTitle.setText(navNames[i]);
            navImg.setImageResource(navImgs[i]);
            navItem.setTag(i);

            navItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragOne.openAdScreen(view);
                    navDrawer.animate().setDuration(0).translationX(animeValue);
                }
            });
            navDrawer.addView(navItem);
        }
    }

    private void adsRequest() {
        //view.setEnabled(false);
        final AdsStatisticsRequest adsStatisticsRequest = new AdsStatisticsRequest(0);
        Call<AdsStatisticsData> call1 = apiInterface.getAdsStatistics(adsStatisticsRequest);
        call1.enqueue(new Callback<AdsStatisticsData>() {
            @Override
            public void onResponse(Call<AdsStatisticsData> call, retrofit2.Response<AdsStatisticsData> response) {
                AdsStatisticsData adsStatisticsData = response.body();
                if (adsStatisticsData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                int[] nums = new int[]{adsStatisticsData.result.AllAds, adsStatisticsData.result.StarAdsCount,
                        adsStatisticsData.result.ImageAdsCount, adsStatisticsData.result.VedioAdsCount
                        , adsStatisticsData.result.TextAdsCount};
                setNavData(nums);
            }

            @Override
            public void onFailure(Call<AdsStatisticsData> call, Throwable t) {
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

    /*@Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bottomNavigationView = (ExtendedBottomNavigationView) getActivity().findViewById(R.id.the_bottom_navigation);

        bottomNavigationView.setVisibility(View.VISIBLE);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
         bottomNavigationViewBehavior = new BottomNavigationViewBehavior();
        layoutParams2.setBehavior(bottomNavigationViewBehavior);
        bottomNavigationViewBehavior.slideUp(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return true;
                    }
                });

        bottomNavigationView.setSelectedItemId(R.id.action_item1);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

    }*/

}
