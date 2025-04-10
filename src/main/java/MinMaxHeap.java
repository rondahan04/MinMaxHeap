import java.util.NoSuchElementException;

public class MinMaxHeap <T extends Comparable <T>>{
    private static final int DEFAULT_CAPACITY = 50;
    private T[] minHeap;
    private T[] maxHeap;
    private int[] minToMax;
    private int[] maxToMin;
     private int capacity;
    private int size;
    public MinMaxHeap() {// O(1), default constructor , constructing a heap with 50 capacity
        capacity = DEFAULT_CAPACITY;
        minHeap = (T[]) new Comparable[capacity];
        maxHeap = (T[]) new Comparable[capacity];
        minToMax = new int[capacity];
        maxToMin = new int[capacity];
        size = 0;
    }
    public MinMaxHeap(int initialCapacity){ // O(1), constructing a heap of size initialCapacity
        capacity = initialCapacity +1; // +1 because im using 1 based indexing
        minHeap = (T[]) new Comparable[capacity];
        maxHeap = (T[]) new Comparable[capacity];
        minToMax = new int[capacity];
        maxToMin = new int[capacity];
        size = 0;
    }
    public MinMaxHeap(T[]initialData){  // O(n), constructing a heap from InitialData array
        size = initialData.length;
        capacity = size;
        minHeap = (T[]) new Comparable[size+1]; // size is +1 because im using 1 based indexing
        maxHeap = (T[]) new Comparable[size+1];
        minToMax = new int[size+1];
        maxToMin = new int[size+1];
        int j = 1;
        int id = 0;
        // Copy the initial data into the minHeap and maxHeap
        while (id < initialData.length){
            minHeap[j] = initialData[id];
            maxHeap[j] = initialData[id];
            minToMax[j] = j;
            maxToMin[j] = j;
            j++;
            id++;
        }
        // Build the minHeap and maxHeap
        for (int i = size / 2; i >= 1; i--) {
            percolateDownMin(i);
            percolateDownMax(i);
        }
    }
    public void insert (T element){ // O(n) if the heap is full, otherwise O(log(n))
        if (this.size == minHeap.length - 1) { // check if the heap is full
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
    private void resize() { // helper function for the insert.
        this.capacity *= 2;
        T[] newMinHeap = (T[]) new Comparable[capacity];
        T[] newMaxHeap = (T[]) new Comparable[capacity];
        int[] newMinToMax = new int[capacity];
        int[] newMaxToMin = new int[capacity];
        for (int i = 1; i <= this.size; i++) { // create new mappings
            newMinHeap[i] = this.minHeap[i];
            newMaxHeap[i] = this.maxHeap[i];
            newMinToMax[i] = this.minToMax[i];
            newMaxToMin[i] = this.maxToMin[i];
        }
        // update new mappings
        this.minHeap = newMinHeap;
        this.maxHeap = newMaxHeap;
        this.minToMax = newMinToMax;
        this.maxToMin = newMaxToMin;
    }
    public T deleteMin(){ // O(log(n)) ,delete min from maxHeap and minHeap and update the minToMax and maxToMin
        if (this.size == 0){
            throw new NoSuchElementException("Heap is empty");
        }
        T min = this.minHeap[1];
        int index = this.minToMax[1]; // index that should be removed from the MaxHeap
        this.minHeap[1] = this.minHeap[this.size];
        this.maxHeap[index] = this.maxHeap[this.size]; // necessary only if the min is not the last element
        this.maxToMin[index] = 1; // updating mapping
        this.minToMax[1] = index; // updating mapping
        this.size--;
        percolateDownMin(1); // fixing heaps
        percolateDownMax(index); // fixing heaps
        return min;
    }
    public T deleteMax(){ // O(log(n)), delete max from minHeap and maxHeap and update the minToMax and maxToMin
        if (this.size == 0){
            throw new NoSuchElementException("Heap is empty");
        }
        T max = this.maxHeap[1];
        int index = this.maxToMin[1]; // index that should be removed from the MinHeap
        this.maxHeap[1] = this.maxHeap[this.size];
        this.minHeap[index] = this.minHeap[this.size]; // necessary only if the max is not the last element
        this.maxToMin[index] = 1; // updating mapping
        this.minToMax[1] = index; // updating mapping
        this.size--;
        percolateDownMax(1); // fixing heaps
        percolateDownMin(index); // fixing heaps
        return max;

    }
    public T findMin(){ // O(1) returns the minimum element in the heap otherwise, throws an exception
        if (this.size == 0){
            throw new NoSuchElementException("Heap is empty");
        }
        return this.minHeap[1];
    }
    public T findMax(){ // O(1) returns the maximum element in the heap otherwise, throws an exception
        if (this.size == 0){
            throw new NoSuchElementException("Heap is empty");
        }
        return this.maxHeap[1];
    }
    public int getSize () { // O(1), returns the size of the heap
        return this.size;
    }
    private int parent(int i){ // O(1), helper function for better readabillity
        return (i/2);
    }
    private int leftChild (int i){ //O(1), helper function for better readabillity
        return (2*i);
    }
    private int rightChild (int i){ //O(1), helper function for better readabillity
        return (2*i + 1);
    }
    private void percolateDownMax (int i){
        while ( 2 * i <= size) { //percolate down the max heap
            int left = leftChild(i);
            int right = rightChild(i);
            int largest = i;
            if (left <= size && maxHeap[left].compareTo(maxHeap[largest]) > 0) {
                largest = left;
            }
            if (right <= size && maxHeap[right].compareTo(maxHeap[largest]) > 0) {
                largest = right;
            }
            if (largest != i) {
                swapMax(largest, i);
                i = largest;
            } else {
                break;
            }
        }
    }
    private void percolateUpMax (int i){
        while (i > 1 && maxHeap[i].compareTo(maxHeap[parent(i)]) > 0) { //percolate up the max heap
            swapMax(i, parent(i));
            i = parent(i);
        }
    }
    private void percolateDownMin (int i){
        while (2 * i <= size) { //percolate down the min heap
            int left = leftChild(i);
            int right = rightChild(i);
            int smallest = i;
            if (left <= size && minHeap[left].compareTo(minHeap[smallest]) < 0) {
                smallest = left;
            }
            if (right <= size && minHeap[right].compareTo(minHeap[smallest]) < 0) {
                smallest = right;
            }
            if (smallest != i) {
                swapMin(smallest, i);
                i = smallest;
            } else {
                break;
            }
        }
    }
    private void percolateUpMin (int i){
        while (i > 1 && minHeap[i].compareTo(minHeap[parent(i)]) < 0) { //percolate up the min heap
            swapMin(i, parent(i));
            i = parent(i);
        }
    }
    private void swapMax (int i, int j){ //O(1) helper function for the percolate function.
            T temp = maxHeap[i];
            maxHeap[i] = maxHeap[j];
            maxHeap[j] = temp;
            int tempIndex = maxToMin[i];
            maxToMin[i] = maxToMin[j];
            maxToMin[j] = tempIndex;
            minToMax[maxToMin[i]] = i;
            minToMax[maxToMin[j]] = j;
    }
    private void swapMin (int i, int j){ // O(1) helper function for the percolate function.
            T temp = minHeap[i];
            minHeap[i] = minHeap[j];
            minHeap[j] = temp;
            int tempIndex = minToMax[i];
            minToMax[i] = minToMax[j];
            minToMax[j] = tempIndex;
            maxToMin[minToMax[i]] = i;
            maxToMin[minToMax[j]] = j;
    }

    public void toStringMinHeap(){ // tester function to see the min heap
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb.append(minHeap[i]).append(" ");
        }
        System.out.println(sb.toString());
    }
    public void toStringMaxHeap(){ // tester function to see the max heap
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb.append(maxHeap[i]).append(" ");
        }
        System.out.println(sb.toString());
    }
    public void toStringMaxHeapMaxtoMin(){ // tester function to see the index of the max to min heap
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb.append(maxToMin[i]).append(" ");
        }
        System.out.println(sb.toString());
    }
    public void toStringMinHeapMintoMax(){ // tester function to see the index of the min to max heap
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb.append(minToMax[i]).append(" ");
        }
        System.out.println(sb.toString());
    }
    public void toStringMinHeapMaxHeap(){ // tester function to see the index of the min to max heap
        StringBuilder sb = new StringBuilder();
        System.out.println("The Indices of the min to max heap");
        for (int i = 1; i <= size; i++) {
            sb.append(minToMax[i]).append(" ");
        }
       // System.out.println ("The Index you should remove is " + maxToMin[1] + " and the value is " + minHeap[maxToMin[1]]);
        System.out.println(sb.toString());
        System.out.println("The Indices of the max to min heap");
        StringBuilder sb2 = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            sb2.append(maxToMin[i]).append(" ");
        }
        System.out.println(sb2.toString());
    }
}
