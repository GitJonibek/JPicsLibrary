package com.jpics.jpics;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class JPics {

    private Context context;
    private String url;
    private DownloadManager.OnCompleteListener<Bitmap> onCompleteListener;

    private JPics(Context context) {
        this.context = context;
    }

    public static JPicsHider with(Context context) {
        return new JPicsHider(context);
    }

    public JPicsDownloadTask into(final ImageView imageView) {
        return DownloadManager.getInstance().downloadImage(url, imageView, onCompleteListener);
    }

    public String getUrl() {
        return url;
    }

    private JPics setUrl(String url) {
        this.url = url;
        return this;
    }

    public static class JPicsHider {
        private Context context;

        private JPicsHider(Context context) {
            this.context = context;
        }

        public JPics setUrl(String url) {
            JPics jPics = new JPics(context);
            return jPics.setUrl(url);
        }

    }

    public static class Config{

        public static void setCacheCapacity(int capacity) {
            DownloadManager.getInstance().setCacheCapacity(capacity);
        }
        public static int getCacheCapacity() {
            return DownloadManager.getInstance().getCacheCapacity();
        }
    }

}
