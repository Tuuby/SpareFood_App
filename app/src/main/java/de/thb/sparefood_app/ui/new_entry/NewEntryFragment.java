package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;


public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;
    private static final int pic_id = 123;
    ImageButton camera_open_id;
    MaterialButton filterButtonDummy;
    String currentPhotoPath;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewEntryViewModel plusViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBarLayout);
        navView.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);

        camera_open_id = (ImageButton) binding.cameraButton;

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            galleryAddPic();
                            File photoTest = getActivity().getExternalFilesDir(currentPhotoPath);
                            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                            camera_open_id.setImageBitmap(bitmap);
                        }
                    }
                });

        camera_open_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        someActivityResultLauncher.launch(takePictureIntent);
                    }
                }
            }
        });

        filterButtonDummy = (MaterialButton) binding.filterButtonDummy;
        filterButtonDummy.setSelected(false);
        filterButtonDummy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                toggleFilterButton();
            }
        });

        return root;
    }

    public void toggleFilterButton () {
        filterButtonDummy.setSelected(!filterButtonDummy.isSelected());
        filterButtonDummy.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_background));
        filterButtonDummy.setRippleColor(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_icon));
        filterButtonDummy.setIconTint(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_icon));
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        String[] scanFileArray = {currentPhotoPath};
        MediaScannerConnection.scanFile(getActivity(), scanFileArray,
                null, null);
        mediaScanIntent.setData(contentUri);
        Log.d("contentUri", currentPhotoPath + " COMP " + contentUri);
        getActivity().sendBroadcast(mediaScanIntent);

        File photoTest = getActivity().getExternalFilesDir(currentPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);

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

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBarLayout);
        navView.setVisibility(View.VISIBLE);
        bottomAppBar.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
        appBarLayout.setVisibility(View.VISIBLE);
    }
}