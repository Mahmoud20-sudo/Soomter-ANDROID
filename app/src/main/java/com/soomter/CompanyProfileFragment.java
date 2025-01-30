package com.soomter;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;

    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private int companyID;
    private String title;
    private TextView viewsCountTv, branchesCountTv, prodsCountTv, adsCountTv, eventsCountTv, joiuntsCountTv;
    private TextView viewsTitleTv, branchesTitleTv, prodsTitleTv, adsTitleTv, eventsTitleTv, joiuntsTitleTv;
    private ImageView viewsCountImg, branchesCountImg, prodsCountImg, adsCountImg, eventsCountImg, joiuntsCountImg;
    private ImageView firstStar, secondStar, thirdStar, forthStar, fifthStar;
    private ImageView googlePlus, snap, fb, twitter, instg, youtube;
    private View detailsContainer, branchesContainer, jointsContainer, prodsContainer, adsContainer, eventsContainer;

    private ProgressDialog progressDialog;
    private View follow;
    private TextView ownerTv;
    private ImageView compImg;

    public CompanyProfileFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CompanyProfileFragment newInstance(int companyID, String title) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, companyID);
        args.putString(ARG_PARAM4, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            companyID = getArguments().getInt(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_profile, container, false);

        TextView titleTv = (TextView) v.findViewById(R.id.profile_title);
        TextView companyNameTv = (TextView) v.findViewById(R.id.company_title);
        ownerTv = (TextView) v.findViewById(R.id.owner_tv);

        TextView adToolbar = (TextView) v.findViewById(R.id.ad_toolbar);
        TextView adTv = (TextView) v.findViewById(R.id.ad_tv);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        Typeface typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        titleTv.setText(title);
        titleTv.setTypeface(type);
        companyNameTv.setText(title);
        companyNameTv.setTypeface(typeRoman);
        ownerTv.setTypeface(typeRoman);
        adToolbar.setTypeface(type);
        adTv.setTypeface(type);

        compImg = v.findViewById(R.id.company_img);

        initProgress();

        detailsContainer = v.findViewById(R.id.details_container);
        branchesContainer = v.findViewById(R.id.branches_container);
        jointsContainer = v.findViewById(R.id.joints_container);
        prodsContainer = v.findViewById(R.id.prods_container);
        adsContainer = v.findViewById(R.id.ads_container);
        eventsContainer = v.findViewById(R.id.events_container);

        viewsCountTv = v.findViewById(R.id.views_counts);
        branchesCountTv = v.findViewById(R.id.branches_counts);
        prodsCountTv = v.findViewById(R.id.products_counts);
        adsCountTv = v.findViewById(R.id.ads_counts);
        eventsCountTv = v.findViewById(R.id.events_counts);
        joiuntsCountTv = v.findViewById(R.id.joints_counts);

        viewsCountTv.setTypeface(typeRoman);
        branchesCountTv.setTypeface(typeRoman);
        prodsCountTv.setTypeface(typeRoman);
        adsCountTv.setTypeface(typeRoman);
        eventsCountTv.setTypeface(typeRoman);
        joiuntsCountTv.setTypeface(typeRoman);

        viewsCountImg = v.findViewById(R.id.views_img);
        branchesCountImg = v.findViewById(R.id.branches_img);
        prodsCountImg = v.findViewById(R.id.prods_img);
        adsCountImg = v.findViewById(R.id.ads_img);
        eventsCountImg = v.findViewById(R.id.events_img);
        joiuntsCountImg = v.findViewById(R.id.joints_img);

        viewsTitleTv = v.findViewById(R.id.views_name);
        branchesTitleTv = v.findViewById(R.id.branches_name);
        prodsTitleTv = v.findViewById(R.id.products_name);
        adsTitleTv = v.findViewById(R.id.ads_name);
        eventsTitleTv = v.findViewById(R.id.events_name);
        joiuntsTitleTv = v.findViewById(R.id.joints_name);

        viewsTitleTv.setTypeface(type);
        branchesTitleTv.setTypeface(type);
        prodsTitleTv.setTypeface(type);
        adsTitleTv.setTypeface(type);
        eventsTitleTv.setTypeface(type);
        joiuntsTitleTv.setTypeface(type);

        firstStar = v.findViewById(R.id.first_start);
        secondStar = v.findViewById(R.id.second_start);
        thirdStar = v.findViewById(R.id.third_start);
        forthStar = v.findViewById(R.id.fourth_start);
        fifthStar = v.findViewById(R.id.fifth_start);

        googlePlus = v.findViewById(R.id.google_img);
        snap = v.findViewById(R.id.snap_img);
        fb = v.findViewById(R.id.fb_img);
        twitter = v.findViewById(R.id.twitter_img);
        instg = v.findViewById(R.id.instagram_img);
        youtube = v.findViewById(R.id.youtube_img);

        follow = v.findViewById(R.id.follow_comp);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        if (sharedPref.getUser() == null){
            v.findViewById(R.id.owner_view).setVisibility(View.GONE);
        }

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ownerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                OwnInistDialog ownInistDialog = OwnInistDialog.newInstance(companyID);
                ownInistDialog.show(fm, "fragment_edit_name");
            }
        });

        viewsCountTv.setTextColor(Color.parseColor("#D9B878"));
        viewsCountImg.setImageResource(R.mipmap.views_on);
        viewsTitleTv.setTextColor(Color.parseColor("#212121"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((View) viewsCountTv.getParent()).setElevation(30);
        }

        try {
            profileReq();
        } catch (Exception e) {
            e.printStackTrace();
        }

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followCompany(companyID);
            }
        });

        return v;
    }

    private void followCompany(int comanyId) {
        Call<Void> call1 = apiInterface.followCompany(comanyId, sharedPref.getUser().id);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                    follow.setVisibility(View.GONE);
                    ownerTv.setVisibility(View.GONE);
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

    private void initProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    private void clearSelections() {
        viewsCountTv.setTextColor(Color.parseColor("#CBCCCE"));
        branchesCountTv.setTextColor(Color.parseColor("#CBCCCE"));
        prodsCountTv.setTextColor(Color.parseColor("#CBCCCE"));
        adsCountTv.setTextColor(Color.parseColor("#CBCCCE"));
        eventsCountTv.setTextColor(Color.parseColor("#CBCCCE"));
        joiuntsCountTv.setTextColor(Color.parseColor("#CBCCCE"));

        viewsCountImg.setImageResource(R.mipmap.views_off);
        branchesCountImg.setImageResource(R.mipmap.branches_off);
        prodsCountImg.setImageResource(R.mipmap.products_off);
        adsCountImg.setImageResource(R.mipmap.ads_off);
        eventsCountImg.setImageResource(R.mipmap.events_off);
        joiuntsCountImg.setImageResource(R.mipmap.joinus_off);

        viewsTitleTv.setTextColor(Color.parseColor("#CBCCCE"));
        branchesTitleTv.setTextColor(Color.parseColor("#CBCCCE"));
        prodsTitleTv.setTextColor(Color.parseColor("#CBCCCE"));
        adsTitleTv.setTextColor(Color.parseColor("#CBCCCE"));
        eventsTitleTv.setTextColor(Color.parseColor("#CBCCCE"));
        joiuntsTitleTv.setTextColor(Color.parseColor("#CBCCCE"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((View) viewsCountTv.getParent()).setElevation(0);
            ((View) branchesCountTv.getParent()).setElevation(0);
            ((View) prodsCountTv.getParent()).setElevation(0);
            ((View) adsCountTv.getParent()).setElevation(0);
            ((View) eventsCountTv.getParent()).setElevation(0);
            ((View) joiuntsCountTv.getParent()).setElevation(0);
        }
    }

    private void profileReq() throws Exception {
        final CompanyProfileRequest companiesRequest = new CompanyProfileRequest(
                Locale.getDefault().getLanguage().toString(), 0, companyID);//600408 //599576
        Call<CompanyProfileData> call1 = apiInterface.getCompanyProfile(companiesRequest);
        call1.enqueue(new Callback<CompanyProfileData>() {
            @Override
            public void onResponse(Call<CompanyProfileData> call, retrofit2.Response<CompanyProfileData> response) {
                final CompanyProfileData companiesData = response.body();
                if (companiesData.result == null) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.dismiss();
                viewsCountTv.setText(companiesData.result.CompanyViewsCount + "");
                branchesCountTv.setText(companiesData.result.CompanyBanchesCount + "");
                prodsCountTv.setText(companiesData.result.CompanyProductsCount + "");
                adsCountTv.setText(companiesData.result.CompanyAdsCount + "");
                eventsCountTv.setText(companiesData.result.CompanyEventsCount + "");
                //joiuntsCountTv.setText(companiesData.result. + "");
                Glide.with(getContext()).
                        load("https://soomter.com/AttachmentFiles/" + companiesData.result.LogoId + ".png")
                        .into(compImg);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(companiesData.result);
                addDetailsFrag(jsonString);

                prodsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        prodsCountTv.setTextColor(Color.parseColor("#D9B878"));
                        prodsCountImg.setImageResource(R.mipmap.products_on);
                        prodsTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((View) prodsContainer.getParent()).setElevation(30);
                        }
                        addProductsFrag(jsonString);
                    }
                });

                branchesContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        branchesCountTv.setTextColor(Color.parseColor("#D9B878"));
                        branchesCountImg.setImageResource(R.mipmap.branches_on);
                        branchesTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((View) branchesCountTv.getParent()).setElevation(30);
                        }
                        addBranchesFrag(jsonString);
                    }
                });

                eventsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        eventsCountTv.setTextColor(Color.parseColor("#D9B878"));
                        eventsCountImg.setImageResource(R.mipmap.events_on);
                        eventsTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            eventsContainer.setElevation(30);
                        }
                        addEventsFrag(jsonString);
                    }
                });

                detailsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        viewsCountTv.setTextColor(Color.parseColor("#D9B878"));
                        viewsCountImg.setImageResource(R.mipmap.views_on);
                        viewsTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((View) viewsCountTv.getParent()).setElevation(30);
                        }
                        addDetailsFrag(jsonString);
                    }
                });

                jointsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        joiuntsCountTv.setTextColor(Color.parseColor("#D9B878"));
                        joiuntsCountImg.setImageResource(R.mipmap.joinus_on);
                        joiuntsTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            jointsContainer.setElevation(30);
                        }
                        addJointsFrag(jsonString);
                    }
                });

                adsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        adsCountTv.setTextColor(Color.parseColor("#D9B878"));
                        adsCountImg.setImageResource(R.mipmap.ads_on);
                        adsTitleTv.setTextColor(Color.parseColor("#212121"));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            ((View) adsCountTv.getParent()).setElevation(30);
                        }
                        addAdsFrag(jsonString);
                    }
                });

                switch (Integer.valueOf(companiesData.result.ratingModel.AverageRating.intValue())) {
                    case 1:
                        firstStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 2:
                        firstStar.setImageResource(R.mipmap.gold_star);
                        secondStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 3:
                        firstStar.setImageResource(R.mipmap.gold_star);
                        secondStar.setImageResource(R.mipmap.gold_star);
                        thirdStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 4:
                        firstStar.setImageResource(R.mipmap.gold_star);
                        secondStar.setImageResource(R.mipmap.gold_star);
                        thirdStar.setImageResource(R.mipmap.gold_star);
                        forthStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 5:
                        firstStar.setImageResource(R.mipmap.gold_star);
                        secondStar.setImageResource(R.mipmap.gold_star);
                        thirdStar.setImageResource(R.mipmap.gold_star);
                        forthStar.setImageResource(R.mipmap.gold_star);
                        fifthStar.setImageResource(R.mipmap.gold_star);
                        break;
                }

                if (companiesData.result.GooglePlus != null)
                    googlePlus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openGoogleLink(companiesData.result.GooglePlus);
                        }
                    });
                else {
                    googlePlus.setImageResource(R.mipmap.googleplus_off);

                }
                if (companiesData.result.Twitter != null)
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openTwitterLink(companiesData.result.Twitter);
                        }
                    });
                else {
                    twitter.setImageResource(R.mipmap.twitter_off);

                }
                if (companiesData.result.Facebook != null)
                    fb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getOpenFacebookIntent(getContext(), companiesData.result.Facebook);
                        }
                    });
                else {
                    fb.setImageResource(R.mipmap.facebook_off);

                }
                if (companiesData.result.Instagram != null)
                    instg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openInstaUrl(companiesData.result.Instagram);
                        }
                    });
                else {
                    instg.setImageResource(R.mipmap.instagram_off);

                }
                if (companiesData.result.YouTube != null)
                    youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openYoutubeVideo(getContext(),
                                    companiesData.result.YouTube);
                        }
                    });
                else {
                    youtube.setImageResource(R.mipmap.youtube_off);

                }
            }

            @Override
            public void onFailure(Call<CompanyProfileData> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
                progressDialog.dismiss();
            }
        });
    }

    private void addDetailsFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyDetailsFragment.newInstance(jsonString)).commit();
    }

    private void addJointsFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyJointsFragment.newInstance(jsonString)).commit();
    }

    private void addAdsFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyAdsFragment.newInstance(jsonString)).commit();
    }

    private void addBranchesFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyBranchesFragment.newInstance(jsonString)).commit();
    }

    private void addProductsFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyProductsFragment.newInstance(jsonString)).commit();
    }

    private void addEventsFrag(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.company_profile_frag_cont,
                CompanyEventsFragment.newInstance(jsonString)).commit();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    private void openInstaUrl(String url) {
        Intent likeIng = new Intent(Intent.ACTION_VIEW, Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                "http://" + url : url));

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                            "http://" + url : url)));
        }
    }

    public void openYoutubeVideo(Context context, String url) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                        "http://" + url : url));
        try {
            context.startActivity(webIntent);
        } catch (ActivityNotFoundException ex) {
        }
    }

    private void openTwitterLink(String url) {
        Intent intent = null;
        try {
            getContext().getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name="
                    + url.split("/")[1]));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                    "http://" + url : url));
        }
        startActivity(intent);
    }

    private void openGoogleLink(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            if (getContext().getPackageManager().getPackageInfo("com.google.android.apps.plus", 0) != null) {
                intent.setPackage("com.google.android.apps.plus");
            }
        } catch (PackageManager.NameNotFoundException e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                    "http://" + url : url));
        }
        startActivity(intent);
    }

    private void getOpenFacebookIntent(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
            if (applicationInfo.enabled) {
                uri = Uri.parse("fb://facewebmodal/f?href=" + url);
                intent = new Intent(Intent.ACTION_VIEW, uri);
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse((!url.startsWith("http://") && !url.startsWith("https://")) ?
                    "http://" + url : url));
        }
        startActivity(intent);

        return;
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
}
