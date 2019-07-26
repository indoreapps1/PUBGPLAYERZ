package com.pubgplayerzofficial.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.framework.IAsyncWorkCompletedCallback;
import com.pubgplayerzofficial.framework.ServiceCaller;

import es.dmoral.toasty.Toasty;

public class NewsActivity extends AppCompatActivity {

    WebView news;
//    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_news);
        init();
    }

    private void init() {
        news = findViewById(R.id.news);
        news.getSettings().setJavaScriptEnabled(true);
        news.setWebViewClient(new WebViewController());
        news.getSettings().setPluginState(WebSettings.PluginState.ON);
        news.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        getNewsData();
//        mInterstitialAd = new InterstitialAd(NewsActivity.this);
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                showInterstitial();
//            }
//        });
    }

//    private void showInterstitial() {
//        if (mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//        }
//    }

    public class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    private void getNewsData() {
        final ProgressDialog progressDialog = new ProgressDialog(NewsActivity.this);
        progressDialog.setMessage("Just Sec..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        ServiceCaller serviceCaller = new ServiceCaller(NewsActivity.this);
        serviceCaller.callNewsService(new IAsyncWorkCompletedCallback() {
            @Override
            public void onDone(String workName, boolean isComplete) {
                if (isComplete) {
                    if (workName.trim().equalsIgnoreCase("no")) {
                        Toasty.error(NewsActivity.this, "No Data Found").show();
//                        getFragmentManager().popBackStack();
                    } else {
//                        Toasty.success(NewsActivity.this, workName).show();
                        news.loadUrl(workName.trim());
                    }
                } else {
                    Toasty.error(NewsActivity.this, " Error Try Again").show();
                }
                progressDialog.dismiss();
            }
        });

    }
}
