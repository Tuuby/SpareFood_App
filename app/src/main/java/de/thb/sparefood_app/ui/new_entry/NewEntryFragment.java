package de.thb.sparefood_app.ui.new_entry;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
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

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
//                            doSomeOperations();

                                // BitMap is data structure of image file
                                // which stores the image in memory
                                Bitmap photo = (Bitmap) data.getExtras()
                                        .get("data");

                                // Set the image in imageview for display
                                click_image_id.setImageBitmap(photo);
                        }
                    }
                });

        try {
            Log.d("FileTest TAG", "" + createImageFile());
        } catch (IOException e) {
            e.printStackTrace();
        }

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