package br.com.beta.superplayground.http.factory;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ClientFactory {

    public static OkHttpClient basicAuthInterceptor(final String basic) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", "Basic " + basic).build();

                        return chain.proceed(request);
                    }
                }).build();

        return httpClient;
    }
}
