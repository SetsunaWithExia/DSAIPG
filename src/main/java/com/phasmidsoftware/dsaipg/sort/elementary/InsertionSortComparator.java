/*
 * Copyright (c) 2024. Robin Hillyard
 */
package com.phasmidsoftware.dsaipg.sort.elementary;

import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.sort.helper.HelperFactory;
import com.phasmidsoftware.dsaipg.sort.generic.Sort;
import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.util.config.Config;
import com.phasmidsoftware.dsaipg.util.config.Config_Benchmark;

import java.io.File;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import static com.phasmidsoftware.dsaipg.sort.helper.InstrumentedComparatorHelper.getRunsConfig;
import static com.phasmidsoftware.dsaipg.util.config.Config_Benchmark.setupConfigFixes;

/**
 * A class for performing insertion sort using a comparator, extending functionality from SortWithHelper.
 * This includes methods for initialization and invocation of insertion sort,
 * along with specific utilities like counting inversions.
 *
 * @param <X> the type of elements to be sorted, which can be compared using a provided comparator.
 */
public class InsertionSortComparator<X> extends SortWithHelper<X> {
    /**
     * Constructor for InsertionSortComparator, which initializes the comparator with the provided helper.
     *
     * @param helper the Helper object to be used for managing the sorting process.
     */
    public InsertionSortComparator(Helper<X> helper) {
        super(helper);
    }

    /**
     * Constructor for any subclasses to use.
     *
     * @param description the description.
     * @param comparator  the comparator to use.
     * @param N           the number of elements expected.
     * @param nRuns       the number of runs to be expected (this is only significant when instrumenting).
     * @param config      the configuration.
     */
    protected InsertionSortComparator(String description, Comparator<X> comparator, int N, int nRuns, Config config) {
        super(description, comparator, N, nRuns, config);
    }

    /**
     * Constructor for InsertionSort
     *
     * @param N      the number elements we expect to sort.
     * @param nRuns  the number of runs to be expected (this is only significant when instrumenting).
     * @param config the configuration.
     */
    public InsertionSortComparator(Comparator<X> comparator, int N, int nRuns, Config config) {
        this(DESCRIPTION, comparator, N, nRuns, config);
    }

    /**
     * Sort the sub-array xs:from:to using insertion sort.
     *
     * @param xs   sort the array xs from "from" to "to".
     * @param from the index of the first element to sort
     * @param to   the index of the first element not to sort
     */
    public void sort(X[] xs, int from, int to) {
        final Helper<X> helper = getHelper();
        for(int i = from; i < to - 1; i++){
            for(int j = i+1; j> from; j--){

                if(helper.compare(xs[j],xs[j-1])<0){
                    helper.swapStable(xs,j);
                }else break;
            }
        }
    }

    public static final String DESCRIPTION = "Insertion sort";

    /**
     * Sorts the given array in-place using the provided insertion sort comparator.
     *
     * @param <T> the generic type parameter that extends Comparable.
     * @param ts  the array of elements to be sorted, where elements must implement {@code Comparable}.
     *            The method modifies this array directly to produce the sorted order.
     * @throws RuntimeException if an IOException occurs during the sorting process.
     */
    public static <T extends Comparable<T>> void sort(T[] ts) {
        try (InsertionSortComparator<T> sort = new InsertionSortComparator<>(DESCRIPTION, Comparable::compareTo, ts.length, 1, Config.load(InsertionSortComparator.class))) {
            sort.mutatingSort(ts);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a case-insensitive string sorter using an insertion sort comparator.
     *
     * @param n      the expected number of elements to be sorted.
     * @param config the configuration object containing necessary settings.
     * @return a {@code SortWithHelper<String>} instance configured for case-insensitive string sorting.
     */
    public static Sort<String> stringSorterCaseInsensitive(int n, Config config) {
        return new InsertionSortComparator<>(DESCRIPTION, String.CASE_INSENSITIVE_ORDER, n, getRunsConfig(config), config);
    }

    /**
     * This method is designed to count inversions in quadratic time, using insertion sort.
     *
     * @param ts  an array of comparable T elements.
     * @param <T> the underlying type of the elements.
     * @return the number of inversions in ts, which remains unchanged.
     */
    public static <T> long countInversions(T[] ts, Comparator<T> comparator) {
        final Config config = Config_Benchmark.setupConfigFixes();
        try (InsertionSortComparator<T> sorter = new InsertionSortComparator<>(comparator, ts.length, getRunsConfig(config), config)) {
            Helper<T> helper = sorter.getHelper();
            sorter.sort(ts, true);
            return helper.getFixes();
        }
    }
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\NortheasternUniversity\\INFO 6205 Program Structure and Algorithms\\Assignment3\\Benchmark3.csv");
        PrintWriter writer = new PrintWriter(file);
        Integer list[] = new Integer[496000];
        int maxm =(int) 1E7+9;
        Random random = new Random();

        writer.println("n,random,ordered,partially-ordered,reverse-ordered");
        int n[]= {20,40,80,160,320,640,1280,2560,5120,12400,24800,49600};
        // random

        for(int maxn:n){
            System.out.println(maxn);
            int runs=10;
            long t[]= {0,0,0,0};
            Helper<Integer> helper = HelperFactory.createGeneric("Benchmark",Integer::compareTo,maxn,10,setupConfigFixes());
            InsertionSortComparator<Integer> sorter = new InsertionSortComparator<>(helper);
            while(runs--!=0){
                for(int i = 0; i < maxn; i++) list[i] = random.nextInt(maxm);
                double st = System.nanoTime();
                sorter.sort(list,0,maxn);
                double ed = System.nanoTime();
                t[0]+=(long)ed-st;
                // ordered
                st = System.nanoTime();
                sorter.sort(list,0,maxn);
                ed = System.nanoTime();
                t[1]+=(long)ed-st;

                // partially-ordered
                for(int i = 0; i < maxn; i++) list[i] = random.nextInt(maxm);
                for(int i=0;i<maxn*Math.log10(maxn)/0.301/2;i++){
                    int l = random.nextInt(maxn);
                    int r = random.nextInt(maxn);
                    if(l>r){
                        l+=r; r=l-r; l=l-r; //swap l r
                    }
                    if(list[l]>list[r]){
                        list[l]+=list[r]; list[r]=list[l]-list[r];list[l]=list[l]-list[r]; // swap list[l] list[r]
                    }
                }
                st = System.nanoTime();
                sorter.sort(list,0,maxn);
                ed = System.nanoTime();
                t[2]+=(long)ed-st;

                // reverse-ordered
                for(int i=0;i<maxn/2;i++) {
                    int r = maxn-i-1;
                    list[i]+=list[r];
                    list[r]=list[i]-list[r];
                    list[i]=list[i]-list[r];
                }
                st = System.nanoTime();
                sorter.sort(list,0,maxn);
                ed = System.nanoTime();
                t[3]+=(long)ed-st;
            }
            writer.printf("%d",maxn);
            for(long k:t){
                writer.printf(",%d",k/10);
            }
            writer.println();
        }
        writer.close();



    }

}