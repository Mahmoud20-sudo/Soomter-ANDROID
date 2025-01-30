package com.soomter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class AdTypeFragment extends Fragment {

    public final int typeId;
    private final List<?> adsListObj;
    private GridView mGridView;
    private AdGridAdapter mGridAdapter;
    private Activity activity;
    private APIInterface apiInterface;
    private int myLastVisiblePos;
    private ExtendedBottomNavigationView bottomNavigationView;
    private BottomNavigationViewBehavior bottomNavigationViewBehavior;
    EditText serchEd;
    int catgId;
    private ImageView adToolbar;

    public AdTypeFragment(int catgId, Activity activity, int typeId, List<?> adsListObj) {
        this.activity = activity;
        this.adsListObj = adsListObj;
        this.catgId = catgId;
        this.typeId = typeId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        View view;
        view = inflater.inflate(R.layout.app_gridview, container, false);
        mGridView = (GridView) view.findViewById(R.id.gridView);

        adToolbar = (ImageView) view.findViewById(R.id.ad_toolbar);
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        serchEd = (EditText) view.findViewById(R.id.serch_ed);

        serchEd.setTypeface(type);

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    EventsMainFragment.hideKeyboardFrom(getActivity(), serchEd);
                    adsRequest();
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
                    adsRequest();
                }
            }
        });

        getAdImage();

        return view;
    }

    private void getAdImage() {
        Call<TopDownAdsData> call1 = apiInterface.getTopDownAds(4);
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

    private void adsRequest() {
        final AdsRequest adsRequest = new AdsRequest(catgId,
                Locale.getDefault().getLanguage().toString(),
                serchEd.getText().toString().trim(), 0, 0, 200, typeId);
        Call<AdsData> call1 = apiInterface.getAds(adsRequest);
        call1.enqueue(new Callback<AdsData>() {
            @Override
            public void onResponse(Call<AdsData> call, Response<AdsData> response) {
                final AdsData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                switch (typeId) {
                    case 1:
                        //spec
                        mGridAdapter = new AdGridAdapter(activity, (List<AdsData.ImagesAds>) adsData.result.promAds);
                        break;
                    case 2:
                        //text
                        mGridAdapter = new AdGridAdapter(activity, (List<AdsData.ImagesAds>) adsData.result.textAds);
                        break;
                    case 3:
                        //videos
                        mGridAdapter = new AdGridAdapter(activity, (List<AdsData.ImagesAds>) adsData.result.videoAds);
                        break;
                    case 4:
                        //images
                        mGridAdapter = new AdGridAdapter(activity, (List<AdsData.ImagesAds>) adsData.result.imgsAds);
                        break;
                }

                if (mGridView != null) {
                    mGridView.setAdapter(mGridAdapter);
                    //mGridView.setVerticalScrollBarEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<AdsData> call, Throwable t) {

            }
        });
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (activity != null) {
            //adsRequest();

            mGridAdapter = new AdGridAdapter(activity, (List<AdsData.ImagesAds>) adsListObj);
            if (mGridView != null) {
                mGridView.setAdapter(mGridAdapter);
                //mGridView.setVerticalScrollBarEnabled(false);
            }
            int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());


            ((LinearLayout.LayoutParams)mGridView.getLayoutParams()).setMargins(px , 0 , px ,0);

            mGridView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView parent, View view,
                                        int position, long id) {
                    /*onGridItemClick((GridView) parent, view, position, id);
                    getFragmentManager().beginTransaction().addToBackStack(null).
                            replace(R.id.frag_container,
                                    BusinessCatgsFragment.newInstance(list.get(position).id, null)).commit();
*/
                }
            });

        }

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

    }

    public void onGridItemClick(GridView g, View v, int position, long id) {
       /* Toast.makeText(
                activity,
                "Position Clicked: - " + position + " & " + "Text is: - "
                        + gridItems[position].title, Toast.LENGTH_LONG).show();
       */ Log.e("TAG", "POSITION CLICKED " + position);
    }
}