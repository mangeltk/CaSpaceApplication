package com.example.caspaceapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.MyViewHolder> {

    ArrayList<BranchModel> datalist;

    public BranchAdapter(ArrayList<BranchModel> datalist) {
        this.datalist = datalist;
    }

    @NonNull
    @Override
    public BranchAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BranchAdapter.MyViewHolder holder, int position) {
        /*BranchModel branchModel = datalist.get(position);

        holder.branchName.setText(branchModel.getBranchName());
        holder.branchAddress.setText(branchModel.getBranchAddress());*/
        /*String imageUri = null;
        imageUri = branchModel.getBranchPicture();
        Picasso.get().load(imageUri).into(holder.imageView);
*/

        holder.t1.setText(datalist.get(position).getBranchName());
        holder.t2.setText(datalist.get(position).getBranchAddress());
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        //mageView imageView;
        TextView t1, t2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //imageView = itemView.findViewById(R.id.Bar);
            t1 = itemView.findViewById(R.id.t1);
            t2 = itemView.findViewById(R.id.t2);
        }
    }
}
