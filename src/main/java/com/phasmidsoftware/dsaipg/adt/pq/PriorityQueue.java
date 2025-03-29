/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.benchmark.Benchmark_Timer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static java.lang.Math.log;
import static java.lang.Math.min;

/**
 * Priority Queue Data Structure which uses a binary heap.
 * <p/>
 * It is unlimited in capacity, although there is no code to grow it after it has been constructed.
 * It can serve as a minPQ or a maxPQ (define "max" as either false or true, respectively).
 * <p/>
 * It can support the root at index 1 or the root at index 2 variants.
 * <p/>
 * It follows the code from Sedgewick and Wayne more or less. I have changed the names a bit. For example,
 * the methods to insert and remove the max (or min) element are called "give" and "take," respectively.
 * <p/>
 * It operates on arbitrary Object types which implies that it requires a Comparator to be passed in.
 * <p/>
 * For all details on usage, please see PriorityQueueTest.java
 *
 * @param <K>
 */
public class PriorityQueue<K> implements Iterable<K> {

    /**
     * Primary constructor that takes the max value, an actual array of elements, and a comparator.
     *
     * @param max        whether or not this is a Maximum Priority Queue as opposed to a Minimum PQ.
     * @param binHeap    a pre-formed array with length one greater than the required capacity.
     * @param first      the index of the root element.
     * @param last       the number of elements in binHeap
     * @param comparator a comparator for the type K
     * @param floyd      true if we use Floyd's trick
     */
    public PriorityQueue(boolean max, Object[] binHeap, int first, int last, Comparator<K> comparator, boolean floyd) {
        this.max = max;
        this.first = first;
        this.comparator = comparator;
        this.last = last;
        //noinspection unchecked
        this.binHeap = (K[]) binHeap;
        this.floyd = floyd;
    }

    /**
     * Secondary constructor which takes only the priority queue's maximum capacity and a comparator
     *
     * @param n          the desired maximum capacity.
     * @param first      the index to use for the first (root) element.
     * @param max        whether or not this is a Maximum Priority Queue as opposed to a Minimum PQ.
     * @param comparator a comparator for the type K
     */
    public PriorityQueue(int n, int first, boolean max, Comparator<K> comparator, boolean floyd) {

        // NOTE that we reserve the first element of the binary heap, so the length must be n+1, not n
        this(max, new Object[n + first], first, 0, comparator, floyd);
    }

    /**
     * Secondary constructor which takes only the priority queue's maximum capacity and a comparator
     *
     * @param n          the desired maximum capacity.
     * @param max        whether or not this is a Maximum Priority Queue as opposed to a Minimum PQ.
     * @param comparator a comparator for the type K
     */
    public PriorityQueue(int n, boolean max, Comparator<K> comparator, boolean floyd) {

        // NOTE that we reserve the first element of the binary heap, so the length must be n+1, not n
        this(n, 1, max, comparator, floyd);
    }

    /**
     * Secondary constructor which takes only the priority queue's maximum capacity and a comparator
     *
     * @param n          the desired maximum capacity.
     * @param max        whether or not this is a Maximum Priority Queue as opposed to a Minimum PQ.
     * @param comparator a comparator for the type K
     */
    public PriorityQueue(int n, boolean max, Comparator<K> comparator) {

        // NOTE that we reserve the first element of the binary heap, so the length must be n+1, not n
        this(n, 1, max, comparator, false);
    }

    /**
     * Secondary constructor which takes only the priority queue's maximum capacity and a comparator
     *
     * @param n          the desired maximum capacity.
     * @param comparator a comparator for the type K
     */
    public PriorityQueue(int n, Comparator<K> comparator) {
        this(n, 1, true, comparator, true);
    }

    /**
     * @return true if the current size is zero.
     */
    public boolean isEmpty() {
        return last == 0;
    }

    /**
     * @return the number of elements actually stored in this Priority Queue
     */
    public int size() {
        return last;
    }

    /**
     * Insert an element with the given key into this Priority Queue.
     *
     * @param key the value of the key to give
     */
    public void give(K key) {
        if (last == binHeap.length - first)
            last--; // if we are already at capacity, then we arbitrarily trash the least eligible element
        // (even if it's more eligible than key).
        binHeap[++last + first - 1] = key; // insert the key into the binary heap just after the last element
        swimUp(last + first - 1); // reorder the binary heap
    }

    /**
     * Remove the root element from this Priority Queue and adjust the binary heap accordingly.
     * If max is true, then the result will be the maximum element, else the minimum element.
     * NOTE that this method is called DelMax (or DelMin) in the book.
     *
     * @return If max is true, then the maximum element, otherwise the minimum element.
     * @throws PQException if this priority queue is empty
     */
    public K take() throws PQException {
        if (isEmpty()) throw new PQException("Priority queue is empty");
        if (floyd) return doTake(this::snake);
        else return doTake(this::sink);
    }

    /**
     * Package-private method to remove the root element from the priority queue,
     * reorganizes the heap to maintain the priority queue properties,
     * and applies the provided function to the root index.
     *
     * @param f a consumer function that manipulates the root index to maintain the heap order.
     * @return the root element of the priority queue before reorganization.
     */
    K doTake(Consumer<Integer> f) {
        K result = binHeap[first]; // get the root element (the largest or smallest, according to field max)
        swap(first, last-- + first - 1); // swap the root element with the last element
        f.accept(first); // invoke the function f so that it is ordered again
        binHeap[last + first] = null; // prevent loitering
        return result;
    }

    /**
     * Sink the element at index k down
     */
    void sink(@SuppressWarnings("SameParameterValue") int k) {
        doHeapify(k, (a, b) -> !unordered(a, b));
    }

    /**
     * Special sink method that sinks the element and then swim the element back
     *
     * @param k the starting index of the element in the heap to be adjusted.
     */
    void snake(@SuppressWarnings("SameParameterValue") int k) {
        swimUp(doHeapify(k, (a, b) -> !unordered(a, b)));
    }

    /**
     * Swim the element at index k up
     */
    void swimUp(int k) {
        int i = k;
        while (i > first && unordered(parent(i), i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    /**
     * Compare the elements at indices i and j.
     * We expect the first index (the smaller one) to be greater than the second, assuming that max is true.
     * In this case, we return false.
     *
     * @param i the lower index, numerically
     * @param j the higher index, numerically
     * @return true if the values are out of order.
     */
    boolean unordered(int i, int j) {
        return (comparator.compare(binHeap[i], binHeap[j]) > 0) ^ max;
    }

    /**
     * Non-mutating iterator over all values of this PriorityQueue.
     * NOTE: after the first element, there is no definite ordering of the remaining elements.
     * NOTE: this method is really not truly a method of the PriorityQueue API.
     * It is here only for convenience.
     *
     * @return an iterator based on a copy of the underlying array.
     */
    public Iterator<K> iterator() {
        Collection<K> copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(binHeap, last + first)));
        Iterator<K> result = copy.iterator();
        if (first > 0) result.next(); // strip off the leading null value.
        return result;
    }

    /**
     * Adjusts a subtree rooted at index k to ensure it satisfies the heap property.
     * The method reorganizes the binary heap by comparing parent and child nodes,
     * swapping their positions if necessary, until the correct heap order is maintained.
     *
     * @param k the starting index of the element in the heap that needs to be adjusted.
     * @param p a predicate that determines the heap condition to be satisfied.
     *          It takes two indices (parent and child) and returns true if the parent satisfies the heap property relative to the child.
     * @return the final position of the element originally at index k after reorganization.
     */
    private int doHeapify(int k, BiPredicate<Integer, Integer> p) {
        int i = k;
        while (firstChild(i) <= last + first - 1) {
            int j = firstChild(i);
            if (j < last + first - 1 && unordered(j, j + 1)) j++;
            if (p.test(i, j)) break;
            swap(i, j);
            i = j;
        }
        return i;
    }

    /**
     * Exchange the values at indices i and j
     */
    private void swap(int i, int j) {
        K tmp = binHeap[i];
        binHeap[i] = binHeap[j];
        binHeap[j] = tmp;
    }

    /**
     * Get the index of the parent of the element at index k
     */
    private int parent(int k) {
        return (k + 1 - first) / 2 + first - 1;
    }

    /**
     * Get the index of the first child of the element at index k.
     * The index of the second child will be one greater than the result.
     */
    private int firstChild(int k) {
        return (k + 1 - first) * 2 + first - 1;
    }

    /**
     * The following methods are for unit testing ONLY!!
     */

    @SuppressWarnings("unused")
    private K peek(int k) {
        return binHeap[k];
    }

    @SuppressWarnings("unused")
    private boolean getMax() {
        return max;
    }

    private final boolean max;
    private final int first;
    private final Comparator<K> comparator;
    private final K[] binHeap; // binHeap[i] is ith element of binary heap (first element is reserved)
    private int last; // number of elements in the binary heap
    private final boolean floyd; //Determine whether floyd's snake method is on or off inside the take method


    public static void main(String[] args) throws PQException, FileNotFoundException {
        doMain();
    }
    public static final class Input_data{
        static int maxInput;
        static int M;
        static int maxOutput;
        public Input_data(int maxInput, int M, int maxOutput) {
            Input_data.maxInput = maxInput;
            Input_data.M = M;
            Input_data.maxOutput = maxOutput;
        }
        public static int getMaxOutput() {
            return Input_data.maxOutput;
        }

        public static int getM() {
            return Input_data.M;
        }


        public static int getMaxInput() {
            return Input_data.maxInput;
        }



    }
    /**
     * XXX Huh?
     */
    static void doMain() throws PQException, FileNotFoundException {

        ArrayList<Integer> overFill = new ArrayList<>();
        Random random = new Random();

        File file = new File("D:\\NortheasternUniversity\\INFO 6205 Program Structure and Algorithms\\Assignment4\\BenchMark.csv");
        PrintWriter writer = new PrintWriter(file);
        int maxLength = 16384000;
        PriorityQueue<Integer> pq= new PriorityQueue<>(maxLength,Comparator.comparing(Integer::intValue));
        int input_Array[] = {15,31,62,125,250,500,1000,2000,4000,8000,16000,32000,64000,128000,256000,512000,1024000,2048000,4096000,8192000,16384000};

/*PQ with Floyd*/
        int M,Insert,remove;
        Benchmark_Timer<Input_data> benchMarkTimer = new Benchmark_Timer<>("BenchMark for PQ floyd",(ipt)->{
            overFill.clear();while(!pq.isEmpty()) {
            try {
                pq.take();
            } catch (PQException e) {
                throw new RuntimeException(e);
            }
        }
            return ipt;
        },(ipt)->{
            for(int i = 1; i <= Input_data.getMaxInput(); i++){
                if(pq.size() == Input_data.getM()) {
                    try {
                        overFill.add(pq.take());
                    } catch (PQException e) {
                        throw new RuntimeException(e);
                    }
                }
                pq.give(random.nextInt());
            }
            for(int i = 1; i <= Input_data.getMaxOutput(); i++){
                try {
                    if(!pq.isEmpty())
                      pq.take();
                } catch (PQException e) {
                    throw new RuntimeException(e);
                }
            }
        },null);

        ArrayList<Double> pqFloyd = new ArrayList<>();
        ArrayList<Double> pqn = new ArrayList<>();
        System.out.println("===============================");
        System.out.println("Running PQ with floyd");
        for(int i = 0; i < input_Array.length; i++){
            double total_time = benchMarkTimer.run(new Input_data(input_Array[i],input_Array[i]/4+95,input_Array[i]/4),20);
            System.out.printf("M=%d\nInsert=%d\nRemove=%d\nAverage time (msc): %.3f\n",input_Array[i]/4+95,input_Array[i],input_Array[i]/4,total_time);
            pqn.add(log(input_Array[i]/4+95)/log(2));
            pqFloyd.add(log((int)(total_time*1e6))/log(2));
            overFill.sort(Comparator.comparing(Integer::intValue));
            System.out.println("Total Overfill size = "+overFill.size());
            if(!overFill.isEmpty())
                System.out.printf("The element with the highest priority in PQ is %d\n",overFill.get(overFill.size()-1));
            else
                System.out.printf("No element overfill in PQ floyd\n");
            System.out.println();
        }
/*PQ Without Floyd*/
        PriorityQueue<Integer> pq_no_floyd = new PriorityQueue<>(maxLength,true,Comparator.comparing(Integer::intValue),false);
        benchMarkTimer = new Benchmark_Timer<>("BenchMark for PQ without floyd",(ipt)->{
            overFill.clear();while(!pq_no_floyd.isEmpty()) {
                try {
                    pq_no_floyd.take();
                } catch (PQException e) {
                    throw new RuntimeException(e);
                }
            }
            return ipt;
        },(ipt)->{
            int maxInput=Input_data.getMaxInput(),maxOutput=Input_data.getMaxOutput();
            int maxM=Input_data.getM();
            for(int i = 1; i <=maxInput; i++){
                if(pq_no_floyd.size() == maxM) {
                    try {
                        overFill.add(pq_no_floyd.take());
                    } catch (PQException e) {
                        throw new RuntimeException(e);
                    }
                }
                pq_no_floyd.give(random.nextInt());
            }
            for(int i = 1; i <= maxOutput; i++){
                try {
                    if(!pq_no_floyd.isEmpty())
                        pq_no_floyd.take();
                } catch (PQException e) {
                    throw new RuntimeException(e);
                }
            }
        },null);
        System.out.println("===============================");
        System.out.println("Running PQ without floyd");
        ArrayList<Double> pqNoFloydFloyd = new ArrayList<>();

        for(int i = 0; i < input_Array.length; i++){
            double total_time = benchMarkTimer.run(new Input_data(input_Array[i],input_Array[i]/4+95,input_Array[i]/4),20);
            System.out.printf("M=%d\nInsert=%d\nRemove=%d\nAverage time (msc): %.3f\n",input_Array[i]/4+95,input_Array[i],input_Array[i]/4,total_time);
            pqNoFloydFloyd.add(log((int)(total_time*1e6))/log(2));
            overFill.sort(Comparator.comparing(Integer::intValue));
            System.out.println("Total Overfill size = "+overFill.size());
            if(!overFill.isEmpty())
                System.out.printf("The element with the highest priority in PQ is %d\n",overFill.get(overFill.size()-1));
            else
                System.out.print("No element overfill in PQ without floyd\n");
            System.out.println();
        }

/*4 Array Heap With Floyd*/
        ArrayList<Double> pqFourArrayHeapwithFloyd = new ArrayList<>();
        FourArrayHeap<Integer> fourArrayHeapwithFloyd = new FourArrayHeap<>(maxLength,Comparator.comparing(Integer::intValue),true,true);
        benchMarkTimer = new Benchmark_Timer<>("BenchMark for 4-array Heap with floyd",(ipt)->{
            overFill.clear();while(!fourArrayHeapwithFloyd.isEmpty()) {
                fourArrayHeapwithFloyd.take();
            }
            return ipt;
        },(ipt)->{
            int maxInput=Input_data.getMaxInput(),maxOutput=Input_data.getMaxOutput(),maxM=Input_data.getM();
            for(int i = 1; i <=maxInput ; i++){
                if(fourArrayHeapwithFloyd.size() == maxM) {
                    overFill.add(fourArrayHeapwithFloyd.take());
                }
                fourArrayHeapwithFloyd.give(random.nextInt());
            }
            for(int i = 1; i <= maxOutput; i++){
                if(!fourArrayHeapwithFloyd.isEmpty())
                    fourArrayHeapwithFloyd.take();
            }
        },null);
        System.out.println("===============================");
        System.out.println("Running 4 Array Heap with floyd");
        for(int i = 0; i < input_Array.length; i++){
            double total_time = benchMarkTimer.run(new Input_data(input_Array[i],input_Array[i]/4+95,input_Array[i]/4),20);
            System.out.printf("M=%d\nInsert=%d\nRemove=%d\nAverage time (msc): %.3f\n",input_Array[i]/4+95,input_Array[i],input_Array[i]/4,total_time);
            pqFourArrayHeapwithFloyd.add(log((int)(total_time*1e6))/log(2));
            overFill.sort(Comparator.comparing(Integer::intValue));
            System.out.println("Total Overfill size = "+overFill.size());
            if(!overFill.isEmpty())
                System.out.printf("The element overfilled with the highest priority in 4 Array Heap is %d\n",overFill.get(overFill.size()-1));
            else
                System.out.print("No element overfill in 4 Array Heap with floyd\n");
            System.out.println();
        }
/*4 Array Heap Without Floyd*/
        ArrayList<Double> pqFourArrayHeapwithoutFloyd = new ArrayList<>();
        FourArrayHeap<Integer> fourArrayHeapwithoutFloyd = new FourArrayHeap<>(maxLength,Comparator.comparing(Integer::intValue),false,true);
        benchMarkTimer = new Benchmark_Timer<>("BenchMark for 4-array Heap without floyd",(ipt)->{
            overFill.clear();while(!fourArrayHeapwithoutFloyd.isEmpty()) {
                fourArrayHeapwithoutFloyd.take();
            }
            return ipt;
        },(ipt)->{
            int maxInput=Input_data.getMaxInput(),maxOutput=Input_data.getMaxOutput(),maxM=Input_data.getM();
            for(int i = 1; i <=maxInput ; i++){
                if(fourArrayHeapwithoutFloyd.size() == maxM) {
                    overFill.add(fourArrayHeapwithoutFloyd.take());
                }
                fourArrayHeapwithoutFloyd.give(random.nextInt());
            }
            for(int i = 1; i <= maxOutput; i++){
                if(!fourArrayHeapwithoutFloyd.isEmpty())
                    fourArrayHeapwithoutFloyd.take();
            }
        },null);
        System.out.println("===============================");
        System.out.println("Running 4 Array Heap without floyd");
        for(int i = 0; i < input_Array.length; i++){
            double total_time = benchMarkTimer.run(new Input_data(input_Array[i],input_Array[i]/4+95,input_Array[i]/4),20);
            System.out.printf("M=%d\nInsert=%d\nRemove=%d\nAverage time (msc): %.3f\n",input_Array[i]/4+95,input_Array[i],input_Array[i]/4,total_time);
            pqFourArrayHeapwithoutFloyd.add(log((int)(total_time*1e6))/log(2));
            overFill.sort(Comparator.comparing(Integer::intValue));
            System.out.println("Total Overfill size = "+overFill.size());
            if(!overFill.isEmpty())
                System.out.printf("The element overfilled with the highest priority in 4 Array Heap is %d\n",overFill.get(overFill.size()-1));
            else
                System.out.print("No element overfill in 4 Array Heap without floyd\n");
            System.out.println();
        }

/*Print result to CSV*/
        writer.println("lg(n),"+"PQ with Floyd lg(Nano Seconds) nano,"+"PQ without Floyd lg(Nano Seconds),"+"4-array Heap with Floyd lg(Nano Seconds),"+"" +
                "4-array Heap without Floyd lg(Nano Seconds)");
        for(int i = 0 ;i<input_Array.length; i++){
            writer.printf("%.1f,%.1f,%.1f,%.1f,%.1f\n",pqn.get(i),pqFloyd.get(i),pqNoFloydFloyd.get(i),pqFourArrayHeapwithFloyd.get(i),pqFourArrayHeapwithoutFloyd.get(i));
        }

        writer.close();
    }

}