package de.thb.sparefood_app.ui.home;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.io.IOException;
import java.util.ArrayList;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentHomeBinding;
import de.thb.sparefood_app.model.MealRepository;
import de.thb.sparefood_app.model.PROPERTIES;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CardStackView cards = binding.cardStackView;
        CardStackLayoutManager cslManager = new CardStackLayoutManager(getContext(), new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                Log.d("SpareFood_Debug", "You swiped to direction " + direction.name());
            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        });


        cslManager.setStackFrom(StackFrom.Top);
        cards.setLayoutManager(cslManager);
        CardStackAdapter adapter = new CardStackAdapter();
        cards.setAdapter(adapter);
        homeViewModel.meals.observe(MainActivity.getInstance(), adapter::setCardsList);

        DrawerLayout filterDrawer = binding.cardLayout;
        filterDrawer.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                try {
                    homeViewModel.refreshMeals();
                    adapter.refresh();
                } catch (IOException e) {
                    Snackbar.make(root, R.string.refresh_error + e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Slider slider = binding.filterView.slider;
        slider.addOnChangeListener((slider1, value, fromUser) -> {
            homeViewModel.setRadius((int) value);
        });

        MaterialButton fishButton = binding.filterView.fishButton;
        fishButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NO_FISH, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.fisch_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.fisch_neutral));
            }
        });

        MaterialButton lactoseButton = binding.filterView.lactoseButton;
        lactoseButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NO_LACTOSE, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.laktose_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.laktose_neutral));
            }
        });

        MaterialButton proteinButton = binding.filterView.proteinButton;
        proteinButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.PROTEIN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.proteinreich_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.proteinreich_neutral));
            }
        });

        MaterialButton nutsButton = binding.filterView.nutsButton;
        nutsButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NO_NUTS, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schalenfruechte_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schalenfruechte_neutral));
            }
        });

        MaterialButton hotButton = binding.filterView.hotButton;
        hotButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NOT_HOT, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.scharf_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.scharf_neutral));
            }
        });

        MaterialButton porkButton = binding.filterView.porkButton;
        porkButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NO_PORK, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schwein_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.schwein_neutral));
            }
        });

        MaterialButton soyButton = binding.filterView.soyButton;
        soyButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.SOY, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.soja_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.soja_neutral));
            }
        });

        MaterialButton veganButton = binding.filterView.veganButton;
        veganButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.VEGAN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegan_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegan_neutral));
            }
        });

        MaterialButton vegetarianButton = binding.filterView.vegetarianButton;
        vegetarianButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.VEGETARIAN, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegetarisch_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.vegetarisch_neutral));
            }
        });

        MaterialButton wheatButton = binding.filterView.wheatButton;
        wheatButton.addOnCheckedChangeListener((button, isChecked) -> {
            homeViewModel.setFilter(PROPERTIES.NO_WHEAT, isChecked);
            if (isChecked) {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.weizen_aktiv));
            } else {
                button.setIcon(ContextCompat.getDrawable(MainActivity.getInstance(), R.drawable.weizen_neutral));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}