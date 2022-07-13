package de.thb.sparefood_app.ui.home;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.model.Meal;
import de.thb.sparefood_app.model.MealRepository;

public class HomeViewModel extends ViewModel {

    private final MealRepository mealRepository = MainActivity.getInstance().getMealRepository();
    public LiveData<List<Meal>> meals = mealRepository.meals;

    public HomeViewModel() { }
}