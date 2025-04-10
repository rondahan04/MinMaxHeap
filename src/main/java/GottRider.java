public class GottRider implements Comparable<GottRider>{
    private String name;
    private long previousRides;
    private double averageReview;
    private double totalReviewGiven; // a private field that I use to calculate the averageReview more easily.

    public GottRider (String name, Long previousRides, Double averageReviewGiven){
        this.name = name;
        this.previousRides = previousRides;
        this.averageReview = averageReviewGiven;
        this.totalReviewGiven = previousRides * averageReviewGiven;
    }
    public void rideCompleted (Long starsGiven){
        this.previousRides++;
        this.totalReviewGiven += starsGiven;
        this.averageReview = totalReviewGiven / this.previousRides;
    }
    public String getName(){
        return this.name;
    }
    public String toString() {
        return "Name: " + this.name + " Previous Rides: " + this.previousRides + " Average Review: " + this.averageReview;
    }
    public int compareTo (GottRider otherRider) { // add comments
        if (this.averageReview < otherRider.getAverageReviewGiven()) { // this rider average is less than otherRider
            return -1;
        } else if (this.averageReview > otherRider.getAverageReviewGiven()) { // otherRider average is less than this rider
            return 1;
        }
            return 0;  // averages are equal
    }

    public double getAverageReviewGiven() {
        return this.averageReview;
    }
}
