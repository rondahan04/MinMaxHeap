import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;
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
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        List<Integer> repeatedMinimums = repeatedDeleteMin(heap);
        assertTrue(isSortedAscending(repeatedMinimums));

    }

    @Test
    public void repeatedDeleteMinProducesAscendingElementConstructedFromInsertions() {
        int size = LARGE_HEAP_SIZE;
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(size);
        for (Integer element : shuffledElements) {
            heap.insert(element);
        }
        List<Integer> repeatedMinimums = repeatedDeleteMin(heap);
        assertTrue(isSortedAscending(repeatedMinimums));

    }

    @Test
    public void findMinDeleteMinEquivalence() {
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        while (heap.getSize() > 0) {
            assertEquals(heap.findMin(), heap.deleteMin());
        }
    }
    @Test
    public void insertMaxDeleteMaxEquivalence() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        Integer thousand = 1000;
        Integer five = 5;
        Integer one = 1;
        heap.insert(thousand);
        heap.insert(one);
        heap.insert(five);
        assertEquals(thousand,heap.deleteMax());
        assertNotEquals(one,heap.deleteMax());
        assertEquals(one,heap.deleteMax());
    }
    @Test
    public void findMinNMaxNoDeletion() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        Integer thousand = 1000;
        Integer five = 5;
        Integer one = 1;
        heap.insert(thousand);
        heap.insert(one);
        heap.insert(five);
        assertEquals(thousand, heap.findMax());
        assertEquals(one, heap.findMin());
        assertEquals(3, heap.getSize());
    }

    @Test
    public void repeatedDeleteMaxProducesDescendingElementConstructedFromArray() {
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        List<Integer> repeatedMaximums = repeatedDeleteMax(heap);
        Collections.reverse(repeatedMaximums);
        assertTrue(isSortedAscending(repeatedMaximums));
    }

    @Test
    public void repeatedDeleteMaxProducesDescendingElementConstructedFromInsertions() {
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        for (Integer element : shuffledElements) {
            heap.insert(element);
        }
        List<Integer> repeatedMaximums = repeatedDeleteMax(heap);
        Collections.reverse(repeatedMaximums);
        assertTrue(isSortedAscending(repeatedMaximums));
    }

    @Test
    public void findMaxDeleteMaxEquivalence() {
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new));
        while (heap.getSize() > 0) {
            assertEquals(heap.findMax(), heap.deleteMax());
        }
    }
    @Test
    public void deleteDuplicateElements() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        for (int i=1; i<=LARGE_HEAP_SIZE; i++) { // also resizing
            heap.insert(1);
            heap.insert(24);
        }
        assertEquals((Integer) 24 , heap.deleteMax());
        assertEquals((Integer) 1 , heap.deleteMin());
        assertEquals(198 , heap.getSize()); // inserted 200, deleted 2
    }
    @Test
    public void zeroCapacityHeapThrowsException (){ // on construction.
        assertThrows(IllegalArgumentException.class, () -> new MinMaxHeap<>(0));
    }
    @Test
    public void deleteUntilZeroCapacityThenTryInsert(){
        List <Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new)); // creating the heap
        while (heap.getSize() != 0) {
            heap.deleteMin();
        }
        assertThrows(Exception.class,heap::deleteMin);
    }
    @Test
    public void insertAndDeleteSingleElement1000Times(){ // also using resizing
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        for (int i = 0; i < 1000; i++) {
            heap.insert(1);
            assertEquals((Integer) 1, heap.deleteMin());
        }
        assertEquals(0, heap.getSize());
        MinMaxHeap<Integer>heapWith1000 = new MinMaxHeap<>(1000);
        for (int i = 0; i < 1000; i++){
            heapWith1000.insert(1);
        }
        for (int i = 0; i < 1000; i++) {
            heapWith1000.deleteMax();
        }
        assertEquals(0, heapWith1000.getSize());
    }

    @Test
    public void mappingAfterInsertionToAnExistingHeap() throws NoSuchFieldException, IllegalAccessException{
        int maxIndex;
        int minIndex;
        List<Integer> shuffledElements = makeShuffledElementList();
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(shuffledElements.toArray(Integer[]::new)); // creating the heap
        heap.insert(10000);
        heap.insert(-5);
        heap.insert(50);
        Field minToMaxField = MinMaxHeap.class.getDeclaredField("minToMax");
        Field maxToMinField = MinMaxHeap.class.getDeclaredField("maxToMin");
        Field minHeapField = MinMaxHeap.class.getDeclaredField("minHeap");
        Field maxHeapField = MinMaxHeap.class.getDeclaredField("maxHeap");
        minToMaxField.setAccessible(true);
        maxToMinField.setAccessible(true);
        minHeapField.setAccessible(true);
        maxHeapField.setAccessible(true);
        int[] minToMax = (int[]) minToMaxField.get(heap);
        int[] maxToMin = (int[]) maxToMinField.get(heap);
        Comparable[] minHeap = (Comparable[]) minHeapField.get(heap);
        Comparable[] maxHeap = (Comparable[]) maxHeapField.get(heap);
        for (int i = 1; i<= heap.getSize(); i++){
            maxIndex = minToMax[i];
            minIndex = maxToMin[i];
            assertEquals(minHeap[i], maxHeap[maxIndex]);
            assertEquals(maxHeap[i], minHeap[minIndex]);
        }
    }
    @Test
    public void negativeCapacityHeapThrowsException(){
        assertThrows(IllegalArgumentException.class, () -> new MinMaxHeap<>(-5));
    }
    @Test
    public void heapPropertyTestAfterFewInsertionsAndDeletions() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(LARGE_HEAP_SIZE);
        for (int i = 1; i <= 100; i++){
            heap.insert(i);
        }
        heap.insert(101);
        assertEquals((Integer) (101), heap.deleteMax());
        heap.insert(-100);
        assertEquals((Integer) (-100), heap.deleteMin());
        assertEquals((Integer) (1), heap.findMin());
        assertEquals((Integer) (100), heap.findMax());
    }

    @Test
    public void insertNullException()   {
        MinMaxHeap<Integer> heap = new MinMaxHeap(LARGE_HEAP_SIZE);
        assertThrows(IllegalArgumentException.class, () -> heap.insert(null));
    }
    @Test
    public void stressTestRandomData()  {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(100000);
        Random random = new Random();
        for (int i = 0; i < 100000; i++) {
            heap.insert(random.nextInt());
        }
        while (heap.getSize() > 0) {
            heap.deleteMin();
        }
        assertEquals(0, heap.getSize());
    }
    @Test
    public void insertMaxIntegerMinInteger() {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();
        heap.insert(Integer.MAX_VALUE);
        heap.insert(Integer.MIN_VALUE);
        assertEquals(Integer.MAX_VALUE, (int) heap.findMax());
        assertEquals(Integer.MIN_VALUE, (int) heap.findMin());
    }
    @Test
    public void heapWithString() {
        MinMaxHeap<String> heap = new MinMaxHeap<>();
        heap.insert("Sung Jin-Woo");
        heap.insert("Cha Hae-In");
        heap.insert("Choi Jong-In");
        assertEquals("Sung Jin-Woo", heap.findMax());
        assertEquals("Cha Hae-In", heap.findMin());
    }
    @Test
    public void testMinToMaxWithReflection() throws NoSuchFieldException, IllegalAccessException {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(10);
        for(int i=1; i<=10; i++){
            heap.insert(i);
        }
        Field minToMaxField = MinMaxHeap.class.getDeclaredField("minToMax");
        minToMaxField.setAccessible(true);
        int[] minToMax = (int[]) minToMaxField.get(heap);
        Field minHeapField = MinMaxHeap.class.getDeclaredField("minHeap");
        Field maxHeapField = MinMaxHeap.class.getDeclaredField("maxHeap");
        minHeapField.setAccessible(true);
        maxHeapField.setAccessible(true);
        Comparable[] minHeap = (Comparable[]) minHeapField.get(heap);
        Comparable[] maxHeap = (Comparable[]) maxHeapField.get(heap);
        for (int i = 1; i <= 10; i++) {
            int maxIndex = minToMax[i];
            assertEquals(minHeap[i], maxHeap[maxIndex]);
        }
    }
    @Test
    public void testMaxToMinWithReflection() throws NoSuchFieldException, IllegalAccessException {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(10);
        for(int i=1; i<=10; i++){
            heap.insert(i);
        }
        Field maxToMinField = MinMaxHeap.class.getDeclaredField("maxToMin");
        maxToMinField.setAccessible(true);
        int[] maxToMin = (int[]) maxToMinField.get(heap);
        Field minHeapField = MinMaxHeap.class.getDeclaredField("minHeap");
        Field maxHeapField = MinMaxHeap.class.getDeclaredField("maxHeap");
        minHeapField.setAccessible(true);
        maxHeapField.setAccessible(true);
        Comparable[] minHeap = (Comparable[]) minHeapField.get(heap);
        Comparable[] maxHeap = (Comparable[]) maxHeapField.get(heap);
        for (int i = 1; i <= 10; i++) {
            int minIndex = maxToMin[i];
            assertEquals(maxHeap[i], minHeap[minIndex]);
        }
    }
    @Test
    public void testMinToMaxDeletionWithReflection() throws NoSuchFieldException, IllegalAccessException {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(10);
        for(int i=1; i<=10; i++){
            heap.insert(i);
        }
        int deletedVal = heap.deleteMax();
        System.out.println ("The deleted Value is" + deletedVal);
        Field minToMaxField = MinMaxHeap.class.getDeclaredField("minToMax");
        minToMaxField.setAccessible(true);
        int[] minToMax = (int[]) minToMaxField.get(heap);
        Field minHeapField = MinMaxHeap.class.getDeclaredField("minHeap");
        Field maxHeapField = MinMaxHeap.class.getDeclaredField("maxHeap");
        minHeapField.setAccessible(true);
        maxHeapField.setAccessible(true);
        Comparable[] minHeap = (Comparable[]) minHeapField.get(heap);
        Comparable[] maxHeap = (Comparable[]) maxHeapField.get(heap);
        for (int i = 1; i <= 10; i++) {
            int maxIndex = minToMax[i];
            assertEquals(minHeap[i], maxHeap[maxIndex]);
        }
    }
    @Test
    public void TestMaxToMinDeletionWithReflection () throws NoSuchFieldException, IllegalAccessException {
        MinMaxHeap<Integer> heap = new MinMaxHeap<>(10);
        for(int i=1; i<=10; i++){
            heap.insert(i);
        }
        int deletedVal = heap.deleteMin();
        System.out.println("The Deleted Value Is" + deletedVal);
        heap.deleteMin(); // try another deletion
        Field maxToMinField = MinMaxHeap.class.getDeclaredField("maxToMin");
        maxToMinField.setAccessible(true);
        int[] maxToMin = (int[]) maxToMinField.get(heap);
        Field minHeapField = MinMaxHeap.class.getDeclaredField("minHeap");
        Field maxHeapField = MinMaxHeap.class.getDeclaredField("maxHeap");
        minHeapField.setAccessible(true);
        maxHeapField.setAccessible(true);
        Comparable[] minHeap = (Comparable[]) minHeapField.get(heap);
        Comparable[] maxHeap = (Comparable[]) maxHeapField.get(heap);
        for (int i = 1; i <= 10; i++) {
            int minIndex = maxToMin[i];
            assertEquals(maxHeap[i], minHeap[minIndex]);
        }
    }
    @Test
    public void deleteTwiceThrowException(){
        MinMaxHeap<Integer> heap = new MinMaxHeap<>();
        heap.insert(1);
        heap.deleteMax();
        assertThrows(NoSuchElementException.class, heap::deleteMax);
        assertThrows(NoSuchElementException.class, heap::deleteMax);
    }
    private List<Integer> makeShuffledElementList() {
        List<Integer> shuffledElements = IntStream.range(0, LARGE_HEAP_SIZE).boxed().collect(Collectors.toCollection(ArrayList::new));
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