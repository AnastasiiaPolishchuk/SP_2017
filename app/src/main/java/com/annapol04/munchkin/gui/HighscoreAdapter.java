package com.annapol04.munchkin.gui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annapol04.munchkin.R;
import com.annapol04.munchkin.data.HighscoreEntry;
import com.annapol04.munchkin.databinding.HighscoreEntryBinding;

import java.util.Collections;
import java.util.List;

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.ViewHolder> {

    private List<HighscoreEntry> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    HighscoreAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setEntries(List<HighscoreEntry> entries) {
        this.mData = entries;
    }

    public List<HighscoreEntry> getEntries() {
        return this.mData;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HighscoreEntryBinding binding = DataBindingUtil.inflate(
                mInflater, R.layout.highscore_entry, parent, false);


        return new HighscoreAdapter.ViewHolder(binding);
    }

    // binds the data to the textview in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindEntry(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).getId();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private HighscoreEntryBinding binding;

        public ViewHolder(HighscoreEntryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        private void bindEntry(HighscoreEntry entry) {
            binding.setEntry(entry);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}