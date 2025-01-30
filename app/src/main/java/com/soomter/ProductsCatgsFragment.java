package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import com.soomter.utils.HozriontalSpacesItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsCatgsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsCatgsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsCatgsFragment extends Fragment implements View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Locale locale;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private RecyclerView vogueRv, bestPickedRv;
    private ProgressBar vogueProg, besPickedProg;

    public ProductsCatgsFragment() {
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
    public static ProductsCatgsFragment newInstance(String param1, String param2) {
        ProductsCatgsFragment fragment = new ProductsCatgsFragment();
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
        View v = inflater.inflate(R.layout.fragment_products_catgs, container, false);

        TextView bestPicked = (TextView) v.findViewById(R.id.best_picked_txt);
        TextView bestPickedShow = (TextView) v.findViewById(R.id.best_picked_txt_show);
        TextView vogueTxt = (TextView) v.findViewById(R.id.vogue_txt);
        TextView vogueShow = (TextView) v.findViewById(R.id.vogue_txt_show);
        EditText searchED = (EditText) v.findViewById(R.id.serch_ed);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        bestPicked.setTypeface(type);
        bestPickedShow.setTypeface(type);
        vogueTxt.setTypeface(type);
        vogueShow.setTypeface(type);
        searchED.setTypeface(type);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getActivity());
        lang = Locale.getDefault().getLanguage().toString();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.hspacing);

        LinearLayoutManager horizontalLayoutManagaer =
                new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        vogueRv = v.findViewById(R.id.vogue_rv);
        bestPickedRv = v.findViewById(R.id.best_picked_rv);

        vogueProg = v.findViewById(R.id.vogue_progress);
        besPickedProg = v.findViewById(R.id.best_picked_progress);

        vogueRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        vogueRv.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing_1dp)));

        bestPickedRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        bestPickedRv.addItemDecoration(new
                HozriontalSpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.hspacing_1dp)));

        getBestPromotedProducts();
        getBestPickedProducts();

        return v;
    }

    private void getBestPromotedProducts() {
        final PortalCatgRequest request = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<PromotedProductsData> call1 = apiInterface.getPromotedProduct(request);
        call1.enqueue(new Callback<PromotedProductsData>() {
            @Override
            public void onResponse(Call<PromotedProductsData> call, retrofit2.Response<PromotedProductsData> response) {
                PromotedProductsData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                PromotedProductsAdapter adapter = new PromotedProductsAdapter(getContext(), resp.result);
                //adapter.setClickListener(ProductsCatgsFragment.this);
                vogueRv.setVisibility(View.VISIBLE);
                vogueProg.setVisibility(View.GONE);
                vogueRv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PromotedProductsData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }


    private void getBestPickedProducts() {
        final PortalCatgRequest request = new PortalCatgRequest(Locale.getDefault().getLanguage().toString());
        Call<ProductsCatgData> call1 = apiInterface.getBestCategories(request);
        call1.enqueue(new Callback<ProductsCatgData>() {
            @Override
            public void onResponse(Call<ProductsCatgData> call, retrofit2.Response<ProductsCatgData> response) {
                ProductsCatgData resp = response.body();
                if (resp.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                BestProductsAdapter adapter = new BestProductsAdapter(getContext(), resp.result.subList(0,3));
                //adapter.setClickListener(ProductsCatgsFragment.this);
                bestPickedRv.setVisibility(View.VISIBLE);
                besPickedProg.setVisibility(View.GONE);
                bestPickedRv.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ProductsCatgData> call, Throwable t) {
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
