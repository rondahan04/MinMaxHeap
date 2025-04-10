import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class BasicMinMaxHeapTest {

    static final long FIXED_RANDOMNESS_SEED = 1234L;

    static final int LARGE_HEAP_SIZE = 100;

    // Used to verify that all mandatory methods exist and have the expected parameters
    static final Map<String, Class<?>[]> EXPECTED_METHOD_NAMES_TO_PARAMETER_TYPES = Map.of(
            "insert", new Class<?>[] {Comparable.class},
            "findMin", new Class<?>[] {},
            "deleteMin", new Class<?>[] {},
            "findMax", new Class<?>[] {},
            "deleteMax", new Class<?>[] {},
            "getSize", new Class<?>[] {}
    );

    /**
     * Simple test to ensure the heap is not missing any of the required methods (also helps avoid typos).
     * The code here is a bit complicated, using more "advanced" parts of Java like "reflection" and "streams".
     * It just lets you know if you have all the mandatory methods, it's fine if you don't understand the code
     */
    @Test
    public void allMethodsPresent() {
        List<String> actualMethodNames = Arrays.stream(MinMaxHeap.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .map(Method::getName)
                .toList();

        List<String> missingNames = EXPECTED_METHOD_NAMES_TO_PARAMETER_TYPES.keySet().stream()
                .filter(name -> !actualMethodNames.contains(name))
                .collect(Collectors.toList());

        String message = "The following methods are missing: " + String.join(", ", missingNames);
        assertEquals(message, 0, missingNames.size());

        Map<String, Class<?>[]> actualMethodNamesToParameterTypes = Arrays.stream(MinMaxHeap.class.getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()))
                .collect(Collectors.toMap(Method::getName, Method::getParameterTypes));

        for (Map.Entry<String, Class<?>[]> entry : EXPECTED_METHOD_NAMES_TO_PARAMETER_TYPES.entrySet()) {
            Class<?>[] actualTypeParameters = actualMethodNamesToParameterTypes.get(entry.getKey());
            String errorMessage = "Method '%s' has incorrect parameters. Expected: %s. Actual: %s".formatted(
                    entry.getKey(), Arrays.toString(entry.getValue()), Arrays.toString(actualTypeParameters)
            );
            assertArrayEquals(errorMessage, entry.getValue(), actualTypeParameters);
        }
    }

    /**
     * Some tests can be very simple. We know how the heap should behave under certain circumstances, so we
     * write tests to verify (assert) that the heap behaves as we expect.
     */
    @Test
    public void unusedEmptyHeapThrowsExceptionOnDeletion() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();
        assertThrows(Exception.class, heap::deleteMin);
    }

    /**
     * Some tests require a bit of "preparation" before we can "assert" the heap behaves as we expect.
     * This test demonstrates that we can set up a situation before calling any assert methods.
     * Each test should check one behavior you expect from your code (like everything in Software Engineering
     * this isn't a law, just a guideline).
     */
    @Test
    public void noElementsMissingFromHeap() {
        // Prepare a new heap (empty)
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();

        // Prepare integers in a random order
        List<Integer> startingElements = makeShuffledElementList();
        // Insert the integers to the heap
        for(Integer i : startingElements) {
            heap.insert(i);
        }

        // Delete the elements one-by-one, storing them in a list
        List<Integer> elementsFromHeap = new ArrayList<>();
        while (heap.getSize() > 0) {
            elementsFromHeap.add(heap.deleteMin());
        }

        // We expect every element we inserted was retrieved by deleteMin at some point. I.e., every element in
        // startingElements should be present in elementsFromHeap. If not, some element got "lost" somewhere (bug).
        for (Integer i: startingElements) {
            assertTrue("Starting Element missing from heap: " + i, elementsFromHeap.contains(i));
        }
    }

    /**
     * Think of as many behaviors you expect your code to have and write tests for all of them!
     * Tests are a great way of proving to yourself that your code does what you expect it to do.
     * When you change your code, tests also give you confidence that you didn't accidentally break something.
     */
    @Test
    public void repeatedDeleteMinProducesElementsInAscendingOrder() {
        System.err.println("You should implement this test! It's great for finding bugs (repeatedDeleteMinProducesElementsInAscendingOrder)");
    }

    /**
     * Create a list of the integers in the range [0, LARGE_HEAP_SIZE) "shuffled" to a random order.
     * Useful when writing your own tests!
     */
    private List<Integer> makeShuffledElementList() {
        List<Integer> shuffledElements = IntStream.range(0, LARGE_HEAP_SIZE).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(shuffledElements, new Random(FIXED_RANDOMNESS_SEED));

        return shuffledElements;
    }
}
