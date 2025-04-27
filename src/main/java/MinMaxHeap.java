import java.util.NoSuchElementException;

public class MinMaxHeap <T extends Comparable <T>>{
    private static final int DEFAULT_CAPACITY = 50;
    private T[] minHeap;
    private T[] maxHeap;
    private int[] minToMax;
    private int[] maxToMin;
    private int capacity; // a private field that I use to keep track of the heap capacity. (the size of the heap)
    private int size; // the actual size of the heap

    // ###### CONSTRUCTORS ######

    public MinMaxHeap() {// O(1), default constructor , constructing a heap with 50 capacity
        this.capacity = DEFAULT_CAPACITY;
        this.minHeap = (T[]) new Comparable[capacity];
        this.maxHeap = (T[]) new Comparable[capacity];
        this.minToMax = new int[capacity];
        this.maxToMin = new int[capacity];
        this.size = 0;

    }
    public MinMaxHeap(int initialCapacity){ // O(1), constructing a heap of size initialCapacity
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be at least 1");
        }
        this.capacity = initialCapacity +1; // +1 because im using 1 based indexing
        this.minHeap = (T[]) new Comparable[capacity];
        this.maxHeap = (T[]) new Comparable[capacity];
        this.minToMax = new int[capacity];
        this.maxToMin = new int[capacity];
        this.size = 0;
    }
    public MinMaxHeap(T[]initialData){  // O(n), constructing a heap from InitialData array
        this.size = initialData.length;
        this.capacity = this.size;
        this.minHeap = (T[]) new Comparable[this.size+1]; // size is +1 because im using 1 based indexing
        this.maxHeap = (T[]) new Comparable[this.size+1]; // size is +1 because im using 1 based indexing
        this.minToMax = new int[this.size+1]; // size is +1 because im using 1 based indexing
        this.maxToMin = new int[this.size+1]; // size is +1 because im using 1 based indexing
        int j = 1; // j starts from 1 because of the 1 based indexing
        int id = 0;
        // Copy the initialData array into the minHeap and maxHeap in no particular order.
        while (id < initialData.length){
            this.minHeap[j] = initialData[id];
            this.maxHeap[j] = initialData[id];
            this.minToMax[j] = j;
            this.maxToMin[j] = j;
            j++;
            id++;
        }
        // Build the minHeap and maxHeap according to the Heap property.
        for (int i = this.size / 2; i >= 1; i--) {
            percolateDownMin(i);
            percolateDownMax(i);
        }
    }

    // ###### METHODS ######

    public void insert (T element){ // O(n) if the heap is full, otherwise O(log(n)), if the capacity is zero throws an exception
        if (this.capacity == 0) {
            throw new IllegalStateException("Cannot Insert an element to a 0 capacity heap");
        }
        if (element == null) { // given element is null
            throw new IllegalArgumentException ("Cannot Insert null element to the heap");
        }
        if (this.size >= this.capacity - 1) { // check if the heap is full
            resize(); // resize the heap
        }
        this.size++;
        this.minHeap[this.size] = element; // insert the element into the minHeap
        this.maxHeap[this.size] = element; // insert the element into the maxHeap
        this.minToMax[this.size] = this.size; // update the mapping
        this.maxToMin[this.size] = this.size; // update the mapping
        percolateUpMin(this.size); // percolate up the minHeap
        percolateUpMax(this.size); // percolate up the maxHeap
    }
    public T deleteMin() {// O(log(n)), delete min from maxHeap and minHeap and update the minToMax and maxToMin
       int index;
       if (isEmpty()) {
           throw new NoSuchElementException("No element to be removed as the heap is empty.");
       }
       T min = this.minHeap[1];
       index = this.minToMax[1]; // index that should be removed from the MaxHeap
       swapMin(1, this.size); // Swap the root with the last element
       this.size--;
       percolateDownMin(1); // according to the heap prop
       swapMax(index, this.size+1); // swap the element with the last one
       percolateDownMax(index); // according to the heap prop
       return min;
   }
    public T deleteMax(){ // O(log(n)), delete max from minHeap and maxHeap and update the minToMax and maxToMin
        int index;
        if (isEmpty()){
            throw new NoSuchElementException("No element to be deleted as hhe Heap is empty.");
        }
        T max = this.maxHeap[1];
        index = this.maxToMin[1]; // index that should be removed from the MinHeap
        swapMax(1,this.size);
        this.size--;
        percolateDownMax(1); // according to the heap prop.
        swapMin(index, this.size+1); // swap the element with the last one
        percolateDownMin(index); // according to the heap prop.
        return max;
    }
    public T findMin(){ // O(1) returns the minimum element in the heap otherwise, throws an exception.
        if (isEmpty()){
            throw new NoSuchElementException("Heap is empty");
        }
        return this.minHeap[1];
    }
    public T findMax(){ // O(1) returns the maximum element in the heap otherwise, throws an exception.
        if (isEmpty()){
            throw new NoSuchElementException("Heap is empty");
        }
        return this.maxHeap[1];
    }
    public int getSize () { // O(1), returns the size of the heap
        return this.size;
    }
    private void percolateDownMax (int i){
        int left;
        int right;
        int maximal;
        while ( 2 * i <= this.size) { //percolate down the max heap, 2*i<=size is to ensure that it has at least one child.
            left = leftChild(i);
            right = rightChild(i);
            maximal = i;
            if (left <= this.size && this.maxHeap[left].compareTo(this.maxHeap[maximal]) > 0) {
                maximal = left;
            }
            if (right <= this.size && this.maxHeap[right].compareTo(this.maxHeap[maximal]) > 0) {
                maximal = right;
            }
            if (maximal != i) {
                swapMax(maximal, i);
                i = maximal;
            } else {
                break; // stop if the heap property is satisfied.
            }
        }
    }
    private void percolateUpMax (int i){
        while (i > 1 && this.maxHeap[i].compareTo(this.maxHeap[parent(i)]) > 0) { //percolate up the max heap ,i>1 is to ensure that im not in the root
            swapMax(i, parent(i));
            i = parent(i);
        }
    }
    private void percolateDownMin (int i){
        int left;
        int right;
        int minimal;
        while (2 * i <= this.size) { //percolate down the min heap, 2*i<=size is to ensure that it has at least one child.
            left = leftChild(i);
            right = rightChild(i);
            minimal = i;
            if (left <= this.size && this.minHeap[left].compareTo(this.minHeap[minimal]) < 0) {
                minimal = left;
            }
            if (right <= this.size && this.minHeap[right].compareTo(this.minHeap[minimal]) < 0) {
                minimal = right;
            }
            if (minimal != i) {
                swapMin(minimal, i);
                i = minimal;
            } else {
                break;
            }
        }
    }
    private void percolateUpMin (int i){
        while (i > 1 && this.minHeap[i].compareTo(this.minHeap[parent(i)]) < 0) { //percolate up the min heap, i>1 is to ensure that im not in the root
            swapMin(i, parent(i));
            i = parent(i);
        }
    }

    // ###### HELPER FUNCTIONS ######
    // These functions are used to make the code more readable and to avoid code duplication.
    private void resize() { //O(n), helper function for the insert.
        this.capacity *= 2;
        T[] newMinHeap = (T[]) new Comparable[this.capacity]; // creating a new MinHeap with 2*capacity
        T[] newMaxHeap = (T[]) new Comparable[this.capacity]; // creating a new MaxHeap with 2*capacity
        int[] newMinToMaxMapping = new int[this.capacity]; // creating a new minToMax mapping with 2*capacity
        int[] newMaxToMinMapping = new int[this.capacity]; // creating a new maxToMin mapping with 2*capacity
        for (int i = 1; i <= this.size; i++) { // create new mappings index starts from 1 because of the 1 based indexing until this.size which is the last element.
            newMinHeap[i] = this.minHeap[i];
            newMaxHeap[i] = this.maxHeap[i];
            newMinToMaxMapping[i] = this.minToMax[i];
            newMaxToMinMapping[i] = this.maxToMin[i];
        }
        // update new mappings
        this.minHeap = newMinHeap;
        this.maxHeap = newMaxHeap;
        this.minToMax = newMinToMaxMapping;
        this.maxToMin = newMaxToMinMapping;
    }
    private int parent(int i){ // O(1), helper function for better readability
        return (i / 2);
    }
    private int leftChild (int i){ //O(1), helper function for better readability
        return (2 * i);
    }
    private int rightChild (int i){ //O(1), helper function for better readability
        return (2 * i + 1);
    }
    private void swapMax (int i, int j){ //O(1) helper function for the percolate function.
        int tempElementIndex;
        T tempElement = this.maxHeap[i];
        this.maxHeap[i] = this.maxHeap[j];
        this.maxHeap[j] = tempElement;
        tempElementIndex = this.maxToMin[i];
        this.maxToMin[i] = this.maxToMin[j];
        this.maxToMin[j] = tempElementIndex;
        this.minToMax[this.maxToMin[i]] = i;
        this.minToMax[this.maxToMin[j]] = j;
    }
    private void swapMin (int i, int j){ // O(1) helper function for the percolate function.
        int tempElementIndex;
        T tempElement = this.minHeap[i];
        this.minHeap[i] = this.minHeap[j];
        this.minHeap[j] = tempElement;
        tempElementIndex = this.minToMax[i];
        this.minToMax[i] = this.minToMax[j];
        this.minToMax[j] = tempElementIndex;
        this.maxToMin[this.minToMax[i]] = i;
        this.maxToMin[this.minToMax[j]] = j;
    }
    private boolean isEmpty() { // O(1), helper function to check if the heap is empty
        return this.size == 0;
    }
}
