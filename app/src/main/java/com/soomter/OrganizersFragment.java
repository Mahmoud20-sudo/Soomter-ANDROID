package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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
import com.soomter.utils.SpacesItemDecoration3;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrganizersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrganizersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganizersFragment extends Fragment implements View.OnClickListener, BestCatgsAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Locale locale;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView ;

    private List<EventDetailData.Organizers> list;

    public OrganizersFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrganizersFragment newInstance(String param1) {
        OrganizersFragment fragment = new OrganizersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

            Gson gson = new Gson();
            Type listOfdoctorType = new TypeToken<List<EventDetailData.Organizers>>() {}.getType();
            list = gson.fromJson(mParam1,listOfdoctorType );

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_company_ads, container, false);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        if(!list.isEmpty()){
            mRecyclerView = v.findViewById(R.id.ads_rv);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            mRecyclerView.addItemDecoration(new
                    SpacesItemDecoration3(getResources().getDimensionPixelSize(R.dimen.hspacing)));

            OrganizersAdapter adapter = new OrganizersAdapter(getContext(), list);
            //adapter.setClickListener(this);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setAdapter(adapter);
        }
        else{
            View nodata_view = v.findViewById(R.id.nodata_view);
            nodata_view.setVisibility(View.VISIBLE);
            ImageView img = nodata_view.findViewById(R.id.nodata_img);
            img.setImageResource(R.mipmap.no_orgnizer);
            TextView title = nodata_view.findViewById(R.id.nodata_txt);
            title.setText(R.string.no_org);
            TextView breif = nodata_view.findViewById(R.id.nodata_brief_txt);
            title.setTypeface(type);
            breif.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
