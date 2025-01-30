package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsProcessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsProcessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsProcessFragment extends Fragment implements View.OnClickListener, ProductAddressDialog.ClickEventListener, AddressesAdapter.ItemClickListener {
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
    private RecyclerView adressesRV, prodsRV;
    private View progress, emptyCartView;
    private TextView price_value_tv, ship_charges_value_tv, orders_total_value_tv, location_tv, delivery_value_tv;
    private ProductCartData.Result resultObj;

    public ProductsProcessFragment() {
        // Required empty public constructor
    }

    public static void setTitle(String title) {
        tvTitle.setText(title);
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
    public static ProductsProcessFragment newInstance(String param1, String param2) {
        ProductsProcessFragment fragment = new ProductsProcessFragment();
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
            String jsonString = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            Type type = new TypeToken<ProductCartData.Result>() {
            }.getType();
            resultObj = gson.fromJson(jsonString, type);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).bottomNavigationView.setVisibility(View.GONE);
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);

        final View v = inflater.inflate(R.layout.fragment_product_buying, container, false);
        //shipment_address_txt  ad_address_txt ad_address_img  adresses_rv shipment_details_txt  location_tv prods_rv
        //payment_summary_tv price_tv price_value_tv delivery_tv delivery_value_tv ship_charges_tv  ship_charges_value_tv
        //orders_total_tv orders_total_value_tv confirm_btn confirm_tv

        //tvTitle = (TextView) v.findViewById(R.id.products_title);
        TextView shipment_address_txt = (TextView) v.findViewById(R.id.shipment_address_txt);
        TextView ad_address_txt = (TextView) v.findViewById(R.id.ad_address_txt);
        ImageView ad_address_img = (ImageView) v.findViewById(R.id.ad_address_img);
        location_tv = (TextView) v.findViewById(R.id.location_tv);
        TextView shipment_details_txt = (TextView) v.findViewById(R.id.shipment_details_txt);
        TextView payment_summary_tv = (TextView) v.findViewById(R.id.payment_summary_tv);
        TextView price_tv = (TextView) v.findViewById(R.id.price_tv);
        price_value_tv = (TextView) v.findViewById(R.id.price_value_tv);
        TextView delivery_tv = (TextView) v.findViewById(R.id.delivery_tv);
        delivery_value_tv = (TextView) v.findViewById(R.id.delivery_value_tv);
        TextView ship_charges_tv = (TextView) v.findViewById(R.id.ship_charges_tv);
        ship_charges_value_tv = (TextView) v.findViewById(R.id.ship_charges_value_tv);
        TextView orders_total_tv = (TextView) v.findViewById(R.id.orders_total_tv);
        orders_total_value_tv = (TextView) v.findViewById(R.id.orders_total_value_tv);
        TextView confirm_tv = (TextView) v.findViewById(R.id.confirm_tv);
        LinearLayout confirm_btn = (LinearLayout) v.findViewById(R.id.confirm_btn);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");

        location_tv.setTypeface(type);
        ad_address_txt.setTypeface(type);

        delivery_tv.setTypeface(typeRoman);
        shipment_details_txt.setTypeface(typeRoman);
        price_tv.setTypeface(typeRoman);
        price_value_tv.setTypeface(typeRoman);
        ship_charges_tv.setTypeface(typeRoman);
        ship_charges_value_tv.setTypeface(typeRoman);
        shipment_address_txt.setTypeface(typeRoman);
        confirm_tv.setTypeface(typeRoman);

        payment_summary_tv.setTypeface(typeMedium);
        orders_total_tv.setTypeface(typeMedium);
        orders_total_value_tv.setTypeface(typeMedium);

        adressesRV = (RecyclerView) v.findViewById(R.id.adresses_rv);
        prodsRV = (RecyclerView) v.findViewById(R.id.prods_rv);

        // RecyclerView has some built in animations to it, using the DefaultItemAnimator.
        // Specifically when you call notifyItemChanged() it does a fade animation for the changing
        // of the data in the ViewHolder. If you would like to disable this you can use the following:
        RecyclerView.ItemAnimator animator = prodsRV.getItemAnimator();
        prodsRV.setLayoutManager(new LinearLayoutManager(getContext()));
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        RecyclerView.ItemAnimator animator2 = adressesRV.getItemAnimator();
        adressesRV.setLayoutManager(new LinearLayoutManager(getContext()));
        if (animator2 instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator2).setSupportsChangeAnimations(false);
        }

        sharedPref = new SharedPref(getContext());
        loadAddresses();

        ProductFinalAdapter adapter = new ProductFinalAdapter(getContext(), resultObj.Items);
        prodsRV.setAdapter(adapter);

        progress = v.findViewById(R.id.loadmore_progress);

        ProductsFragment.resetScroll();
        ProductsFragment.setTitle(getString(R.string.shopping_cart));

        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCart();
            }
        });

        location_tv.setText(getString(R.string.shipped_to) + " " +
                resultObj.ShippingCity + " -  " + getString(R.string.getit_on) + " " + resultObj.DeliverDate);

        ad_address_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddAddressDialog(null);
            }
        });

        ad_address_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddAddressDialog(null);
            }
        });

        int price = 0;
        for (ProductCartData.Items item : resultObj.Items) {
            price += item.Price;
        }
        int totalPrice = price + resultObj.ShippingExpenses;
        ship_charges_value_tv.setText(resultObj.ShippingExpenses + " " + getString(R.string.rial));
        price_value_tv.setText(price + " " + getString(R.string.rial));
        orders_total_value_tv.setText(totalPrice + " " + getString(R.string.rial));
        delivery_value_tv.setText(resultObj.CashOnDeliveryFees + " " + getString(R.string.rial));

        return v;
    }

    private void openAddAddressDialog(ProductAddreseData.Result obj) {
        Gson gson = new Gson();
        String jsonString = "";
        if (obj != null)
            jsonString = gson.toJson(obj);

        FragmentManager fm = getFragmentManager();
        ProductAddressDialog productAddressDialog = ProductAddressDialog.newInstance(jsonString);
        productAddressDialog.clickEventListener = this;
        productAddressDialog.show(fm, "ProductAddressDialog");
    }

    private void loadAddresses() {
        Call<ProductAddreseData> call1 = apiInterface.getAddresses(Locale.getDefault().toString(), sharedPref.getUser().id);
        call1.enqueue(new Callback<ProductAddreseData>() {
            @Override
            public void onResponse(Call<ProductAddreseData> call, retrofit2.Response<ProductAddreseData> response) {
                progress.setVisibility(View.GONE);
                ProductAddreseData productCartData = response.body();
                if (productCartData.result == null) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                AddressesAdapter addressesAdapter = new AddressesAdapter(getContext(), productCartData.result);
                addressesAdapter.setClickListener(ProductsProcessFragment.this);
                adressesRV.setAdapter(addressesAdapter);
            }

            @Override
            public void onFailure(Call<ProductAddreseData> call, Throwable t) {
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

    @Override
    public void click(int id, String name, String address, String mobile, ProductAddressDialog dialog) {
        dialog.dismiss();
        addEditAddress(id, name, address, mobile);
    }

    private void addEditAddress(int id ,String name, String address, String mobile) {
        //String UserId, String ReceiverName, String ContactTel, String Address
        AddAddressRequest addAddressRequest = new AddAddressRequest(sharedPref.getUser().id, name, mobile, address);
        Call<Void> call1 = apiInterface.addAddress(addAddressRequest);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.recover_success), Toast.LENGTH_LONG).show();
                    loadAddresses();
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

    private void processCart() {
        Call<Void> call1 = apiInterface.processCart(Locale.getDefault().toString(), sharedPref.getUser().id);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.confirm_success), Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
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

    @Override
    public void onItemClick(ProductAddreseData.Result obj) {
        openAddAddressDialog(obj);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
