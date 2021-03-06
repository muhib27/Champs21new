package com.champs21.schoolapp.fragment;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.util.Log;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.adapter.PaginationAdapter;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.model.TopRatedMovies;
import com.champs21.schoolapp.retrofit.MovieApi;
import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.PaginationAdapterCallback;
import com.champs21.schoolapp.utils.PaginationScrollListener;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
//
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaginationHomeFragment extends Fragment implements PaginationAdapterCallback {

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    private static final int PAGE_START = 1;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;


    public PaginationHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main_another, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        //MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        rv = (RecyclerView) view.findViewById(R.id.main_recycler);
        progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) view.findViewById(R.id.error_layout);
        btnRetry = (Button) view.findViewById(R.id.error_btn_retry);
        txtError = (TextView) view.findViewById(R.id.error_txt_cause);

        adapter = new PaginationAdapter(getContext(), this);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());


        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
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
        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });

    }

    private void loadFirstPage() {
//        Log.d(TAG, "loadFirstPage: ");
        hideErrorView();
        MovieApi.getApiInterface().getTopRatedMovies(getString(R.string.my_api_key), "en_US", currentPage).enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                if (response.code() == 200) {
                    //Log.d(Constant.tag, "Submit ok");
                    //hideProgress();
                    hideErrorView();

                    ArrayList<Result> results = fetchResults(response);
                    ArrayList<Result> newResult = new ArrayList<Result>();
//                    int total = results.size();
//                    newResult.add(results.get(0));
//
//                        for (int i = 1; i < total; i++) {
//
//                                newResult.add(results.get(i));
//                        }
                    progressBar.setVisibility(View.GONE);
                    adapter.addAll(results);

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;


                } else {

                    // Log.d(Constant.tag, "Submit response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                showErrorView(t);
                // Log.d(Constant.tag, "Error submit task:", error);
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });


//        MovieApi.getApiInterface().getTopRatedMovies( getString(R.string.my_api_key), "en_US", currentPage)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            if (response.code() == 200) {
//                                hideErrorView();
//                                //Log.d(Constant.tag, "Submit ok");
//                                //hideProgress();
//                                List<Result> results = fetchResults(response);
//                                progressBar.setVisibility(View.GONE);
//                                adapter.addAll(results);
//
//                                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
//                                else isLastPage = true;
//
//
//                            } else {
//
//                                // Log.d(Constant.tag, "Submit response code " + response.code());
//                            }
//                        },
//                        error -> {
//                            showErrorView(error);
//                            //hideProgress();
//                            // Log.d(Constant.tag, "Error submit task:", error);
//                        },
//                        () -> {
//                            //hideProgress();
//
//                        }
//                );
    }

    private void loadNextPage() {
        MovieApi.getApiInterface().getTopRatedMovies(getString(R.string.my_api_key), "en_US", currentPage).enqueue(new Callback<TopRatedMovies>() {
            @Override
            public void onResponse(Call<TopRatedMovies> call, Response<TopRatedMovies> response) {
                if (response.code() == 200) {
                    //Log.d(Constant.tag, "Submit ok");
                    //hideProgress();
                    adapter.removeLoadingFooter();
                    isLoading = false;

                    List<Result> results = fetchResults(response);
//                    int total = results.size();
//                    if (results.size() >= 5) {
//                        for (int i = 5; i < total; i = i + 5) {
//                            results.add(i, results.get(i));
//                        }
//
//                    }
                    adapter.addAll(results);

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;


                } else {

                    // Log.d(Constant.tag, "Submit response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TopRatedMovies> call, Throwable t) {
                //hideProgress();
                // Log.d(Constant.tag, "Error submit task:", error);
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });

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
        loadNextPage();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(true);
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(Color.BLACK);
//        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));
//
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                return false;
//            }
//        });
//        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                //Toast.makeText(getActivity(), "ddd", Toast.LENGTH_LONG).show();
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, childList.get(currentPosition).getTitle().getRendered());
//                shareIntent.putExtra(Intent.EXTRA_TITLE, childList.get(currentPosition).getTitle().getRendered());
//                shareIntent.putExtra(Intent.EXTRA_TEXT, childList.get(currentPosition).getLink());
//                getActivity().startActivity(Intent.createChooser(shareIntent, "Share link using"));
//                return false;
//            }
//        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
