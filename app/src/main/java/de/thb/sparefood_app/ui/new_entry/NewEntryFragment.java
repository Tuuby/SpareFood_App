package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
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
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.transform.Result;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;

import de.thb.sparefood_app.model.PROPERTIES;
import de.thb.sparefood_app.threading.ApplicationExecutors;

public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;

    EditText mealName;
    EditText mealDescription;
    MaterialButton submitEntryBtn;

    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    ActivityResultLauncher<String> requestPermissionLauncher;

    MaterialButton filterButtonDummy;
    private ImageCapture imageCapture;
    private ExecutorService cameraExecutor;
    private ApplicationExecutors executors;

    private final ActivityResultLauncher<String> requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Do something
        } else {
            Snackbar.make(binding.getRoot(), "Permissions needed for this feature", Snackbar.LENGTH_LONG).show();
        }
    });

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NewEntryViewModel newEntryViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        executors = new ApplicationExecutors();

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this.requireContext());
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
                cameraExecutor = Executors.newSingleThreadExecutor();

                imageCapture = new ImageCapture.Builder().setTargetRotation(Surface.ROTATION_0).build();

                cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, imageCapture);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this.requireContext()));

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBarLayout);
        navView.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        appBarLayout.setVisibility(View.GONE);


        ImageButton camera_open_id = (ImageButton) binding.cameraButton;
        camera_open_id.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                imageCapture.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        executors.getMainThread().execute(() -> {
                            Bitmap capturedImage = imageProxyToBitmap(image);
                            plusViewModel.setCapturedImage(capturedImage);
                            camera_open_id.setImageBitmap(capturedImage);
                        });
                    }
                    @Override
                    public void onError(@NonNull ImageCaptureException exception) {
                        Snackbar.make(root, "Image capture failed.", Snackbar.LENGTH_LONG).show();
                    }
                });
            } else {
                requestPermissionsLauncher.launch(Manifest.permission.CAMERA);
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

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy planeProxy = image.getPlanes()[0];
        ByteBuffer buffer = planeProxy.getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bm, bm.getWidth(), bm.getHeight(), true);
        return Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
    }

    public void toggleFilterButton () {
        filterButtonDummy.setSelected(!filterButtonDummy.isSelected());
        filterButtonDummy.setBackgroundTintList(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_background));
        filterButtonDummy.setRippleColor(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_icon));
        filterButtonDummy.setIconTint(getActivity().getResources().getColorStateList(R.color.color_state_list_filter_button_icon));
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