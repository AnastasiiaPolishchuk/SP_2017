package com.annapol04.munchkin.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "highscores")
public class HighscoreEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double score;

    public HighscoreEntry(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getScore() { return score; }
    public void setScore(double score) { this.score = score; }
}
