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
    protected final @LayoutRes int resLayout;
    protected final @IdRes int resId;
    protected ButtonSetup setup;
    protected List<Card> cards;
    protected PlayDeskViewModel viewModel;
    private SelectableCardAdapter handAdapter;

    public enum ButtonSetup {
        HAND,
        PLAYED,
        DESK,
    }

    public CardAdapter(@LayoutRes int resLayout, @IdRes int resId, ButtonSetup setup, List<Card> cards, PlayDeskViewModel viewModel) {
        this.resLayout = resLayout;
        this.resId = resId;
        this.setup = setup;
        this.cards = cards;
        this.viewModel = viewModel;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
        notifyDataSetChanged();

        if (handAdapter == null)
            handAdapter = new SelectableCardAdapter(R.layout.card_item_zoom, R.id.card_item_zoom, setup, cards, viewModel);

        handAdapter.setCards(cards);
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(resLayout, parent, false);
        ViewHolder holder = new ViewHolder(view, resId);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(parent.getContext());
                dialog.setContentView(R.layout.zoom_layout);
                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view_zoom_cards);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);

                if (handAdapter == null)
                    handAdapter = new SelectableCardAdapter(R.layout.card_item_zoom, R.id.card_item_zoom, setup, cards, viewModel);

                handAdapter.resetSelected();
                recyclerView.setAdapter(handAdapter);

                Button moveToDesk = dialog.findViewById(R.id.move_to_desk_button);      // test setEnabled = false - die Taste wird grau
                moveToDesk.setVisibility(setup == ButtonSetup.HAND ? View.VISIBLE : View.INVISIBLE);
                moveToDesk.setOnClickListener(v2 -> {
                    Card selected = handAdapter.getSelected();
                    if (selected != null) {
                        viewModel.playCard(selected);
                        handAdapter.resetSelected();
                    }
                });

                Button moveToHand = dialog.findViewById(R.id.move_to_hand_button);
                moveToHand.setVisibility(setup == ButtonSetup.PLAYED ? View.VISIBLE : View.INVISIBLE);
                moveToHand.setOnClickListener(v2 -> {
                    Card selected = handAdapter.getSelected();
                    if (selected != null) {
                        viewModel.pickupCard(selected);
                        handAdapter.resetSelected();
                    }
                });

                dialog.show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Card card = cards.get(position);
        holder.imageButton.setImageResource(card.getImageResourceID());
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