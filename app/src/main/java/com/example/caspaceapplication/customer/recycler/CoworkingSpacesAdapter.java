package com.example.caspaceapplication.customer.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caspaceapplication.R;

import java.util.List;

public class CoworkingSpacesAdapter extends RecyclerView.Adapter<CoworkingSpacesAdapter.CoworkingSpacesViewHolder> {

    private List<CoworkingSpaces> cws;

    public void setData(List<CoworkingSpaces> list)
    {
        this.cws = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoworkingSpacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cws_item, parent, false);
        return new CoworkingSpacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoworkingSpacesViewHolder holder, int position) {

        CoworkingSpaces coworkingSpaces = cws.get(position);
        if(coworkingSpaces == null)
        {
            return;
        }

        holder.cws_one.setImageResource(coworkingSpaces.getResourceId());
        holder.enspace.setText(coworkingSpaces.getTitle());


    }

    @Override
    public int getItemCount() {
        if(cws !=null)
        {
            return cws.size();
        }
        return 0;
    }

    public class CoworkingSpacesViewHolder extends RecyclerView.ViewHolder{

        private ImageView cws_one;
        private TextView enspace;

        public CoworkingSpacesViewHolder(@NonNull View itemView) {
            super(itemView);

            cws_one = itemView.findViewById(R.id.cws_one);
            enspace = itemView.findViewById(R.id.enspace);
        }
    }
}
