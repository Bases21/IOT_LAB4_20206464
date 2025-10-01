package com.example.lab20206464.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab20206464.R;
import com.example.lab20206464.models.SportsResponse;

import java.util.List;

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportsViewHolder> {

    private List<SportsResponse.FootballMatch> footballMatches;

    public SportsAdapter(List<SportsResponse.FootballMatch> footballMatches) {
        this.footballMatches = footballMatches;
    }

    @NonNull
    @Override
    public SportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sports, parent, false);
        return new SportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SportsViewHolder holder, int position) {
        SportsResponse.FootballMatch match = footballMatches.get(position);
        holder.bind(match);
    }

    @Override
    public int getItemCount() {
        return footballMatches.size();
    }

    static class SportsViewHolder extends RecyclerView.ViewHolder {
        private TextView textStadium;
        private TextView textCountry;
        private TextView textRegion;
        private TextView textTournament;
        private TextView textStart;
        private TextView textMatch;

        public SportsViewHolder(@NonNull View itemView) {
            super(itemView);
            textStadium = itemView.findViewById(R.id.textStadium);
            textCountry = itemView.findViewById(R.id.textCountry);
            textRegion = itemView.findViewById(R.id.textRegion);
            textTournament = itemView.findViewById(R.id.textTournament);
            textStart = itemView.findViewById(R.id.textStart);
            textMatch = itemView.findViewById(R.id.textMatch);
        }

        public void bind(SportsResponse.FootballMatch match) {
            textStadium.setText("Estadio: " + (match.getStadium() != null ? match.getStadium() : "N/A"));
            textCountry.setText("País: " + (match.getCountry() != null ? match.getCountry() : "N/A"));
            textRegion.setText("Región: " + (match.getRegion() != null && !match.getRegion().isEmpty() ? match.getRegion() : "N/A"));
            textTournament.setText("Torneo: " + (match.getTournament() != null ? match.getTournament() : "N/A"));
            textStart.setText("Inicio: " + (match.getStart() != null ? match.getStart() : "N/A"));
            textMatch.setText("Partido: " + (match.getMatch() != null ? match.getMatch() : "N/A"));
        }
    }
}