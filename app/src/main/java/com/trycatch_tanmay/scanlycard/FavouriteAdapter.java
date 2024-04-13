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

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private Context context;
    private List<ModelHome> favouriteList;
    FavouriteFragment favouriteFragment;
    AdapterListener adapterListener;
    UpDateListner upDateListner;
    private OnItemClickListener listener; // Listener for item clicks
    public interface OnItemClickListener {
        void onItemClick(ModelHome modelHome);
    }

    public interface AdapterListener {
        void OnDelete(double id, int pos);
    }
    public interface UpDateListner{
        void OnUpdate(ModelHome modelHome);
    }
    public FavouriteAdapter(Context context, List<ModelHome> favouriteList, FavouriteFragment favouriteFragment) {
        this.context = context;
        this.favouriteList = favouriteList;
        this.favouriteFragment = favouriteFragment;
    }

    public void addUser(ModelHome modelHome) {
        favouriteList.add(modelHome);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favouriteitemview, parent, false);
        return new ViewHolder(view);
    }

    public void clearData() {
        favouriteList.clear();
        notifyDataSetChanged();
    }
    public void removeUser(int position){
        favouriteList.remove(position);
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        int pos = position;
        ModelHome modelHome = favouriteList.get(position);
        holder.bind(modelHome);
        holder.deleteOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                on delete is performing
                adapterListener.OnDelete(favouriteList.get(pos).getId(), pos);
            }
        });
        holder.editOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upDateListner.OnUpdate(modelHome);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pass the details of the clicked item to the listener
                if (listener != null) {
                    listener.onItemClick(modelHome);
                }
            }
        });
        holder.shareOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the details of the modelHome
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

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public void setOnFavoriteItemClickListener(FavouriteFragment favouriteFragment) {
    }

    public interface OnFavoriteItemClickListener {
        void onFavoriteItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_1, image_menu, editOpt, deleteOpt, shareOpt;
        TextView textView1, textView2, textView3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_1 = itemView.findViewById(R.id.image_1);
            image_menu = itemView.findViewById(R.id.image_menu);
            editOpt = itemView.findViewById(R.id.editOpt);
            deleteOpt = itemView.findViewById(R.id.deleteOpt);
            shareOpt = itemView.findViewById(R.id.shareOpt);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);

        }

        public void bind(ModelHome modelHome) {
            textView1.setText("Name: " + modelHome.getName());
            textView2.setText("Position: " + modelHome.getPosition());
            textView3.setText("Company: " + modelHome.getAddress());
            Glide.with(context).load(modelHome.getImagePath()).into(image_1);
        }
    }
}
