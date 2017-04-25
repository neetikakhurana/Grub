package com.lecture.nitika.grub.Helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import com.lecture.nitika.grub.MyRecipeResultRecyclerViewAdapter;
import com.lecture.nitika.grub.R;

import java.io.IOException;
import java.net.URL;

/**
 * Created by tchet on 4/23/2017.
 */

public class DownloadImageAsyncTask extends AsyncTask<
                                MyRecipeResultRecyclerViewAdapter.ViewHolder,
                                Void,
                                MyRecipeResultRecyclerViewAdapter.ViewHolder> {

    @Override
    protected MyRecipeResultRecyclerViewAdapter.ViewHolder doInBackground(MyRecipeResultRecyclerViewAdapter.ViewHolder... params) {

        MyRecipeResultRecyclerViewAdapter.ViewHolder viewHolder = params[0];
        try {
            URL imageURL = viewHolder.mImageUrl;
            Bitmap bitmapfrmServer = BitmapFactory.decodeStream(imageURL.openStream());
            viewHolder.bitmap = bitmapfrmServer;
        } catch (IOException e) {
            Log.e("error", "Downloading Image Failed");
            viewHolder.bitmap = null;
        }

        return viewHolder;
    }

    @Override
    protected void onPostExecute(MyRecipeResultRecyclerViewAdapter.ViewHolder result) {
        if (result.bitmap == null) {

            result.mImageView.setImageResource(R.drawable.com_facebook_close);
        } else {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(null,result.bitmap);
            dr.setCornerRadius(15);
            result.mImageView.setImageDrawable(dr);
            //result.mImageView.setImageBitmap(result.bitmap);
        }
    }
}
