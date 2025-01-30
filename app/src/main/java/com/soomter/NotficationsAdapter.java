package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.soomter.utils.PaginationAdapterCallback;

/**
 * Created by Suleiman on 19/10/16.
 */

public class NotficationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Typeface type, typeRoman;

    private List<NotificationsData.Items> Items;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public NotficationsAdapter(Context context, PaginationAdapterCallback callback,
                               List<NotificationsData.Items> eventsResult) {
        this.context = context;
        this.mCallback = callback;
        this.Items = eventsResult;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
        typeRoman = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Roman.ttf");
    }

    public List<NotificationsData.Items> getMovies() {
        return Items;
    }

    public void setMovies(List<NotificationsData.Items> movieResults) {
        this.Items = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.notification_item, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    private String formatDate(String inputFormat, String outputFormat, String inputDate, boolean isDays) {
        //2018-07-24T00:19:18.653
        if (inputFormat.equals("")) { // if inputFormat = "", set a default input format.
            inputFormat = "yyyy-MM-dd'T'hh:mm:ss.sss";
        }
        if (outputFormat.equals("")) {
            if (isDays)
                outputFormat = "EEEE yyyy/MM/d"; // if inputFormat = "", set a default output format.
            else
                outputFormat = "hh:mm a";
        }
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        // You can set a different Locale, This example set a locale of Country Mexico.
        //SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, new Locale("es", "MX"));
        //SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, new Locale("es", "MX"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);
        } catch (Exception e) {
            Log.e("formattedDateFromString", "Exception in formateDateFromstring(): " + e.getMessage());
        }
        return outputDate;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final NotificationsData.Items result = Items.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.title.setText(result.ShortMessageBody);
                movieVH.date.setText(formatDate("", "", result.CreateDate , false));
                movieVH.loc.setText(formatDate("", "", result.CreateDate , true));//context.getString(R.string.bank_loc) + " - " +

                movieVH.title.setTypeface(typeRoman);
                movieVH.date.setTypeface(typeRoman);
                movieVH.loc.setTypeface(typeRoman);

                Glide.with(context).load(result.Image).into(movieVH.img);

                /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) context)
                                .getSupportFragmentManager().beginTransaction();
                        EventsDetailsFragment fragOne = EventsDetailsFragment.newInstance(result.Id, result.Image);
                        ft.add(R.id.event_fragment_container, fragOne);
                        ft.hide(eventsMainFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });*/

                // load movie thumbnail
                /*loadImage(result.getPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .into(movieVH.mPosterImg);*/
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error));

                } else {
//                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return Items == null ? 0 : Items.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == Items.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
  ______________________________________________________
    */
    public void add(NotificationsData.Items r) {
        Items.add(r);
        notifyItemInserted(Items.size() - 1);
    }

    public void addAll(List<NotificationsData.Items> moveResults) {
        for (NotificationsData.Items result : moveResults) {
            add(result);
        }
    }

    public void remove(NotificationsData.Items r) {
        int position = Items.indexOf(r);
        if (position > -1) {
            Items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NotificationsData.Items());
    }

    public void removeLoadingFooter() {
        if (Items.isEmpty())
            return;

        isLoadingAdded = false;

        int position = Items.size() - 1;
        NotificationsData.Items result = getItem(position);

        if (result != null) {
            Items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public NotificationsData.Items getItem(int position) {
        return Items.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(Items.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView date;
        private TextView loc; // displays "year | language"
        private ImageView img;

        public MovieVH(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.notf_img);
            title = (TextView) itemView.findViewById(R.id.notf_title);
            date = (TextView) itemView.findViewById(R.id.notf_date);
            loc = (TextView) itemView.findViewById(R.id.notf_loc);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
           /* mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);*/

           /* mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);*/
        }

        @Override
        public void onClick(View view) {
            /*switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }*/
        }
    }

}
