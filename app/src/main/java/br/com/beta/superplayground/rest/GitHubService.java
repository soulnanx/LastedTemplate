package br.com.beta.superplayground.rest;

import java.util.List;

import br.com.beta.superplayground.entity.Repository;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by renan on 10/05/16.
 */
public interface GitHubService {

    @GET("users/{user}/repos")
    void listAllRepositoryByUser(
            @Path("user") String user,
            Callback<List<Repository>> callback);
}
