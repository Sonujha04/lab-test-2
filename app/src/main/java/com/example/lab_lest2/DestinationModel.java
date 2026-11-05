package com.example.lab_lest2;

import java.io.Serializable;

/**
 * Model class representing travel destination details retrieved from Wikipedia API.
 */
public class DestinationModel implements Serializable {
    private final String title;
    private final String extract;
    private final String imageUrl;
    private final String pageUrl;

    public DestinationModel(String title, String extract, String imageUrl, String pageUrl) {
        this.title = title;
        this.extract = extract;
        this.imageUrl = imageUrl;
        this.pageUrl = pageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getExtract() {
        return extract;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPageUrl() {
        return pageUrl;
    }
}