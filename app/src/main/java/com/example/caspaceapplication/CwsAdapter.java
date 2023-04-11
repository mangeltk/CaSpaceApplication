package com.example.caspaceapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CwsAdapter extends RecyclerView.Adapter<CwsAdapter.MyHolder> {

    Context context;
    ArrayList<ModelClass> arrayList;

    public CwsAdapter(Context context, ArrayList<ModelClass> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        layoutInflater = LayoutInflater.from(context);
    }

    LayoutInflater layoutInflater;

    @NonNull
    @Override
    public CwsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_file, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CwsAdapter.MyHolder holder, int position) {
        holder.cwsName.setText(arrayList.get(position).getCwsName());
        holder.cwsNum.setText(arrayList.get(position).getCwsNum());
        holder.img.setImageResource(arrayList.get(position).getImg());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView cwsName, cwsNum;
        ImageView img;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cwsName = itemView.findViewById(R.id.enspace);
            cwsName = itemView.findViewById(R.id.regus);
            img=itemView.findViewById(R.id.img);

        }
    }
}
