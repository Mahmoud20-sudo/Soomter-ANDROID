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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soomter.expand.GenreAdapter;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

import static com.soomter.GenreDataFactory.makeGenres;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static LockableScrollView scrollView;
    public static LinearLayout eventScollView;
    public static View productTopMenu;
    public static int IsFavourite;
    public static ImageView productFav;
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
    private Typeface type;
    private FragmentTransaction ft;
    private int animeValue;
    private RecyclerView recyclerView;

    public ProductsFragment() {
        // Required empty public constructor
    }

    public static void resetScroll() {
        scrollView.scrollTo(0, 0);
    }

    public static void hideView() {
        productTopMenu.setVisibility(View.GONE);
        gearImg.setVisibility(View.GONE);
    }

    public static void showView() {
        productTopMenu.setVisibility(View.VISIBLE);
        gearImg.setVisibility(View.VISIBLE);
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
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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

        final View v = inflater.inflate(R.layout.fragment_products, container, false);

        productFav = (ImageView) v.findViewById(R.id.fav_img);

        gearImg = v.findViewById(R.id.gear);
        productTopMenu = v.findViewById(R.id.product_top_menu);
        productTopMenu.setVisibility(View.VISIBLE);

        tvTitle = (TextView) v.findViewById(R.id.products_title);
        TextView allEvents = (TextView) v.findViewById(R.id.all_products_txt);
        TextView cartTxt = (TextView) v.findViewById(R.id.cart_txt);
        TextView favEvents = (TextView) v.findViewById(R.id.products_fav);

        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");

        tvTitle.setTypeface(type);
        cartTxt.setTypeface(type);
        allEvents.setTypeface(type);
        favEvents.setTypeface(type);

        final FragmentManager fm = getFragmentManager();
        ft = fm.beginTransaction();

        /*getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.products_fragment_container,
                        CategMainFragment.newInstance(null, 2))
                .commit();*/
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.products_fragment_container,
                        ProductsFragmentAll.newInstance(null, null), "ProductsFragmentAll")
                .commit();

        animeValue = Locale.getDefault().toString().equalsIgnoreCase("ar") ? 1200 : -1200;

        eventScollView = v.findViewById(R.id.product_nav_scroller);
        eventScollView.bringToFront();
        eventScollView.animate().setDuration(0).translationX(animeValue);

        navDrawer = v.findViewById(R.id.events_nav_drawer);//.animate().translationX(-500);

        // setNavData(new EventsCountsData().result);

        //eventsRequest();

        scrollView = (LockableScrollView) v.findViewById(R.id.nested_scroll_view);

        final ImageView menuIcon = v.findViewById(R.id.menu_icon);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventScollView.getTranslationX() == 0) {
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    // bottomNavigationView.setVisibility(View.VISIBLE);
                    scrollView.setScrollingEnabled(true);
                } else {
                    eventScollView.animate().setDuration(100).translationX(0);
                    scrollView.setScrollingEnabled(false);
                    //  bottomNavigationView.setVisibility(View.GONE);
                }
            }
        });

        v.findViewById(R.id.bck_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventScollView.getTranslationX() == 0) {
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    scrollView.setScrollingEnabled(true);
                } else {
                    getActivity().getSupportFragmentManager().popBackStack();
                    setTitle(getString(R.string.product));
                    showView();
                }
            }
        });

        /*scrollView.setOnTouchListener(new View.OnTouchListener() {
            float y1 = 0;
            float y0 = 0;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    y0 = motionEvent.getY();
                    eventScollView.animate().setDuration(100).translationX(animeValue);
                    Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    y1 = motionEvent.getY();
                    if (y0 - y1 > 0) {
                        bottomNavigationViewBehavior.slideDown(bottomNavigationView);
                        Log.i("Y", motionEvent.getAction() + " + " + (y0 - y1));
                    } else if (y0 - y1 < 0) {
                        bottomNavigationViewBehavior.slideUp(bottomNavigationView);
                        Log.e("Y", motionEvent.getAction() + " - " + (y0 - y1));
                    }
                }

                return false;
            }
        });*/

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY(); //for verticalScrollView
                if (scrollY == 0)
                    menuIcon.setEnabled(true);
                else
                    menuIcon.setEnabled(false);
            }
        });

        recyclerView = (RecyclerView) v.findViewById(R.id.nav_recycler_view);
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
        List<ProductCatgsTreeData.Datum> result = sharedPref.getTree();
        if (result != null) {
            GenreAdapter adapter = new GenreAdapter(getContext(), makeGenres(result));
            recyclerView.setAdapter(adapter);
        } else
            getProductCatgsTree();

        productFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (prefObj.getUser() == null) {
                    Toast.makeText(getContext(), getString(R.string.require_login), Toast.LENGTH_LONG).show();
                    return;
                }

                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.products_fragment_container,
                                ProductsFragmentMain.newInstance(null, null, ""))
                        .addToBackStack(null).commit();
                /*if ((ProductsFragmentAll) getActivity().getSupportFragmentManager()
                        .findFragmentByTag("ProductsFragmentAll") != null) {

                    ProductsFragmentAll fragmentAll = (ProductsFragmentAll) getActivity().getSupportFragmentManager()
                            .findFragmentByTag("ProductsFragmentAll");

                    if (IsFavourite == 1) {
                        IsFavourite = 0;
                        fragmentAll.isFavourite = IsFavourite;
                        productFav.setImageResource(R.mipmap.star_empty);
                        fragmentAll.getBestCatgs();
                    } else {
                        IsFavourite = 1;
                        fragmentAll.isFavourite = IsFavourite;
                        productFav.setImageResource(R.mipmap.ad_details_star);
                        fragmentAll.getFavProducts();
                    }
                }else {

                    ProductsFragmentMain fragmentMain = (ProductsFragmentMain) getActivity().getSupportFragmentManager()
                            .findFragmentByTag("ProductsFragmentMain");

                    if (IsFavourite == 1) {
                        IsFavourite = 0;
                        fragmentMain.isFavourite = IsFavourite;
                        productFav.setImageResource(R.mipmap.star_empty);
                        fragmentMain.getBestCatgs();
                    } else {
                        IsFavourite = 1;
                        fragmentMain.isFavourite = IsFavourite;
                        productFav.setImageResource(R.mipmap.ad_details_star);
                        fragmentMain.getFavProducts();
                    }
                }*/

                /*ft = fm.beginTransaction();
                ft.remove(getActivity().getSupportFragmentManager()
                        .findFragmentByTag("ProductsFragmentAll")).commitAllowingStateLoss();*/
               /* Bundle arguments = new Bundle();
                arguments.putBoolean("shouldYouCreateAChildFragment", true);
                arguments.putInt("IsFavourit", IsFavourite);
                fragOne.setArguments(arguments);
                ft = fm.beginTransaction();
                ft.add(R.id.products_fragment_container, fragOne, "ProductsFragmentAll");
                ft.commit();*/
            }

        });

        cartTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });

        v.findViewById(R.id.cart_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCart();
            }
        });

        return v;
    }


    private void openCart() {
        if (sharedPref.getUser() == null) {
            Toast.makeText(getContext(), getString(R.string.need_login), Toast.LENGTH_LONG).show();
            return;
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.products_fragment_container,
                        ProductsCartFragment.newInstance(null, null))
                .addToBackStack(null).commit();
    }

    private void getProductCatgsTree() {
        Call<ProductCatgsTreeData> call1 = apiInterface.getProductCatgsTree(Locale.getDefault().toString());
        call1.enqueue(new Callback<ProductCatgsTreeData>() {
            @Override
            public void onResponse(Call<ProductCatgsTreeData> call, retrofit2.Response<ProductCatgsTreeData> response) {
                ProductCatgsTreeData productSubCatgsData = response.body();
                if (productSubCatgsData.result == null) {
                    // Toast.makeText(getApplicationContext(),getString(R.string.wrong_user),Toast.LENGTH_LONG).show();
                    return;
                }
                prefObj.saveProductCatgsTree(productSubCatgsData.result);
                GenreAdapter adapter = new GenreAdapter(getContext(), makeGenres(productSubCatgsData.result));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ProductCatgsTreeData> call, Throwable t) {
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

   /* @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bottomNavigationView = (ExtendedBottomNavigationView) getActivity().findViewById(R.id.the_bottom_navigation);

        bottomNavigationView.setVisibility(View.VISIBLE);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        CoordinatorLayout.LayoutParams layoutParams2 = (CoordinatorLayout.LayoutParams) bottomNavigationView.getLayoutParams();
        bottomNavigationViewBehavior = new BottomNavigationViewBehavior();
        layoutParams2.setBehavior(bottomNavigationViewBehavior);
        bottomNavigationViewBehavior.slideUp(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        return true;
                    }
                });

        bottomNavigationView.setSelectedItemId(R.id.action_item1);
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }

    }*/

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

}
