package com.soomter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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

import com.soomter.utils.HozriontalSpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyBranchesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyBranchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductGeneralDescpFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    TextView Ittxt, furnTxt, homeTxt;
    ImageView ItImg, fuImage, homeImg;

    Locale locale;
    ProgressBar determinateBar;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private ProductDetailData.Datum resultObj;
    private View child;
    private LayoutInflater layoutInflater;
    private Typeface type, typeRoman;
    private LinearLayout parentView;

    public ProductGeneralDescpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductGeneralDescpFragment newInstance(String mParam1, String mParam2) {
        ProductGeneralDescpFragment fragment = new ProductGeneralDescpFragment();
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
        View v = inflater.inflate(R.layout.fragment_general_desc, container, false);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        LinearLayout.LayoutParams params = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);

        SimilarProductsAdapter adapter = new SimilarProductsAdapter(getContext(), resultObj.listSelectedProducts);
        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        RecyclerView recyclerView = v.findViewById(R.id.product_similar_rv);

        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing10)));

        recyclerView.setAdapter(adapter);

        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.scrollToPosition(0);

        //
        TextView desc_title = v.findViewById(R.id.desc_title);
        TextView product_adv_tv = v.findViewById(R.id.product_adv_tv);
        TextView product_sismilar_tv = v.findViewById(R.id.product_sismilar_tv);

        desc_title.setTypeface(type);
        product_adv_tv.setTypeface(typeRoman);
        product_sismilar_tv.setTypeface(typeRoman);

        desc_title.setText(resultObj.Details);

        for (int i = 0; i < resultObj.productsProperties.size(); i++) {
            TextView tt = new TextView(getContext());
            tt.setText(resultObj.productsProperties.get(i).Value + " " + resultObj.productsProperties.get(i).Text);
            tt.setGravity(Locale.getDefault().getLanguage().toString().equals("en") ? Gravity.LEFT : Gravity.RIGHT);
            tt.setBackgroundResource(R.drawable.rounded_white_view3);
            tt.setPadding(20, 5, 20, 5);
            tt.setLayoutParams(params);
            tt.setTypeface(type);
            tt.setTextColor(Color.parseColor("#606060"));
            ((LinearLayout) v.findViewById(R.id.avntgs_container)).addView(tt);
        }

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
