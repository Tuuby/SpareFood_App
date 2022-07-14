package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;


public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;
    // Define the button and imageview type variable
    ImageButton camera_open_id;
    String currentPhotoPath;
    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Uri> takePictureLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (result) {
                Log.d("CALLED_LOG", "PERMISSION IS GRANTED");
            } else {
                Log.d("CALLED_LOG", "PERMISSION IS NOT GRANTED");
                //crying mainly I'd imagine
                // todo what now??
            }
        });

        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {

            if (result) {
                Log.d("CALLED_LOG", "SUCCESS");
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                camera_open_id.setImageBitmap(bitmap);
            } else {
                Log.d("CALLED_LOG", "FAILED");

            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewEntryViewModel plusViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        camera_open_id = (ImageButton) binding.cameraButton;

        camera_open_id.setOnClickListener(v -> {
            boolean hasCameraPermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

            if (!hasCameraPermission) {
                permissionLauncher.launch(Manifest.permission.CAMERA);
            }

            File photoFile = null;
            try {
                photoFile = reserveFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ERROR", "Shit...", ex);
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d("CALLED_LOG", "URI=" + currentPhotoPath);
                takePictureLauncher.launch(Uri.parse(currentPhotoPath));
            }
            Log.d("CALLED_LOG", "DONE");
        });

        return root;
    }

    private File reserveFile() throws IOException {
        // Create an image file name
        DateFormat dateTimeInstance = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = dateTimeInstance.format(new Date());

        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}