package br.com.beta.superplayground.rest;

import retrofit2.Retrofit;

/**
 * Created by renan on 10/05/16.
 */
public class ServiceGenerator {

    public static <S>S generate(Class<S> serviceClass){
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
//                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(serviceClass);
    }
}
