package com.champs21.schoolapp.fragment;


import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.CircularPropagation;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.champs21.schoolapp.R;
import com.champs21.schoolapp.utils.AppConstant;
import com.champs21.schoolapp.utils.NetworkConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    ProgressBar progressBar;
    private WebView webView;
    private String SELECTED;


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
        if(getArguments().containsKey(AppConstant.SELECTED_ITEM))
        {
            SELECTED = getArguments().getString(AppConstant.SELECTED_ITEM);
        }
        webView = (WebView) view.findViewById(R.id.webView);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMax(100);
        progressDialog.setMessage("Please wait....");
        //showProgress();
        loadWebView();

    }

    private void loadWebView() {
        if (!NetworkConnection.getInstance().isNetworkAvailable()) {
            //Toast.makeText(getActivity(), "No Connectivity", Toast.LENGTH_SHORT).show();
            showAlert();
            return;
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
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
            @TargetApi(Build.VERSION_CODES.M)
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //super.onReceivedError(view, request, error);
                //showAlert();
                final Uri uri = request.getUrl();
                handleError(view, error.getErrorCode(), error.getDescription().toString(), uri);
            }

            @SuppressWarnings("deprecation")
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                //super.onReceivedError(view, errorCode, description, failingUrl);
                //showAlert();
                final Uri uri = Uri.parse(failingUrl);
                handleError(view, errorCode, description, uri);
            }

            private void handleError(WebView view, int errorCode, String description, final Uri uri) {
//                final String host = uri.getHost();// e.g. "google.com"
//                final String scheme = uri.getScheme();// e.g. "https"
                // TODO: logic
                try {
                    webView.stopLoading();
                } catch (Exception e) {
                }

                if (webView.canGoBack()) {
                    webView.goBack();
                }
               showAlert();
            }
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                try {
//                    webView.stopLoading();
//                } catch (Exception e) {
//                }
//
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                }
//
//                webView.loadUrl("about:blank");
//                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage("Check your internet connection and try again.");
//                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //getActivity().finish();
//                        startActivity(getActivity().getIntent());
//                    }
//                });
//                super.onReceivedError(view, request, error);
//
//
//            }

            //            public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
//                try {
//                    webView.stopLoading();
//                } catch (Exception e) {
//                }
//
//                if (webView.canGoBack()) {
//                    webView.goBack();
//                }
//
//                webView.loadUrl("about:blank");
//                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
//                alertDialog.setTitle("Error");
//                alertDialog.setMessage("Check your internet connection and try again.");
//                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        //getActivity().finish();
//                        startActivity(getActivity().getIntent());
//                    }
//                });
//
//                alertDialog.show();
//                super.onReceivedError(webView, errorCode, description, failingUrl);
//            }
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
        if (webView != null) {
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
                loadWebView();
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

//        alertDialog = new AlertDialog.Builder(getActivity()).create();
//        alertDialog.setTitle("Error");
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage("Check your internet connection and try again.");
//        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                alertDialog.dismiss();
//                loadWebView();
//
//            }
//        });
//        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Exit", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                getActivity().finish();
//            }
//        });
//        alertDialog.show();
    }

}
