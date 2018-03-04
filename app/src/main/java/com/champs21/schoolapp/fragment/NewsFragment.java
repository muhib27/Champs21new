package com.champs21.schoolapp.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.adapter.PaginationAdapter;
import com.champs21.schoolapp.adapter.PaginationAdapterTest;
import com.champs21.schoolapp.adapter.SingleAdapter;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.retrofit.RetrofitApiClient;
import com.champs21.schoolapp.utils.AppConstant;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    public SingleAdapter singleAdapter;
    List<String> list;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<CategoryModel> listTopic;
    private PaginationAdapterTest adapter;
    private int SELECTED =139 ;


    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(getArguments().containsKey(AppConstant.SELECTED_ITEM))
        {
            SELECTED = getArguments().getInt(AppConstant.SELECTED_ITEM);
        }
        listTopic = new ArrayList<>();
        list = new ArrayList<>();
        list.add("ewe");
        list.add("ewe");
        list.add("ewe");
        list.add("ewe");
        list.add("ewe");
        list.add("ewe");

        initView(view);

        callNewsApi(SELECTED);


    }

    private void callNewsApi( int selected) {
        showProgress();
        HashMap<String, String> params = new HashMap<>();
        listTopic.clear();

//        RetrofitApiClient.getApiInterface().getTopics(selected, 5, 6).enqueue(new Callback<JsonElement>() {
//            @Override
//            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                if (response.code() == 200) {
//                    //Log.d(Constant.tag, "Submit ok");
//                    hideProgress();
//                    JsonArray jsonArray = response.body().getAsJsonArray();
//
//                    listTopic = parseTopicList(jsonArray.toString());
//
//                    singleAdapter.setData(listTopic);
//                    singleAdapter.notifyDataSetChanged();
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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
    }

    public void hideProgress() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void initView(View v) {

        // SwipeRefreshLayout
//        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */

//        mSwipeRefreshLayout.post(new Runnable() {
//
//            @Override
//            public void run() {
//
//                mSwipeRefreshLayout.setRefreshing(false);
//
//                // Fetching data from server
//                //loadRecyclerViewData();
//            }
//        });



        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        recyclerView.addItemDecoration(decoration);
        //recyclerView.setNestedScrollingEnabled(false);
        singleAdapter = new SingleAdapter(getActivity());
        recyclerView.setAdapter(singleAdapter);

    }

    @Override
    public void onRefresh() {
        // Fetching data from server
        //loadRecyclerViewData();
    }

}
