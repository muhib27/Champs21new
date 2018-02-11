package com.champs21.schoolapp.fragment;


import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.CircularPropagation;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.champs21.schoolapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    ProgressBar progressBar;
    private WebView webView;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //progressBar = (ProgressBar)view.findViewById(R.id.progress);
        webView = (WebView) view.findViewById(R.id.webView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        //showProgress();
        loadWebView();

    }

    private void loadWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress >= 80) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }

            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                //if (!progressBar.isEnabled())
//                    showProgress();
                if (!progressDialog.isShowing())
                    progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                if (!progressBar.isEnabled())
//                    hideProgress();
//                if (progressDialog.isShowing())
//                    progressDialog.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
            }
        });
        webView.loadUrl("http://www.champs21.com");

    }

    public boolean canGoBack() {
        return webView != null && webView.canGoBack();
    }

    public void goBack() {
        if(webView != null) {
            webView.goBack();
        }
    }



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (webView.canGoBack()) {
//                        webView.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    ProgressDialog progressDialog;
    public void showProgress() {
//        progressBar = new ProgressBar(getActivity());
//        progressBar.setMax(100);
//        progressBar.setVisibility(View.VISIBLE);
        progressDialog.show();

    }

    public void hideProgress() {
//        progressBar.setVisibility(View.INVISIBLE);
        progressDialog.dismiss();
    }

}
