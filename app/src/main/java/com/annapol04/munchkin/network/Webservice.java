package com.annapol04.munchkin.network;

import com.annapol04.munchkin.data.HighscoreEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/highscore")
    Call<List<HighscoreEntry>> getUser(@Path("user") String userId);
}
