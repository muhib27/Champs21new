package com.champs21.schoolapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.adapter.AdapterTwo;
import com.champs21.schoolapp.adapter.PaginationSingleAdapter;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.model.TopRatedMovies;
import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.DrawerLocker;
import com.champs21.schoolapp.utils.PaginationAdapterCallback;
import com.champs21.schoolapp.utils.PaginationScrollListener;
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
import okhttp3.Headers;
import retrofit2.Response;

//import android.util.Log;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragmentTwo extends Fragment implements PaginationAdapterCallback, SwipeRefreshLayout.OnRefreshListener{

    AdapterTwo adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private static final int PAGE_START = 15;
    private static final int PAGE_START_OFFSET = 0;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private static int TOTAL_ITEM = 60;
    private int currentPage = PAGE_START;
    private int currentOffst = PAGE_START_OFFSET;
    private int SELECTED;
    ArrayList<CategoryModel> results = new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;


    public HomeFragmentTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_initial, container, false);

        ((DrawerLocker) getActivity()).setDrawerEnabled(true);


        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);

                // Fetching data from server
                //loadRecyclerViewData();
            }
        });

        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
//        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        adapter = new AdapterTwo(getContext(), this);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
//                currentPage = 15;
                //currentOffst += 15;

                //loadNextPage();
                callNewsApiNext();
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
        callNewsApiFirst();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadFirstPage();
                callNewsApiFirst();
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

    public void callNewsApiFirst() {
        showProgress();
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
                        //newresults.add(singleList.get(4));
                        results.clear();
                        results.addAll(newresults);
                        //progressBar.setVisibility(View.GONE);
                        if (progressDialog != null && progressDialog.isShowing())
                            hideProgress();
                        if (errorLayout.getVisibility() == View.VISIBLE)
                            errorLayout.setVisibility(View.GONE);
                        adapter.addAllData(results);
                        adapter.addLoadingFooter();
                        isLastPage = false;
//                        if (results.size() < TOTAL_ITEM) adapter.addLoadingFooter();
//                        else isLastPage = true;
                        //mSwipeRefreshLayout.setRefreshing(false);
                        //callChainApiNext();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        if (progressDialog != null && progressDialog.isShowing())
                            hideProgress();
                        showErrorView(e);
                        adapter.showRetry(true, fetchErrorMessage(e));

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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

    private void callNewsApiNext() {
        hideErrorView();
        final ArrayList<CategoryModel> newresults = new ArrayList<>();
        RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION, 5, 0)

                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.add(singleList.get(0));
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
                        adapter.removeLoadingFooter();
                        List<CategoryModel> singleList = value.body();
                        singleList.size();
                        newresults.addAll(singleList);
                        newresults.add(singleList.get(4));
                        results.clear();
                        results.addAll(newresults);
//                        if (progressDialog!= null && progressDialog.isShowing())
//                            hideProgress();
//                        if (errorLayout.getVisibility() == View.VISIBLE)
//                            errorLayout.setVisibility(View.GONE);
                        adapter.addAllData(results);
                        isLastPage = true;
                        //mSwipeRefreshLayout.setRefreshing(false);
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
            //progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(throwable));
        }
    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            //progressBar.setVisibility(View.VISIBLE);
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
        callNewsApiNext();
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
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if (listResponse.code() == 200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            newresults.addAll(singleList);
                            newresults.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION, 5, 0);
                        } else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.APPS_GAMES, 5, 0);
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
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.VIDEO, 5, 0);
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
                        adapter.addAllNewData(results);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mSwipeRefreshLayout.setRefreshing(false);

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }


    @Override
    public void onRefresh() {
        callChainApi();
    }
}
