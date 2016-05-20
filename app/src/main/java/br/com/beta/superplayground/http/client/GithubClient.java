package br.com.beta.superplayground.http.client;

import java.util.List;

import br.com.beta.superplayground.entity.Repository;
import br.com.beta.superplayground.http.factory.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;


public class GithubClient {

    interface Client {
        @GET("users/{user}/repos")
        Call<List<Repository>> getRepositories(
                @Path("user") String user);
    }

    public void getRepositories(String user, Callback<List<Repository>> callback) {
        Client service = ServiceFactory.service().create(Client.class);
        Call<List<Repository>> response = service.getRepositories(user);
        response.enqueue(callback);
    }

}
