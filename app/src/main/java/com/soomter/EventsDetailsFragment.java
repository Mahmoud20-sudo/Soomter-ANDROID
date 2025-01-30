package com.soomter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsDetailsFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    Locale locale;
    TextView nowHappen, eventTime, eventGeorgian, eventHijri, eventName, eventDetailsTitle, eventDetails, eventLocation, eventCatg;
    ImageView eventFavImg, eventImg;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private APIInterface apiInterface;
    private SharedPref sharedPref;
    private String lang;
    private SharedPref prefObj;
    private ExtendedBottomNavigationView bottomNavigationView;
    private int Id;
    private Typeface type, typeRoman, typeMedium;
    private int width;
    private FragmentTransaction ft;
    private MySpinnerDialog myInstance;
    private CardView orgContainer, detailsContainer, locContainer;
    private TextView orgNameTv, orgDetTv, locNameTv;

    public EventsDetailsFragment() {
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
    public static EventsDetailsFragment newInstance(int param1, String param3) {
        EventsDetailsFragment fragment = new EventsDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bitmap getRoundCornerBitmap(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final RectF rectF = new RectF(0, 0, w, h);

        canvas.drawRoundRect(rectF, radius, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        /**
         * here to define your corners, this is for left bottom and right bottom corners
         */
        //final Rect clipRect = new Rect(0, 0, w, h - radius);3
        final Rect clipRect = new Rect(0, radius, w + radius, h + radius);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawRect(clipRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rectF, paint);

        bitmap.recycle();

        return output;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        prefObj = new SharedPref(getActivity().getApplicationContext());
        apiInterface = APIClient.getClient().create(APIInterface.class);
        type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Roman.ttf");
        typeMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/SST-Arabic-Medium.ttf");

        myInstance = new MySpinnerDialog();
        myInstance.setCancelable(false);

        myInstance.show(getFragmentManager(), "some_tag");

        final View v = inflater.inflate(R.layout.fragment_events_details, container, false);

        orgContainer = (CardView) v.findViewById(R.id.orgnizers_container);
        locContainer = (CardView) v.findViewById(R.id.ev_loc_container);
        detailsContainer = (CardView) v.findViewById(R.id.details_container);

        orgNameTv = v.findViewById(R.id.orgnizers_name);
        locNameTv = v.findViewById(R.id.ev_loc_name);
        orgDetTv = v.findViewById(R.id.org_dets_name);

        nowHappen = (TextView) v.findViewById(R.id.event_now_happen);
        eventTime = (TextView) v.findViewById(R.id.event_time);
        eventGeorgian = (TextView) v.findViewById(R.id.event_georgian);
        eventHijri = (TextView) v.findViewById(R.id.event_hijri);
        eventName = (TextView) v.findViewById(R.id.event_name);
        eventDetailsTitle = (TextView) v.findViewById(R.id.event_details_title);
        eventDetails = (TextView) v.findViewById(R.id.event_details_text);
        eventLocation = (TextView) v.findViewById(R.id.event_location);
        eventCatg = (TextView) v.findViewById(R.id.event_catg);

        nowHappen.setTypeface(typeRoman);
        eventTime.setTypeface(type);
        eventGeorgian.setTypeface(type);
        eventHijri.setTypeface(type);
        eventName.setTypeface(typeMedium);
        eventDetailsTitle.setTypeface(typeMedium);
        eventDetails.setTypeface(type);
        eventLocation.setTypeface(type);
        eventCatg.setTypeface(type);

        orgNameTv.setTypeface(typeRoman);
        locNameTv.setTypeface(typeRoman);
        orgDetTv.setTypeface(typeRoman);

        eventImg = (ImageView) v.findViewById(R.id.event_details_img);
        eventFavImg = (ImageView) v.findViewById(R.id.event_fav_img);

        Glide.with(getContext()).load(mParam2).into(eventImg);

        eventDetailsRequest();

        try {
            EventsFragment.resetScroll();
        } catch (Exception e) {
            Glide.with(getContext()).load("https://soomter.com/AttachmentFiles/"
                    + mParam2 + ".png").into(eventImg);
        }

        locNameTv.setTextColor(Color.parseColor("#333132"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            locContainer.setCardElevation(5);
        }

        return v;
    }

    private void clearSelections() {
        //orgContainer , detailsContainer, locContainer
        orgNameTv.setTextColor(Color.parseColor("#C9CACB"));
        orgDetTv.setTextColor(Color.parseColor("#C9CACB"));
        locNameTv.setTextColor(Color.parseColor("#C9CACB"));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            orgContainer.setCardElevation(0);
            detailsContainer.setCardElevation(0);
            locContainer.setCardElevation(0);
        }
    }

    private void eventDetailsRequest() {
        Call<EventDetailData> call1 = apiInterface.getEventDetails(Id, Locale.getDefault().getLanguage().toString());
        call1.enqueue(new Callback<EventDetailData>() {
            @Override
            public void onResponse(Call<EventDetailData> call, Response<EventDetailData> response) {

                myInstance.dismiss();
                final EventDetailData eventDetailData = response.body();
                if (eventDetailData.result == null) {
                    Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                    return;
                }

               addLocFragment(eventDetailData.result.Longitude,eventDetailData.result.Latitude);

                //لو 0 يبقي انتهي الحدث & 1 يحدث الان & 2 مستقبلا
                switch (eventDetailData.status) {
                    case 0:
                        nowHappen.setText(getString(R.string.happened));
                        break;
                    case 1:
                        nowHappen.setText(getString(R.string.now_happen));
                        break;
                    case 2:
                        nowHappen.setText(getString(R.string.later));
                        break;
                }

                detailsContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        orgDetTv.setTextColor(Color.parseColor("#333132"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            detailsContainer.setCardElevation(5);
                        }
                        addDetFragment(new Gson().toJson(eventDetailData.result));
                    }
                });

                orgContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        orgNameTv.setTextColor(Color.parseColor("#333132"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            orgContainer.setCardElevation(5);
                        }
                        addOrgFragment(new Gson().toJson(eventDetailData.result.organizers));
                    }
                });

                locContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clearSelections();
                        locNameTv.setTextColor(Color.parseColor("#333132"));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            locContainer.setCardElevation(5);
                        }
                        addLocFragment(eventDetailData.result.Longitude,eventDetailData.result.Latitude);
                    }
                });

                /*if (eventDetailData.result.Date == null) {
                    ((View) eventGeorgian.getParent()).setVisibility(View.GONE);
                    ((View) eventHijri.getParent()).setVisibility(View.GONE);
                    ((View) eventTime.getParent()).setVisibility(View.GONE);
                }*/
                if (eventDetailData.result.Title == null)
                    ((View) eventName.getParent()).setVisibility(View.GONE);
                if (eventDetailData.result.Details == null)
                    ((View) eventDetails.getParent()).setVisibility(View.GONE);
                if (eventDetailData.result.Location == null)
                    ((View) eventLocation.getParent()).setVisibility(View.GONE);
                if (eventDetailData.result.BusinessCategory == null)
                    eventCatg.setVisibility(View.GONE);

                eventHijri.setText(eventDetailData.result.HigriFromDate +
                        " " + getString(R.string.to)+ " " + eventDetailData.result.HigriToDate);
                eventGeorgian.setText(eventDetailData.result.DateFrom +
                        " " + getString(R.string.to)+ " " + eventDetailData.result.DateTo);
                eventTime.setText(getString(R.string.from)+ " " +eventDetailData.result.timeFrom +
                        " " + getString(R.string.to)+ " " + eventDetailData.result.timeTo);
                eventName.setText(eventDetailData.result.Title);
                eventDetails.setText(eventDetailData.result.Details);
                eventLocation.setText(eventDetailData.result.Location);
                eventCatg.setText(getString(R.string.the_catgeory) + " ( " +
                        eventDetailData.result.BusinessCategory + " ) ");
            }

            @Override
            public void onFailure(Call<EventDetailData> call, Throwable t) {
                myInstance.dismiss();
                Toast.makeText(getContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addLocFragment(String longt, String lat) {
        getChildFragmentManager().beginTransaction().replace(R.id.ev_details_frag_cont,
                MapLocFragment.newInstance(longt != null ?
                        Double.parseDouble(longt) :  0,
                        lat!= null ? Double.parseDouble(lat) : 0)).commit();
    }

    private void addDetFragment(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.ev_details_frag_cont,
                OrganizersDetailsFragment.newInstance(jsonString)).commit();
    }

    private void addOrgFragment(String jsonString) {
        getChildFragmentManager().beginTransaction().replace(R.id.ev_details_frag_cont,
                OrganizersFragment.newInstance(jsonString)).commit();
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

}