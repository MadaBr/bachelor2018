package com.mygdx.game;

/**
 * Created by Mada on 3/27/2018.
 */

public class TeachingItem {
    private String imgURL;
    private String destinationLanguage;
    private String translatedItem;
    private String nativeItem;
    private String sourceLanguage;

    public TeachingItem(String imgURL, String translatedItem, String nativeItem, String destinationLanguage, String sourceLanguage) {
        this.imgURL = imgURL;
        this.translatedItem = translatedItem;
        this.nativeItem = nativeItem;
        this.destinationLanguage = destinationLanguage;
        this.sourceLanguage = sourceLanguage;

    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getDestinationLanguage() {
        return destinationLanguage;
    }

    public void setDestinationLanguage(String destinationLanguage) {
        this.destinationLanguage = destinationLanguage;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTranslatedItem() {
        return translatedItem;
    }

    public void setTranslatedItem(String translatedItem) {
        this.translatedItem = translatedItem;
    }

    public String getNativeItem() {
        return nativeItem;
    }

    public void setNativeItem(String nativeItem) {
        this.nativeItem = nativeItem;
    }
}
