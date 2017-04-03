package models;

public class OrderData {
    private int id;
    private int numberOfPlaces;
    private ScheduleData movie;

    public OrderData(int id, int numberOfPlaces, ScheduleData movie) {
        this.id = id;
        this.numberOfPlaces = numberOfPlaces;
        this.movie = movie;
    }

    public OrderData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public void setNumberOfPlaces(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
    }

    public ScheduleData getMovie() {
        return movie;
    }

    public void setMovie(ScheduleData movie) {
        this.movie = movie;
    }
}
