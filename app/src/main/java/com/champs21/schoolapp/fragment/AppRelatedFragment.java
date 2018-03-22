package com.champs21.schoolapp.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppRelatedFragment extends Fragment{

    private TextView headerTextVew, descriptionTextView;

    public AppRelatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_related, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            MainActivity.toggle.setDrawerIndicatorEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        headerTextVew = (TextView)view.findViewById(R.id.tv_header_text);
        descriptionTextView = (TextView) view.findViewById(R.id.tv_description_body);
        setContent();
    }

    private void setContent() {
        // TODO Auto-generated method stub
        headerTextVew.setText(getArguments().getString("title"));
        descriptionTextView.setText(Html.fromHtml(getArguments().getString("description")));
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
