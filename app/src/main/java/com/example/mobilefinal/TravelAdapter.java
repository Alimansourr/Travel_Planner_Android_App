package com.example.mobilefinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class TravelAdapter extends BaseAdapter {
    private Context context;
    private List<Travel> travelList;
    private LayoutInflater inflater;

    public TravelAdapter(Context context, List<Travel> travelList) {
        this.context = context;
        this.travelList = travelList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return travelList.size();
    }

    @Override
    public Object getItem(int position) {
        return travelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.travel_experience_layout, parent, false);
            holder = new ViewHolder();
            holder.tvTravelName = convertView.findViewById(R.id.tvTravelName);
            holder.tvTravelNote = convertView.findViewById(R.id.tvTravelNote);
            holder.tvLocation = convertView.findViewById(R.id.tvLocation);
            holder.ivTravelImage = convertView.findViewById(R.id.ivTravelImage);
            holder.btnShare = convertView.findViewById(R.id.sharelayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Travel travel = travelList.get(position);
        holder.tvTravelName.setText(travel.getName());

        // Set first note
        if (!travel.getNotes().isEmpty()) {
            holder.tvTravelNote.setText(travel.getNotes().get(0).getNote());
        } else {
            holder.tvTravelNote.setText("No notes available");
        }

        // Set location
        Location location = travel.getLocation();
        holder.tvLocation.setText(String.format("Location: %f, %f", location.getLongitude(), location.getLatitude()));

        // Set first image
        if (!travel.getPhotos().isEmpty()) {
            holder.ivTravelImage.setImageBitmap(BitmapFactory.decodeByteArray(travel.getPhotos().get(0).getImage(), 0, travel.getPhotos().get(0).getImage().length));
        } else {
            holder.ivTravelImage.setImageResource(R.drawable.ic_launcher_background); // default image
        }

        // Set click listener for the share button
        holder.btnShare.setOnClickListener(v -> {
            Log.d("TravelAdapter", "Share button clicked for position: " + position);
            if (holder.ivTravelImage.getDrawable() == null) {
                Toast.makeText(context, "No image to share", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap = ((BitmapDrawable) holder.ivTravelImage.getDrawable()).getBitmap();
            shareTextWithImage(holder.tvTravelNote.getText().toString(), bitmap);
        });

        return convertView;
    }

    private void shareTextWithImage(String text, Bitmap bitmap) {
        File imageFile = new File(context.getExternalFilesDir(null), "shared_image.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Uri imageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", imageFile);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        context.startActivity(chooser);
    }

    private static class ViewHolder {
        TextView tvTravelName;
        TextView tvTravelNote;
        TextView tvLocation;
        ImageView ivTravelImage;
        Button btnShare;
    }
}
