package com.lecture.nitika.grub;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lecture.nitika.grub.Helper.DownloadImageAsyncTask;
import com.lecture.nitika.grub.Helper.RecipeItem;
import com.lecture.nitika.grub.RecipeResultFragment.OnListFragmentInteractionListener;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MyRecipeResultRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipeResultRecyclerViewAdapter.ViewHolder> {

    private final List<RecipeItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyRecipeResultRecyclerViewAdapter(List<RecipeItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reciperesult, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTimeView.setText(String.valueOf(mValues.get(position).getContent().getLikes()));
        holder.mTitleView.setText(mValues.get(position).getContent().getTitle());
        try {
            holder.mImageUrl = new URL(mValues.get(position).getContent().getImage());
            new DownloadImageAsyncTask().execute(holder);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTimeView;
        public final TextView mTitleView;
        public final ImageView mImageView;
        public RecipeItem mItem;
        public URL mImageUrl;
        public Bitmap bitmap;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTimeView = (TextView) view.findViewById(R.id.list_time);
            mTitleView = (TextView) view.findViewById(R.id.list_title);
            mImageView = (ImageView)view.findViewById(R.id.list_imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }
    }
}
