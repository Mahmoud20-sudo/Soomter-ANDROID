package com.soomter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import net.hockeyapp.android.metrics.model.User;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdsDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdsDetailsFragment extends Fragment implements View.OnClickListener , AdReportDialog.ClickEventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Locale locale;
    TextView offerTxt, titleTxt, locTxt, catgTxt, subCatgTxt, mobileTxt, detailsTxt, pickedTxt, pickedShowTxt;
    ImageView mainPlayIcon;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private SharedPref prefObj;
    private ExtendedBottomNavigationView bottomNavigationView;
    private int typeId, Id;
    private ImageView detailsIFavImg;
    private ImageView detailsImg;
    private String offetString;
    private TextView emailTxt;
    private TextView landLineTxt;
    private Typeface type, typeRoman, typeMedium;
    private TextView reportTxt;
    private TextView adBuildingTxt;
    private int width;
    private LinearLayout cont;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private MySpinnerDialog myInstance;
    private UserData.Datum user;

    public AdsDetailsFragment() {
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
    public static AdsDetailsFragment newInstance(int param1, int param2, String param3) {
        AdsDetailsFragment fragment = new AdsDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF rectF = new RectF(0, 0, w, h);

        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        /**
         * here to define your corners, this is for left bottom and right bottom corners
         */
        //final Rect clipRect = new Rect(0, 0, w, h - radius);3
        final Rect clipRect = new Rect(0, radius, w + radius, h + radius);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawRect(clipRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);


        bitmap.recycle();

        return output;
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
            typeId = getArguments().getInt(ARG_PARAM2);
            Id = getArguments().getInt(ARG_PARAM1);
            offetString = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");

        myInstance = new MySpinnerDialog();
        myInstance.setCancelable(false);

        myInstance.show(getFragmentManager(), "some_tag");

        final View v = inflater.inflate(R.layout.fragment_ads_details, container, false);

        /*Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.maxresdefault);

        detailsImg.setImageBitmap(getRoundCornerBitmap(largeIcon, 55));
*/
        offerTxt = v.findViewById(R.id.add_details_offer);
        titleTxt = v.findViewById(R.id.add_details_title);
        locTxt = v.findViewById(R.id.add_details_location);
        catgTxt = v.findViewById(R.id.add_details_category);
        subCatgTxt = v.findViewById(R.id.add_details_sub_category);
        mobileTxt = v.findViewById(R.id.add_details_mobile);
        landLineTxt = v.findViewById(R.id.add_details_phone);
        detailsTxt = v.findViewById(R.id.ad_details_text);
        pickedTxt = v.findViewById(R.id.ads_picked_txt);
        pickedShowTxt = v.findViewById(R.id.ads_picked_show);
        detailsIFavImg = v.findViewById(R.id.ad_fav_img);
        emailTxt = v.findViewById(R.id.add_details_mail);
        reportTxt = v.findViewById(R.id.ad_details_report);
        adBuildingTxt = v.findViewById(R.id.add_details_tt);
        detailsImg = v.findViewById(R.id.ad_details_img);
        mainPlayIcon = v.findViewById(R.id.main_play_icon);

        offerTxt.setTypeface(typeRoman);
        adBuildingTxt.setTypeface(typeMedium);
        titleTxt.setTypeface(type);
        locTxt.setTypeface(type);
        catgTxt.setTypeface(type);
        subCatgTxt.setTypeface(type);
        mobileTxt.setTypeface(type);
        landLineTxt.setTypeface(type);
        detailsTxt.setTypeface(type);
        emailTxt.setTypeface(type);
        reportTxt.setTypeface(typeRoman);
        pickedShowTxt.setTypeface(type);
        pickedTxt.setTypeface(type);

        cont = (LinearLayout) v.findViewById(R.id.ads_picked_container);

        if (APIClient.isInternetAvailable( getContext()) == false)
            Toast.makeText(getContext(), getString(R.string.network_issue), Toast.LENGTH_SHORT).show();
        else
            adsDetailsRequest();

        try {
            AdsFragment.resetScroll();
        }catch (Exception e){}

        user = prefObj.getUser();

        v.findViewById(R.id.ad_details_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user == null){
                    Toast.makeText(getContext(), getString(R.string.require_login), Toast.LENGTH_LONG).show();
                    return;
                }

                FragmentManager fm = getFragmentManager();
                AdReportDialog editNameDialogFragment = AdReportDialog.newInstance(1);
                editNameDialogFragment.clickEventListener = AdsDetailsFragment.this;
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        return v;
    }

    private void adsDetailsRequest() {
        final AdsDetailsRequest adsRequest = new AdsDetailsRequest(Id, Locale.getDefault().getLanguage().toString()
                , 0, typeId);
        Call<AdsDetailData> call1 = apiInterface.getAdDetails(adsRequest);
        call1.enqueue(new Callback<AdsDetailData>() {
            @Override
            public void onResponse(Call<AdsDetailData> call, Response<AdsDetailData> response) {

                myInstance.dismiss();
                final AdsDetailData adsData = response.body();
                if (adsData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                offerTxt.setText(offetString);
                titleTxt.setText(adsData.result.CompanyName);
                locTxt.setText(adsData.result.CityName);
                catgTxt.setText(adsData.result.CategoryName);
                subCatgTxt.setText(adsData.result.SubCategoryName);
                mobileTxt.setText(adsData.result.Mobile != null ? adsData.result.Mobile : getString(R.string.no_inform));
                landLineTxt.setText(adsData.result.Phone != null ? adsData.result.Phone : getString(R.string.no_inform));
                detailsTxt.setText(adsData.result.Details != null ? adsData.result.Details : getString(R.string.no_inform));
                detailsIFavImg.setImageResource(adsData.result.IsFavorite != 0 ? R.mipmap.ad_details_star : R.mipmap.white_star);
                emailTxt.setText(getString(R.string.no_inform));

                //https://img.youtube.com/vi/<insert-youtube-video-id-here>/hqdefault.jpg
                if (adsData.result.VedioUrl != null && adsData.result.VedioUrl.length() > 0) {
                    mainPlayIcon.setVisibility(View.VISIBLE);
                    String videoId = "";
                    //https://youtu.be/BqLe52pjNrI?t=6s
                    if (adsData.result.VedioUrl.contains("be/")) {
                        videoId = adsData.result.VedioUrl.split("be/")[1];
                    } else
                        videoId = adsData.result.VedioUrl.split("v=")[1];

                    if (videoId.contains("?t"))
                        videoId = videoId.split("\\?t")[0];
                    if (videoId.contains("&t"))
                        videoId = videoId.split("&t5")[0];
                    if (videoId.contains("&"))
                        videoId = videoId.split("&")[0];

                    Glide.with(getContext())
                            .asBitmap()
                            .load("https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg")
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    detailsImg.setImageBitmap(getRoundCornerBitmap(resource, 15));
                                    detailsImg.setScaleType(ImageView.ScaleType.FIT_XY);

                                }
                            });
                    detailsImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(adsData.result.VedioUrl)));
                        }
                    });

                } else
                    Glide.with(getContext())
                            .asBitmap()
                            .load(adsData.result.Image)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    detailsImg.setImageBitmap(getRoundCornerBitmap(resource, 15));
                                    detailsImg.setScaleType(ImageView.ScaleType.FIT_XY);
                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    detailsImg.setImageResource(R.drawable.no_logo);
                                    detailsImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                }
                            });

                int count = adsData.result.ListSemilarAds.size() <= 3 ? adsData.result.ListSemilarAds.size() : 3;

                for (int i = 0; i < count; i++) {
                    final AdsDetailData.Datum.SimilarAds data = adsData.result.ListSemilarAds.get(i);
                    View view = LayoutInflater.from(getContext()).inflate(R.layout.ad_item, null);
                    cont.addView(view);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ft = getFragmentManager().beginTransaction();
                            ft = fm.beginTransaction();
                            AdsDetailsFragment fragOne = AdsDetailsFragment.newInstance(data.Id, typeId, data.Title);
                            ft.add(R.id.ads_fragment_container, fragOne);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    });

                    TextView adDesc = view.findViewById(R.id.ad_desc);
                    adDesc.setText(data.Title);
                    adDesc.setTypeface(typeRoman);
                    final ImageView adImg = view.findViewById(R.id.ad_img);
                    adImg.setVisibility(View.VISIBLE);
                    ImageView playIcon = view.findViewById(R.id.play_icon);
                    if (typeId == 3) {
                        playIcon.setVisibility(View.VISIBLE);
                        if (adsData.result.VedioUrl.length() > 0) {
                            String videoId = adsData.result.VedioUrl.split("v=")[1];
                            Glide.with(getContext())
                                    .load("https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg")
                                    .into(adImg);
                        }
                    }

                    ImageView favIcon = view.findViewById(R.id.special_ads_fav_icon);
                    favIcon.setImageResource(data.IsFavorite > 0 ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);
                    /*Picasso.get().load(data.)
                            .into(adImg);*/

                    if (i == 1)
                        setMargins(view, 15, 10, 15, 10);
                    else if (i == 0)
                        setMargins(view, 0, 10, 5, 10);
                    else
                        setMargins(view, 5, 10, 0, 10);
                }
            }

            @Override
            public void onFailure(Call<AdsDetailData> call, Throwable t) {
                myInstance.dismiss();
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View view) {
        getFragmentManager().beginTransaction().addToBackStack(null)
                .replace(R.id.frag_container, LoginFragment.newInstance(null, null)).commit();
    }

    @Override
    public void click(String name , String address , String mobile, String details, AdReportDialog dialog) {
        reportAd(name , address, mobile, details , dialog);
    }

    private void reportAd(String name , String address , String mobile, String details, final AdReportDialog dialog) {
        /*String Name, String Mobile, String Email, String UserId, String Title
                , String Body, int AdsId, int CompanyId*/
        AdReportRequest reportRequest = new AdReportRequest(name, mobile, user.email, user.id, getString(R.string.report_title)
                                                        , details, Id, 0);
        Call<Void> call1 = apiInterface.reportAd(reportRequest);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                dialog.progressBar.setVisibility(View.VISIBLE);
                dialog.send.setVisibility(View.GONE);
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.report_succes), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dialog.progressBar.setVisibility(View.VISIBLE);
                dialog.send.setVisibility(View.GONE);
                Toast.makeText(getContext(),getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

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