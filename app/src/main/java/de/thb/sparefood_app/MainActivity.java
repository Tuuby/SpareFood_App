package de.thb.sparefood_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import de.thb.sparefood_app.databinding.ActivityMainBinding;
import de.thb.sparefood_app.model.MealRepository;
import de.thb.sparefood_app.ui.home.HomeFragment;
import de.thb.sparefood_app.ui.new_entry.NewEntryFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MealRepository mealRepository;
    private static MainActivity appInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appInstance = this;
        mealRepository = new MealRepository();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_new_entry)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        MaterialButton settingsMenuButton = (MaterialButton) findViewById(R.id.settingsMenuButton);
        settingsMenuButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this, settingsMenuButton);
                popup.getMenuInflater().inflate(R.menu.top_app_bar_settings_menu, popup.getMenu());

                popup.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar_settings_menu, menu);
        return true;
    }

    public static MainActivity getInstance() {
        return appInstance;
    }

    public MealRepository getMealRepository() {
        return mealRepository;
    }

    public void createNewEntry(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_new_entry);
        View view1 = findViewById(R.id.container);
        view1.setPadding(0,0,0,0);
    }

    public void closeNewEntry(View view) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_home);
        View view1 = findViewById(R.id.container);
    }

}