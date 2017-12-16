package com.annapol04.munchkin.gui;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.annapol04.munchkin.engine.Card;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final @LayoutRes int resLayout;
    private final @IdRes int resId;
    private List<Card> cards;

    public CardAdapter(@LayoutRes int resLayout, @IdRes int resId, List<Card> cards) {
        this.resLayout = resLayout;
        this.resId = resId;
        this.cards = cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resLayout, parent, false);
        ViewHolder holder = new ViewHolder(view, resId);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageButton.setImageResource(cards.get(position).getImageResourceID());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imageButton;

        public ViewHolder(View itemView, @IdRes int resource) {
            super(itemView);
            imageButton = itemView.findViewById(resource);
        }
    }
}
