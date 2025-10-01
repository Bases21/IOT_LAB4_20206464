package com.example.lab20206464.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.R;
import com.example.lab20206464.adapters.ForecastAdapter;
import com.example.lab20206464.api.ApiClient;
import com.example.lab20206464.models.WeatherResponse;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastFragment extends Fragment implements SensorEventListener {

    private EditText editTextLocationId;
    private EditText editTextDays;
    private MaterialButton buttonGetForecast;
    private RecyclerView recyclerViewForecast;
    private ForecastAdapter forecastAdapter;
    private List<WeatherResponse.ForecastDay> forecastList;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private boolean firstUpdate = true;
    private boolean isDialogShowing = false;
    private static final float SHAKE_THRESHOLD = 20.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        editTextLocationId = view.findViewById(R.id.editTextLocationId);
        editTextDays = view.findViewById(R.id.editTextDays);
        buttonGetForecast = view.findViewById(R.id.buttonGetForecast);
        recyclerViewForecast = view.findViewById(R.id.recyclerViewForecast);

        setupSensor();
        setupRecyclerView();
        setupButton();
        checkArguments();

        return view;
    }

    private void setupSensor() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void setupRecyclerView() {
        forecastList = new ArrayList<>();
        forecastAdapter = new ForecastAdapter(forecastList);
        recyclerViewForecast.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewForecast.setAdapter(forecastAdapter);
    }

    private void setupButton() {
        buttonGetForecast.setOnClickListener(v -> {
            String locationId = editTextLocationId.getText().toString().trim();
            String daysStr = editTextDays.getText().toString().trim();
            
            if (!locationId.isEmpty() && !daysStr.isEmpty()) {
                try {
                    int days = Integer.parseInt(daysStr);
                    if (days > 0 && days <= 14) {
                        getForecast("id:" + locationId, days);
                    } else {
                        Toast.makeText(getContext(), "Los días deben ser entre 1 y 14", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Ingrese un número válido de días", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });
        
        editTextLocationId.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                android.view.inputmethod.InputMethodManager imm = 
                    (android.view.inputmethod.InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        
        editTextDays.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                android.view.inputmethod.InputMethodManager imm = 
                    (android.view.inputmethod.InputMethodManager) getActivity().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
    }

    private void checkArguments() {
        Bundle args = getArguments();
        if (args != null) {
            int locationId = args.getInt("locationId", -1);
            String locationName = args.getString("locationName", "");
            
            if (locationId != -1) {
                editTextLocationId.setText(String.valueOf(locationId));
                editTextDays.setText("7");
                getForecast("id:" + locationId, 7);
            }
        }
    }

    private void getForecast(String location, int days) {
        Call<WeatherResponse> call = ApiClient.getWeatherApi().getForecast(ApiClient.API_KEY, location, days);
        
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getForecast() != null) {
                    forecastList.clear();
                    forecastList.addAll(response.body().getForecast().getForecastday());
                    forecastAdapter.notifyDataSetChanged();
                    
                    if (forecastList.isEmpty()) {
                        Toast.makeText(getContext(), "No se encontraron pronósticos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Error al obtener pronósticos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER && !isDialogShowing) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (!firstUpdate) {
                float deltaX = Math.abs(lastX - x);
                float deltaY = Math.abs(lastY - y);
                float deltaZ = Math.abs(lastZ - z);

                if ((deltaX > SHAKE_THRESHOLD && deltaY > SHAKE_THRESHOLD) ||
                    (deltaX > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD) ||
                    (deltaY > SHAKE_THRESHOLD && deltaZ > SHAKE_THRESHOLD)) {
                    showShakeDialog();
                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
            firstUpdate = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void showShakeDialog() {
        if (isDialogShowing) return;
        
        isDialogShowing = true;
        
        new AlertDialog.Builder(getContext())
                .setTitle("Detectada agitación")
                .setMessage("¿Desea eliminar los últimos pronósticos obtenidos?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    forecastList.clear();
                    forecastAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Pronósticos eliminados", Toast.LENGTH_SHORT).show();
                    isDialogShowing = false;
                })
                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                    isDialogShowing = false;
                })
                .setOnDismissListener(dialog -> {
                    isDialogShowing = false;
                })
                .show();
    }
    
    public void setArgumentsAndUpdate(Bundle bundle) {
        if (bundle != null) {
            int locationId = bundle.getInt("locationId", -1);
            String locationName = bundle.getString("locationName", "");
            
            if (locationId != -1 && editTextLocationId != null) {
                editTextLocationId.setText(String.valueOf(locationId));
                if (editTextDays != null) {
                    editTextDays.setText("7");
                }
                getForecast("id:" + locationId, 7);
                Toast.makeText(getContext(), "Cargando pronóstico para: " + locationName, Toast.LENGTH_SHORT).show();
            }
        }
    }
}