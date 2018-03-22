package com.champs21.schoolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.champs21.schoolapp.R;
import com.champs21.schoolapp.fragment.SingleNewsFragment;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.ImageUrl;
import com.champs21.schoolapp.model.MediaDetails;
import com.champs21.schoolapp.model.MediaSizes;
import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.PaginationAdapterCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Suleiman on 19/10/16.
 */

public class PaginationInitialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private int[] titleArray = {R.string.news , R.string.scitech, R.string.apps_games, R.string.champion, R.string.life_style, R.string.resource_center, R.string.sports, R.string.entertainment, R.string.video};

    // View Types
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;
    private static final int ADD = 5;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    private List<CategoryModel> movieResults;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    public PaginationInitialAdapter(Context context, ArrayList<CategoryModel> movieResults) {
        this.context = context;
//        this.mCallback = mCallback;
        this.movieResults = movieResults;
    }
    public PaginationInitialAdapter(Context context, PaginationAdapterCallback mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        movieResults = new ArrayList<>();
    }
    public PaginationInitialAdapter(Context context) {
        this.context = context;
//        this.mCallback = mCallback;
        movieResults = new ArrayList<>();
    }

    public List<CategoryModel> getMovies() {
        return movieResults;
    }

    public void setMovies(List<CategoryModel> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
//            case LOADING:
//                View viewLoading = inflater.inflate(R.layout.item_progress_single, parent, false);
//                viewHolder = new LoadingVH(viewLoading);
//                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
            case ADD:
                View viewAdd = inflater.inflate(R.layout.layout, parent, false);
                viewHolder = new HeroAdd(viewAdd);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CategoryModel result = movieResults.get(position); // Movie
        final Bundle bundle = new Bundle();
        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH topItem = (HeroVH) holder;
                topItem.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int next=0;
                        if(position>4)
                            next = 5;
                        else
                            next = 1;
                        ArrayList<CategoryModel> childList = new ArrayList<CategoryModel>(movieResults.subList(position, position+next));
                        String str = new Gson().toJson(childList);
                        bundle.putString("childList", str);
                        //bundle.putString(AppConstant.SELECTED_ITEM_LINK, movieResults.get(position).getEmbedded().getFeatureMedia().get(0).get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("medium").getAsJsonObject().get("source_url").getAsString());

//                        bundle.putString(AppConstant.SELECTED_ITEM_LINK, movieResults.get(position).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString());
//                        bundle.putString(AppConstant.SELECTED_ITEM_TITLE, movieResults.get(position).getTitle().getRendered());
//                        bundle.putString(AppConstant.SELECTED_ITEM_CONTENT, movieResults.get(position).getContent().getMainConten());
//                        bundle.putString(AppConstant.SELECTED_ITEM_AUTHOR, movieResults.get(position).getEmbedded().getAuthor().get(0).get("name").getAsString());
                        gotoSingleNewsFragment(bundle);
                    }
                });

                topItem.mMovieTitle.setText(result.getTitle().getRendered());
                //heroVh.mYear.setText(formatYearLabel(result));
                topItem.mMovieDesc.setText(android.text.Html.fromHtml(result.getExcerptModel().getRendered()).toString());
                //List<ImageUrl> imageUrls =  result.getEmbedded().getFeatureMedia().get(0);
                String jsonObject=  result.getEmbedded().getFeatureMedia().get(0).get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("medium").getAsJsonObject().get("source_url").getAsString();
                //jsonObject.get("sizes");

//                String ht = jsonElement.toString();
//                parseTopicList(ht);
//                String i = jsonArray.get(12).toString();


//                loadImage(ht);
                loadImage(result.getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(topItem.mPosterImg);

//                loadImage(result.getEmbedded().)
//                        .into(heroVh.mPosterImg);
                break;

            case ITEM:
                final MovieVH itemHolder = (MovieVH) holder;

                itemHolder.mMovieTitle.setText(result.getTitle().getRendered());
//                movieVH.mYear.setText(formatYearLabel(result));
                itemHolder.mMovieDesc.setText(android.text.Html.fromHtml(result.getExcerptModel().getRendered()).toString());
                itemHolder.mPosterImg.setImageResource(R.drawable.sample);
                itemHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int next=0;
                        if(position>4)
                         next = 5-(position%6);
                        else
                            next = 1;
                        ArrayList<CategoryModel> childList = new ArrayList<CategoryModel>(movieResults.subList(position, position+next));
                        String str = new Gson().toJson(childList);
                        bundle.putString("childList", str);
                        //bundle.putString(AppConstant.SELECTED_ITEM_LINK, movieResults.get(position).getEmbedded().getFeatureMedia().get(0).get("media_details").getAsJsonObject().get("sizes").getAsJsonObject().get("medium").getAsJsonObject().get("source_url").getAsString());
//                        bundle.putString(AppConstant.SELECTED_ITEM_LINK, movieResults.get(position).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString());
//                        bundle.putString(AppConstant.SELECTED_ITEM_TITLE, movieResults.get(position).getTitle().getRendered());
//                        bundle.putString(AppConstant.SELECTED_ITEM_CONTENT, movieResults.get(position).getContent().getMainConten());
//                        bundle.putString(AppConstant.SELECTED_ITEM_AUTHOR, movieResults.get(position).getEmbedded().getAuthor().get(0).get("name").getAsString());
                        gotoSingleNewsFragment(bundle);
                    }
                });
                itemHolder.menuOption.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(context,  itemHolder.menuOption);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.option_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.share:
//                                        //handle menu1 click
//                                        Intent sendIntent = new Intent();
//                                        sendIntent.setAction(Intent.ACTION_SEND);
////                                        sendIntent.putExtra(Intent.EXTRA_TEXT,editText.getText().toString());
//                                        sendIntent.setType("text/plain");
//                                        Intent.createChooser(sendIntent,"Share via");
//                                        context.startActivity(sendIntent);
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, movieResults.get(position).getTitle().getRendered());
                                        shareIntent.putExtra(Intent.EXTRA_TITLE, movieResults.get(position).getTitle().getRendered());
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, movieResults.get(position).getLink());
                                        context.startActivity(Intent.createChooser(shareIntent, "Share link using"));
                                        break;
//                                    case R.id.save:
//                                        //handle menu2 click
//                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });

//                // load movie thumbnail

                loadImage( result.getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                itemHolder.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                itemHolder.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .into(itemHolder.mPosterImg);
                break;

//            case LOADING:
//                LoadingVH loadingVH = (LoadingVH) holder;

//                if (retryPageLoad) {
//                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
//                    loadingVH.mProgressBar.setVisibility(View.GONE);
//
//                    loadingVH.mErrorTxt.setText(
//                            errorMsg != null ?
//                                    errorMsg :
//                                    context.getString(R.string.error_msg_unknown));
//
//                } else {
//                    loadingVH.mErrorLayout.setVisibility(View.GONE);
//                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
//                }
//                break;
            case ADD:
                final HeroAdd addHolder = (HeroAdd) holder;
                if(movieResults.size()==24 && position==23)
                {
                    addHolder.mProgressBar.setVisibility(View.VISIBLE);
                }
                else {
                    addHolder.mProgressBar.setVisibility(View.GONE);
                }
                if(position/6 <= 8) {
                    addHolder.title.setText(titleArray[position / 6]);
                    addHolder.title.setVisibility(View.VISIBLE);
                }
                else
                    addHolder.title.setVisibility(View.GONE);

                //heroAdd.mMovieTitle.setText(result.getTitle().getRendered());
                break;
        }
    }

    private void gotoSingleNewsFragment(Bundle bundle) {
        SingleNewsFragment singleNewsFragment = new SingleNewsFragment();
        FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        singleNewsFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, singleNewsFragment, "singleNewsFragment").addToBackStack(null);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
//        if(movieResults.size() == 24 && position==23){
//            return LOADING;
//        }
//        else
            if (position%6 == 0 ) {
            return HERO;
        }
        else if((position%6) == 5)
        {
            return ADD;
        }
        else {
            return ITEM;
        }
    }

    /*
        Helpers - bind Views
   _________________________________________________________________________________________________
    */

    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
    private String formatYearLabel(Result result) {
        return result.getReleaseDate().substring(0, 4)  // we want the year only
                + " | "
                + result.getOriginalLanguage().toUpperCase();
    }

    /**
     * Using Glide to handle image loading.
     * Learn more about Glide here:
     * <a href="http://blog.grafixartist.com/image-gallery-app-android-studio-1-4-glide/" />
     *
     * @param posterPath from {@link Result#getPosterPath()}
     * @return Glide builder
     */
    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }


    /*
        Helpers - Pagination
   _________________________________________________________________________________________________
    */

    public void add(CategoryModel r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAllData(List<CategoryModel> moveResults) {
        for (CategoryModel result : moveResults) {
            add(result);
        }

    }

    public void remove(CategoryModel r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
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
        add(new CategoryModel());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        CategoryModel result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public CategoryModel getItem(int position) {
        return movieResults.get(position);
    }



    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Header ViewHolder
     */
    protected class HeroVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private CardView cardView;

        public HeroVH(View itemView) {
            super(itemView);

            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMovieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
//            mYear = (TextView) itemView.findViewById(R.id.movie_year);
            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        private TextView mMovieTitle;
        private TextView mMovieDesc;
        private TextView mYear; // displays "year | language"
        private ImageView mPosterImg;
        private ProgressBar mProgress;
        private TextView menuOption;
        private FrameLayout itemLayout;

        public MovieVH(View itemView) {
            super(itemView);

            mMovieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            mMovieDesc = (TextView) itemView.findViewById(R.id.movie_desc);
//            mYear = (TextView) itemView.findViewById(R.id.movie_year);
            mPosterImg = (ImageView) itemView.findViewById(R.id.movie_poster);
            mProgress = (ProgressBar) itemView.findViewById(R.id.movie_progress);
            menuOption = (TextView) itemView.findViewById(R.id.menuOptions);
            itemLayout = (FrameLayout)itemView.findViewById(R.id.itemLayout);
        }
    }

    protected class HeroAdd extends RecyclerView.ViewHolder {
        private TextView title;
        private ProgressBar mProgressBar;

        public HeroAdd(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

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
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
//                case R.id.cardView:
//                case R.id.itemLayout:
//                    bundle.putString(AppConstant.SELECTED_ITEM_LINK, movieResults.get(position).getLink());
//                    gotoSingleNewsFragment(bundle);

//                    break;
//                case R.id.loadmore_retry:
//                case R.id.loadmore_errorlayout:
//
//                    showRetry(false, null);
//                    mCallback.retryPageLoad();
//
//                    break;
            }
        }
    }

    private List<MediaSizes> parseTopicList(String object) {

        List<MediaSizes> tags = new ArrayList<MediaSizes>();
        Type listType = new TypeToken<List<MediaSizes>>() {
        }.getType();
        tags = new Gson().fromJson(object, listType);
        return tags;
    }

}
