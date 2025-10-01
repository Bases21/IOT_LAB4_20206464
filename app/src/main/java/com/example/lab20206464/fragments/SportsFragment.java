package com.example.lab20206464.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.R;
import com.example.lab20206464.adapters.SportsAdapter;
import com.example.lab20206464.api.ApiClient;
import com.example.lab20206464.models.SportsResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SportsFragment extends Fragment {

    private EditText editTextSportsLocation;
    private MaterialButton buttonSearchSports;
    private RecyclerView recyclerViewSports;
    private TextView textNoData;
    private SportsAdapter sportsAdapter;
    private List<SportsResponse.FootballMatch> footballMatches;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sports, container, false);

        editTextSportsLocation = view.findViewById(R.id.editTextSportsLocation);
        buttonSearchSports = view.findViewById(R.id.buttonSearchSports);
        recyclerViewSports = view.findViewById(R.id.recyclerViewSports);
        textNoData = view.findViewById(R.id.textNoData);

        setupRecyclerView();
        setupSearchButton();

        return view;
    }

    private void setupRecyclerView() {
        footballMatches = new ArrayList<>();
        sportsAdapter = new SportsAdapter(footballMatches);
        recyclerViewSports.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSports.setAdapter(sportsAdapter);
    }

    private void setupSearchButton() {
        buttonSearchSports.setOnClickListener(v -> {
            String location = editTextSportsLocation.getText().toString().trim();
            if (!location.isEmpty()) {
                searchSports(location);
            } else {
                Toast.makeText(getContext(), "Por favor ingrese una ubicación", Toast.LENGTH_SHORT).show();
            }
        });
        
        editTextSportsLocation.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                String location = editTextSportsLocation.getText().toString().trim();
                if (!location.isEmpty()) {
                    searchSports(location);
                }
                android.view.inputmethod.InputMethodManager imm = 
                    (android.view.inputmethod.InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void searchSports(String location) {
        Call<SportsResponse> call = ApiClient.getWeatherApi().getSports(ApiClient.API_KEY, location);
        
        call.enqueue(new Callback<SportsResponse>() {
            @Override
            public void onResponse(Call<SportsResponse> call, Response<SportsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    footballMatches.clear();
                    
                    if (response.body().getFootball() != null && !response.body().getFootball().isEmpty()) {
                        footballMatches.addAll(response.body().getFootball());
                        textNoData.setVisibility(View.GONE);
                        recyclerViewSports.setVisibility(View.VISIBLE);
                    } else {
                        textNoData.setVisibility(View.VISIBLE);
                        recyclerViewSports.setVisibility(View.GONE);
                        textNoData.setText("No se encontraron partidos de fútbol para esta ubicación");
                    }
                    
                    sportsAdapter.notifyDataSetChanged();
                } else {
                    showNoDataMessage("Error al buscar deportes");
                }
            }

            @Override
            public void onFailure(Call<SportsResponse> call, Throwable t) {
                showNoDataMessage("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void showNoDataMessage(String message) {
        textNoData.setVisibility(View.VISIBLE);
        recyclerViewSports.setVisibility(View.GONE);
        textNoData.setText(message);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}