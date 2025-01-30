package com.soomter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
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
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CompanyBranchesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CompanyBranchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Locale locale;
    ProgressBar determinateBar;
    TextView name, productModel, productDiscount, productPrice, productOldPrice, taxTxt, shippingTxt, locTxt,
            timeTxt, colorTxt, quantityTxt, sizeTxt, xsTv, sTv, mTv, lTv, xlTv, xxlTv, productSeller,
            productAvailability, addtocartTv, genName, ratingsName, charsName;
    int selectedColors ;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private EventDetailData.Datum resultObj;
    private View child;
    private LayoutInflater layoutInflater;
    private Typeface type, typeRoman, typeMedium;
    private int position;
    private CardView genCrdView, ratingCrdView, charsCrdView;
    private MySpinnerDialog myInstance;
    private ViewPager viewPager;
    private CirclePageIndicator circleIndicator;
    private ProductsFragmentAll productsFragmentAll;
    private ProductsFragmentMain productsFragmentMain;
    private BestSellingData.Datum obj;
    private ImageView favIcon;
    private ImageView selectedColorView;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ProductDetailsFragment newInstance(ProductsFragmentMain productsFragmentMain,
                                                     String mParam1, int mParam2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        args.putInt(ARG_PARAM2, mParam2);
        fragment.setArguments(args);
        fragment.productsFragmentMain = productsFragmentMain;
        return fragment;
    }

    // TODO: Rename and change types and number of parameters
    public static ProductDetailsFragment newInstance(ProductsFragmentAll productsFragmentAll,
                                                     String mParam1, int mParam2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, mParam1);
        args.putInt(ARG_PARAM2, mParam2);
        fragment.setArguments(args);
        fragment.productsFragmentAll = productsFragmentAll;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String jsonString = getArguments().getString(ARG_PARAM1);
            position = getArguments().getInt(ARG_PARAM2);
            Gson gson = new Gson();
            Type type = new TypeToken<BestSellingData.Datum>() {
            }.getType();
            obj = gson.fromJson(jsonString, type);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product_details, container, false);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        sharedPref = new SharedPref(getContext());

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");

        viewPager = (ViewPager) v.findViewById(R.id.view_pager);

        circleIndicator = (CirclePageIndicator) v.findViewById(R.id.indicator);

        LinearLayout addToCart = (LinearLayout)v.findViewById(R.id.add_cart_btn);

        ImageView leftNav = (ImageView) v.findViewById(R.id.left_arrow);
        ImageView rightNav = (ImageView) v.findViewById(R.id.right_arrow);

        // Images left navigation
        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                if (tab > 0) {
                    tab--;
                    viewPager.setCurrentItem(tab);
                } else if (tab == 0) {
                    viewPager.setCurrentItem(tab);
                }
            }
        });

        // Images right navigatin
        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager.getCurrentItem();
                tab++;
                viewPager.setCurrentItem(tab);
            }
        });

        /*final int colors[] = {R.color.red, R.color.black, R.color.blue, R.color.green};

        colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.setOnColorSelectedListener(this);
        colorPickerDialog.initialize(
                R.string.pick_color, colors, R.color.red, 5, colors.length);*/

        final EditText countEd = v.findViewById(R.id.count_ed);

        v.findViewById(R.id.plus_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countEd.setText(String.valueOf(Integer.parseInt(countEd.getText().toString()) + 1));
            }
        });

        v.findViewById(R.id.minus_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(countEd.getText().toString()) - 1 < 1) {
                    countEd.setText(String.valueOf(1));
                    return;
                }
                countEd.setText(String.valueOf(Integer.parseInt(countEd.getText().toString()) - 1));
            }
        });

        genCrdView = v.findViewById(R.id.gen_container);
        charsCrdView = v.findViewById(R.id.chars_container);
        ratingCrdView = v.findViewById(R.id.ratings_container);

        selectedColorView = (ImageView)v.findViewById(R.id.selected_color_img);

        clearSelection();
        genCrdView.setElevation(10);
        genCrdView.setCardBackgroundColor(Color.parseColor("#FFDB92"));

        name = v.findViewById(R.id.product_name);
        productModel = v.findViewById(R.id.product_model);
        productDiscount = v.findViewById(R.id.product_discount);
        productPrice = v.findViewById(R.id.product_price);
        productOldPrice = v.findViewById(R.id.product_old_price);
        taxTxt = v.findViewById(R.id.tax_txt);
        shippingTxt = v.findViewById(R.id.shipping_txt);
        locTxt = v.findViewById(R.id.loc_txt);
        timeTxt = v.findViewById(R.id.time_txt);
        quantityTxt = v.findViewById(R.id.quantity_txt);
        colorTxt = v.findViewById(R.id.color_txt);
        sizeTxt = v.findViewById(R.id.size_txt);

        xsTv = v.findViewById(R.id.xs_tv);
        sTv = v.findViewById(R.id.s_tv);
        mTv = v.findViewById(R.id.m_tv);
        lTv = v.findViewById(R.id.l_tv);
        xlTv = v.findViewById(R.id.xl_tv);
        xxlTv = v.findViewById(R.id.xxl_tv);
        productSeller = v.findViewById(R.id.product_seller);
        productAvailability = v.findViewById(R.id.product_availability);
        addtocartTv = v.findViewById(R.id.add_to_cart_tv);
        genName = v.findViewById(R.id.gen_name);
        ratingsName = v.findViewById(R.id.ratings_name);
        charsName = v.findViewById(R.id.chars_name);

        colorTxt.setTypeface(type);
        charsName.setTypeface(type);
        ratingsName.setTypeface(type);
        genName.setTypeface(type);
        productAvailability.setTypeface(type);
        productSeller.setTypeface(type);
        addtocartTv.setTypeface(typeRoman);

        xxlTv.setTypeface(typeRoman);
        xlTv.setTypeface(typeRoman);
        lTv.setTypeface(typeRoman);
        mTv.setTypeface(typeRoman);
        sTv.setTypeface(typeRoman);
        xsTv.setTypeface(typeRoman);
        addtocartTv.setTypeface(typeRoman);

        sizeTxt.setTypeface(type);
        quantityTxt.setTypeface(type);
        timeTxt.setTypeface(type);
        locTxt.setTypeface(type);
        shippingTxt.setTypeface(type);

        taxTxt.setTypeface(type);
        productOldPrice.setTypeface(typeRoman);
        productPrice.setTypeface(typeMedium);
        productDiscount.setTypeface(typeMedium);
        productModel.setTypeface(type);
        name.setTypeface(typeRoman);

        myInstance = new MySpinnerDialog();
        myInstance.setCancelable(false);
        myInstance.show(getFragmentManager(), "some_tag");

        productDetailsRequest();

        favIcon = (ImageView) v.findViewById(R.id.fav_img);

        favIcon.setImageResource(obj.IsFavorite > 0 ? R.mipmap.ad_details_star : R.mipmap.fav_star);

        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPref.getUser() == null) {
                    Toast.makeText(getContext(), getString(R.string.need_login), Toast.LENGTH_LONG).show();
                    return;
                }
                favouriteReq();
            }
        });


        selectedColorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorPicker colorPicker = new ColorPicker(getActivity());
                colorPicker.setTitle(getString(R.string.pick_color));
                colorPicker.getPositiveButton().setText(getString(R.string.ok));
                colorPicker.getNegativeButton().setText(getString(R.string.cancel));
                colorPicker.getPositiveButton().setTypeface(type);
                colorPicker.getNegativeButton().setTypeface(type);

                colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        // put code
                        selectedColors = color;
                        Drawable mDrawable = ContextCompat.getDrawable(getContext(), R.drawable.red_circle);
                        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
                            selectedColorView.setImageDrawable(mDrawable);
                    }

                    @Override
                    public void onCancel() {
                        // put code
                    }
                });

                colorPicker.show();
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedPref.getUser() == null){
                    Toast.makeText(getContext(), getString(R.string.need_login) , Toast.LENGTH_LONG).show();
                    return;
                }
                if(Integer.parseInt(countEd.getText().toString()) == 0){
                    Toast.makeText(getContext(), getString(R.string.invalid_quant) , Toast.LENGTH_LONG).show();
                    return;
                }
                addToCart(Integer.parseInt(countEd.getText().toString()));
            }
        });

        return v;
    }

    private void favouriteReq() {//  String UserId, int ProductId, int CompanyId, int ProductFieldValueId
        final ProductFavRequest request = new ProductFavRequest(sharedPref.getUser().id, obj.id, obj.CompanyId, obj.ProductFieldValueId);
        Call<ProductFavData> call1 = obj.IsFavorite > 0 ? apiInterface.removeFavProduct(request)
                : apiInterface.addFavProduct(request);
        call1.enqueue(new Callback<ProductFavData>() {
            @Override
            public void onResponse(Call<ProductFavData> call, retrofit2.Response<ProductFavData> response) {
                ProductFavData resp = response.body();
                if (resp.result == false) {
                    Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                if (obj.IsFavorite > 0) {
                    obj.IsFavorite = 0;
                    favIcon.setImageResource(R.mipmap.fav_star);
                } else {
                    obj.IsFavorite = Math.abs(new Random().nextInt());
                    favIcon.setImageResource(R.mipmap.ad_details_star);
                }
                if (productsFragmentAll != null)
                    productsFragmentAll.updateProductList(position, obj);
                else
                    productsFragmentMain.updateProductList(position, obj);

            }

            @Override
            public void onFailure(Call<ProductFavData> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void addToCart(int quantity) {//  String UserId, int ProductId, int CompanyId, int ProductFieldValueId
        final ProductCartRequest request = new ProductCartRequest(sharedPref.getUser().id ,obj.id, quantity, obj.ProductFieldValueId);
        Call<Void> call1 = apiInterface.addToCart(request);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getContext(), getString(R.string.added_success), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_LONG).show();
                /*if (obj.IsFavorite > 0) {
                    obj.IsFavorite = 0;
                    favIcon.setImageResource(R.mipmap.fav_star);
                } else {
                    obj.IsFavorite = Math.abs(new Random().nextInt());
                    favIcon.setImageResource(R.mipmap.ad_details_star);
                }
                if (productsFragmentAll != null)
                    productsFragmentAll.updateProductList(position, obj);
                else
                    productsFragmentMain.updateProductList(position, obj);*/
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(),getString(R.string.error),Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void clearSelection() {
        genCrdView.setCardBackgroundColor(Color.parseColor("#FBFBFB"));
        charsCrdView.setCardBackgroundColor(Color.parseColor("#FBFBFB"));
        ratingCrdView.setCardBackgroundColor(Color.parseColor("#FBFBFB"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            genCrdView.setElevation(0);
            charsCrdView.setElevation(0);
            ratingCrdView.setElevation(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void productDetailsRequest() {
        ProductDetailsRequest request = new ProductDetailsRequest(obj.id, obj.ProductFieldValueId, "",
                Locale.getDefault().getLanguage().toString());
        Call<ProductDetailData> call1 = apiInterface.getProductDetails(request);
        call1.enqueue(new Callback<ProductDetailData>() {
            @Override
            public void onResponse(Call<ProductDetailData> call, Response<ProductDetailData> response) {
                myInstance.dismiss();
                final ProductDetailData productDetailData = response.body();
                if (productDetailData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }
                name.setText(productDetailData.result.Title);
                productModel.setText(productDetailData.result.TradeMark);
                productDiscount.setText(getString(R.string.discount) + " " + productDetailData.result.Discount + "%");
                productPrice.setText(productDetailData.result.PriceAfterDis + " " + getString(R.string.rial));
                productOldPrice.setText(productDetailData.result.Price + " " + getString(R.string.rial));
                productOldPrice.setPaintFlags(productOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                taxTxt.setText(productDetailData.result.IncludesValueAddedstring);
                shippingTxt.setText(productDetailData.result.FreeShipping ? getString(R.string.free_ship) : "");
                timeTxt.setText(productDetailData.result.ShippingDate == null ? getString(R.string.no_inform) :
                        productDetailData.result.ShippingDate);
                locTxt.setText(productDetailData.result.ShippingCityId == null ? getString(R.string.no_inform) :
                        productDetailData.result.ShippingCityId);
                productSeller.setText(getString(R.string.seller) + " " + productDetailData.result.CompanyName);

                ImagePagerAdapter adapter = new ImagePagerAdapter(productDetailData.result.imageLists);
                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);

                Gson gson = new Gson();
                final String jsonString = gson.toJson(productDetailData.result);

                getChildFragmentManager().beginTransaction().replace(R.id.container,
                        ProductGeneralDescpFragment.newInstance(jsonString, null)).commit();

                charsCrdView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelection();
                        charsCrdView.setElevation(10);
                        charsCrdView.setCardBackgroundColor(Color.parseColor("#FFDB92"));
                        getChildFragmentManager().beginTransaction().replace(R.id.container,
                                ProductPropertiesFragment.newInstance(jsonString, null)).commit();
                    }
                });

                genCrdView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelection();
                        genCrdView.setElevation(10);
                        genCrdView.setCardBackgroundColor(Color.parseColor("#FFDB92"));
                        getChildFragmentManager().beginTransaction().replace(R.id.container,
                                ProductGeneralDescpFragment.newInstance(jsonString, null)).commit();
                    }
                });

                ratingCrdView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelection();
                        ratingCrdView.setElevation(10);
                        ratingCrdView.setCardBackgroundColor(Color.parseColor("#FFDB92"));
                        getChildFragmentManager().beginTransaction().replace(R.id.container,
                                ProductRatingsFragment.newInstance(jsonString, null)).commit();
                    }
                });
            }

            @Override
            public void onFailure(Call<ProductDetailData> call, Throwable t) {
                myInstance.dismiss();
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
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

        private List<ProductDetailData.ImageList> imglist;

        public ImagePagerAdapter(List<ProductDetailData.ImageList> imglist) {
            this.imglist = imglist;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return this.imglist.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Context context = getContext();
            final ImageView imageView = new ImageView(context);
            final int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_medium);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Glide.with(getContext()).load(imglist.get(position).Image)
                    .into(imageView);
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == ((ImageView) object);
        }
    }
}
