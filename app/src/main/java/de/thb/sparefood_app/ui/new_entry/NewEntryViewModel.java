package de.thb.sparefood_app.ui.new_entry;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewEntryViewModel extends ViewModel {

    private Bitmap capturedImage;

    public NewEntryViewModel() {
        capturedImage = null;
    }

    public void setCapturedImage(Bitmap capturedImage) {
        this.capturedImage = capturedImage;
    }
}