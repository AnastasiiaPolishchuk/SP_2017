package com.annapol04.munchkin.data;

/**
 * Created by chris on 16.11.2017.
 */

public class HighscoreEntry {
    private String name;
    private double score;

    public HighscoreEntry(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() { return name; }
    public double getScore() { return score; }
}
