package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategMainFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public CirclePageIndicator mIndicator;
    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;
    Locale locale;
    ArrayList codeCategory;
    String deviceNames[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W",
            "X", "Y", "Z"};
    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private ViewPager awesomePager;
    private PagerAdapter pm;
    private int divNum;
    private EditText serchEd;


    public CategMainFragment() {
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
    public static CategMainFragment newInstance(String param1, int param2) {
        CategMainFragment fragment = new CategMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;

        if (mParam2 == 1)
            view = inflater.inflate(R.layout.portal_catg_main, container, false);
        else
            view = inflater.inflate(R.layout.fragment_products_main, container, false);

         sharedPref = new SharedPref(getActivity().getApplicationContext());

        awesomePager = (ViewPager) view.findViewById(R.id.pager);
        mIndicator = (CirclePageIndicator) view.findViewById(R.id.pagerIndicator);
        serchEd = (EditText) view.findViewById(R.id.serch_ed);

        ArrayList a = new ArrayList();

        apiInterface = APIClient.getClient().create(APIInterface.class);
       /* Category m = new Category();
        for (int i = 0; i < deviceNames.length; i++) {
            a.add(i, deviceNames[i]);
            m.name = (String) a.get(i);
        }

        codeCategory = new ArrayList();
        codeCategory.add(m);*/

        List gridFragments = new ArrayList();
        List<?> list;
        if (mParam2 == 1) {
            divNum = 15;
            list = sharedPref.getPortalCatg();
        } else {
            list = sharedPref.getProductsCatg();
            divNum = 10;
        }

        for (int i = 0; i < list.size() / divNum; i++) {
            List<?> head = list.subList(i * divNum, ((i + 1) * divNum));
            gridFragments.add(new CatgsFragmentV2(getActivity(), head, mParam2));
        }

        pm = new PagerAdapter(getFragmentManager(), gridFragments);
        awesomePager.setAdapter(pm);
        awesomePager.setOffscreenPageLimit(1);
        mIndicator.setViewPager(awesomePager);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        TextView title = (TextView) view.findViewById(R.id.cat_title);

        serchEd.setTypeface(type);

        if (title != null)
            title.setTypeface(type);

        if (mParam2 == 1)
            view.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });

        serchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE) || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                    EventsMainFragment.hideKeyboardFrom(getActivity(), serchEd);
                    if (mParam2 == 1) {
                        searchCatgs(serchEd.getText().toString().trim());
                    }
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
                    if (mParam2 == 1) {
                        List gridFragments = new ArrayList();
                        List<?> list;
                        divNum = 15;
                        list = sharedPref.getPortalCatg();

                        for (int i = 0; i < list.size() / divNum ; i++) {
                            List<?> head = list.subList(i * divNum, ((i + 1) * divNum));
                            gridFragments.add(new CatgsFragmentV2(getActivity(), head, mParam2));
                        }

                        pm = new PagerAdapter(getFragmentManager(), gridFragments);
                        awesomePager.setAdapter(pm);
                        mIndicator.setViewPager(awesomePager);

                    }
                }
            }
        });


        return view;
    }

    private void searchCatgs(String name) {
        //view.setEnabled(false);
        Call<PortalCatgData> call1 = apiInterface.getBusinessCategoriesByNam(name , Locale.getDefault().toString());
        call1.enqueue(new Callback<PortalCatgData>() {
            @Override
            public void onResponse(Call<PortalCatgData> call, retrofit2.Response<PortalCatgData> response) {
                PortalCatgData portalCatgData = response.body();
                if (portalCatgData.result == null || portalCatgData.result.isEmpty()) {
                   Toast.makeText(getContext(),getString(R.string.no_result), Toast.LENGTH_LONG).show();
                    return;
                }
                List gridFragments = new ArrayList();
                List<?> list;
                divNum = 15;
                list = portalCatgData.result;

                int dividr = list.size() > 15 ? list.size() / divNum : 1;

                for (int i = 0; i < dividr ; i++) {
                    List<?> head = list.subList(i * dividr, ((i + 1) * dividr));
                    gridFragments.add(new CatgsFragmentV2(getActivity(), head, mParam2));
                }

                pm = new PagerAdapter(getFragmentManager(), gridFragments);
                awesomePager.setAdapter(pm);
                mIndicator.setViewPager(awesomePager);

            }
            @Override
            public void onFailure(Call<PortalCatgData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void getPortalCategories() {
        //view.setEnabled(false);
        final PortalCatgRequest cities = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<PortalCatgData> call1 = apiInterface.getPortalCatg(cities);
        call1.enqueue(new Callback<PortalCatgData>() {
            @Override
            public void onResponse(Call<PortalCatgData> call, retrofit2.Response<PortalCatgData> response) {
                PortalCatgData portalCatgData = response.body();
                if (portalCatgData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                sharedPref.savePortalCatg(portalCatgData.result);
                Ittxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(0).name :
                        portalCatgData.result.get(0).nameEN);
               /* Picasso.get().load(portalCatgData.result.get(0).businessIcon64)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(ItImg);*/

                Glide.with(getContext()).load(portalCatgData.result.get(0).businessIcon64).into(ItImg);

                furnTxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(1).name :
                        portalCatgData.result.get(1).nameEN);
                /*Picasso.get().load(portalCatgData.result.get(1).businessIcon64)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(fuImage);*/

                Glide.with(getContext()).load(portalCatgData.result.get(1).businessIcon64).into(fuImage);

                homeTxt.setText(lang.equalsIgnoreCase("ar") ? portalCatgData.result.get(2).name :
                        portalCatgData.result.get(2).nameEN);
                /*Picasso.get().load(portalCatgData.result.get(2).businessIcon64)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(homeImg);*/
                Glide.with(getContext()).load(portalCatgData.result.get(2).businessIcon64).into(homeImg);

            }

            @Override
            public void onFailure(Call<PortalCatgData> call, Throwable t) {
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

    private class PagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;

        public PagerAdapter(FragmentManager fm, List fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {

        }
    }

}
