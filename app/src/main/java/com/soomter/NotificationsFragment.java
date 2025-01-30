package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.soomter.utils.PaginationAdapterCallback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class NotificationsFragment extends Fragment implements View.OnClickListener, PaginationAdapterCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PAGE_START = 1;
    private static LockableScrollView scrollView;
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
    private TextView titleTv;
    private View topMenu, topBar;
    private int TOTAL_PAGES = 500;
    private int currentPage = PAGE_START;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private NotficationsAdapter adapter;
    private View progress;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView mRecyclerView;
    private ExtendedBottomNavigationView bottomNavigationView;

    public NotificationsFragment() {
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
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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
        bottomNavigationView =
                (ExtendedBottomNavigationView) getActivity().findViewById(R.id.the_bottom_navigation);

        bottomNavigationView.setVisibility(View.VISIBLE);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        View view = (View) scrollView.getChildAt(scrollView.getChildCount() - 1);

                        int diff = (view.getBottom() - (scrollView.getHeight() + scrollView
                                .getScrollY()));

                        if (diff == 0) {
                            // your pagination code
                            isLoading = true;
                            currentPage += 1;

                            loadNextPage();
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_notifications, container, false);

        progress = v.findViewById(R.id.notf_progress);
        progress.setVisibility(View.VISIBLE);

        titleTv = (TextView) v.findViewById(R.id.notf_title);
        TextView allNotf = (TextView) v.findViewById(R.id.all_notf_txt);
        TextView deleteNotf = (TextView) v.findViewById(R.id.delete_notf_txt);

        topBar = v.findViewById(R.id.topbar_view);
        topMenu = v.findViewById(R.id.topmenu_view);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        titleTv.setTypeface(type);
        allNotf.setTypeface(type);
        deleteNotf.setTypeface(type);

        navDrawer = v.findViewById(R.id.notf_nav_drawer);//.animate().translationX(-500);
        navDrawer.bringToFront();

        animeValue = Locale.getDefault().toString().equalsIgnoreCase("ar") ? 1200 : -1200;

        navDrawer.animate().setDuration(0).translationX(animeValue);

        setNavData(new int[]{0, 0, 0, 0, 0});

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

        scrollView.setOnTouchListener(new View.OnTouchListener() {
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
                        //bottomNavigationViewBehavior.slideDown(bottomNavigationView);
                        Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                    } else if (y0 - y1 < 0) {
                        //bottomNavigationViewBehavior.slideUp(bottomNavigationView);
                        Log.e("Y", motionEvent.getAction() + " - " + (y0 - y1));
                    }
                }

                return false;
            }
        });

        mRecyclerView = (RecyclerView) v.findViewById(R.id.notifcations_rv);
        mRecyclerView.setNestedScrollingEnabled(false);

        adapter = new NotficationsAdapter(getContext(),
                this, new ArrayList<NotificationsData.Items>());

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setAdapter(adapter);

        notifsRequest();

        return v;
    }

    private void loadNextPage() {
        notifsRequest();
    }

    private void notifsRequest() {
        final NotificationsRequest notificationsRequest = new NotificationsRequest("", 10, currentPage);
        Call<NotificationsData> call1 = apiInterface.getNotifications(notificationsRequest);
        call1.enqueue(new Callback<NotificationsData>() {
            @Override
            public void onResponse(Call<NotificationsData> call, Response<NotificationsData> response) {
                final NotificationsData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                progress.setVisibility(View.GONE);
                adapter.removeLoadingFooter();
                adapter.addAll(adsData.result.Items);
                isLoading = false;

                BottomNavigationMenuView bottomNavigationMenuView =
                        (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
                View v = bottomNavigationMenuView.getChildAt(2);
                BottomNavigationItemView itemView = (BottomNavigationItemView) v;
                View badge = LayoutInflater.from(getContext())
                        .inflate(R.layout.notifications_badge, bottomNavigationMenuView, false);
                itemView.addView(badge);

                TextView count = badge.findViewById(R.id.notifications_count);
                count.setText(adsData.result.TotalItemsCount > 9 ? "+9" : adsData.result.TotalItemsCount + "");

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<NotificationsData> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                t.printStackTrace();
                adapter.showRetry(true, t.getMessage());
                progress.setVisibility(View.GONE);
            }
        });
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
            navDrawer.addView(navItem);
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
    public void retryPageLoad() {

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
