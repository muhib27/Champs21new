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
public class PagerFragment extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener{
    MyPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private TabHost mTabHost;
    HorizontalScrollView horizontalScrollView;
    PaginationSingleFragment f1;
    FirstFragment f2;
    SecondFragment f3;
    ThirdFragment f4;
    FourthFragment f5;
    FifthFragment f6;
    SixthFragment f7;
    SeventhFragment f8;
    EighthFragment f9;
    NinthFragment f10;



    public PagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pager, container, false);
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
        pagerAdapter = new MyPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        return view;
    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }


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

        // TODO Put here your Tabs
        if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.NEWS) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.international)).setIndicator(getString(R.string.international)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.bangladesh)).setIndicator(getString(R.string.bangladesh)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.SCITECH) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.gadget)).setIndicator(getString(R.string.gadget)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.techworld)).setIndicator(getString(R.string.techworld)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.APPS_GAMES) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.apps_review)).setIndicator(getString(R.string.apps_review)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.CHAMPION) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.entrepreneur)).setIndicator(getString(R.string.entrepreneur)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.personality)).setIndicator(getString(R.string.personality)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.business)).setIndicator(getString(R.string.business)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.LIFE_STYLE) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.extra_curricular)).setIndicator(getString(R.string.extra_curricular)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.shopping)).setIndicator(getString(R.string.shopping)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.food)).setIndicator(getString(R.string.food)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.household)).setIndicator(getString(R.string.household)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.job)).setIndicator(getString(R.string.job)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.travel)).setIndicator(getString(R.string.travel)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.recipes)).setIndicator(getString(R.string.recipes)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.health)).setIndicator(getString(R.string.health)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.RESOURCE_CENTER) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.history)).setIndicator(getString(R.string.history)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.flora_and_fauna)).setIndicator(getString(R.string.flora_and_fauna)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.tradition)).setIndicator(getString(R.string.tradition)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.math)).setIndicator(getString(R.string.math)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.geography)).setIndicator(getString(R.string.geography)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.science)).setIndicator(getString(R.string.science)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.rocking_experiments)).setIndicator(getString(R.string.rocking_experiments)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.teaching_resources)).setIndicator(getString(R.string.teaching_resources)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.literature)).setIndicator(getString(R.string.literature)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.SPORTS) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.others)).setIndicator(getString(R.string.others)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.cricket)).setIndicator(getString(R.string.cricket)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.tennis)).setIndicator(getString(R.string.tennis)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.football)).setIndicator(getString(R.string.football)));
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.ENTERTAINMENT) {
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.all_news)).setIndicator(getString(R.string.all_news)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.tv_show)).setIndicator(getString(R.string.tv_show)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.music)).setIndicator(getString(R.string.music)));
            AddTab(getActivity(), this.mTabHost, this.mTabHost.newTabSpec(getString(R.string.cinema)).setIndicator(getString(R.string.cinema)));
        }
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
        bundle5 = new Bundle();
        bundle6 = new Bundle();
        bundle7 = new Bundle();
        bundle8 = new Bundle();
        bundle9 = new Bundle();
        bundle10 = new Bundle();

        if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.NEWS) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.INTERNATIONAL);
            f2.setArguments(bundle2);

            f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.BANGLADESH);
            f3.setArguments(bundle3);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.SCITECH) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.GADGET);
            f2.setArguments(bundle2);

            f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.TECHWORLD);
            f3.setArguments(bundle3);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.APPS_GAMES) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.APPS_REVIEW);
            f2.setArguments(bundle2);

            fList.add(f1);
            fList.add(f2);

        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.CHAMPION) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.ENTREPRENEUR);
            f2.setArguments(bundle2);

            f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.PERSONALITY);
            f3.setArguments(bundle3);

            f4 = new ThirdFragment();
            bundle4.putInt(AppConstant.SELECTED_ITEM, AppConstant.BUSINESS);
            f4.setArguments(bundle4);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.LIFE_STYLE) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.EXTRA_CURRICULAR);
            f2.setArguments(bundle2);

            f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.SHOPPING);
            f3.setArguments(bundle3);

            f4 = new ThirdFragment();
            bundle4.putInt(AppConstant.SELECTED_ITEM, AppConstant.FOOD);
            f4.setArguments(bundle4);

            f5 = new FourthFragment();
            bundle5.putInt(AppConstant.SELECTED_ITEM, AppConstant.HOUSEHOLD);
            f5.setArguments(bundle5);

            f6 = new FifthFragment();
            bundle6.putInt(AppConstant.SELECTED_ITEM, AppConstant.JOB);
            f6.setArguments(bundle6);

            f7 = new SixthFragment();
            bundle7.putInt(AppConstant.SELECTED_ITEM, AppConstant.TRAVEL);
            f7.setArguments(bundle7);

            f8 = new SeventhFragment();
            bundle8.putInt(AppConstant.SELECTED_ITEM, AppConstant.RECIPES);
            f8.setArguments(bundle8);

            f9 = new EighthFragment();
            bundle9.putInt(AppConstant.SELECTED_ITEM, AppConstant.HEALTH);
            f9.setArguments(bundle9);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
            fList.add(f6);
            fList.add(f7);
            fList.add(f8);
            fList.add(f9);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.RESOURCE_CENTER) {
            // TODO Put here your Fragments
            f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.EXTRA_CURRICULAR);
            f2.setArguments(bundle2);

            f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.SHOPPING);
            f3.setArguments(bundle3);

            f4 = new ThirdFragment();
            bundle4.putInt(AppConstant.SELECTED_ITEM, AppConstant.FOOD);
            f4.setArguments(bundle4);

            f5 = new FourthFragment();
            bundle5.putInt(AppConstant.SELECTED_ITEM, AppConstant.HOUSEHOLD);
            f5.setArguments(bundle5);

            f6 = new FifthFragment();
            bundle6.putInt(AppConstant.SELECTED_ITEM, AppConstant.JOB);
            f6.setArguments(bundle6);

            f7 = new SixthFragment();
            bundle7.putInt(AppConstant.SELECTED_ITEM, AppConstant.TRAVEL);
            f7.setArguments(bundle7);

            f8 = new SeventhFragment();
            bundle8.putInt(AppConstant.SELECTED_ITEM, AppConstant.RECIPES);
            f8.setArguments(bundle8);

            f9 = new EighthFragment();
            bundle9.putInt(AppConstant.SELECTED_ITEM, AppConstant.HEALTH);
            f9.setArguments(bundle9);

            f10 = new NinthFragment();
            bundle10.putInt(AppConstant.SELECTED_ITEM, AppConstant.LITERATURE);
            f10.setArguments(bundle10);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
            fList.add(f6);
            fList.add(f7);
            fList.add(f8);
            fList.add(f9);
            fList.add(f10);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.SPORTS) {
            // TODO Put here your Fragments
            PaginationSingleFragment f1 = new PaginationSingleFragment();
            bundle1.putInt(AppConstant.SELECTED_ITEM, getArguments().getInt(AppConstant.SELECTED_ITEM));
            f1.setArguments(bundle1);

            FirstFragment f2 = new FirstFragment();
            bundle2.putInt(AppConstant.SELECTED_ITEM, AppConstant.OTHERS);
            f2.setArguments(bundle2);

            SecondFragment f3 = new SecondFragment();
            bundle3.putInt(AppConstant.SELECTED_ITEM, AppConstant.CRICKET);
            f3.setArguments(bundle3);

            ThirdFragment f4 = new ThirdFragment();
            bundle4.putInt(AppConstant.SELECTED_ITEM, AppConstant.TENNIS);
            f4.setArguments(bundle4);

            FourthFragment f5 = new FourthFragment();
            bundle5.putInt(AppConstant.SELECTED_ITEM, AppConstant.FOOTBALL);
            f5.setArguments(bundle5);

            fList.add(f1);
            fList.add(f2);
            fList.add(f3);
            fList.add(f4);
            fList.add(f5);
        }
        else if(getArguments().getInt(AppConstant.SELECTED_ITEM)== AppConstant.ENTERTAINMENT) {
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
        }


        return fList;
    }
}
