package com.champs21.schoolapp.activity;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.fragment.MainFragment;

import static com.champs21.schoolapp.R.id.webView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    public static ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    NavigationView navigationView;
    MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = new MainFragment();
//        showToolbar();
        gotoMainFragment();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.setItemIconTintList(null);
//
//        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
//        navMenuView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
    }

    private void gotoMainFragment() {
       // MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_acitivity_container, mainFragment, "mainFragment");
        transaction.commit();
    }

//    private void showToolbar() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        //mActionBar = getSupportActionBar();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
////        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        //drawer.setDrawerListener(toggle);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        final View.OnClickListener originalToolbarListener = toggle.getToolbarNavigationClickListener();
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                int backStack = getSupportFragmentManager().getBackStackEntryCount();
//                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                    toggle.setDrawerIndicatorEnabled(false);
//                    toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            getSupportFragmentManager().popBackStack();
//
//                        }
//                    });
//                } else {
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    toggle.setDrawerIndicatorEnabled(true);
//                    toggle.setHomeAsUpIndicator(R.drawable.ic_drawer);
//                    toggle.setToolbarNavigationClickListener(originalToolbarListener);
////                    toggle.syncState();
//                }
//            }
//        });
//    }

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
        MainFragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("mainFragment");
        if (mainFragment != null && mainFragment.canGoBack()) {
            this.mainFragment.goBack();
        } else {
            // The back key event only counts if we execute super.onBackPressed();
            super.onBackPressed();
        }
    }

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
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.spelling_bee) {
//            // Handle the camera action
//        } else if (id == R.id.resource_centre) {
//
//        } else if (id == R.id.news_articles) {
//
//        } else if (id == R.id.entertainment) {
//
//        } else if (id == R.id.sports) {
//
//        } else if (id == R.id.games) {
//
//        } else if (id == R.id.extra_curricular) {
//
//        } else if (id == R.id.fitness_health) {
//
//        } else if (id == R.id.food_nutration) {
//
//        } else if (id == R.id.travel) {
//
//        } else if (id == R.id.personality) {
//
//        } else if (id == R.id.literature) {
//
//        } else if (id == R.id.about_us) {
//
//        } else if (id == R.id.terms_policy) {
//
//        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            MainFragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("mainFragment");
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                    if (mainFragment.canGoBack()) {
                        mainFragment.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}
