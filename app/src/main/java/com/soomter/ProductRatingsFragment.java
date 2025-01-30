package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyBranchesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyBranchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductRatingsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private ProductDetailData.Datum resultObj;
    private View child;
    private LayoutInflater layoutInflater;
    private Typeface type ,typeRoman;
    private LinearLayout parentView;

    public ProductRatingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductRatingsFragment newInstance(String mParam1, String mParam2) {
        ProductRatingsFragment fragment = new ProductRatingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            Type type = new TypeToken<ProductDetailData.Datum>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_ratings, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        ProductRatingsAdapter adapter = new ProductRatingsAdapter(getContext(), resultObj.listRating);

        RecyclerView recyclerView = v.findViewById(R.id.product_ratings_rv);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(adapter);

        recyclerView.scrollToPosition(0);

        v.findViewById(R.id.rating_write_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                RoundedDialog editNameDialogFragment = RoundedDialog.newInstance(1);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        TextView product_sismilar_tv = v.findViewById(R.id.product_sismilar_tv);
        TextView rating_count = v.findViewById(R.id.rating_count);
        TextView rating_txt = v.findViewById(R.id.rating_txt);
        TextView rate_txt = v.findViewById(R.id.rate_txt);
        TextView rating_num = v.findViewById(R.id.rating_num);
        TextView rating_write_txt = v.findViewById(R.id.rating_write_txt);

        product_sismilar_tv.setTypeface(type);
        rating_count.setTypeface(typeRoman);
        rating_txt.setTypeface(type);
        rate_txt.setTypeface(type);
        rating_num.setTypeface(type);
        rating_write_txt.setTypeface(type);


        return v;
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

    private class ImagePagerAdapter extends PagerAdapter {
        private final int[] mImages = new int[]{
                R.mipmap.side_menu_ads_institutes,
                R.mipmap.side_menu_ads_institutes,
                R.mipmap.side_menu_ads_institutes,
                R.mipmap.side_menu_ads_institutes
        };

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return this.mImages.length;
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Context context = getContext();
            final ImageView imageView = new ImageView(context);
            final int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setImageResource(this.mImages[position]);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == ((ImageView) object);
        }
    }
}
