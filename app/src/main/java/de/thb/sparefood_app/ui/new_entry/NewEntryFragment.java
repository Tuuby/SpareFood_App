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

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.transform.Result;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;
import de.thb.sparefood_app.model.PROPERTIES;


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

        MaterialButton fishButton = binding.fishButton;
        fishButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NO_FISH, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.fisch_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.fisch_neutral));
            }
        });

        MaterialButton lactoseButton = binding.lactoseButton;
        lactoseButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NO_LACTOSE, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.laktose_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.laktose_neutral));
            }
        });

        MaterialButton proteinButton = binding.proteinButton;
        proteinButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.PROTEIN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.proteinreich_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.proteinreich_neutral));
            }
        });

        MaterialButton nutsButton = binding.nutsButton;
        nutsButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NO_NUTS, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schalenfruechte_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schalenfruechte_neutral));
            }
        });

        MaterialButton hotButton = binding.hotButton;
        hotButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NOT_HOT, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.scharf_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.scharf_neutral));
            }
        });

        MaterialButton porkButton = binding.porkButton;
        porkButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NO_PORK, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schwein_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schwein_neutral));
            }
        });

        MaterialButton soyButton = binding.soyButton;
        soyButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.SOY, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.soja_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.soja_neutral));
            }
        });

        MaterialButton veganButton = binding.veganButton;
        veganButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.VEGAN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegan_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegan_neutral));
            }
        });

        MaterialButton vegetarianButton = binding.vegetarianButton;
        vegetarianButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.VEGETARIAN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegetarisch_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegetarisch_neutral));
            }
        });

        MaterialButton wheatButton = binding.wheatButton;
        wheatButton.addOnCheckedChangeListener((button, isChecked) -> {
            newEntryViewModel.setFilter(PROPERTIES.NO_WHEAT, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.weizen_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.weizen_neutral));
            }
        });

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBarLayout);
        navView.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);

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