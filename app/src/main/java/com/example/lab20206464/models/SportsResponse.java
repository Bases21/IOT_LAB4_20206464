package com.example.lab20206464.models;

import java.util.List;

public class SportsResponse {
    private List<FootballMatch> football;
    private List<CricketMatch> cricket;
    private List<GolfMatch> golf;

    public List<FootballMatch> getFootball() { return football; }
    public void setFootball(List<FootballMatch> football) { this.football = football; }

    public List<CricketMatch> getCricket() { return cricket; }
    public void setCricket(List<CricketMatch> cricket) { this.cricket = cricket; }

    public List<GolfMatch> getGolf() { return golf; }
    public void setGolf(List<GolfMatch> golf) { this.golf = golf; }

    public static class FootballMatch {
        private String stadium;
        private String country;
        private String region;
        private String tournament;
        private String start;
        private String match;

        public String getStadium() { return stadium; }
        public void setStadium(String stadium) { this.stadium = stadium; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public String getTournament() { return tournament; }
        public void setTournament(String tournament) { this.tournament = tournament; }

        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }

        public String getMatch() { return match; }
        public void setMatch(String match) { this.match = match; }
    }

    public static class CricketMatch {
        private String stadium;
        private String country;
        private String region;
        private String tournament;
        private String start;
        private String match;

        public String getStadium() { return stadium; }
        public void setStadium(String stadium) { this.stadium = stadium; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public String getTournament() { return tournament; }
        public void setTournament(String tournament) { this.tournament = tournament; }

        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }

        public String getMatch() { return match; }
        public void setMatch(String match) { this.match = match; }
    }

    public static class GolfMatch {
        private String stadium;
        private String country;
        private String region;
        private String tournament;
        private String start;
        private String match;

        public String getStadium() { return stadium; }
        public void setStadium(String stadium) { this.stadium = stadium; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }

        public String getRegion() { return region; }
        public void setRegion(String region) { this.region = region; }

        public String getTournament() { return tournament; }
        public void setTournament(String tournament) { this.tournament = tournament; }

        public String getStart() { return start; }
        public void setStart(String start) { this.start = start; }

        public String getMatch() { return match; }
        public void setMatch(String match) { this.match = match; }
    }
}