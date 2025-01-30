package com.soomter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.soomter.utils.PaginationAdapterCallback;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Typeface type;
    //private final EventsMainFragment eventsMainFragment;

    private List<EventsData.Items> eventsResult;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public PaginationAdapter(Context context, PaginationAdapterCallback callback,
                             List<EventsData.Items> eventsResult) {
        this.context = context;
        this.mCallback = callback;
        this.eventsResult = eventsResult;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
       // this.eventsMainFragment = eventsMainFragment;
    }

    public List<EventsData.Items> getMovies() {
        return eventsResult;
    }

    public void setMovies(List<EventsData.Items> movieResults) {
        this.eventsResult = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.event_item, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final EventsData.Items result = eventsResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.eventNme.setText(result.Title);
                movieVH.eventDate.setText(result.Period);
                movieVH.eventLoc.setText(result.Address);//context.getString(R.string.bank_loc) + " - " +
                movieVH.favIcon.setImageResource(result.IsFavorite > 0 ? R.mipmap.fav_on_corner : R.mipmap.fav_off_corner);

                movieVH.eventNme.setTypeface(type);
                movieVH.eventDate.setTypeface(type);
                movieVH.eventLoc.setTypeface(type);

                Glide.with(context).load(result.Image).into(movieVH.eventImg);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        android.support.v4.app.FragmentTransaction ft = ((AppCompatActivity) context)
                                .getSupportFragmentManager().beginTransaction();
                        EventsDetailsFragment fragOne = EventsDetailsFragment.newInstance(result.Id, result.Image);
                        ft.add(R.id.event_fragment_container, fragOne);
                        //ft.hide(eventsMainFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });

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
        return eventsResult == null ? 0 : eventsResult.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == eventsResult.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
   /* private String formatYearLabel(Result result) {
        return result.getReleaseDate().substring(0, 4)  // we want the year only
                + " | "
                + result.getOriginalLanguage().toUpperCase();
    }*/

    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here:
     * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
     *
     * @param posterPath from {@link Result#getPosterPath()}
     * @return Glide builder
     */
    /*private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }*/


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */
    public void add(EventsData.Items r) {
        eventsResult.add(r);
        notifyItemInserted(eventsResult.size() - 1);
    }

    public void setFilter(List<EventsData.Items> newList){
        clear();
        if(newList.size() == 1){
            newList.add(new EventsData.Items());
        }
        eventsResult = newList;
        notifyDataSetChanged();
    }

    public void addAll(List<EventsData.Items> moveResults) {

        for (EventsData.Items result : moveResults) {
            add(result);
        }
    }

    public void remove(EventsData.Items r) {
        int position = eventsResult.indexOf(r);
        if (position > -1) {
            eventsResult.remove(position);
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
        add(new EventsData.Items());
    }

    public void removeLoadingFooter() {
        if(eventsResult.isEmpty())
            return;

        isLoadingAdded = false;

        int position = eventsResult.size() - 1;
        EventsData.Items result = getItem(position);

        if (result != null) {
            eventsResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public EventsData.Items getItem(int position) {
        return eventsResult.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(eventsResult.size() - 1);

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
        private TextView eventNme;
        private TextView eventDate;
        private TextView eventLoc; // displays "year | language"
        private ImageView favIcon , eventImg;
        private ProgressBar mProgress;

        public MovieVH(View itemView) {
            super(itemView);
            eventImg = (ImageView) itemView.findViewById(R.id.event_img);
            favIcon = (ImageView) itemView.findViewById(R.id.event_fav_icon);
            eventNme = (TextView) itemView.findViewById(R.id.event_name);
            eventDate = (TextView) itemView.findViewById(R.id.event_date);
            eventLoc = (TextView) itemView.findViewById(R.id.event_location);
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
