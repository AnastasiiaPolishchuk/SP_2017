package com.annapol04.munchkin.gui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annapol04.munchkin.engine.Card;

import java.util.ArrayList;
import java.util.List;

public class SelectableCardAdapter extends CardAdapter {
    private final List<ViewHolder> holders = new ArrayList<>();
    private Card selectedCard;

    public SelectableCardAdapter(int resLayout, int resId, ButtonSetup setup, List<Card> cards, PlayDeskViewModel viewModel) {
        super(resLayout, resId, setup, cards, viewModel);
    }

    public void resetSelected() {
        for (ViewHolder h : holders)
            h.itemView.setSelected(false);

        selectedCard = null;
    }

    public Card getSelected() {
        return selectedCard;
    }

    @Override
    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resLayout, parent, false);
        ViewHolder holder = new ViewHolder(view, resId);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setOnClickListener(v -> {
            for (ViewHolder h : holders)
                h.itemView.setSelected(false);

            selectedCard = cards.get(position);
            v.setSelected(true);
        });
    }

    public static class ViewHolder extends CardAdapter.ViewHolder {
        public ViewHolder(View itemView, int resource) {
            super(itemView, resource);
        }
    }
}
