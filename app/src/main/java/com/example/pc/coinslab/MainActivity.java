package com.example.pc.coinslab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView webview;
    public ProgressBar bar;
    private FrameLayout frameLayout;
    private String WebAddress = "https://www.coinslab.com/";
    SwipeRefreshLayout swipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        bar = (ProgressBar) findViewById(R.id.progressBar2);
        bar.setMax(100);
        webview = (WebView) findViewById(R.id.mywebview);
        webview.clearCache(true);
        webview.clearHistory();
        swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadWeb();
            }
        });

       LoadWeb();

    }

    public  void LoadWeb(){

        webview = (WebView) findViewById(R.id.mywebview);

        webview.setWebViewClient(new HelpClient());




        webview.setWebChromeClient(new WebChromeClient(){




            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                frameLayout.setVisibility(View.VISIBLE);
                bar.setProgress(newProgress);

                setTitle("Loading....");

                if (newProgress == 100){
                    frameLayout.setVisibility(View.GONE);
                    setTitle(view.getTitle());
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(false);
        webview.loadUrl(WebAddress);
        webview.loadUrl("javascript:window.location.reload(true)");

        swipe.setRefreshing(false);
        bar.setProgress(0);

    }




    private class HelpClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            frameLayout.setVisibility(view.VISIBLE);
            return true;
        }

        

        @Override
        public void onPageFinished(WebView view, String url) {

            swipe.setRefreshing(false);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

            try {
                webview.stopLoading();
            } catch (Exception e) {

            }

            if (webview.canGoBack()) {
                webview.goBack();
            }

            webview.loadUrl("about:blank");
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Check your internet connection and try again.");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    startActivity(getIntent());
                }
            });

            alertDialog.show();

            super.onReceivedError(view, request, error);

        }


}



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (webview.canGoBack()){
                webview.goBack();
                return true;
            }
        }


        return super.onKeyDown(keyCode, event);
    }


}
