package com.champs21.schoolapp.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by RR on 31-Dec-17.
 */

public class SingleAdapter extends RecyclerView.Adapter<SingleAdapter.MyViewHolder>{

    private List<CategoryModel> dataSet =new ArrayList<>();;
    Context context;

    public SingleAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CategoryModel> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        TextView newsTitle = holder.newsTitle;
        TextView newsDesc = holder.newsDesc;
        final TextView menuOptions = holder.menuOptions;

        newsTitle.setText(dataSet.get(position).getTitle().getRendered());
        newsDesc.setText((android.text.Html.fromHtml(dataSet.get(position).getExcerptModel().getRendered()).toString()));
        //Toast.makeText(context, dataSet.get(position), Toast.LENGTH_SHORT).show();
        menuOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, menuOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.share:
                                //handle menu1 click
                                break;
//                            case R.id.save:
//                                //handle menu2 click
//                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle, menuOptions, newsDesc;

        public MyViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView)itemView.findViewById(R.id.movie_title);
            newsDesc = (TextView)itemView.findViewById(R.id.movie_desc);
            menuOptions = (TextView)itemView.findViewById(R.id.menuOptions);

        }
    }

}
