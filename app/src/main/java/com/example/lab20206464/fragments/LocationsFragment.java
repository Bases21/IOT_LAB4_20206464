package com.example.lab20206464.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.AppActivity;
import com.example.lab20206464.R;
import com.example.lab20206464.adapters.LocationsAdapter;
import com.example.lab20206464.api.ApiClient;
import com.example.lab20206464.models.Location;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationsFragment extends Fragment {

    private EditText editTextLocation;
    private MaterialButton buttonSearch;
    private RecyclerView recyclerViewLocations;
    private LocationsAdapter locationsAdapter;
    private List<Location> locationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations, container, false);

        editTextLocation = view.findViewById(R.id.editTextLocation);
        buttonSearch = view.findViewById(R.id.buttonSearch);
        recyclerViewLocations = view.findViewById(R.id.recyclerViewLocations);

        setupRecyclerView();
        setupSearchButton();

        return view;
    }

    private void setupRecyclerView() {
        locationList = new ArrayList<>();
        locationsAdapter = new LocationsAdapter(locationList, location -> {
            try {
                AppActivity appActivity = (AppActivity) getActivity();
                if (appActivity != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("locationId", location.getId());
                    bundle.putString("locationName", location.getName());
                    
                    appActivity.navigateToForecastWithLocationData(bundle);
                    
                    Toast.makeText(getContext(), "Cargando pronóstico para: " + location.getName() + " (ID: " + location.getId() + ")", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerViewLocations.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewLocations.setAdapter(locationsAdapter);
    }

    private void setupSearchButton() {
        buttonSearch.setOnClickListener(v -> {
            String location = editTextLocation.getText().toString().trim();
            if (!location.isEmpty()) {
                searchLocations(location);
            } else {
                Toast.makeText(getContext(), "Por favor ingrese una ubicación", Toast.LENGTH_SHORT).show();
            }
        });
        
        editTextLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                String location = editTextLocation.getText().toString().trim();
                if (!location.isEmpty()) {
                    searchLocations(location);
                }
                android.view.inputmethod.InputMethodManager imm =
                    (android.view.inputmethod.InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void searchLocations(String query) {
        Call<List<Location>> call = ApiClient.getWeatherApi().searchLocations(ApiClient.API_KEY, query);
        
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    locationList.clear();
                    locationList.addAll(response.body());
                    locationsAdapter.notifyDataSetChanged();
                    
                    if (locationList.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron ubicaciones", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al buscar ubicaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}