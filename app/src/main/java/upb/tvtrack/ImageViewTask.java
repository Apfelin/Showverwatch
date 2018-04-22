package upb.tvtrack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageViewTask extends AsyncTask<String, Void, Bitmap> {

    ImageView poster;

    public ImageViewTask(ImageView _poster) {

        this.poster = _poster;
    }

    protected Bitmap doInBackground(String... urls) {

        String urldisplay = urls[0];
        Bitmap poster = null;

        try {

            InputStream in = new java.net.URL(urldisplay).openStream();
            poster = BitmapFactory.decodeStream(in);
        } catch (Exception e) {

            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        return poster;
    }

    protected void onPostExecute(Bitmap result) {

        poster.setImageBitmap(result);
    }
}