package com.example.caspaceapplication.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.TextViewOnReceiveContentListener;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.caspaceapplication.R;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerHomepageRecyclerViewAdapter extends RecyclerView.Adapter<CustomerHomepageRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CustomerHomepageRecyclerViewAdapter";

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private Context mContext;

    public CustomerHomepageRecyclerViewAdapter(Context context, ArrayList<String> names, ArrayList<String> mImageUrls) {
        this.mNames = mNames;
        this.mImageUrls = mImageUrls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recycler_homepage_customer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
        Glide.with(mContext)
                .asBitmap()
                .load(mImageUrls.get(position))
                .into(holder.image);
        holder.title.setText(mNames.get(position));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on an image: "+mNames.get(position));
                Toast.makeText(mContext, mNames.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }
    }
}
