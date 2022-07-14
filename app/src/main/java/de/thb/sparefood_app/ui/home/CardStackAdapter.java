package de.thb.sparefood_app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.thb.sparefood_app.databinding.CardLayoutBinding;
import de.thb.sparefood_app.model.Meal;
import de.thb.sparefood_app.model.MealRepository;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private final List<Card> cardsList = new ArrayList<>();

    public CardStackAdapter() {
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    public void setCardsList(List<Meal> meals) {
        List<Card> cards = new ArrayList<>();
        for (Meal meal: meals) {
            String imageURL = MealRepository.getURL("meals/" + meal.getId() + "/image", null);
            cards.add(new Card(meal.getName(), meal.getDescription(), imageURL));
        }
        cardsList.clear();
        cardsList.addAll(cards);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardLayoutBinding binding = CardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(this.cardsList.get(position));
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private final CardLayoutBinding cardLayoutBinding;

        public ViewHolder(@NonNull CardLayoutBinding binding) {
            super(binding.getRoot());
            cardLayoutBinding = binding;
        }

        public void bind(Card card) {
            cardLayoutBinding.cardTitle.setText(card.getText());
            cardLayoutBinding.cardDistance.setText(card.getDistance());
            Picasso.get().load(card.getImageURL()).into(cardLayoutBinding.imageView);
        }
    }
}
