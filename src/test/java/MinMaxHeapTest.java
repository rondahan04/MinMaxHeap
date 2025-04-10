import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MinMaxHeapTest {

    static final long FIXED_RANDOMNESS_SEED = 1234L;
    static final int LARGE_HEAP_SIZE = 100;
    Integer[] comparables;

    @Before
    public void setup() {
        comparables = IntStream.range(0, 5).boxed().toArray(Integer[]::new);
    }

    @Test
    public void unusedEmptyHeapThrowsExceptionOnDeletion() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();
        assertThrows(Exception.class, heap::deleteMin);
        assertThrows(Exception.class, heap::deleteMax);
    }

    @Test
    public void usedEmptyHeapThrowsExceptionOnDeletion() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();
        heap.insert(comparables[0]);
        heap.insert(comparables[1]);
        try {
            heap.deleteMin();
            heap.deleteMin();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertThrows(Exception.class, heap::deleteMin);

    }

    @Test
    public void heapResizesAndUpdatesSizeCorrectly() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(1);
        for (Integer element : comparables) {
            heap.insert(element);
        }
        assertEquals(comparables.length, heap.getSize());
    }

    @Test
    public void repeatedDeleteMinProducesAscendingElementConstructedFromArray() {
        List<Integer> shuffledElements = makeShuffledElementList(LARGE_HEAP_SIZE);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        List<Integer> repeatedMinimums = repeatedDeleteMin(heap);

        assertTrue(isSortedAscending(repeatedMinimums));

    }

    @Test
    public void repeatedDeleteMinProducesAscendingElementConstructedFromInsertions() {
        int size = LARGE_HEAP_SIZE;
        List<Integer> shuffledElements = makeShuffledElementList(size);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(size);
        for (Integer element : shuffledElements) {
            heap.insert(element);
        }

        List<Integer> repeatedMinimums = repeatedDeleteMin(heap);

        assertTrue(isSortedAscending(repeatedMinimums));

    }

    @Test
    public void findMinDeleteMinEquivalence() {
        List<Integer> shuffledElements = makeShuffledElementList(LARGE_HEAP_SIZE);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));

        while (heap.getSize() > 0) {
            assertEquals(heap.findMin(), heap.deleteMin());
        }
    }

    @Test
    public void repeatedDeleteMaxProducesDescendingElementConstructedFromArray() {
        List<Integer> shuffledElements = makeShuffledElementList(LARGE_HEAP_SIZE);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        List<Integer> repeatedMaximums = repeatedDeleteMax(heap);
        Collections.reverse(repeatedMaximums);

        assertTrue(isSortedAscending(repeatedMaximums));

    }

    @Test
    public void repeatedDeleteMaxProducesDescendingElementConstructedFromInsertions() {
        int size = LARGE_HEAP_SIZE;
        List<Integer> shuffledElements = makeShuffledElementList(size);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(size);
        for (Integer element : shuffledElements) {
            heap.insert(element);
        }

        List<Integer> repeatedMaximums = repeatedDeleteMax(heap);
        Collections.reverse(repeatedMaximums);

        assertTrue(isSortedAscending(repeatedMaximums));

    }

    @Test
    public void findMaxDeleteMaxEquivalence() {
        List<Integer> shuffledElements = makeShuffledElementList(LARGE_HEAP_SIZE);

        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));

        while (heap.getSize() > 0) {
            assertEquals(heap.findMax(), heap.deleteMax());
        }
    }

    private List<Integer> makeShuffledElementList(int stop) {
        List<Integer> shuffledElements = IntStream.range(0, stop).boxed().collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(shuffledElements, new Random(FIXED_RANDOMNESS_SEED));

        return shuffledElements;
    }

    private <T extends Comparable<T>> List<T> repeatedDeleteMin(MinMaxHeap<T> heap) {
        List<T> repeatedMinimums = new ArrayList<>();

        while (heap.getSize() != 0) {
            repeatedMinimums.add(heap.deleteMin());
        }
        return repeatedMinimums;
    }

    private <T extends Comparable<T>> List<T> repeatedDeleteMax(MinMaxHeap<T> heap) {
        List<T> repeatedMaximums = new ArrayList<>();

        while (heap.getSize() != 0) {
            repeatedMaximums.add(heap.deleteMax());
        }
        return repeatedMaximums;
    }

    private <T extends Comparable<T>> boolean isSortedAscending(List<T> elements) {
        boolean isSorted = true;
        for (int index = 0; index < elements.size() - 1; index++) {
            isSorted = isSorted && elements.get(index).compareTo(elements.get(index + 1)) <= 0;
        }
        return isSorted;
    }

}
