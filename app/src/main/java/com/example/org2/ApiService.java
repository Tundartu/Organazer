package com.example.org2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {


    @GET("/api/events")
    Call<List<Event>> getAllEvents();

    @POST("/api/events")
    Call<Event> createEvent(@Body Event event);

    @PUT("events/{id}")
    Call<Event> updateEvent(@Path("id") Long id, @Body Event event);


    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Path("id") Long id);
}
