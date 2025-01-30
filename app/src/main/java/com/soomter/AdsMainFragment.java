package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdsMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdsMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsMainFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg, adToolbar;
    Locale locale;
    TextView adsText,
            adsTextShow,
            adsPhotos,
            adsPhotosShow,
            adsVideos,
            adsVideosShow,
            adsSpec,
            adsSpecShow;
    ProgressBar specProg,
            photoProg,
            videosProg,
            textProg;

    List<?>[] arrOfLists;
    int currentTag;
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
    private ExtendedBottomNavigationView bottomNavigationView;
    private LinearLayout cont;
    private Typeface typeRoman;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private int catgId;
    private EditText serchEd;

    public AdsMainFragment() {
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
    public static AdsMainFragment newInstance(int param1, String param2) {
        AdsMainFragment fragment = new AdsMainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View rootView = inflater.inflate(R.layout.fragment_ads_main, container, false);

        serchEd = (EditText) rootView.findViewById(R.id.serch_ed);

        adsText = (TextView) rootView.findViewById(R.id.ads_text);
        adsTextShow = (TextView) rootView.findViewById(R.id.ads_text_show);
        adsPhotos = (TextView) rootView.findViewById(R.id.ads_photos_txt);
        adsPhotosShow = (TextView) rootView.findViewById(R.id.ads_photos_show);
        adsVideos = (TextView) rootView.findViewById(R.id.ads_videos_txt);
        adsVideosShow = (TextView) rootView.findViewById(R.id.ads_videos_show);
        adsSpec = (TextView) rootView.findViewById(R.id.ads_spec_txt);
        adsSpecShow = (TextView) rootView.findViewById(R.id.ads_spec_show);
        adToolbar = (ImageView) rootView.findViewById(R.id.ad_toolbar);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        serchEd.setTypeface(type);

        //adToolbar.setTypeface(type);
        adsText.setTypeface(type);
        adsTextShow.setTypeface(type);
        adsTextShow.setTag("2");
        adsPhotos.setTypeface(type);
        adsPhotosShow.setTypeface(type);
        adsPhotosShow.setTag("4");
        adsVideos.setTypeface(type);
        adsVideosShow.setTypeface(type);
        adsVideosShow.setTag("3");
        adsSpec.setTypeface(type);
        adsSpecShow.setTypeface(type);
        adsSpecShow.setTag("1");

        fm = getFragmentManager();

        adsTextShow.setOnClickListener(this);
        adsPhotosShow.setOnClickListener(this);
        adsVideosShow.setOnClickListener(this);
        adsSpecShow.setOnClickListener(this);

        adsPhotosShow.setEnabled(false);
        adsVideosShow.setEnabled(false);
        adsTextShow.setEnabled(false);
        adsSpecShow.setEnabled(false);

        specProg = (ProgressBar) rootView.findViewById(R.id.special_ads_progress);
        photoProg = (ProgressBar) rootView.findViewById(R.id.photo_ads_progress);
        videosProg = (ProgressBar) rootView.findViewById(R.id.videos_ads_progress);
        textProg = (ProgressBar) rootView.findViewById(R.id.text_ads_progress);

        rootView.findViewById(R.id.settings_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().addToBackStack(null)
                        .add(R.id.ads_fragment_container, AdsSearchFragment.newInstance(null, null))
                        .hide(AdsMainFragment.this).commit();
            }
        });

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    EventsMainFragment.hideKeyboardFrom(getActivity(), serchEd);
                    //    currentPage = 1;
                    adsRequest(rootView, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        adsRequest(rootView, 0);
        getAdImage();

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
                    adsRequest(rootView, 0);
                    EventsMainFragment.hideKeyboardFrom(getActivity(), serchEd);
                }
            }
        });

        return rootView;
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

    private void adsRequest(final View v, final int type) {
        final AdsRequest adsRequest = new AdsRequest(catgId,
                Locale.getDefault().getLanguage().toString(), serchEd.getText().toString().trim(), 0, 0, 200, type);
        Call<AdsData> call1 = apiInterface.getAds(adsRequest);
        call1.enqueue(new Callback<AdsData>() {
            @Override
            public void onResponse(Call<AdsData> call, Response<AdsData> response) {
                final AdsData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                arrOfLists = new List<?>[]{adsData.result.promAds,
                        adsData.result.videoAds,
                        adsData.result.imgsAds,
                        adsData.result.textAds};

                adsPhotosShow.setEnabled(true);
                adsVideosShow.setEnabled(true);
                adsTextShow.setEnabled(true);
                adsSpecShow.setEnabled(true);

                specProg.setVisibility(View.GONE);
                photoProg.setVisibility(View.GONE);
                videosProg.setVisibility(View.GONE);
                textProg.setVisibility(View.GONE);

                setAdsData(v, R.id.ads_text_container, adsData.result.textAds);
                setAdsData(v, R.id.ads_videos_container, adsData.result.videoAds);
                setAdsData(v, R.id.photos_ads_container, adsData.result.imgsAds);
                setAdsData(v, R.id.special_ads_container, adsData.result.promAds);
            }

            @Override
            public void onFailure(Call<AdsData> call, Throwable t) {

            }
        });
    }

    private void setAdsData(View v, int id, final List<AdsData.ImagesAds> adsList) {
        cont = (LinearLayout) v.findViewById(id);
        cont.removeAllViews();
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
            ImageView favIcon = view.findViewById(R.id.special_ads_fav_icon);

            favIcon.setImageResource(adsList.get(i).IsIdentified > 0 ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);

            if (adsList.get(i).Type == 3) {
                playIcon.setVisibility(View.VISIBLE);
                playIcon.bringToFront();
            }

           /* Picasso.get().load(adsList.get(i).Image)
                    .into(adImg);*/

            Glide.with(getContext()).load(adsList.get(i).Image).into(adImg);

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
        openAdScreen(view);
    }

    public void openAdScreen(View view) {
        if (Integer.parseInt(view.getTag().toString()) == currentTag)
            return;
        currentTag = Integer.parseInt(view.getTag().toString());
        if (currentTag == 0) {
            AdPagerMainFagment fragment = (AdPagerMainFagment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag("AdPagerMainFagment");
            if (fragment != null) {
                getActivity().getSupportFragmentManager().beginTransaction().hide(fragment).remove(fragment).commit();
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
            return;
        }
        AdPagerMainFagment fragment = (AdPagerMainFagment) getActivity().getSupportFragmentManager()
                .findFragmentByTag("AdPagerMainFagment");
        if (fragment != null) {
            //1, 3, 4, 2

            fragment.awesomePager.setCurrentItem(currentTag);
            return;
        }
        getFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.ads_fragment_container,
                        AdPagerMainFagment.newInstance(currentTag, catgId,
                                arrOfLists), "AdPagerMainFagment").hide(this).addToBackStack(null).commit();
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
