package com.example.practica5ordenamiento;

public class VideoJuego {

    private int    index;
    private String title;
    private String releaseDate;
    private String team;
    private double rating;
    private String timesListed;
    private String numberOfReviews;
    private String summary;
    private String reviews;
    private String plays;
    private String playing;
    private String backlogs;
    private String wishlist;

    public VideoJuego(int index, String title, String releaseDate, String team,
                      double rating, String timesListed, String numberOfReviews,
                      String summary, String reviews, String plays,
                      String playing, String backlogs, String wishlist) {
        this.index = index;
        this.title = title;
        this.releaseDate = releaseDate;
        this.team = team;
        this.rating = rating;
        this.timesListed = timesListed;
        this.numberOfReviews = numberOfReviews;
        this.summary = summary;
        this.reviews = reviews;
        this.plays = plays;
        this.playing = playing;
        this.backlogs = backlogs;
        this.wishlist = wishlist;
    }

    // Getters
    public int getIndex(){
        return index;
    }
    public String getTitle(){
        return title;
    }
    public String getReleaseDate(){
        return releaseDate;
    }
    public String getTeam(){
        return team;
    }
    public double getRating(){
        return rating;
    }
    public String getTimesListed(){
        return timesListed;
    }
    public String getNumberOfReviews() {
        return numberOfReviews;
    }
    public String getSummary(){
        return summary;
    }
    public String getReviews(){
        return reviews;
    }
    public String getPlays(){
        return plays;
    }
    public String getPlaying(){
        return playing;
    }
    public String getBacklogs(){
        return backlogs;
    }
    public String getWishlist(){
        return wishlist;
    }
    
    public static long convertirANumero(String value) {
        if (value == null || value.isBlank()) return 0;
        value = value.replace(",", "").replace("K", "000").replace("k", "000");
        return (long) Double.parseDouble(value);
    }
    
    @Override
    public String toString() {
        return title;
    }
}
