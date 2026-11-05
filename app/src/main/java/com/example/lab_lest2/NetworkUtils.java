package com.example.lab_lest2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for performing HTTP requests and handling Wikipedia API interactions.
 */
public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String BASE_URL = "https://en.wikipedia.org/api/rest_v1/page/summary/";
    private static final String USER_AGENT = "DestinationGuideApp/1.0 (Android; Java; contact@example.com)";

    /**
     * Fetches destination summary from Wikipedia REST API synchronously.
     * Must be called on a background thread.
     */
    public static DestinationModel fetchDestinationSummary(String destinationQuery) throws Exception {
        if (destinationQuery == null || destinationQuery.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination query cannot be empty");
        }

        String formattedQuery = destinationQuery.trim().replace(" ", "_");
        String encodedTitle;
        try {
            encodedTitle = URLEncoder.encode(formattedQuery, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encodedTitle = formattedQuery;
        }

        String urlString = BASE_URL + encodedTitle;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
            connection.disconnect();
            throw new IOException("Destination not found");
        } else if (responseCode != HttpURLConnection.HTTP_OK) {
            connection.disconnect();
            throw new IOException("Server error: HTTP " + responseCode);
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        connection.disconnect();

        String jsonResponse = builder.toString();
        return parseDestinationJson(jsonResponse, destinationQuery);
    }

    /**
     * Parses Wikipedia REST API summary JSON into DestinationModel.
     */
    private static DestinationModel parseDestinationJson(String jsonStr, String originalQuery) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);

        // Check for error response or missing content
        String type = jsonObject.optString("type", "");
        if ("https://mediawiki.org/wiki/HyperSwitch/errors/not_found".equals(type)) {
            throw new JSONException("Destination not found");
        }

        String title = jsonObject.optString("title", originalQuery);
        String extract = jsonObject.optString("extract", "");

        if (extract.isEmpty()) {
            extract = jsonObject.optString("description", "No detailed summary available for this destination.");
        }

        // Parse thumbnail image URL
        String imageUrl = null;
        if (jsonObject.has("thumbnail")) {
            JSONObject thumbnailObj = jsonObject.optJSONObject("thumbnail");
            if (thumbnailObj != null) {
                imageUrl = thumbnailObj.optString("source", null);
            }
        }

        // Parse desktop page URL
        String pageUrl = null;
        if (jsonObject.has("content_urls")) {
            JSONObject contentUrls = jsonObject.optJSONObject("content_urls");
            if (contentUrls != null) {
                JSONObject desktopObj = contentUrls.optJSONObject("desktop");
                if (desktopObj != null) {
                    pageUrl = desktopObj.optString("page", null);
                }
            }
        }

        if (pageUrl == null || pageUrl.isEmpty()) {
            try {
                pageUrl = "https://en.wikipedia.org/wiki/" + URLEncoder.encode(title.replace(" ", "_"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                pageUrl = "https://en.wikipedia.org/wiki/" + title.replace(" ", "_");
            }
        }

        return new DestinationModel(title, extract, imageUrl, pageUrl);
    }

    /**
     * Downloads an image from a URL into a Bitmap.
     * Must be called on a background thread.
     */
    public static Bitmap fetchImage(String imageUrlStr) {
        if (imageUrlStr == null || imageUrlStr.trim().isEmpty()) {
            return null;
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(imageUrlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error downloading image: " + e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}