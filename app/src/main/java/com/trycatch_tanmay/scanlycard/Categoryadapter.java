package com.trycatch_tanmay.scanlycard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Categoryadapter extends RecyclerView.Adapter<Categoryadapter.ViewHolder> {
    Context contex;
    List<ModelHome> categorylist;
    public Categoryadapter(Context contex, List<ModelHome> categorylist) {
        this.contex = contex;
        this.categorylist = categorylist;
    }
    public void addUser(ModelHome modelHome) {
        categorylist.add(modelHome);
        notifyDataSetChanged();
    }

    public void clearData() {
        categorylist.clear();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Categoryadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(contex).inflate(R.layout.homeitemview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categoryadapter.ViewHolder holder, int position) {
        ModelHome modelHome = categorylist.get(position);
        holder.bind(modelHome);
        holder.shareOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = modelHome.getName();
                String position = modelHome.getPosition();
                String address = modelHome.getAddress();
                String contact = modelHome.getContact();
                String image = modelHome.getImagePath();

                // Create a share intent
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                // Add the data to be shared
                String shareMessage = "Name: " + name + "\n"
                        + "Position: " + position + "\n"
                        + "Address: " + address + "\n"
                        + "Contact: " + contact + "\n"
                        + "Image: " + image;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                // Start the activity to share the data
                contex.startActivity(Intent.createChooser(shareIntent, "Share via"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_1, image_menu, editOpt, favoriteOpt, shareOpt;
        TextView textView1, textView2, textView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_1 = itemView.findViewById(R.id.image_1);
            image_menu = itemView.findViewById(R.id.image_menu);
            editOpt = itemView.findViewById(R.id.editOpt);
            favoriteOpt = itemView.findViewById(R.id.favoriteOpt);
            shareOpt = itemView.findViewById(R.id.shareOpt);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
        }

        public void bind(ModelHome modelHome) {
            textView1.setText("Name: " + modelHome.getName());
            textView2.setText("Position: " + modelHome.getPosition());
            textView3.setText("Company: " + modelHome.getAddress());
            Glide.with(contex).load(modelHome.getImagePath()).into(image_1);
        }
    }
}
