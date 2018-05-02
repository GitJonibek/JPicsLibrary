package com.jpics.jpics;

import android.widget.ImageView;

public class JPicsDownloadTask {

    private DownloadManager.DownloadImageAsyncTask asyncImageTask;
    private ImageView imageView;

    public JPicsDownloadTask(DownloadManager.DownloadImageAsyncTask asyncTask, ImageView imageView) {
        this.asyncImageTask = asyncTask;
        this.imageView = imageView;
    }

    public void cancel() {
        asyncImageTask.removeHolder(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }

}
