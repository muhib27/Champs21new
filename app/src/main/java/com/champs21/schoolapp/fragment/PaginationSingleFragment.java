package com.champs21.schoolapp.fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.adapter.PaginationSingleAdapter;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.model.TopRatedMovies;
import com.champs21.schoolapp.retrofit.MovieApi;
import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.DrawerLocker;
import com.champs21.schoolapp.utils.PaginationAdapterCallback;
import com.champs21.schoolapp.utils.PaginationScrollListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Headers;
import okhttp3.internal.framed.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import android.util.Log;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginationSingleFragment extends Fragment implements PaginationAdapterCallback {

    PaginationSingleAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private static final int PAGE_START = 10;
    private static final int PAGE_START_OFFSET = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;
    private int SELECTED;


    public PaginationSingleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.video_layout, container, false);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            MainActivity.toggle.setDrawerIndicatorEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        if(getArguments().containsKey(AppConstant.SELECTED_ITEM))
        {
            SELECTED = getArguments().getInt(AppConstant.SELECTED_ITEM);
        }
//        listTopic = new ArrayList<>();
        //MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        adapter = new PaginationSingleAdapter(getContext(), this);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage = 15;
                currentOffst += 10;

                //loadNextPage();
                callNewsApiNext(SELECTED);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_ITEM;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        rv.setAdapter(adapter);
//        loadFirstPage();
        callNewsApiFirst(SELECTED);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFirstPage();
                callNewsApiFirst(SELECTED);
            }
        });

        return view;
    }
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v("onActivityCreated", "onActivityCreated");
//        callNewsApiFirst(SELECTED);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }

    public void callNewsApiFirst( int selected) {
        hideErrorView();

        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> value) {
                        if(value.code()==200){
                            Headers headers = value.headers();
                            TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
                            List<CategoryModel> singleList = value.body();
                            //singleList.size();
                            progressBar.setVisibility(View.GONE);
                            adapter.addAll(singleList);

                            if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
                            else isLastPage = true;
//                            results.addAll(singleList);
//                            results.add(singleList.get(4));
//                            si++;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showErrorView(e);
                        adapter.showRetry(true, fetchErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, currentOffst).enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    Headers headers = response.headers();
//                    TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
//                    JsonArray jsonArray = response.body().getAsJsonArray();
//
//                    List<CategoryModel>  singleListResults= parseTopicList(jsonArray.toString());
//
//                    progressBar.setVisibility(View.GONE);
//                    adapter.addAll(singleListResults);
//
//                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
//                    else isLastPage = true;
//
//                } else {
//
//                    // Log.d(Constant.tag, "Submit response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                showErrorView(t);
//                // Log.d(Constant.tag, "Error submit task:", error);
//                adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });

    }

//    private void loadFirstPage() {
////        Log.d(TAG, "loadFirstPage: ");
//        hideErrorView();
//        MovieApi.getApiInterface().getTopRatedMovies(getString(R.string.my_api_key), "en_US", currentPage).enqueue(new Callback<TopRatedMovies>() {
//            @Override
//            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    //hideProgress();
//                    hideErrorView();
//
//                    ArrayList<Result> results = fetchResults(response);
//                    ArrayList<Result> newResult = new ArrayList<Result>();
////                    int total = results.size();
////                    newResult.add(results.get(0));
////
////                        for (int i = 1; i < total; i++) {
////
////                                newResult.add(results.get(i));
////                        }
//                    progressBar.setVisibility(View.GONE);
//                    adapter.addAll(results);
//
//                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                    else isLastPage = true;
//
//
//                } else {
//
//                    // Log.d(Constant.tag, "Submit response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
//                showErrorView(t);
//                // Log.d(Constant.tag, "Error submit task:", error);
//                adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });
//
//    }

    private List<CategoryModel> parseTopicList(String object) {

        List<CategoryModel> tags = new ArrayList<CategoryModel>();
        Type listType = new TypeToken<List<CategoryModel>>() {
        }.getType();
        tags = new Gson().fromJson(object, listType);
        return tags;
    }

    private void callNewsApiNext( int selected) {
        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, currentOffst)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> value) {
                        if(value.code()==200){
                            adapter.removeLoadingFooter();
                            isLoading = false;
                            List<CategoryModel> singleList = value.body();
                            //singleList.size();
                            adapter.addAll(singleList);

                            if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
                            else isLastPage = true;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        adapter.showRetry(true, fetchErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });

//        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, currentOffst).enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    ;
//                    adapter.removeLoadingFooter();
//                    isLoading = false;
//
//                    JsonArray jsonArray = response.body().getAsJsonArray();
//
//                    List<CategoryModel>  singleListResults= parseTopicList(jsonArray.toString());
//
//                    adapter.addAll(singleListResults);
//
//                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
//                    else isLastPage = true;
//
//                } else {
//
//                    // Log.d(Constant.tag, "Submit response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });

    }
    private ArrayList<Result> fetchResults(Response<TopRatedMovies> response) {
        TopRatedMovies topRatedMovies = response.body();
//        ArrayList<Result> list = new ArrayList<>();
//        int lisSize = topRatedMovies.getTotalResults();
//        list = topRatedMovies.getResults();
        return topRatedMovies.getResults();
//        if(lisSize>=5){
//
//        }
//        return list;
    }

    private void showErrorView(Throwable throwable) {

        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param throwable to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    public void retryPageLoad() {
//        loadNextPage();
        callNewsApiNext(SELECTED);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
