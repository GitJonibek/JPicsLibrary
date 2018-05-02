package com.jpics.jpics;

import android.graphics.Bitmap;
import android.widget.ImageView;

class JPicsHolder {
    private ImageView imageView;
    private DownloadManager.OnCompleteListener<Bitmap> onCompleteListener;

    public JPicsHolder(ImageView imageView, DownloadManager.OnCompleteListener<Bitmap> onCompleteListener) {
        this.imageView = imageView;
        this.onCompleteListener = onCompleteListener;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public DownloadManager.OnCompleteListener<Bitmap> getOnCompleteListener() {
        return onCompleteListener;
    }

    public void setOnCompleteListener(DownloadManager.OnCompleteListener<Bitmap> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    public void complete(Bitmap bitmap) {
        if (onCompleteListener != null) {
            onCompleteListener.onComplete(bitmap);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JPicsHolder) {
            JPicsHolder holder = (JPicsHolder)obj;
            return holder.imageView.equals(this.imageView);
        }
        return super.equals(obj);
    }
}