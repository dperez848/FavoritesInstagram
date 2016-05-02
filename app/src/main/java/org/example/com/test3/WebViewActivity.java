package org.example.com.test3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.example.com.test3.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    @Bind(R.id.web_view) WebView mWebView;
    @Bind(R.id.tool_bar) Toolbar mToolbar;
    @Bind(R.id.progress) ProgressBar progressBar;

    String username = "";
    String mUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_web_view);

        ButterKnife.bind(this);

        // Set Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            username = bundle.getString(Constants.KEY_WEBVIEW);
            getSupportActionBar().setTitle("@" + username);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mUrl = Constants.URL_PROFILE + username;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setWebViewClient(new MyWebViewClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals("www.instagram.com")) {
                // This is my web site, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            progressBar.setVisibility(View.INVISIBLE);
            return true;
        }
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_share) {
            startActivity(new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, getString(R.string.share_comment) + mUrl)
                    .setType("text/plain"));
        }
        if (id == android.R.id.home) {
            if(mWebView.canGoBack())
                mWebView.goBack();
            else
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(mWebView.canGoBack())
            mWebView.goBack();
        else
            finish();
    }
}
