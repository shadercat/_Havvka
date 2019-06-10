package com.shadercat.havvka;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

class LoadImage extends AsyncTask<Item, Void, Bitmap> {

    private final WeakReference<ImageView> imageViewReference;
    private final WeakReference<IPermissionForSet> watcherReference;

    public LoadImage(ImageView imageView, IPermissionForSet flag) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        watcherReference = new WeakReference<IPermissionForSet>(flag);
    }

    @Override
    protected Bitmap doInBackground(Item... params) {
        try {
            return downloadBitmap(params[0].getUrl(), params[0].getID());
        } catch (Exception e) {
            // log error
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference.get();
        IPermissionForSet watcher = watcherReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.food_test);
            }
        }
    }

    private Bitmap downloadBitmap(URL url, int id) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    DataAdapter.imgCache.put(id, bitmap);
                }
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }
}

interface IPermissionForSet {
    boolean isInView();
}
