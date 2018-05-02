package com.jpics.jpics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

class DownloadManager {

    private static final DownloadManager ourInstance = new DownloadManager();

    public static DownloadManager getInstance() {
        return ourInstance;
    }

    private Map<String, DownloadImageAsyncTask> tasks = new HashMap<>();
    private Map<String, DownloadFileAsyncTask> file_tasks = new HashMap<>();
    private Cache cache = new Cache();

    private DownloadManager() {
    }

    public int getCacheCapacity() {
        return cache.getCapacity();
    }

    public void setCacheCapacity(int capacity) {
        cache.setCapacity(capacity);
    }

    public JPicsDownloadTask downloadImage(String url, ImageView imageView, OnCompleteListener<Bitmap> onCompleteListener) {
        removeImageViewFromTask(imageView);
        JPicsHolder holder = new JPicsHolder(imageView, onCompleteListener);

        Object data = cache.get(url);
        if (data instanceof Bitmap) {
            Bitmap bitmap = (Bitmap) data;
            imageView.setImageBitmap(bitmap);
            holder.complete(bitmap);
        }

        DownloadImageAsyncTask task = createIfNotExists(url);
        task.addHolder(holder);
        if (task.getStatus() == AsyncTask.Status.PENDING)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return new JPicsDownloadTask(task, imageView);
    }

    private void removeImageViewFromTask(ImageView imageView) {
        for (DownloadImageAsyncTask task: tasks.values()) {
            task.removeHolder(imageView);
        }
    }

    private DownloadImageAsyncTask createIfNotExists(final String url) {
        DownloadImageAsyncTask task = tasks.get(url);

        if (task == null) {
            task = new DownloadImageAsyncTask(url, new OnCompleteListener<Bitmap>() {
                @Override
                public void onComplete(Bitmap data) {
                    tasks.remove(url);
                    cache.put(url, data);
                }
            });
            tasks.put(url, task);
        }

        return task;
    }

    class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        private OnCompleteListener<Bitmap> onCompleteListener;
        private String url;
        private Map<ImageView, JPicsHolder> holders = new HashMap<>();

        public DownloadImageAsyncTask(String url, OnCompleteListener<Bitmap> onCompleteListener) {
            this.onCompleteListener = onCompleteListener;
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlString = this.url;
            InputStream in = null;

            try
            {

                URL url = new URL(urlString);
                URLConnection urlConn = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.connect();

                in = httpConn.getInputStream();

            }catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(in);

            return ImageResizer.ScaleDownBitmap(bitmap, 40 * 40, false);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            complete(bitmap);
            for (JPicsHolder holder: holders.values()) {
                holder.getImageView().setImageBitmap(bitmap);
                holder.complete(bitmap);
            }
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            complete(bitmap);
            for (JPicsHolder holder: holders.values()) {
                if (bitmap != null)
                    holder.getImageView().setImageBitmap(bitmap);
                holder.complete(bitmap);
            }
        }

        void addHolder(JPicsHolder holder) {
            holders.put(holder.getImageView(), holder);
        }

        void removeHolder(ImageView imageView) {
            holders.remove(imageView);
        }

        public void complete(Bitmap bitmap) {
            if (onCompleteListener != null) {
                onCompleteListener.onComplete(bitmap);
            }
        }

    }

    static class DownloadFileAsyncTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            String path = strings[0];               // -> file path
            String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());   // -> file name


            String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
            File folder = new File(extStorageDirectory);
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try{
                pdfFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
            FileDownloader.downloadFile(path, pdfFile);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("FILE_TAG", "Successfull!");
        }
    }

    public interface OnCompleteListener<T> {
        public void onComplete(T data);
    }
}
