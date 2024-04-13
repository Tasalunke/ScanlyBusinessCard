package com.trycatch_tanmay.scanlycard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.trycatch_tanmay.scanlycard.databinding.ActivityMainBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CameraActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    private ShapeableImageView img_scaner;
    private MaterialButton input_img_btn, rcng_txt_btn;
    private TextView rcng_txt;
    private EditText edt_txt_view;

    private static final String TAG = "MAIN_TAG";

    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    private static final int EDIT_SCREEN_REQUEST_CODE = 123;

    private String[] cameraPermission;
    private String[] storagePermission;
    private TextRecognizer textRecognizer;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
//
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        img_scaner = findViewById(R.id.img_scaner);
        rcng_txt_btn = findViewById(R.id.rcng_txt_btn);
        edt_txt_view = findViewById(R.id.edt_txt_view);
        MaterialButton navigateToEditBtn = findViewById(R.id.save_btn);

        input_img_btn = findViewById(R.id.input_img_btn);
        rcng_txt = findViewById(R.id.rcng_txt);

        cameraPermission = new String[]{
                android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        navigateToEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(CameraActivity.this, "Pick Image first ......", Toast.LENGTH_SHORT).show();
                } else {
                    saveDataAndNavigateToEditScreen();
                }
            }
        });

        input_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputImageDialog();
            }
        });

        rcng_txt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(CameraActivity.this, "Pick Image first ......", Toast.LENGTH_SHORT).show();
                } else {
                    recognizeTextFromImages();
                }
            }
        });


    }

    private void saveDataAndNavigateToEditScreen() {
        String recognizedText = edt_txt_view.getText().toString();

        if (!recognizedText.isEmpty()) {
            Intent editScreenIntent = new Intent(CameraActivity.this, EditScreenActivity.class);
            editScreenIntent.putExtra("recognizedText", recognizedText);
            editScreenIntent.putExtra("imageUri", imageUri.toString());
            startActivityForResult(editScreenIntent, EDIT_SCREEN_REQUEST_CODE);
        } else {
            Toast.makeText(CameraActivity.this, "Recognized text is empty.", Toast.LENGTH_SHORT).show();
        }
    }

    private void navigateToEditScreen() {
        Intent intent = new Intent(CameraActivity.this, EditScreenActivity.class);
        intent.putExtra("recognizedText", edt_txt_view.getText().toString());
        intent.putExtra("imageUri", imageUri.toString());
        startActivityForResult(intent, EDIT_SCREEN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_SCREEN_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String editedName = data.getStringExtra("editedName");
            String editedContact = data.getStringExtra("editedContact");
            String editedAddress = data.getStringExtra("editedAddress");
            String editedPosition = data.getStringExtra("editedPosition");

            String formattedText = "Name: " + editedName + "\nContact: " + editedContact +
                    "\nAddress: " + editedAddress + "\nPosition: " + editedPosition;
            edt_txt_view.setText(formattedText);
        }
    }

    private void recognizeTextFromImages() {
        progressDialog.setMessage("Preparing Image ...");
        progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            progressDialog.setMessage("Recognizing Text ...");
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();
                            String recognizedText = text.getText();
                            Log.d(TAG, "OnSuccess:recognizedText:" + recognizedText);
                            edt_txt_view.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "onFailure:", e);
                            progressDialog.dismiss();
                            Toast.makeText(CameraActivity.this, "Failed recognizing text due to" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.e(TAG, "recognizeTextFromImages", e);
            Toast.makeText(this, "Failed preparing image due to" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void formatAndDisplayText(String recognizedText) {
        String nameRegex = "(?i)\\bName\\b:\\s*([a-zA-Z]+\\s+[a-zA-Z]+)";
        String contactRegex = "(?i)\\bContact\\b:\\s*([0-9\\-]+)";
        String addressRegex = "(?i)\\bAddress\\b:\\s*(.*)";
        String positionRegex = "(?i)\\bPosition\\b:\\s*([a-zA-Z]+)";

        String name = extractInformation(recognizedText, nameRegex);
        String contact = extractInformation(recognizedText, contactRegex);
        String address = extractInformation(recognizedText, addressRegex);
        String position = extractInformation(recognizedText, positionRegex);

        String formattedText = "Name: " + name + "\nContact: " + contact + "\nAddress: " + address + "\nPosition: " + position;

        edt_txt_view.setText(formattedText);
    }

    private String extractInformation(String recognizedText, String nameRegex) {
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(recognizedText);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "Not found";
        }
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(this, input_img_btn);
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Gallery");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == 1) {
                    Log.d(TAG, "onMenuItemClick:Camera Clicked.....");
                    if (checkCameraPermission()) {
                        pickImageCamera();
                    } else {
                        requestCameraPermission();
                    }
                } else if (id == 2) {
                    Log.d(TAG, "onMenuItemClick:Gallery Clicked");
                    if (checkStoragePermission()) {
                        pickImageGallery();
                    } else {
                        requestStoragePermission();
                    }
                }
                return true;
            }
        });
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult:imageUri " + imageUri);
                        img_scaner.setImageURI(imageUri);
                    } else {
                        Log.d(TAG, "onActivityResult: Canclled");
                        Toast.makeText(CameraActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Descrption");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult:imageUri " + imageUri);
                        img_scaner.setImageURI(imageUri);
                    } else {
                        Log.d(TAG, "onActivityResult: cancelled");
                        Toast.makeText(CameraActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return cameraResult && storageResult;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "Camera and storage permission are required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}
