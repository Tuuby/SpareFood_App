package de.thb.sparefood_app.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.thb.sparefood_app.databinding.CardLayoutBinding;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {

    private final List<Card> cardsList;

    public CardStackAdapter(List<Card> cardsList) {
        this.cardsList = cardsList;
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
            cardLayoutBinding.cardTextView.setText(card.getText());
        }
    }
}
