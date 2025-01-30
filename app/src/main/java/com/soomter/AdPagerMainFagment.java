package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdPagerMainFagment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdPagerMainFagment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdPagerMainFagment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static List<?>[] adsListObj;

    Locale locale;
    ArrayList codeCategory;
    int[] types;
    String[] names;
    private String mParam1;
    private int catgId;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    public ViewPager awesomePager;
    private PagerAdapter pm;
    private int typeId;
    private TabLayout mTabLayout;
    private EditText serchEd;

    public AdPagerMainFagment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdPagerMainFagment newInstance(int param1, int param2, List<?>[] adsList) {
        AdPagerMainFagment fragment = new AdPagerMainFagment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        fragment.setArguments(args);
        adsListObj = adsList;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            typeId = getArguments().getInt(ARG_PARAM1);
            catgId = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.fragment_ads_pager, container, false);
        final SharedPref sharedPref = new SharedPref(getActivity().getApplicationContext());

        apiInterface = APIClient.getClient().create(APIInterface.class);
        awesomePager = (ViewPager) view.findViewById(R.id.ads_pager);

        List gridFragments = new ArrayList();

        pm = new PagerAdapter(getFragmentManager());
        names = new String[]{getString(R.string.ad_text),
                getString(R.string.ad_photos), getString(R.string.ad_videos), getString(R.string.add_special)};

        types = new int[]{1, 3, 4, 2};

        for (int i = 0; i < 4; i++) {
            //gridFragments.add(new AdTypeFragment(getActivity(), null));
            pm.addFrag(new AdTypeFragment(catgId,getActivity(), types[i], adsListObj[i]), names[i]);
        }
        awesomePager.setAdapter(pm);

        if (Locale.getDefault().getLanguage().toString().equals("ar")) {
            //Collections.reverse(pm.fragments);
            //awesomePager.setRotation(180);
            //awesomePager.setCurrentItem(0);
        }

        switch (typeId) {
            case 1:
                //spec
                awesomePager.setCurrentItem(0);
                break;
            case 2:
                //text
                awesomePager.setCurrentItem(3);
                break;
            case 3:
                //videos
                awesomePager.setCurrentItem(1);
                break;
            case 4:
                //images
                awesomePager.setCurrentItem(2);
                break;
        }

        awesomePager.setOffscreenPageLimit(1);
        mTabLayout = (TabLayout) view.findViewById(R.id.pager_header);
        mTabLayout.setupWithViewPager(awesomePager);

        changeTabsFont();


        return view;
    }



    private void changeTabsFont() {
        final Typeface typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeRoman);
                    ((TextView) tabViewChild).setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                    ((TextView) tabViewChild).setPadding(0,
                            (int) (-2 * getResources().getDisplayMetrics().density), 0, 0);
                }
            }
        }
    }

    private void getAllAds() {
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
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private List<Fragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
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

        public void addFrag(Fragment fragment, String title) {
            fragments.add(fragment);
            mFragmentTitleList.add(title);
        }

        public void replace(int index, Fragment fragment, String title) {
            fragments.remove(index);
            fragments.add(index, fragment);
            mFragmentTitleList.remove(index);
            mFragmentTitleList.add(title);
            // do the same for the title
            notifyDataSetChanged();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
