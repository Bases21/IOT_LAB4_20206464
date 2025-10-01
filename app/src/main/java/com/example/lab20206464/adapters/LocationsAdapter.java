package com.example.lab20206464.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.R;
import com.example.lab20206464.models.Location;

import java.util.List;

public class LocationsAdapter extends RecyclerView.Adapter<LocationsAdapter.LocationViewHolder> {

    private List<Location> locations;
    private OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }

    public LocationsAdapter(List<Location> locations, OnLocationClickListener listener) {
        this.locations = locations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locations.get(position);
        holder.bind(location, listener);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textLocationId;
        private TextView textLocationName;
        private TextView textLocationRegion;
        private TextView textLocationCountry;
        private TextView textLocationLat;
        private TextView textLocationLon;
        private TextView textLocationUrl;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textLocationId = itemView.findViewById(R.id.textLocationId);
            textLocationName = itemView.findViewById(R.id.textLocationName);
            textLocationRegion = itemView.findViewById(R.id.textLocationRegion);
            textLocationCountry = itemView.findViewById(R.id.textLocationCountry);
            textLocationLat = itemView.findViewById(R.id.textLocationLat);
            textLocationLon = itemView.findViewById(R.id.textLocationLon);
            textLocationUrl = itemView.findViewById(R.id.textLocationUrl);
        }

        public void bind(Location location, OnLocationClickListener listener) {
            textLocationId.setText("ID: " + location.getId());
            textLocationName.setText("Nombre: " + location.getName());
            textLocationRegion.setText("Región: " + location.getRegion());
            textLocationCountry.setText("País: " + location.getCountry());
            textLocationLat.setText("Latitud: " + String.format("%.2f", location.getLat()));
            textLocationLon.setText("Longitud: " + String.format("%.2f", location.getLon()));
            textLocationUrl.setText("URL: " + location.getUrl());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onLocationClick(location);
                }
            });
        }
    }
}