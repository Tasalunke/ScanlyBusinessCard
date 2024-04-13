package com.trycatch_tanmay.scanlycard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private TextView edt_User_email;
    private TextView User_name;
    private ImageView profileImageView;
    private Button Btn_logout;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Activity activity = getActivity();
        if (activity != null) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.prf));
        }
        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
//        Initialize view
        edt_User_email=view.findViewById(R.id.edt_User_email);
        User_name=view.findViewById(R.id.User_name);
        profileImageView=view.findViewById(R.id.profileImageView);
        Btn_logout=view.findViewById(R.id.Btn_logout);
//        Setting-up logout button  to go back on Login activity
        Btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Log-out User
                mAuth.signOut();
                // navigatie to Login screen
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
//                here u finsh the fragnmet
                requireActivity().finish();
            }
        });
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser !=null){
            String userEmail= currentUser.getEmail();
            String username = currentUser.getDisplayName();

            //setuser data TO textview
            edt_User_email.setText(userEmail);
            User_name.setText(username);
        }else {
            Toast.makeText(requireContext(), "Please sign-in", Toast.LENGTH_SHORT).show();
        }
        return view;



    }

}
