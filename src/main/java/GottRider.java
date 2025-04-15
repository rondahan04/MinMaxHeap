public class GottRider implements Comparable<GottRider>{
    private String name;
    private long previousRides;
    private double averageReview;
    private double totalStarsGiven; // a private field that I use to calculate the averageReview more easily.

    public GottRider (String name, Long previousRides, Double averageReviewGiven){ // O(1)
        this.name = name;
        this.previousRides = previousRides;
        this.averageReview = averageReviewGiven;
        this.totalStarsGiven = previousRides * averageReviewGiven;
    }
    public void rideCompleted (Long starsGiven){ // O(1)
        this.previousRides++;
        this.totalStarsGiven += starsGiven;
        this.averageReview = totalStarsGiven / this.previousRides;
    }
    public String getName(){ // O(1)
        return this.name;
    }
    public String toString() { // O(1)
        return "Name: " + this.name + " Previous Rides: " + this.previousRides + " Average Review: " + this.averageReview;
    }
    public int compareTo (GottRider otherRider) { // O(1)
        if (this.averageReview < otherRider.getAverageReviewGiven()) { // this rider average is less than otherRider
            return -1;
        } else if (this.averageReview > otherRider.getAverageReviewGiven()) { // otherRider average is less than this rider
            return 1;
        }
            return 0;  // averages are equal
    }

    public double getAverageReviewGiven() { // O(1)
        return this.averageReview;
    }
}
