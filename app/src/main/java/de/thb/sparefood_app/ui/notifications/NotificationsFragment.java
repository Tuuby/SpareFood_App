package de.thb.sparefood_app.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.ActionBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    ConstraintLayout constraintLayoutWrapper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        constraintLayoutWrapper = (ConstraintLayout) binding.constraintLayoutWrapper;

        constraintLayoutWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_chat);
            }
        });

//        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
//        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
//        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
//        navView.setVisibility(View.GONE);
//        bottomAppBar.setVisibility(View.GONE);
//        floatingActionButton.setVisibility(View.GONE);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.hide();

        Log.d("NotificationsFragment", "loaded");

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
//        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
//        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
//        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
//        navView.setVisibility(View.VISIBLE);
//        bottomAppBar.setVisibility(View.VISIBLE);
//        floatingActionButton.setVisibility(View.VISIBLE);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        actionBar.show();
    }
}