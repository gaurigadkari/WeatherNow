package com.example.android.weathernow.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Gauri Gadkari on 10/26/17.
 */
public class Source {
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("crawl_rate")
    @Expose
    private int crawlRate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCrawlRate() {
        return crawlRate;
    }

    public void setCrawlRate(int crawlRate) {
        this.crawlRate = crawlRate;
    }
}
