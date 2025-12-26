package com.example.mobilefinal;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class ApiActivity extends AppCompatActivity {

    private PlaceAdapter placeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        ListView listViewPlaces = findViewById(R.id.listViewPlaces);

        placeAdapter = new PlaceAdapter(this, new ArrayList<>());
        listViewPlaces.setAdapter(placeAdapter);

        fetchPlaces();
    }

    private void fetchPlaces() {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.geoapify.com/v2/places?categories=healthcare&filter=rect:-77.0392,38.9066,-77.0176,38.8899&limit=100&api_key=a48db5f41af7495a9eb7a1f18aaa91cb";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    PlaceResponse placeResponse = gson.fromJson(response.body().string(), PlaceResponse.class);
                    runOnUiThread(() -> {
                        placeAdapter.clear();
                        placeAdapter.addAll(placeResponse.getFeatures());
                    });
                }
            }
        });
    }
}