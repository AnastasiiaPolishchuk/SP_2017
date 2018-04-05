package com.annapol04.munchkin.gui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
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
    protected final @LayoutRes
    int resLayout;
    protected final @IdRes
    int resId;
    protected ButtonSetup setup;
    protected List<Card> cards;
    protected PlayDeskViewModel viewModel;
    private SelectableCardAdapter handAdapter;
    private boolean visible = false;

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

    public void setCardVisibillity(boolean visible) {
        this.visible = visible;
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
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RecyclerView recyclerView = dialog.findViewById(R.id.recycler_view_zoom_cards);

                int itemPosition = recyclerView.getChildLayoutPosition(view);

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                layoutManager.scrollToPosition(itemPosition);

                if (handAdapter == null)
                    handAdapter = new SelectableCardAdapter(R.layout.card_item_zoom, R.id.card_item_zoom, setup, cards, viewModel);

                handAdapter.resetSelected();

                recyclerView.setAdapter(handAdapter);

                Button moveToPlayed = dialog.findViewById(R.id.move_to_played_button);      // test setEnabled = false - die Taste wird grau
                moveToPlayed.setVisibility(setup == ButtonSetup.DESK ? View.INVISIBLE : View.VISIBLE);
                moveToPlayed.setEnabled(setup == ButtonSetup.HAND ? true : false);
                moveToPlayed.setOnClickListener(v2 -> {
                    Card selected = handAdapter.getSelected();
                    if (selected != null) {
                        viewModel.playCard(selected);
                        handAdapter.resetSelected();
                    }
                });

                Button moveToHand = dialog.findViewById(R.id.move_to_hand_button);
                moveToHand.setVisibility(setup == ButtonSetup.DESK ? View.INVISIBLE : View.VISIBLE);
                moveToHand.setEnabled(setup == ButtonSetup.PLAYED ? true : false);
                moveToHand.setOnClickListener(v2 -> {
                    Card selected = handAdapter.getSelected();
                    if (selected != null) {
                        viewModel.pickupCard(selected);
                        handAdapter.resetSelected();
                    }
                });

                Button moveToPlayDesk = dialog.findViewById(R.id.move_to_desk_buton);
                moveToPlayDesk.setVisibility(setup == ButtonSetup.DESK ? View.INVISIBLE : View.VISIBLE);
                moveToPlayDesk.setEnabled(setup == ButtonSetup.HAND ? true : false);
                moveToPlayDesk.setOnClickListener(v2 -> {
                    Card selected = handAdapter.getSelected();
                    if (selected != null) {
                        viewModel.moveToPlayDesk(selected);
                        handAdapter.resetSelected();
                    }
                });

                Button dropTheCard = dialog.findViewById(R.id.move_to_passive_card_deck);
                dropTheCard.setVisibility(setup == ButtonSetup.DESK ? View.INVISIBLE : View.VISIBLE);

                dialog.show();

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Card card = cards.get(position);
        holder.imageButton.setImageResource(visible ? card.getImageResourceID() : R.drawable.treasure);
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