package com.trycatch_tanmay.scanlycard;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class FavouriteFragment extends Fragment implements FavouriteAdapter.AdapterListener,FavouriteAdapter.UpDateListner,FavouriteAdapter.OnItemClickListener{

    private RecyclerView favoRv;
    private FavouriteAdapter favouriteAdapter;
    private List<ModelHome> favouriteList;
    private FavouriteDao favouriteDao;
    private FavouriteDatabase favouriteDatabase;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        favoRv = view.findViewById(R.id.favoRv);
        favouriteList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(requireContext(), favouriteList,this);
        // Set the listener to the adapter
        favouriteAdapter.adapterListener = this; // Set the listener to the adapter
//        set the listner to the adapter
        favouriteAdapter.upDateListner=this;
//        set the listner to the adapter

        favoRv.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        favoRv.setAdapter(favouriteAdapter);
        // Initialize the FavouriteDatabase instance
        favouriteDatabase = FavouriteDatabase.getInstance(requireContext());
        // Initialize the FavouriteDao instance
        favouriteDao = favouriteDatabase.getfavouriteDao();

        fetchData();
        return view;
    }
    private void fetchData() {
        // Fetch data from the database
        favouriteList.clear();
        favouriteList.addAll(favouriteDao.getAllUsers()); // Retrieve all favorite items from the database

        // Notify the adapter of the data change
        favouriteAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData(); // Refresh data when fragment resumes
    }

    @Override
    public void OnDelete(double id, int pos) {
        favouriteDao.delete(id);
        favouriteAdapter.removeUser(pos);
        Toast.makeText(requireContext(), "Card deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnUpdate(ModelHome modelHome) {
        // Start the EditScreenActivity
        Intent intent = new Intent(requireContext(), EditScreenActivity.class);
        // Pass the ModelHome object to the EditScreenActivity
        intent.putExtra("model", modelHome);
        startActivity(intent);
    }

    @Override
    public void onItemClick(ModelHome modelHome) {
        // Start a new activity to display the details of the clicked item
        Intent intent = new Intent(requireContext(), ItemDetailsActivity.class);
        intent.putExtra("modelHome", modelHome); // Pass the clicked item details
        startActivity(intent);
        requireActivity().finish();
    }

//    @Override
//    public void onFavoriteItemClick(int position) {
//        // Handle item click in favourites here
//        ModelHome clickedItem = favouriteList.get(position);
//        // Implement your logic
//    }
}