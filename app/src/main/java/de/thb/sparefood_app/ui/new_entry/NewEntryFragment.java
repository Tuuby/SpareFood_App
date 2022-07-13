package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;


public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;
    ImageButton cameraButton;
    EditText mealName;
    EditText mealDescription;
    MaterialButton submitEntryBtn;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ActivityResultLauncher<String> requestPermissionLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewEntryViewModel newEntryViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        cameraButton = (ImageButton) binding.cameraButton;
        mealName = (EditText) binding.mealName;
        mealDescription = (EditText) binding.mealDescription;
        submitEntryBtn = (MaterialButton) binding.submitEntryBtn;

        if (newEntryViewModel.getMealImage() != null) {cameraButton.setImageBitmap(newEntryViewModel.getMealImage());}
        if (newEntryViewModel.getMealName() != null) {mealName.setText(newEntryViewModel.getMealName());}
        if (newEntryViewModel.getMealDescription() != null) {mealDescription.setText(newEntryViewModel.getMealDescription());}


//        ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
////                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            newEntryViewModel.generatePhoto();
//                            cameraButton.setImageBitmap(newEntryViewModel.getMealImage());
//                            galleryAddPic(newEntryViewModel.getMealImage());
//                        }
//                    }
//                });

//        ActivityResultLauncher<Uri> cameraActivityResultLauncher = registerForActivityResult(
//                new ActivityResultContracts.TakePicture(),
//                new ActivityResultCallback<Boolean>() {
//                    @Override
//                    public void onActivityResult(Boolean result) {
//                        // do what you need with the uri here ...
//                        newEntryViewModel.generatePhoto();
//                        cameraButton.setImageBitmap(newEntryViewModel.getMealImage());
//                        galleryAddPic(newEntryViewModel.getMealImage());
//                    }
//        });

        ActivityResultLauncher<Uri> cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        // do what you need with the uri here ...
                        newEntryViewModel.generatePhoto();
                        cameraButton.setImageBitmap(newEntryViewModel.getMealImage());
                        galleryAddPic(newEntryViewModel.getMealImage());
                    }
        });

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (result) {
                            // PERMISSION GRANTED
//                            newEntryViewModel.generateTakePictureIntent();
//                            cameraActivityResultLauncher.launch(newEntryViewModel.getTakePictureIntent());
                            cameraActivityResultLauncher.launch(newEntryViewModel.getNewPhotoUri());
                        } else {
                            // PERMISSION NOT GRANTED
                            Log.d("PERMISSION", "NOT GRANTED");
                        }
                    }
                }
        );


        cameraButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                    newEntryViewModel.generateTakePictureIntent();
//                    cameraActivityResultLauncher.launch(newEntryViewModel.getTakePictureIntent());
//                requestPermissionLauncher.launch(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraActivityResultLauncher.launch(newEntryViewModel.getNewPhotoUri());

            }
        });

        mealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                newEntryViewModel.setMealName(mealName.getText().toString());
            }
        });

        mealName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                newEntryViewModel.setMealDescription(mealDescription.getText().toString());
            }
        });
        
        submitEntryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                newEntryViewModel.submitNewEntry();
            }
        });

        return root;
    }

    private void requestPermission() {

    }

    protected void galleryAddPic(Bitmap bitmap) {
        //Check for External Storage Permission
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        5);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "dummyTitle", "dummyDesc");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}