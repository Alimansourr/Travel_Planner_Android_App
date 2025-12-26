package com.example.mobilefinal;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AddTravelActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_SELECT_IMAGE = 2;
    private static final int REQUEST_LOCATION = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 4;
    private static final int REQUEST_STORAGE_PERMISSION = 5;

    private EditText etTravelName, etTravelNote, etLongitude, etLatitude;
    private Button btnSaveTravel, btnTakePicture, btnSelectFromGallery, btnOpenMap;
    private ImageView ivTravelImage;
    private Bitmap travelImageBitmap;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_travel);

        etTravelName = findViewById(R.id.etTravelName);
        etTravelNote = findViewById(R.id.etTravelNote);
        etLongitude = findViewById(R.id.etLongitude);
        etLatitude = findViewById(R.id.etLatitude);
        btnSaveTravel = findViewById(R.id.btnSaveTravel);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnSelectFromGallery = findViewById(R.id.btnSelectFromGallery);
        ivTravelImage = findViewById(R.id.ivTravelImage);

        dbHandler = new DatabaseHandler(this);

        btnSaveTravel.setOnClickListener(v -> saveTravel());

        btnTakePicture.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            } else {
                dispatchTakePictureIntent();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
        }

        btnSelectFromGallery.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
            } else {
                dispatchSelectFromGalleryIntent();
            }
        });


        if (getIntent().hasExtra("longitude") && getIntent().hasExtra("latitude")) {
            double longitude = getIntent().getDoubleExtra("longitude", 0);
            double latitude = getIntent().getDoubleExtra("latitude", 0);
            etLongitude.setText(String.valueOf(longitude));
            etLatitude.setText(String.valueOf(latitude));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && data != null) {
                Bundle extras = data.getExtras();
                travelImageBitmap = (Bitmap) extras.get("data");
                ivTravelImage.setImageBitmap(travelImageBitmap);
            } else if (requestCode == REQUEST_SELECT_IMAGE && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                    travelImageBitmap = BitmapFactory.decodeStream(imageStream);
                    ivTravelImage.setImageBitmap(travelImageBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_LOCATION && data != null) {
                double longitude = data.getDoubleExtra("longitude", 0);
                double latitude = data.getDoubleExtra("latitude", 0);
                etLongitude.setText(String.valueOf(longitude));
                etLatitude.setText(String.valueOf(latitude));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera permission is required to take pictures", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            dispatchSelectFromGalleryIntent();

        }
    }

    private void dispatchTakePictureIntent() {
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

        if (batteryLevel < 20) {
            Toast.makeText(this, "Battery level is too low to take pictures", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void dispatchSelectFromGalleryIntent() {
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(selectPictureIntent, REQUEST_SELECT_IMAGE);
    }

    private void saveTravel() {
        String travelName = etTravelName.getText().toString();
        String travelNote = etTravelNote.getText().toString();
        double longitude = Double.parseDouble(etLongitude.getText().toString());
        double latitude = Double.parseDouble(etLatitude.getText().toString());

        if (travelName.isEmpty() || travelNote.isEmpty() || etLongitude.getText().toString().isEmpty() || etLatitude.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long locationId = dbHandler.insertLocation(dbHandler.getWritableDatabase(), longitude, latitude);
        long travelId = dbHandler.insertTravel(dbHandler.getWritableDatabase(), locationId, 1, travelName); // Assuming user ID is 1 for simplicity
        dbHandler.insertNote(dbHandler.getWritableDatabase(), travelId, travelNote);

        if (travelImageBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            travelImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            dbHandler.insertImage(dbHandler.getWritableDatabase(), travelId, byteArray);
        }

        Toast.makeText(this, "Travel saved", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddTravelActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
