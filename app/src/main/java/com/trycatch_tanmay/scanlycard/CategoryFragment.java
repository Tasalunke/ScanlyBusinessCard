package com.trycatch_tanmay.scanlycard;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {
    RecyclerView cat_RVC;
    Categoryadapter categoryadapter;
    CatgeforyDao catgeforyDao;
    private List<ModelHome> categoryList;
    private Categorydatabase categorydatabase;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_category, container, false);
        cat_RVC=view.findViewById(R.id.cat_RVC);
        categoryList = new ArrayList<>();
        categoryadapter= new Categoryadapter(requireContext(),categoryList);
        cat_RVC.setLayoutManager(new GridLayoutManager(requireContext(),2));
        cat_RVC.setAdapter(categoryadapter);
        // Initialize the CategoryDatabase instance and assign it to the global variable
        categorydatabase = Categorydatabase.getINSTANCE(requireContext());
        // Initialize the CategoryDao instance
        catgeforyDao = categorydatabase.getCatDao();
        fethData();
        return view;

    }
    private void fethData(){
        categoryadapter.clearData();
        // Fetch data from CategoryDao
        List<ModelHome> userList = catgeforyDao.getAllUser();
        for (ModelHome modelHome : userList) {
            // Add fetched data to the adapter
            categoryadapter.addUser(modelHome);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        fethData();
    }
    // Method to add selected item to CategoryFragment
    public void addItemToCategory(ModelHome modelHome) {
        catgeforyDao.Insert(modelHome); // Insert selected item into CategoryDao
        categoryadapter.addUser(modelHome); // Add selected item to RecyclerView
    }
}