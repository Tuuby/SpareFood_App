package de.thb.sparefood_app.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import de.thb.sparefood_app.R;
import de.thb.sparefood_app.databinding.FragmentChatBinding;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChatViewModel ChatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        navView.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.hide();

        Log.d("ChatFragment", "loaded");

//        final TextView textView = binding.textChat;
//        ChatViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        BottomAppBar bottomAppBar = getActivity().findViewById(R.id.bottom_app_bar);
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.fab);
        navView.setVisibility(View.VISIBLE);
        bottomAppBar.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.show();
    }
}