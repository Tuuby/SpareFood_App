package de.thb.sparefood_app.ui.new_entry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NewEntryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NewEntryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("##### This is NewEntry fragment #####");
    }

    public LiveData<String> getText() {
        return mText;
    }
}