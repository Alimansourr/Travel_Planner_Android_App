package com.example.mobilefinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ListView listView;
    private TravelAdapter adapter;
    private DatabaseHandler dbHandler;
    private List<Travel> travelList;
    private Button btnAddTravel;
    private Button restAPibtn;

    private TextView tvTravelNote;
    private ImageView ivTravelImage;

    private Button layoutShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        listView = findViewById(R.id.listView);
        GeoapifyApiCaller.fetchPlaces();
        dbHandler = new DatabaseHandler(this);
        btnAddTravel = findViewById(R.id.btnAddTravel);
        restAPibtn = findViewById(R.id.btnviewApi);


        travelList = dbHandler.getTravelsByUsername("username");

        adapter = new TravelAdapter(this, travelList);
        listView.setAdapter(adapter);

        restAPibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ApiActivity.class);
                startActivity(intent);
            }
        });

        btnAddTravel.setOnClickListener(v -> {
            BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
            int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if (batteryLevel < 20) {
                Toast.makeText(this, "Battery level is too low to open google maps try to enter longitude and latitude manually", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, AddTravelActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void shareTextWithImage(String text, Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Travel Image", null);
        Uri imageUri = Uri.parse(path);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        startActivity(chooser);
    }
}
