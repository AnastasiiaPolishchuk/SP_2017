package com.annapol04.munchkin.gui;

import android.app.Dialog;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.engine.Card;

import java.util.List;



public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    public interface OnClickListener {
        void onClicked(Card card);
    }

    private final @LayoutRes int resLayout;
    private final @IdRes int resId;
    private List<Card> cards;
    private OnClickListener listener;

    public CardAdapter(@LayoutRes int resLayout, @IdRes int resId, List<Card> cards) {
        this.resLayout = resLayout;
        this.resId = resId;
        this.cards = cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resLayout, parent, false);
        ViewHolder holder = new ViewHolder(view, resId);

        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(parent.getContext());
                dialog.setContentView(R.layout.zoom_layout);
                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view_zoom_cards);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                CardAdapter handAdapter = new CardAdapter(R.layout.card_item_zoom, R.id.card_item_zoom, cards);
                recyclerView.setAdapter(handAdapter);

                Button testButton = dialog.findViewById(R.id.move_to_desk_button);      // test setEnabled = false - die Taste wird grau
                testButton.setEnabled(true);

                dialog.show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Card card = cards.get(position);
        holder.imageButton.setImageResource(card.getImageResourceID());
        holder.imageButton.setOnClickListener(v -> {
            if (listener != null)
                listener.onClicked(card);
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageButton imageButton;
        private int position;

        public ViewHolder(View itemView, @IdRes int resource) {
            super(itemView);
            imageButton = itemView.findViewById(resource);
        }
    }
}