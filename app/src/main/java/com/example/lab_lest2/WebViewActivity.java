package com.example.lab_lest2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity for displaying complete Wikipedia articles inside an embedded WebView.
 * Features navigation controls: Back, Forward, Refresh, and Home.
 */
public class WebViewActivity extends AppCompatActivity {

    public static final String EXTRA_PAGE_URL = "extra_page_url";
    public static final String EXTRA_TITLE = "extra_title";

    private WebView webView;
    private ProgressBar pbHorizontal;
    private ProgressBar pbCenter;
    private TextView tvHeaderTitle;
    private TextView tvHeaderUrl;

    // Navigation Controls
    private LinearLayout btnBackContainer;
    private LinearLayout btnForwardContainer;
    private LinearLayout btnRefreshContainer;
    private LinearLayout btnHomeContainer;
    private ImageView ivBack;
    private ImageView ivForward;
    private TextView tvBack;
    private TextView tvForward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Bind Views
        webView = findViewById(R.id.webViewArticle);
        pbHorizontal = findViewById(R.id.pbWebProgress);
        pbCenter = findViewById(R.id.pbCenterProgress);
        tvHeaderTitle = findViewById(R.id.tvWebHeaderTitle);
        tvHeaderUrl = findViewById(R.id.tvWebHeaderUrl);

        btnBackContainer = findViewById(R.id.btnWebBackContainer);
        btnForwardContainer = findViewById(R.id.btnWebForwardContainer);
        btnRefreshContainer = findViewById(R.id.btnWebRefreshContainer);
        btnHomeContainer = findViewById(R.id.btnWebHomeContainer);

        findViewById(R.id.btnHeaderClose).setOnClickListener(v -> finish());

        ivBack = findViewById(R.id.ivWebBack);
        ivForward = findViewById(R.id.ivWebForward);
        tvBack = findViewById(R.id.tvWebBack);
        tvForward = findViewById(R.id.tvWebForward);

        // Get Intent Data
        Intent intent = getIntent();
        String pageUrl = intent.getStringExtra(EXTRA_PAGE_URL);
        String title = intent.getStringExtra(EXTRA_TITLE);

        if (title != null && !title.isEmpty()) {
            tvHeaderTitle.setText(title);
        } else {
            tvHeaderTitle.setText(R.string.web_title);
        }

        if (pageUrl == null || pageUrl.isEmpty()) {
            Toast.makeText(this, "Invalid article URL.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvHeaderUrl.setText(pageUrl);

        setupWebView();
        setupNavigationControls();
        setupBackPressHandler();

        webView.loadUrl(pageUrl);
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        // Ensure navigation stays strictly inside the application
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbHorizontal.setVisibility(View.VISIBLE);
                if (url != null) {
                    tvHeaderUrl.setText(url);
                }
                updateNavControlsState();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbHorizontal.setVisibility(View.GONE);
                pbCenter.setVisibility(View.GONE);
                if (view.getTitle() != null && !view.getTitle().isEmpty()) {
                    tvHeaderTitle.setText(view.getTitle());
                }
                updateNavControlsState();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                pbHorizontal.setVisibility(View.GONE);
                pbCenter.setVisibility(View.GONE);
                updateNavControlsState();
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress < 100) {
                    pbHorizontal.setVisibility(View.VISIBLE);
                    pbHorizontal.setProgress(newProgress);
                } else {
                    pbHorizontal.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setupNavigationControls() {
        // 1. Back button
        btnBackContainer.setOnClickListener(v -> {
            if (webView.canGoBack()) {
                webView.goBack();
            }
        });

        // 2. Forward button
        btnForwardContainer.setOnClickListener(v -> {
            if (webView.canGoForward()) {
                webView.goForward();
            }
        });

        // 3. Refresh button
        btnRefreshContainer.setOnClickListener(v -> webView.reload());

        // 4. Home button
        btnHomeContainer.setOnClickListener(v -> {
            // Returns to application's home screen
            finish();
        });

        updateNavControlsState();
    }

    /**
     * Dynamically enables and disables Back and Forward buttons according to WebView history.
     */
    private void updateNavControlsState() {
        boolean canBack = webView.canGoBack();
        boolean canForward = webView.canGoForward();

        // Update Back Button
        btnBackContainer.setEnabled(canBack);
        float backAlpha = canBack ? 1.0f : 0.35f;
        ivBack.setAlpha(backAlpha);
        tvBack.setAlpha(backAlpha);

        // Update Forward Button
        btnForwardContainer.setEnabled(canForward);
        float forwardAlpha = canForward ? 1.0f : 0.35f;
        ivForward.setAlpha(forwardAlpha);
        tvForward.setAlpha(forwardAlpha);
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}