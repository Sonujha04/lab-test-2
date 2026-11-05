package com.example.lab_lest2;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main Activity for DestinationGuide travel information application.
 * Manages Home / Search screen and Destination Summary display.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private EditText etDestinationInput;
    private Button btnSearch;
    private LinearLayout layoutProgress;
    private LinearLayout llPopularDestinationsGrid;

    // Summary Card
    private MaterialCardView cardSummary;
    private ImageView ivDestinationImage;
    private TextView tvDestinationTitle;
    private TextView tvDestinationExtract;
    private Button btnReadMore;

    // Error Card
    private MaterialCardView cardError;
    private TextView tvErrorMessage;
    private Button btnRetry;

    // Executor and Handler for Background Threading
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private DestinationModel currentDestination;
    private String currentSearchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        populatePopularDestinations();
        setupListeners();
    }

    private void bindViews() {
        etDestinationInput = findViewById(R.id.etDestinationInput);
        btnSearch = findViewById(R.id.btnSearch);
        layoutProgress = findViewById(R.id.layoutProgress);
        llPopularDestinationsGrid = findViewById(R.id.llPopularDestinationsGrid);

        cardSummary = findViewById(R.id.cardSummary);
        ivDestinationImage = findViewById(R.id.ivDestinationImage);
        tvDestinationTitle = findViewById(R.id.tvDestinationTitle);
        tvDestinationExtract = findViewById(R.id.tvDestinationExtract);
        btnReadMore = findViewById(R.id.btnReadMore);

        cardError = findViewById(R.id.cardError);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        btnRetry = findViewById(R.id.btnRetry);
    }

    /**
     * Dynamically populates 24 famous destinations and palaces into a 4x6 grid.
     */
    private void populatePopularDestinations() {
        if (llPopularDestinationsGrid == null) return;
        llPopularDestinationsGrid.removeAllViews();

        String[] destinations = {
                "Paris", "London", "Tokyo", "Delhi",
                "Rome", "New York", "Dubai", "Sydney",
                "Cairo", "Taj Mahal", "Versailles", "Buckingham",
                "Barcelona", "Kyoto", "Istanbul", "Singapore",
                "Bangkok", "Venice", "Amsterdam", "Rio",
                "Prague", "Athens", "Vienna", "Jaipur"
        };

        int[][] themeColors = {
                {R.color.chip_rose_bg, R.color.chip_rose_stroke, R.color.chip_rose_text},
                {R.color.chip_sky_bg, R.color.chip_sky_stroke, R.color.chip_sky_text},
                {R.color.chip_violet_bg, R.color.chip_violet_stroke, R.color.chip_violet_text},
                {R.color.chip_emerald_bg, R.color.chip_emerald_stroke, R.color.chip_emerald_text},
                {R.color.chip_amber_bg, R.color.chip_amber_stroke, R.color.chip_amber_text},
                {R.color.chip_cyan_bg, R.color.chip_cyan_stroke, R.color.chip_cyan_text},
                {R.color.chip_indigo_bg, R.color.chip_indigo_stroke, R.color.chip_indigo_text}
        };

        float density = getResources().getDisplayMetrics().density;
        int heightPx = (int) (38 * density);
        int marginPx = (int) (3 * density);
        int cornerRadiusPx = (int) (16 * density);
        int strokeWidthPx = (int) (1.2f * density);

        int columns = 4;
        int totalItems = destinations.length;

        for (int row = 0; row < totalItems; row += columns) {
            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowParams.setMargins(0, 0, 0, (int) (6 * density));
            rowLayout.setLayoutParams(rowParams);

            for (int col = 0; col < columns; col++) {
                int index = row + col;
                if (index < totalItems) {
                    final String name = destinations[index];
                    int[] colorTheme = themeColors[index % themeColors.length];

                    MaterialButton button = new MaterialButton(this);
                    button.setText(name);
                    button.setTextSize(11);
                    button.setTypeface(null, Typeface.BOLD);
                    button.setSingleLine(true);
                    button.setEllipsize(TextUtils.TruncateAt.END);
                    button.setTextColor(ContextCompat.getColor(this, colorTheme[2]));
                    button.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, colorTheme[0])));
                    button.setStrokeColor(ColorStateList.valueOf(ContextCompat.getColor(this, colorTheme[1])));
                    button.setStrokeWidth(strokeWidthPx);
                    button.setCornerRadius(cornerRadiusPx);
                    button.setInsetTop(0);
                    button.setInsetBottom(0);
                    button.setPadding(0, 0, 0, 0);

                    LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(
                            0,
                            heightPx,
                            1.0f
                    );
                    btnParams.setMargins(marginPx, 0, marginPx, 0);
                    button.setLayoutParams(btnParams);

                    button.setOnClickListener(v -> searchQuickDestination(name));
                    rowLayout.addView(button);
                } else {
                    View spacer = new View(this);
                    LinearLayout.LayoutParams spacerParams = new LinearLayout.LayoutParams(
                            0,
                            heightPx,
                            1.0f
                    );
                    spacerParams.setMargins(marginPx, 0, marginPx, 0);
                    spacer.setLayoutParams(spacerParams);
                    rowLayout.addView(spacer);
                }
            }
            llPopularDestinationsGrid.addView(rowLayout);
        }
    }

    private void setupListeners() {
        // Search Button click listener
        btnSearch.setOnClickListener(v -> performSearch());

        // Keyboard search action listener
        etDestinationInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });

        // Read More Button click listener -> Screen 3 (WebView)
        btnReadMore.setOnClickListener(v -> {
            if (currentDestination != null && currentDestination.getPageUrl() != null) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra(WebViewActivity.EXTRA_PAGE_URL, currentDestination.getPageUrl());
                intent.putExtra(WebViewActivity.EXTRA_TITLE, currentDestination.getTitle());
                startActivity(intent);
            }
        });

        // Retry Button listener
        btnRetry.setOnClickListener(v -> {
            if (!currentSearchQuery.isEmpty()) {
                fetchDestinationData(currentSearchQuery);
            } else {
                performSearch();
            }
        });
    }

    private void searchQuickDestination(String destinationName) {
        etDestinationInput.setText(destinationName);
        etDestinationInput.setSelection(destinationName.length());
        performSearch();
    }

    private void performSearch() {
        String query = etDestinationInput.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_input, Toast.LENGTH_SHORT).show();
            etDestinationInput.requestFocus();
            return;
        }

        hideKeyboard();
        currentSearchQuery = query;
        fetchDestinationData(query);
    }

    /**
     * Executes HTTP GET request and image download on a background thread.
     */
    private void fetchDestinationData(String query) {
        if (!isNetworkAvailable()) {
            showErrorUI(getString(R.string.error_network));
            return;
        }

        showLoadingUI();

        executorService.execute(() -> {
            try {
                // Fetch destination summary JSON
                DestinationModel destination = NetworkUtils.fetchDestinationSummary(query);

                // Fetch thumbnail image if available
                Bitmap imageBitmap = null;
                if (destination.getImageUrl() != null && !destination.getImageUrl().isEmpty()) {
                    imageBitmap = NetworkUtils.fetchImage(destination.getImageUrl());
                }

                final DestinationModel finalDestination = destination;
                final Bitmap finalImageBitmap = imageBitmap;

                // Post result to main UI thread
                mainHandler.post(() -> showSummaryUI(finalDestination, finalImageBitmap));

            } catch (Exception e) {
                Log.e(TAG, "Error fetching destination: " + e.getMessage());
                String errorMsg = getString(R.string.error_not_found);
                if (e.getMessage() != null && e.getMessage().contains("Destination not found")) {
                    errorMsg = getString(R.string.error_not_found);
                } else if (e.getMessage() != null && e.getMessage().contains("Server error")) {
                    errorMsg = "Unable to fetch information. " + e.getMessage();
                }

                final String finalErrorMsg = errorMsg;
                mainHandler.post(() -> showErrorUI(finalErrorMsg));
            }
        });
    }

    private void showLoadingUI() {
        cardSummary.setVisibility(View.GONE);
        cardError.setVisibility(View.GONE);
        layoutProgress.setVisibility(View.VISIBLE);
    }

    private void showSummaryUI(DestinationModel destination, Bitmap imageBitmap) {
        currentDestination = destination;
        layoutProgress.setVisibility(View.GONE);
        cardError.setVisibility(View.GONE);

        tvDestinationTitle.setText(destination.getTitle());
        tvDestinationExtract.setText(destination.getExtract());

        if (imageBitmap != null) {
            ivDestinationImage.setImageBitmap(imageBitmap);
            ivDestinationImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            ivDestinationImage.setImageResource(R.drawable.ic_image_placeholder);
            ivDestinationImage.setScaleType(ImageView.ScaleType.CENTER);
        }

        cardSummary.setVisibility(View.VISIBLE);
    }

    private void showErrorUI(String message) {
        layoutProgress.setVisibility(View.GONE);
        cardSummary.setVisibility(View.GONE);

        tvErrorMessage.setText(message);
        cardError.setVisibility(View.VISIBLE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) return false;
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
        }
        return false;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}