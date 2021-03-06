package com.champs21.schoolapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.adapter.PaginationInitialAdapter;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.model.TopRatedMovies;
import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.DrawerLocker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

//import android.util.Log;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragmentTestOne extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;

    PaginationInitialAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;
    private static int si = 0;
    private static final int PAGE_START = 5;
    private static final int PAGE_START_OFFSET = 0;
    ArrayList<CategoryModel> results = new ArrayList<>();

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;
    private int SELECTED = AppConstant.CHAMPION;
    private int[] menuArray = {AppConstant.NEWS, AppConstant.SCITECH, AppConstant.APPS_GAMES, AppConstant.CHAMPION, AppConstant.LIFE_STYLE, AppConstant.RESOURCE_CENTER, AppConstant.SPORTS, AppConstant.ENTERTAINMENT, AppConstant.VIDEO};


    public HomeFragmentTestOne() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_initial, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((DrawerLocker) getActivity()).setDrawerEnabled(true);
        si = 0;

//        if(getArguments().containsKey(AppConstant.SELECTED_ITEM))
//        {
//            SELECTED = getArguments().getInt(AppConstant.SELECTED_ITEM);
//        }
//        listTopic = new ArrayList<>();
        //MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */

        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);

                // Fetching data from server
                //loadRecyclerViewData();
            }
        });


        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL);
//        rv.addItemDecoration(itemDecor);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);

        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        try {
            //MainActivity.results.clear();
            adapter = new PaginationInitialAdapter(getContext(), MainActivity.results);
        } catch (Exception e) {
            e.printStackTrace();
            getActivity().finish();
        }

        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
//        if (MainActivity.results.size() <= 0)
//            errorLayout.setVisibility(View.VISIBLE);
//        else
//            errorLayout.setVisibility(View.GONE);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                callChainApi();
            }
        });


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.setAdapter(adapter);
        showProgress();
        callChainApi();
    }


//    private void callNewsApiFirst(int selected, final int callNo) {
//        hideErrorView();
//
//        RetrofitApiClient.getApiInterface().getTopics(selected, currentPage, currentOffst).enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.code() == 200) {
//                    si++;
//                    //Log.d(Constant.tag, "Submit ok");
//                    Headers headers = response.headers();
//                    TOTAL_ITEM = Integer.valueOf(headers.get("X-WP-Total"));
//                    JsonArray jsonArray = response.body().getAsJsonArray();
//                    List<CategoryModel> singleList = parseTopicList(jsonArray.toString());
////                    for (int i = 0; i < singleList.size(); i++)
//                        results.addAll(singleList);
//                        results.add(singleList.get(4));
//
//                    if (si < (menuArray.length))
//                        callNewsApiFirst(menuArray[si], si);
//                    else  {
//                        progressBar.setVisibility(View.GONE);
//                        adapter.addAllData(results);
//                    }
//
////                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
////                    else isLastPage = true;
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
//                //adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });
//
//    }

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

//    private void callNewsApiNext(int selected) {
//        hideErrorView();
//
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
//                    List<CategoryModel> results = parseTopicList(jsonArray.toString());
//
//                    adapter.addAllData(results);
//
////                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
////                    else isLastPage = true;
//
//                } else {
//
//                    // Log.d(Constant.tag, "Submit response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                //adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });
//
//    }

//    private void loadNextPage() {
//        MovieApi.getApiInterface().getTopRatedMovies(getString(R.string.my_api_key), "en_US", currentPage).enqueue(new Callback<TopRatedMovies>() {
//            @Override
//            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    //hideProgress();
//                    adapter.removeLoadingFooter();
//                    isLoading = false;
//
//                    List<Result> results = fetchResults(response);
////                    int total = results.size();
////                    if (results.size() >= 5) {
////                        for (int i = 5; i < total; i = i + 5) {
////                            results.add(i, results.get(i));
////                        }
////
////                    }
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
//                //hideProgress();
//                // Log.d(Constant.tag, "Error submit task:", error);
//                adapter.showRetry(true, fetchErrorMessage(t));
//            }
//        });

//        MovieApi.getApiInterface().getTopRatedMovies( getString(R.string.my_api_key), "en_US", currentPage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            if (response.code() == 200) {
//                                //Log.d(Constant.tag, "Submit ok");
//                                //hideProgress();
//                                adapter.removeLoadingFooter();
//                                isLoading = false;
//
//                                List<Result> results = fetchResults(response);
//                                adapter.addAll(results);
//
//                                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
//                                else isLastPage = true;
//
//
//                            } else {
//
//                                // Log.d(Constant.tag, "Submit response code " + response.code());
//                            }
//                        },
//                        error -> {
//                            //hideProgress();
//                            // Log.d(Constant.tag, "Error submit task:", error);
//                            adapter.showRetry(true, fetchErrorMessage(error));
//                        },
//                        () -> {
//                            //hideProgress();
//
//                        }
//                );
//    }

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
    public void onRefresh() {
        //callChainApi();
    }


    public void callChainApi() {
        final ArrayList<CategoryModel> newresults = new ArrayList<>();
        RetrofitApiClient.getApiInterface().getLatest(5, 0)
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            si++;
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.NEWS, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getLatest(5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SCITECH, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.NEWS, 5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.APPS_GAMES, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SCITECH, 5, 0);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> value) {
                        List<CategoryModel> singleList = value.body();
                        singleList.size();
                        newresults.addAll(singleList);
                        newresults.add(singleList.get(4));
                        results.clear();
                        results.addAll(newresults);
                        if (progressDialog!= null && progressDialog.isShowing())
                            hideProgress();
                        if (errorLayout.getVisibility() == View.VISIBLE)
                            errorLayout.setVisibility(View.GONE);
                        adapter.addAllData(results);
                        mSwipeRefreshLayout.setRefreshing(false);
                        callChainApiNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    public void callChainApiNext() {
        final ArrayList<CategoryModel> newresults = new ArrayList<>();
        RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION, 5, 0)

                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.LIFE_STYLE, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION, 5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.RESOURCE_CENTER, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.LIFE_STYLE, 5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SPORTS, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.RESOURCE_CENTER, 5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.ENTERTAINMENT, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SPORTS, 5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.VIDEO, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.ENTERTAINMENT, 5, 0);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<CategoryModel>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<CategoryModel>> value) {
                        List<CategoryModel> singleList = value.body();
                        singleList.size();
                        newresults.addAll(singleList);
                        newresults.add(singleList.get(4));
                        results.clear();
                        results.addAll(newresults);
                        if (progressDialog!= null && progressDialog.isShowing())
                            hideProgress();
                        if (errorLayout.getVisibility() == View.VISIBLE)
                            errorLayout.setVisibility(View.GONE);
                        adapter.addAllData(results);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private ProgressDialog progressDialog;

    public void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
}
