package com.example.minim2_gerardbelvis;


import com.example.minim2_gerardbelvis.models.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GitHubAPI {
    @GET("/users/{username}")
    Call<User> infoUser (@Path("username") String user);

    @GET("/users/{username}/repos")
    Call<List<Repositories>> reposList(@Path("username") String user);
}
