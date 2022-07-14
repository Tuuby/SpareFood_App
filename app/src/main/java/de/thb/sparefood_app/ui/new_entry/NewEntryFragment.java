package de.thb.sparefood_app.ui.new_entry;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import java.nio.ByteBuffer;
import java.security.Permissions;
import java.security.acl.Permission;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNewEntryBinding;
import de.thb.sparefood_app.threading.ApplicationExecutors;


public class NewEntryFragment extends Fragment {

    private FragmentNewEntryBinding binding;
    private static final int pic_id = 123;
    // Define the button and imageview type variable
    ImageButton camera_open_id;
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
        NewEntryViewModel plusViewModel =
                new ViewModelProvider(this).get(NewEntryViewModel.class);

        binding = FragmentNewEntryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        camera_open_id = (ImageButton) binding.cameraButton;
        camera_open_id.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                imageCapture.takePicture(cameraExecutor, new ImageCapture.OnImageCapturedCallback() {
                    @Override
                    public void onCaptureSuccess(@NonNull ImageProxy image) {
                        executors.getMainThread().execute(() -> {
                            camera_open_id.setImageBitmap(imageProxyToBitmap(image));
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
    }
}