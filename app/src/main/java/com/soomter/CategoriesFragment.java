package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoriesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoriesFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View ItView, furnView, hometoolsView;
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
    private LinearLayout mostViewCon;
    private Typeface type;
    private ProgressBar progress;
    private TextView mvShow;

    public CategoriesFragment() {
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
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        View v = inflater.inflate(R.layout.fragment_categoreis, container, false);

        TextView title = (TextView) v.findViewById(R.id.cat_title);
        TextView mvTxt = (TextView) v.findViewById(R.id.most_views_txt);
         mvShow = (TextView) v.findViewById(R.id.most_views_show);
        TextView catOrdersTxt = (TextView) v.findViewById(R.id.cat_orders_txt);
        TextView catOrdersShowTxt = (TextView) v.findViewById(R.id.cat_orders_show);
        TextView activityOrdersTxt = (TextView) v.findViewById(R.id.actvity_orders_txt);
        TextView activityOrdersShowTxt = (TextView) v.findViewById(R.id.activity_orders_show);
        TextView addInist = (TextView) v.findViewById(R.id.add_inistitue);
        EditText searchED = (EditText) v.findViewById(R.id.serch_ed);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        title.setTypeface(type);
        mvTxt.setTypeface(type);
        mvShow.setTypeface(type);
        catOrdersTxt.setTypeface(type);
        catOrdersShowTxt.setTypeface(type);
        activityOrdersTxt.setTypeface(type);
        activityOrdersShowTxt.setTypeface(type);
        addInist.setTypeface(type);
        searchED.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();
        final List<PortalCatgData.Datum> list = sharedPref.getPortalCatg();

        progress = v.findViewById(R.id.progress);
        mostViewCon = v.findViewById(R.id.mostview_container);

        Ittxt = (TextView) v.findViewById(R.id.it_txt);
        ItImg = (ImageView) v.findViewById(R.id.it_img);
        furnTxt = (TextView) v.findViewById(R.id.furn_txt);
        fuImage = (ImageView) v.findViewById(R.id.furn_img);
        homeTxt = (TextView) v.findViewById(R.id.home_txt);
        homeImg = (ImageView) v.findViewById(R.id.hometools_img);
        ItView = v.findViewById(R.id.it_view);
        hometoolsView = v.findViewById(R.id.hometools_view);
        furnView = v.findViewById(R.id.furn_view);

        if (list == null || list.isEmpty())
            getPortalCategories();
        else {
            Ittxt.setText(lang.equalsIgnoreCase("ar") ? list.get(0).name : list.get(0).nameEN);
            /*Picasso.get().load(list.get(0).businessIcon64).error(R.mipmap.it_icons)
                    .into(ItImg);
*/
            Glide.with(getContext())
                    .load(list.get(0).businessIcon64)
                    .into(ItImg);

            ItView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*getFragmentManager().beginTransaction().addToBackStack(null).
                            replace(R.id.frag_container,
                                    BusinessCatgsFragment.newInstance(((PortalCatgData.Datum) list.get(0)).id,
                                            Locale.getDefault().getLanguage().toString().equals("ar") ? list.get(0).name : list.get(0).nameEN)).addToBackStack(null).commit();*/
                    getFragmentManager()
                            .beginTransaction().add(R.id.frag_container,
                            //categID, 0, list.get(position).id) , mParam2
                            CompaniesFragment.newInstance(((PortalCatgData.Datum) list.get(0)).id, 0, 0,
                                    Locale.getDefault().getLanguage().toString().equals("ar") ?
                                            ((PortalCatgData.Datum) list.get(0)).name :
                                            ((PortalCatgData.Datum) list.get(0)).nameEN , "")).hide(CategoriesFragment.this)
                                            .addToBackStack(null).commit();


                }
            });
/*
            furnTxt.setText(lang.equalsIgnoreCase("ar") ? list.get(1).name : list.get(1).nameEN);
            Picasso.get().load(list.get(1).businessIcon64).error(R.mipmap.furnitures_icon)
                    .into(fuImage);*/
            /*Glide.with(getContext())
                    .load(list.get(1).businessIcon64)
                    .into(fuImage);*/
            Glide.with(getContext())
                    .load(list.get(1).businessIcon64)
                    .into(fuImage);

            furnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
  /*                  getFragmentManager().beginTransaction().addToBackStack(null).
                            replace(R.id.frag_container,
                                    BusinessCatgsFragment.newInstance(((PortalCatgData.Datum) list.get(1)).id,
                                            Locale.getDefault().getLanguage().toString().equals("ar") ? list.get(1).name : list.get(1).nameEN)).addToBackStack(null).commit();
  */
                    getFragmentManager()
                            .beginTransaction().add(R.id.frag_container,
                            //categID, 0, list.get(position).id) , mParam2
                            CompaniesFragment.newInstance(((PortalCatgData.Datum) list.get(1)).id, 0, 0,
                                    Locale.getDefault().getLanguage().toString().equals("ar") ?
                                            ((PortalCatgData.Datum) list.get(1)).name :
                                            ((PortalCatgData.Datum) list.get(1)).nameEN , ""))
                                            .hide(CategoriesFragment.this)
                                            .addToBackStack(null).commit();

                }
            });

            homeTxt.setText(lang.equalsIgnoreCase("ar") ? list.get(2).name : list.get(2).nameEN);
            /*Picasso.get().load(list.get(2).businessIcon64).error(R.mipmap.home_tools_icon)
                    .into(homeImg);*/

            hometoolsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*getFragmentManager().beginTransaction().addToBackStack(null).
                            replace(R.id.frag_container,
                                    BusinessCatgsFragment.newInstance(((PortalCatgData.Datum) list.get(2)).id,
                                            Locale.getDefault().getLanguage().toString().equals("ar") ? list.get(2).name : list.get(2).nameEN)).addToBackStack(null).commit();
*/

                    getFragmentManager()
                            .beginTransaction().add(R.id.frag_container,
                            //categID, 0, list.get(position).id) , mParam2
                            CompaniesFragment.newInstance(((PortalCatgData.Datum) list.get(2)).id, 0, 0,
                                    Locale.getDefault().getLanguage().toString().equals("ar") ?
                                            ((PortalCatgData.Datum) list.get(2)).name :
                                            ((PortalCatgData.Datum) list.get(2)).nameEN , ""))
                                            .hide(CategoriesFragment.this).addToBackStack(null).commit();

                }
            });

            Glide.with(getContext())
                    .load(list.get(2).businessIcon64)
                    .into(homeImg);
        }

        /*TextView woodTxt = (TextView) v.findViewById(R.id.wood_txt);
        TextView elecdevTxt = (TextView) v.findViewById(R.id.elecdev_txt);
        TextView etisalatTxt = (TextView) v.findViewById(R.id.etisalat_txt);*/

        Ittxt.setTypeface(type);
        furnTxt.setTypeface(type);
        homeTxt.setTypeface(type);

        catOrdersShowTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.frag_container,
                                CategMainFragment.newInstance(null, 1))
                        .addToBackStack("portal_main")
                        .hide(CategoriesFragment.this)
                        .commit();
            }
        });

        if (sharedPref.getUser() != null)
            addInist.setVisibility(View.GONE);

        addInist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().addToBackStack("login")
                        .add(R.id.frag_container, RegisterFragment.newInstance("1", null))
                        .hide(CategoriesFragment.this).commit();
            }
        });


        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        MostWatchedCatgReq();

        return v;
    }

    private void MostWatchedCatgReq() {
        //view.setEnabled(false);
        //String lang , String UserId, int CategoryId,int SubCategoryId ,
        //int CityId, int AdsTo ,int CompanyId
        final MostWatchedCatgRequest mostWatchedCatgRequest =
                new MostWatchedCatgRequest(Locale.getDefault().getLanguage().toString(),
                        "", 0, 0, 0, 0, 0);
        Call<MostWatchedData> call1 = apiInterface.getMostWatchedCatgs(mostWatchedCatgRequest);
        call1.enqueue(new Callback<MostWatchedData>() {
            @Override
            public void onResponse(Call<MostWatchedData> call, retrofit2.Response<MostWatchedData> response) {
                MostWatchedData mostWatchedData = response.body();
                if (mostWatchedData.result == null) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    progress.setVisibility(View.GONE);
                    return;
                }

                progress.setVisibility(View.GONE);
                mostViewCon.setVisibility(View.VISIBLE);
                try {
                    setMostWatchedData(mostWatchedData);
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<MostWatchedData> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
                progress.setVisibility(View.GONE);
            }
        });

    }


    private void setMostWatchedData(final MostWatchedData mostWatchedData) throws Exception {
        for (int i = 0; i < mostWatchedData.result.size(); i++) {
            final MostWatchedData.Datum obje = mostWatchedData.result.get(i);

            View view = LayoutInflater.from(getContext()).inflate(R.layout.most_views_item, null);
            TextView catName = (TextView) view.findViewById(R.id.name);
            catName.setText(obje.CompanyName);
            catName.setTypeface(type);
            TextView catFollowTxt = (TextView) view.findViewById(R.id.cat_follow_txt);
            catFollowTxt.setTypeface(type);
            TextView catPriceTxt = (TextView) view.findViewById(R.id.cat_price_txt);
            catPriceTxt.setTypeface(type);
            TextView prodsNum = (TextView) view.findViewById(R.id.prods_num);
            prodsNum.setTypeface(type);
            prodsNum.setText(obje.CountProduct + "");
            TextView eventsNum = (TextView) view.findViewById(R.id.events_num);
            eventsNum.setText(obje.CountEvents + "");
            eventsNum.setTypeface(type);
            TextView viewNum = (TextView) view.findViewById(R.id.views_num);
            viewNum.setText(obje.ViewCounts + "");
            viewNum.setTypeface(type);
            TextView adsNum = (TextView) view.findViewById(R.id.ads_num);
            adsNum.setText(obje.CountADS + "");
            adsNum.setTypeface(type);

            ImageView compImg = (ImageView)view.findViewById(R.id.comp_img);
            Glide.with(getContext()).
                    load("https://soomter.com/AttachmentFiles/" + obje.LogoId + ".png").into(compImg);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getFragmentManager().beginTransaction().addToBackStack(null)
                            .add(R.id.frag_container,
                                    CompanyProfileFragment.newInstance(obje.Id ,
                                            obje.CompanyName)).hide(CategoriesFragment.this).addToBackStack(null).commit();
                }
            });

            final TextView followText = (TextView) view.findViewById(R.id.cat_follow_txt);
            final ImageView followBtn =  view.findViewById(R.id.follow_btn);

            if(obje.IsFlowed > 0){
                followBtn.setVisibility(View.GONE);
                followText.setVisibility(View.GONE);
            }

            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(sharedPref.getUser() == null){
                        Toast.makeText(getContext(), getString(R.string.require_login),Toast.LENGTH_LONG).show();
                        return;
                    }

                    Call<Void> call1 = apiInterface.followCompany(obje.Id, sharedPref.getUser().id);
                    call1.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                            if (response.message().equals("OK")) {
                                Toast.makeText(getContext(), getString(R.string.success), Toast.LENGTH_LONG).show();
                                followBtn.setVisibility(View.GONE);
                                followText.setVisibility(View.GONE);
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
            });

            ImageView firstStar = view.findViewById(R.id.first_start);
            ImageView secondStar = view.findViewById(R.id.second_start);
            ImageView thirdStar = view.findViewById(R.id.third_start);
            ImageView fourthStar = view.findViewById(R.id.fourth_start);
            ImageView fifthStar = view.findViewById(R.id.fifth_start);

            switch (Integer.valueOf(obje.AverageRating.intValue())) {
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
                    fourthStar.setImageResource(R.mipmap.gold_star);
                    break;
                case 5:
                    firstStar.setImageResource(R.mipmap.gold_star);
                    secondStar.setImageResource(R.mipmap.gold_star);
                    thirdStar.setImageResource(R.mipmap.gold_star);
                    fourthStar.setImageResource(R.mipmap.gold_star);
                    fifthStar.setImageResource(R.mipmap.gold_star);
                    break;
            }

            view.findViewById(R.id.cat_price_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            view.findViewById(R.id.cat_follow_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            mvShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(mostWatchedData.result);

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.frag_container,
                                    MostWatchedFragment.newInstance(jsonString))
                            .addToBackStack(null)
                            .hide(CategoriesFragment.this)
                            .commit();
                }
            });

            mostViewCon.addView(view);
        }
    }

    private void getPortalCategories() {
        //view.setEnabled(false);
        final PortalCatgRequest cities = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<PortalCatgData> call1 = apiInterface.getPortalCatg(cities);
        call1.enqueue(new Callback<PortalCatgData>() {
            @Override
            public void onResponse(Call<PortalCatgData> call, retrofit2.Response<PortalCatgData> response) {
                final PortalCatgData portalCatgData = response.body();
                if (portalCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                sharedPref.savePortalCatg(portalCatgData.result);
                Ittxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(0).name : portalCatgData.result.get(0).nameEN);
               /* Picasso.get().load(portalCatgData.result.get(0).businessIcon64).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(ItImg);*/

                Glide.with(getContext()).load(portalCatgData.result.get(0).businessIcon64).into(ItImg);

                ItView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager()
                                .beginTransaction().add(R.id.frag_container,
                                //categID, 0, list.get(position).id) , mParam2
                                CompaniesFragment.newInstance(((PortalCatgData.Datum) portalCatgData.result.get(0)).id, 0, 0,
                                        Locale.getDefault().getLanguage().toString().equals("ar") ?
                                                ((PortalCatgData.Datum) portalCatgData.result.get(0)).name :
                                                ((PortalCatgData.Datum) portalCatgData.result.get(0)).nameEN , ""))
                                .hide(CategoriesFragment.this).addToBackStack(null).commit();
                    }
                });

                furnTxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(1).name : portalCatgData.result.get(1).nameEN);
              /*  Picasso.get().load(portalCatgData.result.get(1).businessIcon64).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(fuImage);*/

                Glide.with(getContext()).load(portalCatgData.result.get(1).businessIcon64).into(fuImage);

                furnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager()
                                .beginTransaction().add(R.id.frag_container,
                                //categID, 0, list.get(position).id) , mParam2
                                CompaniesFragment.newInstance(((PortalCatgData.Datum) portalCatgData.result.get(1)).id, 0, 0,
                                        Locale.getDefault().getLanguage().toString().equals("ar") ?
                                                ((PortalCatgData.Datum) portalCatgData.result.get(1)).name :
                                                ((PortalCatgData.Datum) portalCatgData.result.get(1)).nameEN , ""))
                                .hide(CategoriesFragment.this).addToBackStack(null).commit();
                    }
                });

                homeTxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(2).name : portalCatgData.result.get(2).nameEN);
                /*Picasso.get().load(portalCatgData.result.get(2).businessIcon64).networkPolicy(NetworkPolicy.OFFLINE)
                        .into(homeImg);*/

                Glide.with(getContext()).load(portalCatgData.result.get(2).businessIcon64).into(homeImg);

                hometoolsView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getFragmentManager()
                                .beginTransaction().add(R.id.frag_container,
                                //categID, 0, list.get(position).id) , mParam2
                                CompaniesFragment.newInstance(((PortalCatgData.Datum) portalCatgData.result.get(2)).id, 0, 0,
                                        Locale.getDefault().getLanguage().toString().equals("ar") ?
                                                ((PortalCatgData.Datum) portalCatgData.result.get(2)).name :
                                                ((PortalCatgData.Datum) portalCatgData.result.get(2)).nameEN , ""))
                                .hide(CategoriesFragment.this).addToBackStack(null).commit();
                    }
                });

            }

            @Override
            public void onFailure(Call<PortalCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
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

    /*private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }*/
}
