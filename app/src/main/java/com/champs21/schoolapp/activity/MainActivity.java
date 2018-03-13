package com.champs21.schoolapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.fragment.AppRelatedFragment;
import com.champs21.schoolapp.fragment.HomeFragment;
import com.champs21.schoolapp.fragment.MainFragment;
import com.champs21.schoolapp.fragment.NewsFragment;
import com.champs21.schoolapp.fragment.PagerFragment;
import com.champs21.schoolapp.fragment.PaginationSingleFragment;
import com.champs21.schoolapp.fragment.PaginationHomeFragment;
import com.champs21.schoolapp.model.CategoryModel;

import com.champs21.schoolapp.model.Result;
import com.champs21.schoolapp.model.TopRatedMovies;

import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;

import com.champs21.schoolapp.utils.DrawerLocker;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
//import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
//import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DrawerLocker {
    public static DrawerLayout drawer;
    public static ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    MainFragment mainFragment;
    private List<CategoryModel> listTopic;
    public static boolean apiRunning = true;

    private int[] menuArray = {-1, AppConstant.NEWS, AppConstant.SCITECH, AppConstant.APPS_GAMES, AppConstant.CHAMPION, AppConstant.LIFE_STYLE, AppConstant.RESOURCE_CENTER, AppConstant.SPORTS, AppConstant.ENTERTAINMENT, AppConstant.VIDEO};
    private static int si = 0;
    public static ArrayList<CategoryModel> results = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToolbar();
        showProgressDialog();
        si = 0;
        listTopic = new ArrayList<>();
        results.clear();
        callChainApi();

//        for (int i = 0; i < menuArray.length; i++)
//            callNewsApiFirst(menuArray[i], i);
        Fabric.with(this, new Crashlytics());
        //mainFragment = new MainFragment();
        //gotoPaginationTestFragment();

//        menuiApiCall();

//        Bundle bundle = new Bundle();
//        bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.NEWS);
        //gotoInitialFragment();
        //gotoPaginationTest(bundle);
//        gotoNewsFragment(bundle);
//        gotoMainFragment(bundle);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }

    ProgressDialog progressLoder;

    public void showProgressDialog() {
        progressLoder = new ProgressDialog(this);
        progressLoder.setMessage("Pleast wait....");
        progressLoder.setCancelable(false);
        progressLoder.show();

    }

    public void hideProgressDialog() {
        progressLoder.dismiss();
    }

    public void callChainApi() {
        RetrofitApiClient.getApiInterface().getLatest(5,0)
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            si++;
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.NEWS, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getLatest(5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SCITECH, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.NEWS,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.APPS_GAMES, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SCITECH,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.APPS_GAMES,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.LIFE_STYLE, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.CHAMPION,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.RESOURCE_CENTER, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.LIFE_STYLE,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SPORTS, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.RESOURCE_CENTER,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.VIDEO, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.SPORTS,5, 0);
                        }
                    }
                })
                .flatMap(new Function<Response<List<CategoryModel>>, ObservableSource<Response<List<CategoryModel>>>>() {
                    @Override
                    public ObservableSource<Response<List<CategoryModel>>> apply(Response<List<CategoryModel>> listResponse) throws Exception {
                        if(listResponse.code()==200) {
                            List<CategoryModel> singleList = listResponse.body();
                            singleList.size();
                            results.addAll(singleList);
                            results.add(singleList.get(4));
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.VIDEO, 5, 0);
                        }
                        else {
                            return RetrofitApiClient.getApiInterface().getTopics(AppConstant.ENTERTAINMENT,5, 0);
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
                        results.addAll(singleList);
                        results.add(singleList.get(4));
                        gotoHomeFragment();

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        hideProgressDialog();
                        gotoHomeFragment();

                    }

                    @Override
                    public void onComplete() {

                    }
                });



    }


    private void callNewsApiFirst(int selected, final int callNo) {
        if (selected == -1) {
            RetrofitApiClient.getApiInterface().getLatest(5, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<List<CategoryModel>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<List<CategoryModel>> value) {
                            if (value.code() == 200) {
                                List<CategoryModel> singleList = value.body();
                                singleList.size();
                                results.addAll(singleList);
                                results.add(singleList.get(4));
                                si++;

                            }
                        }


                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
//            RetrofitApiClient.getApiInterface().getLatest(5, 1).enqueue(new Callback<JsonElement>() {
//                @Override
//                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                    if (response.code() == 200) {
//
//                        //Log.d(Constant.tag, "Submit ok");
//                        Headers headers = response.headers();
//
//                        JsonArray jsonArray = response.body().getAsJsonArray();
//                        List<CategoryModel> singleList = parseTopicList(jsonArray.toString());
////                    for (int i = 0; i < singleList.size(); i++)
//                        results.addAll(singleList);
//                        results.add(singleList.get(4));
//                        si++;
//
////                        if (callNo < (menuArray.length) && si > 9) {
////                        callNewsApiFirst(menuArray[si], si);
////                    else  {
////                        progressBar.setVisibility(View.GONE);
////                        adapter.addAllData(results);
////                            apiRunning = false;
////
////                        }
//                        //}
//
////                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
////                    else isLastPage = true;
//
//                    } else {
//
//                        // Log.d(Constant.tag, "Submit response code " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                    // Log.d(Constant.tag, "Error submit task:", error);
//                    //adapter.showRetry(true, fetchErrorMessage(t));
//                }
//            });
        } else {
//            RetrofitApiClient.getApiInterface().getTopics(selected, 5, 1).enqueue(new Callback<JsonElement>() {
//                @Override
//                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                    if (response.code() == 200) {
//
//                        //Log.d(Constant.tag, "Submit ok");
//                        Headers headers = response.headers();
//
//                        JsonArray jsonArray = response.body().getAsJsonArray();
//                        List<CategoryModel> singleList = parseTopicList(jsonArray.toString());
////                    for (int i = 0; i < singleList.size(); i++)
//                        results.addAll(singleList);
//                        results.add(singleList.get(4));
//                        si++;
//
//                        if (callNo < (menuArray.length) && si > 9) {
////                        callNewsApiFirst(menuArray[si], si);
////                    else  {
////                        progressBar.setVisibility(View.GONE);
////                        adapter.addAllData(results);
//                            apiRunning = false;
//                            gotoInitialFragment();
//
//
//                        }
//                        //}
//
////                    if (currentOffst < TOTAL_ITEM) adapter.addLoadingFooter();
////                    else isLastPage = true;
//
//                    } else {
//
//                        // Log.d(Constant.tag, "Submit response code " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<JsonElement> call, Throwable t) {
//
//                    // Log.d(Constant.tag, "Error submit task:", error);
//                    //adapter.showRetry(true, fetchErrorMessage(t));
//                }
//            });


            RetrofitApiClient.getApiInterface().getTopics(selected, 5, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Response<List<CategoryModel>>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<List<CategoryModel>> value) {

                            if (value.code() == 200) {
                                List<CategoryModel> singleList = value.body();
                                singleList.size();
                                results.addAll(singleList);
                                results.add(singleList.get(4));
                                si++;
                                gotoHomeFragment();

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressLoder.dismiss();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });

        }

    }

    private void menuiApiCall() {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        listTopic.clear();
        //params.put("message", "message");

//        RetrofitApiClient.getApiInterface().getTopics(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            if (response.code() == 200) {
//                                //Log.d(Constant.tag, "Submit ok");
//                                hideProgress();
//
//                            } else {
//
//                               // Log.d(Constant.tag, "Submit response code " + response.code());
//                            }
//                        },
//                        error -> {
//                            hideProgress();
//                           // Log.d(Constant.tag, "Error submit task:", error);
//                        },
//                        () -> {
//                            hideProgress();
//
//                        }
//                );
//        Call<List<CategoryModel>> call = RetrofitApiClient.getApiInterface().getTopicsList(142, 2);
//        call.enqueue(new Callback<List<CategoryModel>>() {
//            @Override
//            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
//                hideProgress();
//
//            }
//
//            @Override
//            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
//                hideProgress();
//            }
//        });
//        call.enqueue(new Callback<List<CategoryModel>>() {
//            @Override
//            public void onResponse(Call<List<CategoryModel>> call, Response<Response<List<CategoryModel>> response) {
//                hideProgress();
//                List<CategoryModel> list = response.body();
//
//                for(int i =0; i< list.size(); i++)
//                {
//
//                }
//
//                Toast.makeText(getApplicationContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
//            }
//
//
//
//            @Override
//            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
//                Log.d("msg", ""+t);
//            }
//        });


//        RetrofitApiClient.getApiInterface().getTopics(142, 2, 6).enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    hideProgress();
//                    JsonArray jsonArray = response.body().getAsJsonArray();
//                    try {
//                        JSONArray jsonarray = new JSONArray(jsonArray.toString());
//                        for (int i = 0; i < jsonarray.length(); i++) {
//                            JSONObject jsonobject = jsonarray.getJSONObject(i);
//                            CategoryModel categoryModel = new CategoryModel(jsonobject.getString("id"), jsonobject.getString("title"),jsonobject.getString("link"),jsonobject.getString("excerpt"));
//                           listTopic.add(categoryModel);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

//                    jsonArray.size();
//                    Type listType = new TypeToken<ArrayList<CategoryModel>>(){}.getType();
//                    for(int i =0; i<jsonArray.size(); i++)
//                    {
//                        CategoryModel categoryModel =GsonParser.getInstance().parseServerResponseP(jsonArray);
//                    }
        // CategoryModel categoryModel = new CategoryModel(response)
        //ArrayList<CategoryModel> list = response.body();
//                    Wrapper modelContainer = GsonParser.getInstance()
//                            .parseServerResponse2(response.body());
//                    if(modelContainer.getStatus().getCode()==200) {
//
//                        JsonArray jsonArray = modelContainer.getData().getAsJsonArray();
//                    listTopic = parseTopicList(jsonArray.toString());
////                    }
//
//
//                } else {
//
//                    // Log.d(Constant.tag, "Submit response code " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonElement> call, Throwable t) {
//                hideProgress();
//            }
//        });
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response -> {
//                            if (response.code() == 200) {
//                                //Log.d(Constant.tag, "Submit ok");
//                                hideProgress();
//
//
//                            } else {
//
//                               // Log.d(Constant.tag, "Submit response code " + response.code());
//                            }
//                        },
//                        error -> {
//                            hideProgress();
//                           // Log.d(Constant.tag, "Error submit task:", error);
//                        },
//                        () -> {
//                            hideProgress();
//
//                        }
//                );


//                .subscribe(new Observer<Response<CommonApiResponse>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<CommonApiResponse> value) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    private ArrayList<Result> fetchResults(Response<TopRatedMovies> response) {
        TopRatedMovies topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private List<CategoryModel> parseTopicList(String object) {

        List<CategoryModel> tags = new ArrayList<CategoryModel>();
        Type listType = new TypeToken<List<CategoryModel>>() {
        }.getType();
        tags = new Gson().fromJson(object, listType);
        return tags;
    }

    private ProgressDialog progressDialog;

    public void showProgress() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void gotoMainFragment(Bundle bundle) {
        // MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        mainFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, mainFragment, "mainFragment");
        transaction.commit();
    }

    private void gotoPaginationTestFragment() {
        PaginationHomeFragment paginationTestFragment = new PaginationHomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_acitivity_container, paginationTestFragment, "paginationTestFragment");
        transaction.commit();
    }

    private void showToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //mActionBar = getSupportActionBar();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //drawer.setDrawerListener(toggle);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final View.OnClickListener originalToolbarListener = toggle.getToolbarNavigationClickListener();
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStack = getSupportFragmentManager().getBackStackEntryCount();
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    toggle.setDrawerIndicatorEnabled(false);
                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSupportFragmentManager().popBackStack();

                        }
                    });
                } else {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.setDrawerIndicatorEnabled(true);
//                    toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
                    toggle.setToolbarNavigationClickListener(originalToolbarListener);
                    toggle.syncState();
                }
            }
        });
    }
//
//    @Override
//    public void onBackPressed() {
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onBackPressed() {
//        MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragment");
//        if (mainFragment != null && mainFragment.canGoBack()) {
//            this.mainFragment.goBack();
//        }
//        if (apiRunning) {
//            // this block disable back button
//            return;
//
//        } else {
        // The back key event only counts if we execute super.onBackPressed();
        super.onBackPressed();
//        }
    }
//
//    @Override
//    public void onBackPressed() {
//        //drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
//                //navigationView.setCheckedItem(R.id.homepage);
//                toggle.setDrawerIndicatorEnabled(true);
//            }
//        } else {
//            this.finish();
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // Associate searchable configuration with the SearchView
//        getMenuInflater().inflate(R.menu.actionbar_menu, menu);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        searchEditText.setTextColor(getResources().getColor(R.color.colorAccent));
//        searchEditText.setHintTextColor(getResources().getColor(R.color.colorAccent));

//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

//        this.menu = menu;  // this will copy menu values to upper defined menu so that we can change icon later akash

        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.news) {
            // Handle the camera action
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.NEWS);
            // gotoMainFragment(bundle);
//            gotoNewsFragment(bundle);
            gotoPagerFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
        } else if (id == R.id.scitech) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.SCITECH);
            //gotoPaginationTestFragment();
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);

        } else if (id == R.id.apps_games) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.APPS_GAMES);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);
        } else if (id == R.id.champion) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.CHAMPION);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);
        } else if (id == R.id.life_style) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.LIFE_STYLE);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);
        } else if (id == R.id.resource_center) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.RESOURCE_CENTER);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);
        } else if (id == R.id.sports) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.SPORTS);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);

        } else if (id == R.id.entertainment) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.ENTERTAINMENT);
//            gotoNewsFragment(bundle);
            //gotoPaginationSingleFragment(bundle);
            gotoPagerFragment(bundle);

        } else if (id == R.id.video) {
            bundle.putInt(AppConstant.SELECTED_ITEM, AppConstant.VIDEO);
//            gotoNewsFragment(bundle);
            gotoPaginationSingleFragment(bundle);

        }
//        else if (id == R.id.travel) {
//
//        } else if (id == R.id.personality) {
//
//        } else if (id == R.id.literature) {
//
//        }
        else if (id == R.id.about_us) {
            bundle.putString("title", "About Us");
            bundle.putString("description",
                    getResources().getString(R.string.about_use_text));
            gotoAppRelatedFragment(bundle);

        } else if (id == R.id.terms_policy) {
            bundle.putString("title", "Terms & Policy");
            bundle.putString("description",
                    getResources().getString(R.string.termsandpolicy_text));
            gotoAppRelatedFragment(bundle);

        }


//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoNewsFragment(Bundle bundle) {
        NewsFragment newsFragment = new NewsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        newsFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, newsFragment, "newsFragment");
        transaction.commit();
    }

    private void gotoPaginationSingleFragment(Bundle bundle) {
        PaginationSingleFragment paginationSingleFragment = new PaginationSingleFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        paginationSingleFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, paginationSingleFragment, "paginationSingleFragment").addToBackStack(null);
        transaction.commit();
    }
    private void gotoPagerFragment(Bundle bundle) {
        PagerFragment pagerFragment = new PagerFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        pagerFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, pagerFragment, "pagerFragment").addToBackStack(null);
        transaction.commit();
    }
    private void gotoAppRelatedFragment(Bundle bundle) {
        AppRelatedFragment appRelatedFragment = new AppRelatedFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        appRelatedFragment.setArguments(bundle);
        transaction.replace(R.id.main_acitivity_container, appRelatedFragment, "appRelatedFragment").addToBackStack(null);
        transaction.commit();
    }

    private void gotoHomeFragment() {
        try {
            hideProgressDialog();
            HomeFragment homeFragment = new HomeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//        paginationSingleFragment.setArguments(bundle);
            transaction.replace(R.id.main_acitivity_container, homeFragment, "homeFragment");
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            MainFragment mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag("mainFragment");
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//
//                    if (mainFragment.canGoBack()) {
//                        mainFragment.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }


}


//
//RetrofitApiClient.getApiInterface().getTopics(selected, 5, 1)
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Observer<Response>() {
//@Override
//public void onSubscribe(Disposable d) {
//
//        }
//
//@Override
//public void onNext(Response value) {
//
//        }
//
//@Override
//public void onError(Throwable e) {
//
//        }
//
//@Override
//public void onComplete() {
//
//        }
//        });
