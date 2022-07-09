package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;
import de.thb.sparefood_app.MainActivity;


public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    ImageButton camera_open_id;
    ImageView click_image_id;
    //    Context context = getContext();
    String currentPhotoPath;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewEntryViewModel plusViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        camera_open_id = (ImageButton) binding.cameraButton;
        click_image_id = (ImageView) binding.mealImage;

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Log.d("DUMMY", "DATA FROM INTENT" + data);
                            galleryAddPic();
                            File photoTest = getActivity().getExternalFilesDir(currentPhotoPath);

                            // Get the dimensions of the View
//                            int targetW = click_image_id.getMeasuredWidth() != 0 ? click_image_id.getMeasuredWidth() : 401;
//                            int targetH = click_image_id.getMeasuredHeight() != 0 ? click_image_id.getMeasuredHeight() : 401;
////                            int targetW = 400;
////                            int targetH = 400;
//
//                            Log.d(" TAG3333", "" + targetH + targetW);
//
//                            // Get the dimensions of the bitmap
//                            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//                            bmOptions.inJustDecodeBounds = true;
//
//                            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
//
//                            int photoW = bmOptions.outWidth;
//                            int photoH = bmOptions.outHeight;
//
//                            // Determine how much to scale down the image
//                            int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
//
//                            // Decode the image file into a Bitmap sized to fill the View
//                            bmOptions.inJustDecodeBounds = false;
//                            bmOptions.inSampleSize = scaleFactor;
//                            bmOptions.inPurgeable = true;

//                            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
                            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                            click_image_id.setImageBitmap(bitmap);

//
//                                // BitMap is data structure of image file
//                                // which stores the image in memory
//                                Bitmap photo = (Bitmap) data.getExtras()
//                                        .get("data");
//
//                                // Set the image in imageview for display
//                                click_image_id.setImageBitmap(photo);
                        }
                    }
                });

        try {
            Log.d("FileTest TAG", "" + createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }


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
                        Log.d("dummytag", "" + photoURI + " STRUINGslefjwei" + MediaStore.EXTRA_OUTPUT);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        someActivityResultLauncher.launch(takePictureIntent);
                    }
                }
            }
        });

        // CAMERA Logic
//        camera_open_id = (ImageButton)binding.cameraButton;
//        click_image_id = (ImageView)binding.mealImage;
//
//        camera_open_id.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v)
//            {
//
//                // Create the camera_intent ACTION_IMAGE_CAPTURE
//                // it will open the camera for capture the image
//                Intent camera_intent
//                        = new Intent(MediaStore
//                        .ACTION_IMAGE_CAPTURE);
//
//                // Start the activity with camera_intent,
//                // and request pic id
////                startActivityForResult(camera_intent, pic_id);
//                someActivityResultLauncher.launch(camera_intent);
//            }
//        });

//        final TextView textView = binding.textNewEntry;
//        plusViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
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




//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            }
//        }
//    }



//    public void openSomeActivityForResult() {
//        Intent intent = new Intent(this, SomeActivity.class);
//        someActivityResultLauncher.launch(intent);
//    }

    // This method will help to retrieve the image
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        // Match the request 'pic id with requestCode
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {

            // BitMap is data structure of image file
            // which stor the image in memory
            Bitmap photo = (Bitmap) data.getExtras()
                    .get("data");

            // Set the image in imageview for display
            click_image_id.setImageBitmap(photo);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}