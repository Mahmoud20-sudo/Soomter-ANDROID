package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import com.soomter.utils.SpacesItemDecoration2;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CompanyProductsFragment extends Fragment implements View.OnClickListener, CompanyProductsAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Locale locale;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private CompanyProfileData.Result resultObj;


    public CompanyProductsFragment() {
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
    public static CompanyProductsFragment newInstance(String jsonString) {
        CompanyProductsFragment fragment = new CompanyProductsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, jsonString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            Type type = new TypeToken<CompanyProfileData.Result>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_products_main2, container, false);

        v.findViewById(R.id.serch_panel).setVisibility(View.GONE);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        if (resultObj.listProducts.isEmpty()) {
            View nodata_view = v.findViewById(R.id.nodata_view);
            nodata_view.setVisibility(View.VISIBLE);
            ImageView img = nodata_view.findViewById(R.id.nodata_img);
            img.setImageResource(R.mipmap.products_off);
            TextView title = nodata_view.findViewById(R.id.nodata_txt);
            title.setText(R.string.no_product);
            TextView breif = nodata_view.findViewById(R.id.nodata_brief_txt);
            breif.setText(R.string.no_product_brief);
        } else {
            mRecyclerView = v.findViewById(R.id.products_rv);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRecyclerView.addItemDecoration(new
                    SpacesItemDecoration2(getResources().getDimensionPixelSize(R.dimen.hspacing)));

            CompanyProductsAdapter adapter = new CompanyProductsAdapter(getContext(), resultObj.listProducts);
            adapter.setClickListener(this);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(adapter);
        }

        return v;
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
    public void onItemClick(View view, int position) {

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
