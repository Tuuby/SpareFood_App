package de.thb.sparefood_app.ui.home;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.model.Meal;
import de.thb.sparefood_app.model.MealRepository;
import de.thb.sparefood_app.model.PROPERTIES;

public class HomeViewModel extends ViewModel {

    private boolean[] filter;
    private int radius = 20;

    private final MealRepository mealRepository = MainActivity.getInstance().getMealRepository();
    public LiveData<List<Meal>> meals = mealRepository.meals;

    public HomeViewModel() {
        filter = new boolean[10];
    }

    public void setFilter(PROPERTIES property, boolean value) {
        filter[property.id] = value;
    }

    public void setRadius(int value) {
        radius = value;
    }

    public void refreshMeals() throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("filter.radius", String.valueOf(radius));
        for (int i = 0; i < filter.length; i++) {
            if (filter[i]) {
                params.put("filter.property", getPropertyName(i));
            }
        }
        params.put("longitude", "52.5");
        params.put("latitude", "13.4");
        mealRepository.requestMeals(params);
    }

    private String getPropertyName(int index) {
        for(PROPERTIES propertiy: PROPERTIES.values()) {
            if (propertiy.id == index)
                return propertiy.name;
        }
        return null;
    }
}