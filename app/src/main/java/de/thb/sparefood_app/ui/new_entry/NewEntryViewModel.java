package de.thb.sparefood_app.ui.new_entry;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.thb.sparefood_app.model.PROPERTIES;


public class NewEntryViewModel extends AndroidViewModel {

    private boolean[] filter;

    public Bitmap getMealImage() {
        return mealImage;
    }

    public void setMealImage(Bitmap mealImage) {
        this.mealImage = mealImage;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {this.mealDescription = mealDescription;}

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {this.currentPhotoPath = currentPhotoPath;}

    public void setFilter(PROPERTIES property, boolean value) {
        filter[property.id] = value;
    }


    private Bitmap mealImage;
    private String mealName;
    private String mealDescription;
    private String currentPhotoPath;
    private Intent takePictureIntent;

    @NonNull
    @Override
    public <T extends Application> T getApplication() {
        return super.getApplication();
    }

    public NewEntryViewModel(@NonNull Application application) {
        super(application);
        filter = new boolean[10];

    }

    public Intent getTakePictureIntent() {return takePictureIntent;}
    public void setTakePictureIntent(Intent takePictureIntent) {this.takePictureIntent = takePictureIntent;}

    public void generateTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getApplication().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplication(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        this.takePictureIntent = takePictureIntent;
    }

    public Uri getNewPhotoUri() {
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            // Error occurred while creating the File
        }
        // Continue only if the File was successfully created
//        if (photoFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getApplication(),
                    "com.example.android.fileprovider",
                    photoFile);
            return photoURI;
//        } else {
//            return
//        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        this.currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    protected void generatePhoto() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        String[] scanFileArray = {currentPhotoPath};
        MediaScannerConnection.scanFile(getApplication(), scanFileArray,
                null, null);
        mediaScanIntent.setData(contentUri);
        getApplication().sendBroadcast(mediaScanIntent);

        File photoTest = getApplication().getExternalFilesDir(currentPhotoPath);
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        this.mealImage = bitmap;
    }

//    protected void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        String[] scanFileArray = {currentPhotoPath};
//        MediaScannerConnection.scanFile(getApplication(), scanFileArray,
//                null, null);
//        mediaScanIntent.setData(contentUri);
//        Log.d("contentUri", currentPhotoPath + " COMP " + contentUri);
//        getApplication().sendBroadcast(mediaScanIntent);
//
//        File photoTest = getApplication().getExternalFilesDir(currentPhotoPath);
//        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
//
//        //Check for External Storage Permission
//        if (ContextCompat.checkSelfPermission(getApplication(),
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Permission is not granted
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) getApplication().getApplicationContext(),
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions((Activity) getApplication().getApplicationContext(),
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        5);
//
//                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                // app-defined int constant. The callback method gets the
//                // result of the request.
//            }
//        } else {
//            // Permission has already been granted
//            MediaStore.Images.Media.insertImage(getApplication().getContentResolver(), bitmap, "dummyTitle", "dummyDesc");
//        }
//    }

    protected void submitNewEntry() {
        //TODO
    }


}