package com.example.mobilefinal;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;

import java.io.IOException;

public class GeoapifyApiCaller {

    private static final String BASE_URL = "https://api.geoapify.com/v2/places?";
    private static final String API_KEY = "a48db5f41af7495a9eb7a1f18aaa91cb";

    public static void fetchPlaces() {
        OkHttpClient client = new OkHttpClient();

        String url = BASE_URL + "categories=healthcare&filter=rect:-77.0392,38.9066,-77.0176,38.8899&limit=100&api_key=" + API_KEY;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    PlaceResponse placeResponse = gson.fromJson(response.body().string(), PlaceResponse.class);
                    // Process the response here
                    System.out.println("Received " + placeResponse.getFeatures().size() + " places.");
                }
            }
        });
    }
}
