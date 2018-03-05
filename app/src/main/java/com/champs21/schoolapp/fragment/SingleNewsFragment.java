package com.champs21.schoolapp.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.champs21.schoolapp.R;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.NetworkConnection;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleNewsFragment extends Fragment {
    private WebView webView;
    private String pageUrl;
    private TextView newsTitle;
    private ImageView imageView;



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
        webView = (WebView)view.findViewById(R.id.webView);
        imageView = (ImageView)view.findViewById(R.id.poster);
        newsTitle = (TextView)view.findViewById(R.id.newsTitle);
        if(getArguments().containsKey(AppConstant.SELECTED_ITEM_CONTENT))
        {
            pageUrl = getArguments().getString(AppConstant.SELECTED_ITEM_CONTENT);
        }

        if(getArguments().containsKey(AppConstant.SELECTED_ITEM_TITLE))
        {
            if(getArguments().getString(AppConstant.SELECTED_ITEM_TITLE)!= null)
                newsTitle.setText(getArguments().getString(AppConstant.SELECTED_ITEM_TITLE));
        }
        if(getArguments().containsKey(AppConstant.SELECTED_ITEM_LINK))
        {
            try {
                if(getArguments().getString(AppConstant.SELECTED_ITEM_LINK)!= null)
                    loadImage(getArguments().getString(AppConstant.SELECTED_ITEM_LINK)).into(imageView);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        loadWebView(pageUrl);

    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(getActivity())
                .load(posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }

    private void loadWebView(String pageLink) {
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

//        WebSettings webSetting = webView.getSettings();
//        webSetting.setBuiltInZoomControls(true);
//        webSetting.setDisplayZoomControls(false);
//
//        //webSetting.setUseWideViewPort(true);
////        webSetting.setLoadWithOverviewMode(true);
//        webSetting.setJavaScriptEnabled(true);
//        webSetting.setDomStorageEnabled(true);
//        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        if (Build.VERSION.SDK_INT >= 19) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
//        else {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//
//        webView .getSettings().setUseWideViewPort(true);
//        webView .getSettings().setLoadWithOverviewMode(true);
        webView .getSettings().setJavaScriptEnabled(true);
//        webView .getSettings().setDomStorageEnabled(true);
//        webView .getSettings().setBuiltInZoomControls(true);
//        webView .getSettings().setDisplayZoomControls(false);
//        webView.setInitialScale(100);

//        webView.getSettings().setDefaultTextEncodingName("utf-8");
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            @TargetApi(Build.VERSION_CODES.M)
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                //super.onReceivedError(view, request, error);
//                //showAlert();
//                final Uri uri = request.getUrl();
//                handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
//            }
//
//            @SuppressWarnings("deprecation")
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                //super.onReceivedError(view, errorCode, description, failingUrl);
//                //showAlert();
//                final Uri uri = Uri.parse(failingUrl);
//                handleError(view, errorCode, description, uri);
//            }
//
//            private void handleError(WebView view, int errorCode, String description, final Uri uri) {
////                final String host = uri.getHost();// e.g. "google.com"
////                final String scheme = uri.getScheme();// e.g. "https"
//                // TODO: logic
//                try {
//                    webView.stopLoading();
//                } catch (Exception e) {
//                }
//
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                }
//                showAlert();
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return super.shouldOverrideUrlLoading(view, request);
//            }
//
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//
////                //if (!progressBar.isEnabled())
////                    showProgress();
//                    showProgress();
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
////                if (!progressBar.isEnabled())
////                    hideProgress();
//                    hideProgress();
//            }
//
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//            }
//
//            @Override
//            public void onPageCommitVisible(WebView view, String url) {
//                super.onPageCommitVisible(view, url);
//            }
//        });
//        webView.loadUrl(pageLink);
       // webView.loadData(pageLink, "text/html", "UTF-8");
        webView.loadDataWithBaseURL(null, pageLink, "text/html", "UTF-8", null);

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
        if( dialog != null && dialog.isShowing() )
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
                loadWebView(pageUrl);
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
}
