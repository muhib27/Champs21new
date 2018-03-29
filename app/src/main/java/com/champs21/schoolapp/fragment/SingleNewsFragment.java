package com.champs21.schoolapp.fragment;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.Calendar;
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
    private TextView footerText;
    private List<CategoryModel> childList;
    private LinearLayout frameLayout;
    private static int currentPosition;
    private String[] monthArray = {" জানুয়ারী "," ফেব্রূয়ারি ", " মার্চ ", " এপ্রিল ", " মে ", " জুন ", " জুলাই ", " অগাস্ট ", " সেপ্টেম্বর ", " অক্টোবর ", " নভেম্বর ", " ডিসেম্বর "};


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
        setHasOptionsMenu(true);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            MainActivity.toggle.setDrawerIndicatorEnabled(false);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String footerString = "© চ্যাম্পস টোয়েন্টিওয়ান ডটকম ২০১০-" + getdateInBangla(year);
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
        footerText = (TextView)view.findViewById(R.id.footerText);
        nextImage = (ImageView) view.findViewById(R.id.nextImage);
        footerText.setText(footerString);

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
        showProgress();
        webView.getSettings().setJavaScriptEnabled(true);


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
            writerText.setText(childList.get(currentPosition).getEmbedded().getAuthor().get(0).get("name").getAsString() +" | " + parseDate(childList.get(currentPosition).getNewsDate()));
            loadImage(childList.get(currentPosition).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(imageView);
        }
        try {

            if ((currentPosition + 1) < childList.size()) {
                frameLayout.setVisibility(View.VISIBLE);
                if (childList.get(currentPosition + 1).getTitle().getRendered() != null) {
                    nextTitle.setText(childList.get(currentPosition + 1).getTitle().getRendered());
                    loadThumbImage(childList.get(currentPosition + 1).getEmbedded().getFeatureMedia().get(0).get("source_url").getAsString()).into(nextImage);
                }
            } else {
                frameLayout.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
        loadWebView(pageContent);
    }


    public String getdateInBangla(String string)
    {
        Character bangla_number[]={'০','১','২','৩','৪','৫','৬','৭','৮','৯'};
        Character eng_number[]={'0','1','2','3','4','5','6','7','8','9'};
        String values = "";
        char[] character = string.toCharArray();
        for (int i=0; i<character.length ; i++) {
            Character c = ' ';
            for (int j = 0; j < eng_number.length; j++) {
                if(character[i]==eng_number[j])
                {
                    c=bangla_number[j];
                    break;
                }else {
                    c=character[i];
                }
            }
            values=values + c;
        }
        return values;
    }

    private String parseDate(String rowString){
        String[] parts = rowString.split("T");
        String dateString = parts[0];
        String timeString = parts[1];
        String year="", month="", day="", hour="", mimutes="";

        if(dateString!=null && dateString.contains("-")) {
            String[] dateSubString = dateString.split("-");
            year = getdateInBangla(dateSubString[0]);

            month= monthArray[Integer.parseInt(String.valueOf(dateSubString[1]))-1];
            day = getdateInBangla(dateSubString[2]);
        }
        if(timeString!= null && timeString.contains(":"))
        {
            String[] timeSubString = timeString.split(":");
            hour = getdateInBangla(timeSubString[0]);
            mimutes = getdateInBangla(timeSubString[1]);
        }
        return day + month + year + ", " + hour + ":" + mimutes;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem shareItem = menu.findItem(R.id.mShare);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setVisible(false);
        shareItem.setVisible(true);
        shareItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Toast.makeText(getActivity(), "ddd", Toast.LENGTH_LONG).show();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, childList.get(currentPosition).getTitle().getRendered());
                shareIntent.putExtra(Intent.EXTRA_TITLE, childList.get(currentPosition).getTitle().getRendered());
                shareIntent.putExtra(Intent.EXTRA_TEXT, childList.get(currentPosition).getLink());
                getActivity().startActivity(Intent.createChooser(shareIntent, "Share link using"));
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
