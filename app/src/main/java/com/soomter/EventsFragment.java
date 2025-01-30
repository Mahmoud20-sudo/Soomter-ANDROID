package com.soomter;

import android.app.Dialog;
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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static LockableScrollView scrollView;
    public static NestedScrollView eventScollView;
    private static TextView titleTv;
    private static View topMenu, topBar;
    public ImageView eventFav;
    public int IsFavurite;
    public boolean isClicked;
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;
    Locale locale;
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
    //public static int IsFavourite;
    private EventsMainFragment eventsMainFragment;

    public EventsFragment() {
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
        //titleTv.setTextColor(Color.parseColor("#323232"));
        topMenu.setVisibility(View.GONE);
        //topBar.setBackgroundColor(Color.WHITE);
    }

    public static void showView() {
        //titleTv.setTextColor(Color.parseColor("#f4f4f4"));
        topMenu.setVisibility(View.VISIBLE);
        //topBar.setBackgroundColor(Color.BLACK);
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
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //bottomNavigationView.setVisibility(View.GONE);
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
        ((MainActivity)getActivity()).bottomNavigationView.setVisibility(View.GONE);
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_events, container, false);

        eventFav = (ImageView) v.findViewById(R.id.event_fav_img);


        topMenu = v.findViewById(R.id.topmenu);
        topBar = v.findViewById(R.id.topbar);

        titleTv = (TextView) v.findViewById(R.id.products_title);
        TextView allEvents = (TextView) v.findViewById(R.id.all_products_txt);
        TextView addEvent = (TextView) v.findViewById(R.id.cart_txt);
        TextView favEvents = (TextView) v.findViewById(R.id.products_fav);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        titleTv.setTypeface(type);
        addEvent.setTypeface(type);
        allEvents.setTypeface(type);
        favEvents.setTypeface(type);

        final FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();

         eventsMainFragment = new EventsMainFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean("shouldYouCreateAChildFragment", true);
        eventsMainFragment.setArguments(arguments);
        ft.add(R.id.event_fragment_container, eventsMainFragment, "EventsMainFragment");
        ft.commit();

        eventFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(prefObj.getUser() == null){
                    Toast.makeText(getContext(), getString(R.string.require_login),Toast.LENGTH_LONG).show();
                    return;
                }
                FavEventsFragment favEventsFragment = new FavEventsFragment();
                ft = ((AppCompatActivity) getContext())
                        .getSupportFragmentManager().beginTransaction();
                ft.add(R.id.event_fragment_container, favEventsFragment, "EventsMainFragment");
                ft.addToBackStack(null);
                ft.hide(eventsMainFragment);
                ft.commit();

                /*if(IsFavourite == 1 ){
                    IsFavourite = 0;
                    eventFav.setImageResource(R.mipmap.star_empty);
                }
                else {
                    IsFavourite = 1;
                    eventFav.setImageResource(R.mipmap.ad_details_star);
                }
                ft = fm.beginTransaction();
                ft.remove(getActivity().getSupportFragmentManager()
                        .findFragmentByTag("EventsMainFragment")).commitAllowingStateLoss();
                EventsMainFragment fragOne = new EventsMainFragment();
                Bundle arguments = new Bundle();
                EventsMainFragment.currentPage = 0;
                arguments.putBoolean("shouldYouCreateAChildFragment", true);
                arguments.putInt("IsFavourit", IsFavourite);
                fragOne.setArguments(arguments);
                ft = fm.beginTransaction();
                ft.add(R.id.event_fragment_container, fragOne, "EventsMainFragment");
                ft.commit();*/
            }

        });

        animeValue = Locale.getDefault().toString().equalsIgnoreCase("ar") ? 1200 : -1200;

        eventScollView = v.findViewById(R.id.event_nave_scroller);
        eventScollView.bringToFront();
        eventScollView.animate().setDuration(0).translationX(animeValue);

        scrollView = (LockableScrollView) v.findViewById(R.id.nested_scroll_view);
       /* scrollView.setOnTouchListener(new OnSwipeTouchListener(getContext()) {
            @Override
            public void onSwipeLeft() {
                // Whatever
                eventScollView.animate().setDuration(100).translationX(0);
                scrollView.setScrollingEnabled(false);
                bottomNavigationView.setVisibility(View.GONE);
            }
        });*/

        navDrawer = v.findViewById(R.id.events_nav_drawer);//.animate().translationX(-500);

        // setNavData(new EventsCountsData().result);

        eventsRequest();

        final ImageView menuIcon = v.findViewById(R.id.menu_icon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventScollView.getTranslationX() == 0) {
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                   // bottomNavigationView.setVisibility(View.VISIBLE);
                    scrollView.setScrollingEnabled(true);
                } else {
                    eventScollView.animate().setDuration(100).translationX(0);
                    scrollView.setScrollingEnabled(false);
                    //bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventScollView.getTranslationX() == 0) {
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    scrollView.setScrollingEnabled(true);
                } else{
                    getActivity().getSupportFragmentManager().popBackStack();
                    showView();
                    setTitle(getString(R.string.events));
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
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    y1 = motionEvent.getY();
                    if (y0 - y1 > 0) {
                        bottomNavigationViewBehavior.slideDown(bottomNavigationView);
                        Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                    } else if (y0 - y1 < 0) {
                        bottomNavigationViewBehavior.slideUp(bottomNavigationView);
                        Log.e("Y", motionEvent.getAction() + " - " + (y0 - y1));
                    }
                }

                return false;
            }
        });*/

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    menuIcon.setEnabled(true);
                else
                    menuIcon.setEnabled(false);
            }
        });

        return v;
    }

    private void setNavData(final List<EventsCountsData.Datum> result) {
        navDrawer.removeAllViews();

        final int navImgs[] = new int[]{R.mipmap.event0, R.mipmap.event1
                , R.mipmap.event2, R.mipmap.event3, R.mipmap.event4, R.mipmap.event5, R.mipmap.event6};

        for (int i = 0; i < result.size(); i++) {
            View navItem = LayoutInflater.from(getContext()).inflate(R.layout.events_count, null);
            TextView navTitle = navItem.findViewById(R.id.ads_nav_txt);
            TextView navNum = navItem.findViewById(R.id.ads_nav_num);
            ImageView navImg = navItem.findViewById(R.id.ads_nav_img);
            navTitle.setTypeface(type);
            navNum.setTypeface(type);
            navNum.setText("" + result.get(i).Count);
            navTitle.setText(result.get(i).Name);
            navImg.setImageResource(navImgs[i]);
            navItem.setTag(i+"");

            navItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    eventsMainFragment.EventTypeId = result.get(Integer.parseInt(view.getTag().toString())).Id;
                    eventsMainFragment.currentPage = 1;
                    eventsMainFragment.clearData();
                    eventsMainFragment.loadNextPage();

                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    //bottomNavigationView.setVisibility(View.VISIBLE);
                    scrollView.setScrollingEnabled(true);
                }
            });

            navDrawer.addView(navItem);
        }
    }

    private void eventsRequest() {
        //view.setEnabled(false);
        final EventsCountsRequest eventsCountsRequest = new EventsCountsRequest(0, Locale.getDefault().getLanguage().toString());
        Call<EventsCountsData> call1 = apiInterface.getEventsCount(eventsCountsRequest);
        call1.enqueue(new Callback<EventsCountsData>() {
            @Override
            public void onResponse(Call<EventsCountsData> call, retrofit2.Response<EventsCountsData> response) {
                EventsCountsData eventsCountsData = response.body();
                if (eventsCountsData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                setNavData(eventsCountsData.result);
            }

            @Override
            public void onFailure(Call<EventsCountsData> call, Throwable t) {
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
