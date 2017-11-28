package com.annapol04.munchkin.gui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.engine.Card;

import java.util.List;

/**
 * Created by anastasiiapolishchuk on 17.11.17.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<Card> cards;

    public CardAdapter(List<Card> Data) {
        cards = Data;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        ViewHolder.ViewHolderPlayer holder = new ViewHolder.ViewHolderPlayer(view);
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

    public static class MyDeskAdapter extends CardAdapter {
        public MyDeskAdapter(List<Card> Data) {
            super(Data);
        }

        @Override
        public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_item_desk, parent, false);
            ViewHolder.ViewHolderDesk holder = new ViewHolder.ViewHolderDesk(view);
            return holder;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            //  imageButton = itemView.findViewById(R.id.card_item);
        }

        public static class ViewHolderPlayer extends ViewHolder {


            public ViewHolderPlayer(View itemView) {
                super(itemView);
                imageButton = itemView.findViewById(R.id.card_item);
            }
        }

        public static class ViewHolderDesk extends ViewHolder {


            public ViewHolderDesk(View itemView) {
                super(itemView);
                imageButton = itemView.findViewById(R.id.card_item_desk);
            }
        }
    }
}
