package com.trycatch_tanmay.scanlycard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context context;
    private List<ModelHome> modelHomes;
    private FavouriteDao favouriteDao; // Add FavouriteDao field
    HomeFragment homeFragment;
    private OnFavoriteItemClickListener listener; // Corrected variable name

    public interface OnFavoriteItemClickListener {
        void onFavoriteItemClick(int position,ModelHome modelHome);
    }
    public interface OnAddToCategoryClickListener {
        void onAddToCategoryClick(int position, ModelHome modelHome);
    }
    private OnAddToCategoryClickListener addToCategoryListener;
    public void setOnAddToCategoryClickListener(OnAddToCategoryClickListener listener) {
        this.addToCategoryListener = listener;
    }


    public HomeAdapter(Context context, List<ModelHome> modelHomes, FavouriteDao favouriteDao, HomeFragment homeFragment) {
        this.context = context;
        this.modelHomes = modelHomes;
        this.favouriteDao = favouriteDao;
        this.homeFragment = homeFragment;
    }


    public void addUser(ModelHome modelHome) {
        modelHomes.add(modelHome);
        notifyDataSetChanged();
    }

    public void clearData() {
        modelHomes.clear();
        notifyDataSetChanged();
    }
    // Correcting the listener implementation
    public void setOnFavoriteItemClickListener(OnFavoriteItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.homeitemview, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        final ModelHome modelHome = modelHomes.get(position); // Declare as final here
        if (modelHomes.get(position).getName() != null && modelHomes.get(position).getContact() != null &&
                modelHomes.get(position).getAddress() != null && modelHomes.get(position).getPosition() != null) {
            if (modelHome.getImagePath() != null) {
                // Use Glide or Picasso to load image into the ImageView
                Glide.with(context).load(modelHome.getImagePath()).into(holder.image_1);
            }
            // Set data to views
            holder.textView1.setText("Names:" + modelHomes.get(position).getName());
            holder.textView2.setText(("Position:" + modelHome.getPosition()));
            holder.textView3.setText("Company Name:" + modelHome.getAddress());
            holder.favoriteOpt.setOnClickListener(v -> {
                // Handle favourite option click
//                if (favouriteDao != null) {
//                    favouriteDao.Insert(modelHome); // Insert the clicked item into the database
                if (listener != null) {
                    listener.onFavoriteItemClick(position, modelHome); // Pass position and modelHome to the listener
                }
            });
            holder.image_menu.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, v);
                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_add_to_category) {
                        if (addToCategoryListener != null) {
                            addToCategoryListener.onAddToCategoryClick(position, modelHome);
                        }
                        return true;
                    } else {
                        return false;
                    }
                });
                popupMenu.show();
            });
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
                    context.startActivity(Intent.createChooser(shareIntent, "Share via"));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return modelHomes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_1,image_menu,editOpt,favoriteOpt,shareOpt;
        TextView textView1,textView2,textView3;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_1=itemView.findViewById(R.id.image_1);
            image_menu=itemView.findViewById(R.id.image_menu);
            editOpt=itemView.findViewById(R.id.editOpt);
            favoriteOpt=itemView.findViewById(R.id.favoriteOpt);
            shareOpt=itemView.findViewById(R.id.shareOpt);
            textView1=itemView.findViewById(R.id.textView1);
            textView2=itemView.findViewById(R.id.textView2);
            textView3=itemView.findViewById(R.id.textView3);
//            Set on clickOnclickListener for favouriteOpt
            favoriteOpt.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    if (listener != null) {
                        listener.onFavoriteItemClick(adapterPosition, modelHomes.get(adapterPosition));
                    }
                }
            });
        }

        }
    }

