package com.pubgplayerzofficial.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.UrlQuerySanitizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.pubgplayerzofficial.R;
import com.pubgplayerzofficial.utilities.CompatibilityUtility;
import com.pubgplayerzofficial.utilities.Contants;

import es.dmoral.toasty.Toasty;


public class PatmActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_patm);

//        WebView webView = findViewById(R.id.webview);
//        webView.getSettings().getJavaScriptEnabled();
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        //  webView.getSettings().setUserAgentString(userAgent);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setDatabaseEnabled(true);
//        webView.getSettings().setDomStorageEnabled(true);
////        webView.loadUrl( Contants.patyurl+"ORDER_ID="+oid+"&"+"CUST_ID="+uid+"&"+"INDUSTRY_TYPE_ID=Retail109"+"&"+"CHANNEL_ID=WEB"+"&"+"TXN_AMOUNT="+amou);
//        webView.loadUrl(Contants.patyurl + "CUST_ID=" + uid + "&" + "TXN_AMOUNT=" + amou);
//    }


    private WebView browser = null;
    private String requestedURL = null;
    private ConnectionTimeoutHandler timeoutHandler = null;
    private static int PAGE_LOAD_PROGRESS = 0;
    public static final String KEY_REQUESTED_URL = "requested_url";
    /**
     * A string which will be added in url if server want to send something to client after everything has been done.
     * <p>
     * When you load any url to webview and after task done it doen't go back to caller activity. User must have to press back key.
     * <p>
     * So what we do, we will check url each time for a string if loading url contains the string, we assume that we got result.
     * <p>
     * In my example my server returns success string into url.
     */
    public static final String CALLBACK_URL = " http://pubgplayerz.com/payment/pgResponse.php";
    public static final String CALLBACK_URLS = " https://pubgplayerz.com/payment/pgResponse.php";
    private int userId;
    private ProgressDialog spotsDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (CompatibilityUtility.isTablet(PatmActivity.this)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_patm);
        initComponents();
        addListener();
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        userId = bundle.getInt("uid");
        String amou = bundle.getString("amount");

        requestedURL = Contants.patyurl + "CUST_ID=" + userId + "&" + "TXN_AMOUNT=" + amou;
        if (requestedURL != null) {
            browser.loadUrl(requestedURL);
        } else {
            Log.e(Contants.LOG_TAG, "Can not process ... URL missing.");
        }
        spotsDialog = new ProgressDialog(this);
        spotsDialog.show();
    }

    public void initComponents() {
        browser = findViewById(R.id.webview);
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    public void addListener() {
        browser.setWebViewClient(new MyWebViewClient());
        browser.setWebChromeClient(new MyWebChromeClient());
    }

    //Custom web view client
    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(Contants.LOG_TAG, "Loading url : " + url);
           /* if (url.contains(CALLBACK_URL)) {
                Log.i(Contants.LOG_TAG, "Callback url matched... Handling the result");
                UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
                sanitizer.setAllowUnregisteredParamaters(true);
                sanitizer.parseUrl(String.valueOf(url));
                String transaction_id = sanitizer.getValue("transaction_id");
                if (transaction_id != null) {
                }
                String payment_id = sanitizer.getValue("payment_id");
                String PaymentOrderId = sanitizer.getValue("id");
                sendConfirmDetailsToServer(transaction_id, payment_id, PaymentOrderId);
                return true;
            }
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }*/
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            timeoutHandler = new ConnectionTimeoutHandler(PatmActivity.this, view);
            timeoutHandler.execute();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            if (timeoutHandler != null)
                timeoutHandler.cancel(true);

            Log.i(Contants.LOG_TAG, "Loading url : " + url);
            // Do all your result processing here
//            Toast.makeText(PatmActivity.this, url, Toast.LENGTH_SHORT).show();
            if (url.equalsIgnoreCase(CALLBACK_URL)) {
                UrlQuerySanitizer sanitizer = new UrlQuerySanitizer();
                sanitizer.setAllowUnregisteredParamaters(true);
                sanitizer.parseUrl(String.valueOf(url));
                Toasty.success(PatmActivity.this, "Success").show();
                Intent intent = new Intent(PatmActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


//                WalletFragment walletFragment = WalletFragment.newInstance(false, 0);
//                moveFragment(walletFragment);

//            } else {
//                Toasty.success(PatmActivity.this, "Error Found Try Again").show();
            }
            if (url.equalsIgnoreCase(CALLBACK_URLS)) {
                Toasty.success(PatmActivity.this, "Success").show();
                Intent intent = new Intent(PatmActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
//            } else {
//                Toasty.success(PatmActivity.this, "Error Found Try Again").show();
            }
            if (spotsDialog.isShowing()) {
                spotsDialog.dismiss();
            }
        }

        public void moveFragment(Fragment fragment) {
            FragmentManager fragmentManager = ((FragmentActivity) PatmActivity.this).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frgcontainer, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {

            Log.i(Contants.LOG_TAG, "GOT Page error : code : " + errorCode + " Desc : " + description);
            showError(PatmActivity.this, errorCode);
            //TODO We can show customized HTML page when page not found/ or server not found error.
            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    //Custom web chrome client
    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            PAGE_LOAD_PROGRESS = newProgress;
            //Log.i(TAG, "Page progress [" + PAGE_LOAD_PROGRESS + "%]");
            super.onProgressChanged(view, newProgress);
        }
    }

    private void showError(Context mContext, int errorCode) {
        //Prepare message
        String message = null;
        String title = null;
        if (errorCode == WebViewClient.ERROR_AUTHENTICATION) {
            message = "User authentication failed on server";
            title = "Auth Error";
        } else if (errorCode == WebViewClient.ERROR_TIMEOUT) {
            message = "The server is taking too much time to communicate. Try again later.";
            title = "Connection Timeout";
        } else if (errorCode == WebViewClient.ERROR_TOO_MANY_REQUESTS) {
            message = "Too many requests during this load";
            title = "Too Many Requests";
        } else if (errorCode == WebViewClient.ERROR_UNKNOWN) {
            message = "Generic error";
            title = "Unknown Error";
        } else if (errorCode == WebViewClient.ERROR_BAD_URL) {
            message = "Check entered URL..";
            title = "Malformed URL";
        } else if (errorCode == WebViewClient.ERROR_CONNECT) {
            message = "Failed to connect to the server";
            title = "Connection";
        } else if (errorCode == WebViewClient.ERROR_FAILED_SSL_HANDSHAKE) {
            message = "Failed to perform SSL handshake";
            title = "SSL Handshake Failed";
        } else if (errorCode == WebViewClient.ERROR_HOST_LOOKUP) {
            message = "Server or proxy hostname lookup failed";
            title = "Host Lookup Error";
        } else if (errorCode == WebViewClient.ERROR_PROXY_AUTHENTICATION) {
            message = "User authentication failed on proxy";
            title = "Proxy Auth Error";
        } else if (errorCode == WebViewClient.ERROR_REDIRECT_LOOP) {
            message = "Too many redirects";
            title = "Redirect Loop Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_AUTH_SCHEME) {
            message = "Unsupported authentication scheme (not basic or digest)";
            title = "Auth Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_UNSUPPORTED_SCHEME) {
            message = "Unsupported URI scheme";
            title = "URI Scheme Error";
        } else if (errorCode == WebViewClient.ERROR_FILE) {
            message = "Generic file error";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_FILE_NOT_FOUND) {
            message = "File not found";
            title = "File";
        } else if (errorCode == WebViewClient.ERROR_IO) {
            message = "The server failed to communicate. Try again later.";
            title = "IO Error";
        }

        if (message != null) {
            new AlertDialog.Builder(mContext)
                    .setMessage(message)
                    .setTitle(title)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    setResult(RESULT_CANCELED);
                                    //finish();
                                    dialog.dismiss();
                                }
                            }).show();
        }
    }

    public class ConnectionTimeoutHandler extends AsyncTask<Void, Void, String> {

        private static final String PAGE_LOADED = "PAGE_LOADED";
        private static final String CONNECTION_TIMEOUT = "CONNECTION_TIMEOUT";
        private static final long CONNECTION_TIMEOUT_UNIT = 60000L; //1 minute

        private Context mContext = null;
        private WebView webView;
        private Time startTime = new Time();
        private Time currentTime = new Time();
        private Boolean loaded = false;

        public ConnectionTimeoutHandler(Context mContext, WebView webView) {
            this.mContext = mContext;
            this.webView = webView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.startTime.setToNow();
            PatmActivity.PAGE_LOAD_PROGRESS = 0;
        }

        @Override
        protected void onPostExecute(String result) {

            if (CONNECTION_TIMEOUT.equalsIgnoreCase(result)) {
                showError(this.mContext, WebViewClient.ERROR_TIMEOUT);

                this.webView.stopLoading();
            } else if (PAGE_LOADED.equalsIgnoreCase(result)) {
                //Toast.makeText(this.mContext, "Page load success", Toast.LENGTH_LONG).show();
            } else {
                //Handle unknown events here
            }
        }

        @Override
        protected String doInBackground(Void... params) {

            while (!loaded) {
                currentTime.setToNow();
                if (PatmActivity.PAGE_LOAD_PROGRESS != 100
                        && (currentTime.toMillis(true) - startTime.toMillis(true)) > CONNECTION_TIMEOUT_UNIT) {
                    return CONNECTION_TIMEOUT;
                } else if (PatmActivity.PAGE_LOAD_PROGRESS == 100) {
                    loaded = true;
                }
            }
            return PAGE_LOADED;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PatmActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
