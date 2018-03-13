package com.champs21.schoolapp.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.champs21.schoolapp.R;
import com.champs21.schoolapp.activity.MainActivity;
import com.champs21.schoolapp.model.CategoryModel;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.DrawerLocker;
import com.champs21.schoolapp.utils.NetworkConnection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleNewsFragment extends Fragment{
    private WebView webView;
    private String pageContent = "";
    private TextView newsTitle, nextTitle;
    private ImageView imageView, nextImage;
    private TextView writerText;
    private List<CategoryModel> childList;
    private LinearLayout frameLayout;
    private static int currentPosition;


    public SingleNewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_news, container, false);
    }

    @SuppressLint("AddJavascriptInterface")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            MainActivity.toggle.setDrawerIndicatorEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        currentPosition = 0;
        String customFont = "solaiman_lipi.ttf";
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), customFont);
//        MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((DrawerLocker) getActivity()).setDrawerEnabled(false);

        webView = (WebView) view.findViewById(R.id.webView);

        imageView = (ImageView) view.findViewById(R.id.poster);
        newsTitle = (TextView) view.findViewById(R.id.newsTitle);
        writerText = (TextView) view.findViewById(R.id.writerText);
        frameLayout = (LinearLayout)view.findViewById(R.id.itemLayout);
        nextTitle = (TextView)view.findViewById(R.id.nextNewsTitle);
        nextImage = (ImageView) view.findViewById(R.id.nextImage);

        childList = new ArrayList<>();


        if (getArguments().containsKey("childList")) {
            String str = getArguments().getString("childList");
            if(str!=null )
            childList = parseNewsList(str);
            setAllNews();
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPosition++;
                setAllNews();
            }
        });

    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(getActivity())
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .crossFade();
    }

    private DrawableRequestBuilder<String> loadThumbImage(@NonNull String posterPath) {
        return Glide
                .with(getActivity())
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }

    private void loadWebView(String pageContent) {
        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
            //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
            showAlert();
            return;
        }


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 80) {
                    if (progressDialog != null)
                        hideProgress();
                }

            }
        });


        try {
            webView.loadDataWithBaseURL(null,  "<style>img{display: inline;height: auto;max-width: 100%;} iframe{display: inline;height: auto;max-width: 100%;}</style>" +pageContent, "text/html", "UTF-8", null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    ProgressDialog progressDialog;

    public void showProgress() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.show();

    }

    public void hideProgress() {
//        progressBar.setVisibility(View.INVISIBLE);
        progressDialog.dismiss();
    }

    AlertDialog dialog;

    private void showAlert() {
        if (dialog != null && dialog.isShowing())
            return;

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle(R.string.sign_out_alert_title);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);
        builder.setMessage("ইন্টারনেট সংযোগ প্রয়োজন");
        builder.setPositiveButton("আবার চেষ্টা করুন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                loadWebView(pageContent);
            }
        });
        builder.setNegativeButton("বাহির হোন", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        dialog = builder.create();
        dialog.show();

    }
    

    private List<CategoryModel> parseNewsList(String object) {

        List<CategoryModel> tags = new ArrayList<CategoryModel>();
        Type listType = new TypeToken<List<CategoryModel>>() {
        }.getType();
        tags = new Gson().fromJson(object, listType);
        return tags;
    }

    private void setAllNews(){
        if(childList.size()>0)
        {
            pageContent = childList.get(currentPosition).getContent().getMainConten();
            newsTitle.setText(childList.get(currentPosition).getTitle().getRendered());
            writerText.setText(childList.get(currentPosition).getEmbedded().getAuthor().get(0).get("name").getAsString());
            loadImage(childList.get(currentPosition).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(imageView);
        }
        if((currentPosition +1)< childList.size())
        {
            frameLayout.setVisibility(View.VISIBLE);
            if(childList.get(currentPosition+1).getTitle().getRendered()!=null) {
                nextTitle.setText(childList.get(currentPosition + 1).getTitle().getRendered());
                loadThumbImage(childList.get(currentPosition + 1).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(nextImage);
            }
        }
        else {
            frameLayout.setVisibility(View.GONE);
        }
        loadWebView(pageContent);
    }
}
