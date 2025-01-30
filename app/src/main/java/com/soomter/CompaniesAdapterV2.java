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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soomter.utils.PaginationAdapterCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Suleiman on 19/10/16.
 */

public class CompaniesAdapterV2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private final Typeface type;
    //private final EventsMainFragment eventsMainFragment;

    private List<CompaniesData.Items> companiesResult;
    private Context context;

    private ItemClickListener mClickListener;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;
    private SharedPref sharePref;

    private APIInterface apiInterface;

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public CompaniesAdapterV2(Context context, PaginationAdapterCallback callback,
                              List<CompaniesData.Items> eventsResult) {
        this.context = context;
        this.mCallback = callback;
        this.companiesResult = eventsResult;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/SST-Arabic-Light.ttf");
       // this.eventsMainFragment = eventsMainFragment;
        sharePref = new SharedPref(context);
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }

    public List<CompaniesData.Items> getMovies() {
        return companiesResult;
    }

    public void setMovies(List<CompaniesData.Items> movieResults) {
        this.companiesResult = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.most_views_item, parent, false);
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
        final CompaniesData.Items company = companiesResult.get(position); // Movie

        switch (getItemViewType(position)) {
            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.name.setText(company.CompanyName);
                movieVH.itemView.setId(company.Id);
                movieVH.setIsRecyclable(false);

                movieVH.name.setTypeface(type);
                movieVH.viewsCount.setTypeface(type);
                movieVH.adsCount.setTypeface(type);
                movieVH.productCount.setTypeface(type);
                movieVH.eventsCount.setTypeface(type);

                movieVH.viewsCount.setText("" + company.ViewCounts);
                movieVH.adsCount.setText("" + company.CountADS);
                movieVH.productCount.setText("" + company.CountProduct);
                movieVH.eventsCount.setText("" + company.CountEvents);

                Glide.with(context).
                        load("https://soomter.com/AttachmentFiles/" + company.LogoId + ".png").into(movieVH.compImg);

                if (company.IsFlowed > 0) {
                    movieVH.follow.setVisibility(View.GONE);
                    movieVH.catFollowTxt.setVisibility(View.GONE);
                }

                switch (company.AverageRating) {
                    case 1:
                        movieVH.firstStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 2:
                        movieVH.firstStar.setImageResource(R.mipmap.gold_star);
                        movieVH.secondStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 3:
                        movieVH.firstStar.setImageResource(R.mipmap.gold_star);
                        movieVH.secondStar.setImageResource(R.mipmap.gold_star);
                        movieVH.thirdStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 4:
                        movieVH.firstStar.setImageResource(R.mipmap.gold_star);
                        movieVH.secondStar.setImageResource(R.mipmap.gold_star);
                        movieVH.thirdStar.setImageResource(R.mipmap.gold_star);
                        movieVH.fourthStar.setImageResource(R.mipmap.gold_star);
                        break;
                    case 5:
                        movieVH.firstStar.setImageResource(R.mipmap.gold_star);
                        movieVH.secondStar.setImageResource(R.mipmap.gold_star);
                        movieVH.thirdStar.setImageResource(R.mipmap.gold_star);
                        movieVH.fourthStar.setImageResource(R.mipmap.gold_star);
                        movieVH.fifthStar.setImageResource(R.mipmap.gold_star);
                        break;
                }

                movieVH.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((AppCompatActivity) context)
                                .getSupportFragmentManager().beginTransaction().addToBackStack(null)
                                .add(R.id.frag_container,
                                        CompanyProfileFragment.newInstance(company.Id ,
                                                company.CompanyName)).addToBackStack(null).commit();
                    }
                });

                movieVH.follow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        followCompany(company.Id, movieVH);
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
        return companiesResult == null ? 0 : companiesResult.size();
    }

    @Override
    public int getItemViewType(int position) {

        return (position == companiesResult.size() - 1 && isLoadingAdded) ? LOADING : ITEM;

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
     *
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
    public void add(CompaniesData.Items r) {
        companiesResult.add(r);
        notifyItemInserted(companiesResult.size() - 1);
    }

    public void setFilter(List<CompaniesData.Items> newList){
        clear();
        if(newList.size() == 1){
            newList.add(new CompaniesData.Items());
        }
        companiesResult = newList;
        notifyDataSetChanged();
    }

    public void addAll(List<CompaniesData.Items> moveResults) {

        for (CompaniesData.Items result : moveResults) {
            add(result);
        }
    }

    public void remove(CompaniesData.Items r) {
        int position = companiesResult.indexOf(r);
        if (position > -1) {
            companiesResult.remove(position);
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
        add(new CompaniesData.Items());
    }

    public void removeLoadingFooter() {
        if(companiesResult.isEmpty())
            return;

        isLoadingAdded = false;

        int position = companiesResult.size() - 1;
        CompaniesData.Items result = getItem(position);

        if (result != null) {
            companiesResult.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CompaniesData.Items getItem(int position) {
        return companiesResult.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(companiesResult.size() - 1);

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
        private TextView name, viewsCount, productCount, adsCount, eventsCount, catFollowTxt;
        private ImageView compImg, firstStar, secondStar, thirdStar, fourthStar, fifthStar, follow;

        public MovieVH(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            viewsCount = (TextView) itemView.findViewById(R.id.views_num);
            productCount = (TextView) itemView.findViewById(R.id.prods_num);
            adsCount = (TextView) itemView.findViewById(R.id.ads_num);
            eventsCount = (TextView) itemView.findViewById(R.id.events_num);

            compImg = (ImageView) itemView.findViewById(R.id.comp_img);
            firstStar = (ImageView) itemView.findViewById(R.id.first_start);
            secondStar = (ImageView) itemView.findViewById(R.id.second_start);
            thirdStar = (ImageView) itemView.findViewById(R.id.third_start);
            fourthStar = (ImageView) itemView.findViewById(R.id.fourth_start);
            fifthStar = (ImageView) itemView.findViewById(R.id.fifth_start);

            follow = (ImageView) itemView.findViewById(R.id.follow_btn);
            catFollowTxt = (TextView) itemView.findViewById(R.id.cat_follow_txt);

            //itemView.setOnClickListener(this);
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

    private void followCompany(int comanyId, final MovieVH holder) {
        if (sharePref.getUser() == null) {
            Toast.makeText(context, context.getString(R.string.require_login), Toast.LENGTH_LONG).show();
            return;
        }

        Call<Void> call1 = apiInterface.followCompany(comanyId, sharePref.getUser().id);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(context, context.getString(R.string.success), Toast.LENGTH_LONG).show();
                    holder.follow.setVisibility(View.GONE);
                    holder.catFollowTxt.setVisibility(View.GONE);
                    return;
                }
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });

    }

}
