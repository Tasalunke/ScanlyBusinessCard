package com.trycatch_tanmay.scanlycard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.trycatch_tanmay.scanlycard.databinding.ActivityEditScreenBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class EditScreenActivity extends AppCompatActivity {

    ActivityEditScreenBinding binding;
    Button saveButton;
    EditText editName, editContact, editAddress, editPosition;
    ImageView imageView;
    private Uri imageUri; // Declare imageUri as a field


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_screen);
        binding = ActivityEditScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        saveButton = findViewById(R.id.save_button);
        editName = findViewById(R.id.editName);
        editContact = findViewById(R.id.editContact);
        editAddress = findViewById(R.id.editAddress);
        editPosition = findViewById(R.id.editPosition);
        imageView = binding.imageView;
        Intent intent = getIntent();
        if (intent != null) {
            String recognizedText = intent.getStringExtra("recognizedText");
            String imageUriString = intent.getStringExtra("imageUri");

            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                imageView.setImageURI(imageUri);
            }

            updateUI(recognizedText);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedName = editName.getText().toString();
                String editedContact = editContact.getText().toString();
                String editedAddress = editAddress.getText().toString();
                String editedPosition = editPosition.getText().toString();
                String imagePath = imageUri.toString(); // Assuming imageUri is the Uri of the selected image

                // Check if any field is empty
                if (editedName.isEmpty() || editedContact.isEmpty() || editedAddress.isEmpty() || editedPosition.isEmpty()) {
                    Toast.makeText(EditScreenActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Insert data into the database
                ModelHome modelHome = new ModelHome(editedName, editedContact, editedAddress, editedPosition,imagePath);
                Userdatabase userDatabase = Userdatabase.getINSTANCE(EditScreenActivity.this);
                UserDao userDao = userDatabase.getmodel();
                userDao.Insert(modelHome);

                // Navigate back to the dashboard or any other activity
                Intent intent = new Intent(EditScreenActivity.this, DashBoardActivity.class);
                startActivity(intent);
            }


        });
    }

    private void updateUI(String recognizedText) {
        if (recognizedText == null) {
            // Handle the case where recognized text is null
            Toast.makeText(this, "No text recognized", Toast.LENGTH_SHORT).show();
            return;
        }
        // Extracting information from recognized text using regex
        String nameRegex = "(?i)\\bName\\b:\\s*([a-zA-Z]+\\s+[a-zA-Z]+)";
        String contactRegex = "(?i)\\bContact\\b:\\s*([0-9\\-]+)";
        String addressRegex = "(?i)\\bAddress\\b:\\s*(.*)";
        String positionRegex = "(?i)\\bPosition\\b:\\s*([a-zA-Z]+)";

        String name = extractInformation(recognizedText, nameRegex);
        String contact = extractInformation(recognizedText, contactRegex);
        String address = extractInformation(recognizedText, addressRegex);
        String position = extractInformation(recognizedText, positionRegex);

        // Setting extracted information to corresponding EditTexts
        editName.setText(name);
        editContact.setText(contact);
        editAddress.setText(address);
        editPosition.setText(position);
    }

    private String extractInformation(String recognizedText, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(recognizedText);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "";
        }
    }
}