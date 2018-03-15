package com.champs21.schoolapp.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.adapter.MyPagerAdapter;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.MyTabFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EntertainmentPagerFragment extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    MyPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    HorizontalScrollView horizontalScrollView;
    PaginationSingleFragment f1;
    FirstFragment f2;
    SecondFragment f3;
    ThirdFragment f4;


    public EntertainmentPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            MainActivity.toggle.setDrawerIndicatorEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        horizontalScrollView = (HorizontalScrollView)view.findViewById(R.id.horizontal_scroll);

        // Tab Initialization
        initialiseTabHost(view);

        // Fragments and ViewPager Initialization
        List<Fragment> fragments = getFragments();
        pagerAdapter = new MyPagerAdapter(((FragmentActivity)getActivity()).getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);
    }



    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String s) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
        View tabView = mTabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                -(horizontalScrollView.getWidth() - tabView.getWidth())/2;
        horizontalScrollView.smoothScrollTo(scrollPos, 0);

    }



    // Tabs Creation
    private void initialiseTabHost(View v) {
        mTabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        mTabHost.setup();
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.tv_show)).setIndicator(getString(R.string.tv_show)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.music)).setIndicator(getString(R.string.music)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.cinema)).setIndicator(getString(R.string.cinema)));

        mTabHost.setOnTabChangedListener(this);
    }
    // Method to add a TabHost
    private static void AddTab(Context activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }
    Bundle bundle1, bundle2, bundle3, bundle4, bundle5, bundle6, bundle7, bundle8, bundle9, bundle10;
    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        bundle1 = new Bundle();
        bundle2 = new Bundle();
        bundle3 = new Bundle();
        bundle4 = new Bundle();



            // TODO Put here your Fragments
            PaginationSingleFragment f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            FirstFragment f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.TV_SHOW);
            f2.setArguments(bundle2);

            SecondFragment f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.MUSIC);
            f3.setArguments(bundle3);

            ThirdFragment f4 = new ThirdFragment();
            bundle4.putInt(AppConstant.SELECTED_ITEM, AppConstant.CINEMA);
            f4.setArguments(bundle4);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);


        return fList;
    }
}
