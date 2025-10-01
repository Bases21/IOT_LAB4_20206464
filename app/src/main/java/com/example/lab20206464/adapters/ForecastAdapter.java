package com.example.lab20206464.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.R;
import com.example.lab20206464.models.WeatherResponse;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<WeatherResponse.ForecastDay> forecastDays;

    public ForecastAdapter(List<WeatherResponse.ForecastDay> forecastDays) {
        this.forecastDays = forecastDays;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forecast, parent, false);
        return new ForecastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        WeatherResponse.ForecastDay forecastDay = forecastDays.get(position);
        holder.bind(forecastDay);
    }

    @Override
    public int getItemCount() {
        return forecastDays.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {
        private TextView textDate;
        private TextView textMaxTemp;
        private TextView textMinTemp;
        private TextView textAvgTemp;
        private TextView textCondition;
        private TextView textHumidity;
        private TextView textWind;
        private TextView textPrecipitation;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textDate = itemView.findViewById(R.id.textDate);
            textMaxTemp = itemView.findViewById(R.id.textMaxTemp);
            textMinTemp = itemView.findViewById(R.id.textMinTemp);
            textAvgTemp = itemView.findViewById(R.id.textAvgTemp);
            textCondition = itemView.findViewById(R.id.textCondition);
            textHumidity = itemView.findViewById(R.id.textHumidity);
            textWind = itemView.findViewById(R.id.textWind);
            textPrecipitation = itemView.findViewById(R.id.textPrecipitation);
        }

        public void bind(WeatherResponse.ForecastDay forecastDay) {
            textDate.setText("Fecha: " + forecastDay.getDate());
            
            if (forecastDay.getDay() != null) {
                WeatherResponse.Day day = forecastDay.getDay();
                textMaxTemp.setText("Máx: " + String.format("%.1f°C", day.getMaxtemp_c()));
                textMinTemp.setText("Mín: " + String.format("%.1f°C", day.getMintemp_c()));
                textAvgTemp.setText("Promedio: " + String.format("%.1f°C", day.getAvgtemp_c()));
                textHumidity.setText("Humedad: " + String.format("%.0f%%", day.getAvghumidity()));
                textWind.setText("Viento: " + String.format("%.1f km/h", day.getMaxwind_kph()));
                textPrecipitation.setText("Precipitación: " + String.format("%.1f mm", day.getTotalprecip_mm()));
                
                if (day.getCondition() != null) {
                    textCondition.setText("Condición: " + day.getCondition().getText());
                } else {
                    textCondition.setText("Condición: N/A");
                }
            } else {
                textMaxTemp.setText("Máx: N/A");
                textMinTemp.setText("Mín: N/A");
                textAvgTemp.setText("Promedio: N/A");
                textCondition.setText("Condición: N/A");
                textHumidity.setText("Humedad: N/A");
                textWind.setText("Viento: N/A");
                textPrecipitation.setText("Precipitación: N/A");
            }
        }
    }
}