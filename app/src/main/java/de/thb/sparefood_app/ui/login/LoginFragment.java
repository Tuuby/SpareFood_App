package de.thb.sparefood_app.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.thb.sparefood_app.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textLogin;
//        loginViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}