package com.example.weatherapp;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/*

    https://www.googleapis.com/customsearch/v1?q=Tourist%20places%20of%20Los%20Angeles,CA,USA&
        cx=012110676718765995432:tn1arlbxw37&imgSize=huge&imgType=news&num=8&searchType=image&key=AIzaSyBAY4Pax3D-dg33_HKYnXpbiOus5KrknZA

 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageView;

        public MyViewHolder(ImageView v) {
            super(v);
            imageView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PhotosAdapter(String[] myDataset, Context myContext) {
        mDataset = myDataset;
        mContext = myContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PhotosAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        ImageView v = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_launcher_round)
                .override(1400,800);


        Log.d("Dataset at position", mDataset[position]);
        Glide.with(mContext).load(mDataset[position]).apply(options).into(holder.imageView);


//        Picasso.get()
//                .load(mDataset[position])
//                .resize(1400, 800).centerCrop()
//                .into(holder.imageView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        e.printStackTrace();
//                    }
//                });
//
//        Picasso.get().load(R.drawable.eye).into(holder.imageView);
//        holder.imageView.setImageResource(R.drawable.ic_eye_black_48dp);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}