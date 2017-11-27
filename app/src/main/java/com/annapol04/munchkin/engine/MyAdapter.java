package com.annapol04.munchkin.engine;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.annapol04.munchkin.R;

import java.util.List;

/**
 * Created by anastasiiapolishchuk on 17.11.17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Card> cards;

    public MyAdapter(List<Card> Data) {
        cards = Data;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        ViewHolder.ViewHolderPlayer holder = new ViewHolder.ViewHolderPlayer(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // custom dialog
                final Dialog dialog = new Dialog(parent.getContext());
                dialog.setContentView(R.layout.zoom_layout);
                ImageButton imageButton = (ImageButton) v;
                ImageView imageView = dialog.findViewById(R.id.zoom_image_view);
                imageView.setImageDrawable(imageButton.getDrawable());

                Button testButton = dialog.findViewById(R.id.zoom_button);      // test setEnabled = false - die Taste wird grau
                testButton.setEnabled(false);

                dialog.setTitle("Title...");

                dialog.show();
            }
        });
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

    public static class MyDeskAdapter extends MyAdapter {
        public MyDeskAdapter(List<Card> Data) {
            super(Data);
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
