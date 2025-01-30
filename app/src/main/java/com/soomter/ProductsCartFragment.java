package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsCartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsCartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsCartFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static LockableScrollView scrollView;
    public static LinearLayout eventScollView;
    public static View productTopMenu;
    public static int IsFavourite;
    private static ImageView gearImg;
    private static TextView tvTitle;
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
    private SharedPref prefObj;
    private LinearLayout navDrawer;
    private Typeface type, typeMedium, typeRoman;
    private FragmentTransaction ft;
    private int animeValue;
    private RecyclerView recyclerView;
    private View progress, emptyCartView;
    private TextView price_value_tv, ship_charges_value_tv, orders_total_value_tv, products_own_tv;
    private LinearLayout continou_checkout_btn;

    public ProductsCartFragment() {
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
    public static ProductsCartFragment newInstance(String param1, String param2) {
        ProductsCartFragment fragment = new ProductsCartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //  bottomNavigationView.setVisibility(View.GONE);
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
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_cart, container, false);
        //empty_cart_tv empty_cart_brief_tv open_prods_btn open_prods_tv   button_send products_own_tv
        //         price_tv  price_value_tv  ship_charges_tv ship_charges_value_tv orders_total_tv orders_total_value_tv
        // continou_checkout_btn continou_checkout_tv

        //tvTitle = (TextView) v.findViewById(R.id.products_title);
        TextView empty_cart_tv = (TextView) v.findViewById(R.id.empty_cart_tv);
        TextView empty_cart_brief_tv = (TextView) v.findViewById(R.id.empty_cart_brief_tv);
        TextView open_prods_tv = (TextView) v.findViewById(R.id.open_prods_tv);
        products_own_tv = (TextView) v.findViewById(R.id.products_own_tv);
        TextView price_tv = (TextView) v.findViewById(R.id.price_tv);
        price_value_tv = (TextView) v.findViewById(R.id.price_value_tv);
        TextView ship_charges_tv = (TextView) v.findViewById(R.id.ship_charges_tv);
        ship_charges_value_tv = (TextView) v.findViewById(R.id.ship_charges_value_tv);
        TextView orders_total_tv = (TextView) v.findViewById(R.id.orders_total_tv);
        orders_total_value_tv = (TextView) v.findViewById(R.id.orders_total_value_tv);
        TextView continou_checkout_tv = (TextView) v.findViewById(R.id.continou_checkout_tv);

        LinearLayout open_prods_btn = (LinearLayout) v.findViewById(R.id.open_prods_btn);
         continou_checkout_btn = (LinearLayout) v.findViewById(R.id.continou_checkout_btn);
        Button button_continue_shopping = (Button) v.findViewById(R.id.button_continue_shopping);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        //tvTitle.setTypeface(type);
        //tvTitle.setText(getString(R.string.products));
        button_continue_shopping.setTypeface(type);
        empty_cart_tv.setTypeface(type);
        empty_cart_brief_tv.setTypeface(type);

        open_prods_tv.setTypeface(typeRoman);
        price_tv.setTypeface(typeRoman);
        price_value_tv.setTypeface(typeRoman);
        ship_charges_tv.setTypeface(typeRoman);
        ship_charges_value_tv.setTypeface(typeRoman);
        continou_checkout_tv.setTypeface(typeRoman);

        products_own_tv.setTypeface(typeMedium);
        orders_total_tv.setTypeface(typeMedium);
        orders_total_value_tv.setTypeface(typeMedium);

        recyclerView = (RecyclerView) v.findViewById(R.id.cart_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        recyclerView.setLayoutManager(layoutManager);
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }

        sharedPref = new SharedPref(getContext());
        loadCart();

        progress = v.findViewById(R.id.loadmore_progress);
        emptyCartView = v.findViewById(R.id.empty_cart_view);

        ProductsFragment.setTitle(getString(R.string.shopping_cart));

        button_continue_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.products_fragment_container,
                                ProductsFragmentAll.newInstance(null, null), "ProductsFragmentAll")
                        .commit();
                ProductsFragment.setTitle(getString(R.string.product));
            }
        });

        open_prods_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.products_fragment_container,
                                ProductsFragmentAll.newInstance(null, null), "ProductsFragmentAll")
                        .commit();
                ProductsFragment.setTitle(getString(R.string.product));
            }
        });


        return v;
    }

    private void loadCart() {
        Call<ProductCartData> call1 = apiInterface.loadCart(Locale.getDefault().toString(), sharedPref.getUser().id);
        call1.enqueue(new Callback<ProductCartData>() {
            @Override
            public void onResponse(Call<ProductCartData> call, retrofit2.Response<ProductCartData> response) {
                progress.setVisibility(View.GONE);
                ProductCartData productCartData = response.body();
                if (productCartData.result == null) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (productCartData.result.Items.isEmpty()) {
                    emptyCartView.setVisibility(View.VISIBLE);
                    return;
                }

                int price = 0;
                for (ProductCartData.Items item : productCartData.result.Items) {
                    price += item.Price;
                }
                int totalPrice = price + productCartData.result.ShippingExpenses;
                /*products_own_tv.setText(getString(R.string.product_own) + " (" +
                        ((productCartData.result.Items.size()) + " )" + getString(R.string.product)));*/

                products_own_tv.setText(Html.fromHtml(getString(R.string.product_own) + "( "
                        + "<font color=#D9b878>" + ((productCartData.result.Items.size()) + "</font>"
                        + " )" + getString(R.string.product))));

                //ship_charges_value_tv.setText(productCartData.result.ShippingExpenses+"");
                ship_charges_value_tv.setText(productCartData.result.ShippingExpenses + " " + getString(R.string.rial));
                price_value_tv.setText(price  + " " + getString(R.string.rial));
                orders_total_value_tv.setText(totalPrice  + " " + getString(R.string.rial));

                CartAdapter adapter = new CartAdapter(getContext(), productCartData.result.Items);
                recyclerView.setAdapter(adapter);

                final String jsonString = new Gson().toJson(productCartData.result);

                continou_checkout_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.products_fragment_container,
                                        ProductsProcessFragment.newInstance(jsonString
                                                , null), "ProductsProcessFragment")
                                .commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductCartData> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
                progress.setVisibility(View.GONE);
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



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
