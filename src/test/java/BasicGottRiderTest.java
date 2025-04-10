import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BasicGottRiderTest {
    private static final List<String> EXPECTED_METHOD_NAMES = Arrays.asList("rideCompleted", "getName", "toString", "compareTo");
    private static final Double ACCEPTABLE_ERROR = 0.001;

    @Test
    public void allMethodsPresent() {
        List<String> actualMethodNames = Arrays.stream(GottRider.class.getMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getName)
                .toList();

        List<String> missingNames = EXPECTED_METHOD_NAMES.stream()
                .filter(name -> !actualMethodNames.contains(name))
                .collect(Collectors.toList());

        String message = "The following methods are missing: " + String.join(", ", missingNames);
        assertEquals(message, 0, missingNames.size());
    }

    @Test
    public void averageRatingUpdatedOnRideCompletion() {
        GottRider rider = new GottRider("Jack Ryder", 0L, 0.0);
        assertEquals(0.0, rider.getAverageReviewGiven(), ACCEPTABLE_ERROR);

        rider.rideCompleted(5L);
        assertEquals(5.0, rider.getAverageReviewGiven(), ACCEPTABLE_ERROR);

        rider.rideCompleted(3L);
        assertEquals(4.0, rider.getAverageReviewGiven(), ACCEPTABLE_ERROR);

        rider.rideCompleted(5L);
        assertEquals(13.0 / 3, rider.getAverageReviewGiven(), ACCEPTABLE_ERROR);

        rider.rideCompleted(1L);
        assertEquals(14.0 / 4, rider.getAverageReviewGiven(), ACCEPTABLE_ERROR);
    }

    @Test
    public void toStringBasicValidity() {
        GottRider rider = new GottRider("Jack Ryder", 0L, 0.0);
        try {
            assertNotNull(rider.toString());
        } catch (Exception e) {
            fail("toString() threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void comparisonLogic() {
        GottRider low = new GottRider("Low", 100L, 0.0);
        GottRider med = new GottRider("Med", 10L, 2.5);
        GottRider hai = new GottRider("Hai", 1_000L, 4.99);
        GottRider alsoLow = new GottRider("Also low", 100L, 0.0);

        assertEquals(0, low.compareTo(alsoLow));
        assertTrue(low.compareTo(med) < 0);
        assertTrue(low.compareTo(hai) < 0);
        assertTrue(med.compareTo(hai) < 0);
        assertTrue(hai.compareTo(med) > 0);
        assertTrue(hai.compareTo(low) > 0);
        assertTrue(med.compareTo(low) > 0);
    }
}
