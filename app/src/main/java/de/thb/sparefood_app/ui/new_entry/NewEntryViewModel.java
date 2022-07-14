package de.thb.sparefood_app.ui.new_entry;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.io.IOException;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.model.Meal;
import de.thb.sparefood_app.model.MealRepository;
import de.thb.sparefood_app.model.PROPERTIES;


public class NewEntryViewModel extends AndroidViewModel {

    private boolean[] filter;
    private Bitmap mealImage;
    private String mealName;
    private String mealDescription;

    private final MealRepository mealRepository = MainActivity.getInstance().getMealRepository();

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

    public void setFilter(PROPERTIES property, boolean value) {
        filter[property.id] = value;
    }

    @NonNull
    @Override
    public <T extends Application> T getApplication() {
        return super.getApplication();
    }

    public NewEntryViewModel(@NonNull Application application) {
        super(application);
        filter = new boolean[10];
    }

    protected void submitNewEntry() throws IOException {
        Meal meal = new Meal(mealName, mealDescription);
        for (PROPERTIES property: PROPERTIES.values()) {
            if (filter[property.id]) {
                meal.addProperty(property);
            }
        }
        mealRepository.postMeal(meal);
        // Post image here if clarified how to get the id of newly added Meal
    }


}