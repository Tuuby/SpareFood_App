package de.thb.sparefood_app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;

import java.util.ArrayList;

import de.thb.sparefood_app.MainActivity;
import de.thb.sparefood_app.databinding.FragmentHomeBinding;
import de.thb.sparefood_app.model.MealRepository;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ArrayList<Card> myCards = new ArrayList<>();
        myCards.add(new Card("Lorem", "3.4km"));
        myCards.add(new Card("Ipsum", "7.8km"));
        myCards.add(new Card("Dolor", "2.1km"));
        myCards.add(new Card("Sit amet", "4.9km"));

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}