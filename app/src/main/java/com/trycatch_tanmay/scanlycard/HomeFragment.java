package com.trycatch_tanmay.scanlycard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.OnFavoriteItemClickListener, HomeAdapter.OnAddToCategoryClickListener {
    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private List<ModelHome> modelHomeList = new ArrayList<>();
    private UserDao userDao;
    private FavouriteDatabase favouriteDatabase;
    private FavouriteDao favouriteDao; // Correct initialization
    FavouriteAdapter favouriteAdapter;
    private CatgeforyDao catgeforyDao;
    private Categorydatabase categorydatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        homeAdapter = new HomeAdapter(requireContext(), modelHomeList, favouriteDao, this);
        homeAdapter.setOnFavoriteItemClickListener(this);
        homeAdapter.setOnAddToCategoryClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerView.setAdapter(homeAdapter);

        // Initialize the FavouriteDatabase instance
        favouriteDatabase = FavouriteDatabase.getInstance(requireContext());
        categorydatabase = Categorydatabase.getINSTANCE(requireContext());
// Initialize the FavouriteDao instance
        favouriteDao = favouriteDatabase.getfavouriteDao(); // Proper initialization
        catgeforyDao = categorydatabase.getCatDao();
//        ***********************************************
        Userdatabase userDatabase = Userdatabase.getINSTANCE(requireContext());
        userDao = userDatabase.getmodel();

        fetchData();

        // Set the click listener for the HomeAdapter
        homeAdapter.setOnFavoriteItemClickListener(new HomeAdapter.OnFavoriteItemClickListener() {
            @Override
            public void onFavoriteItemClick(int position, ModelHome modelHome) {
                if (favouriteDao != null) {
                    favouriteDao.Insert(modelHome); // Insert the clicked item into the database
                    Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show();

                }
                fetchData(); // Refresh data in the FavouriteFragment
            }
        });
        fetchData();
        return view;
    }

    private void fetchData() {
        homeAdapter.clearData();
        List<ModelHome> userList = userDao.getAllUsers();
        for (ModelHome modelHome : userList) {
            homeAdapter.addUser(modelHome);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    public void onFavoriteItemClick(int position, ModelHome modelHome) {
//        handleFavoriteItemClick(position, modelHome);
        // Handle item click in favourites here
        ModelHome clickedItem = modelHomeList.get(position);
        // Implement your logic

        // Call the Insert method to save the clickedItem to the database
        favouriteDao.Insert(clickedItem);

        // Notify the adapter of data change in the FavouriteFragment
        favouriteAdapter.addUser(clickedItem);
    }





    public void navigateToCategoryFragment(ModelHome modelHome) {
        // Navigate to CategoryFragment and pass the clicked item
        CategoryFragment categoryFragment = new CategoryFragment();
        // Pass the selected ModelHome object to CategoryFragment
        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedModelHome", modelHome);
        categoryFragment.setArguments(bundle);
        // Replace the current fragment with CategoryFragment
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container, categoryFragment)
                .addToBackStack(null)
                .commit();
    }


    @Override
    public void onAddToCategoryClick(int position, ModelHome modelHome) {
        if (catgeforyDao != null && catgeforyDao.exists(modelHome.getName()) > 0) {
            // If the item already exists, show a toast message
            Toast.makeText(requireContext(), "Already added to category", Toast.LENGTH_SHORT).show();
        } else {
            // If the item doesn't exist, add it to the category
            if (catgeforyDao != null) {
                catgeforyDao.Insert(modelHome);
                // Show a toast message indicating that the item has been added
                Toast.makeText(requireContext(), "Added to category", Toast.LENGTH_SHORT).show();
            }
        }
//            if (catgeforydao !=null){
//                catgeforydao.Insert(modelHome);
//                Toast.makeText(requireContext(), "Added to Category", Toast.LENGTH_SHORT).show();
//            }
        // For example, you can navigate to the CategoryFragment and pass the clicked item
        navigateToCategoryFragment(modelHome);
    }
    }

