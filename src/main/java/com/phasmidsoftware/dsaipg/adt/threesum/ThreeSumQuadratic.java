/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.adt.threesum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of ThreeSum which follows the approach of dividing the solution-space into
 * N sub-spaces where each sub-space corresponds to a fixed value for the middle index of the three values.
 * Each sub-space is then solved by expanding the scope of the other two indices outwards from the starting point.
 * Since each sub-space can be solved in O(N) time, the overall complexity is O(N^2).
 * <p>
 * NOTE: The array provided in the constructor MUST be ordered.
 */
public class ThreeSumQuadratic implements ThreeSum {
    /**
     * Construct a ThreeSumQuadratic on a.
     *
     * @param a a sorted array.
     */
    public ThreeSumQuadratic(int[] a) {
        this.a = a;
        length = a.length;
    }

    /**
     * Retrieves an array of unique Triples. Each Triple represents a unique combination of three integers from
     * the source array that sum to zero.
     *
     * @return an array of distinct Triples, sorted in natural order, where each Triple satisfies the condition that
     * the sum of its three integers is zero.
     */
    public Triple[] getTriples() {
        List<Triple> triples = new ArrayList<>();
        for (int i = 0; i < length; i++) triples.addAll(getTriples(i));
        Collections.sort(triples);
        return triples.stream().distinct().toArray(Triple[]::new);
    }

    /**
     * Get a list of Triples such that the middle index is the given value j.
     *
     * @param j the index of the middle value.
     * @return a Triple such that
     */
     List<Triple> getTriples(int j) {
         List<Triple> triples = new ArrayList<>();
         int i = j - 1, k = j + 1;
         int temp = - a[j];
         while(i >= 0 && k < length){

             if(a[i] + a[k] == temp){
                 triples.add(new Triple(a[i],a[j],a[k]));
                 i--;k++;
             }

             int step;
             if(i>=0 && k<length && a[i] + a[k] > temp) {
                 for (step = (int) (Math.log10(length) / 0.3) + 1; step >= 0; step--){//Doubling find a smaller number on the left side
                     if (i - (1 << step) < 0) continue;
                     if (a[i - (1 << step)] + a[k] >= temp) {
                         i = i - (1 << step);
                     }
                 }
             }
             if(i>=0 && k<length && a[i] + a[k] <temp) {
                 for (step = (int) (Math.log10(length) / 0.3) + 1; step >= 0; step--){//Doubling find a larger number on the right side
                     if (k + (1 << step) >= length) continue;
                     if (a[k + (1 << step)] + a[i] <= temp) {
                         k = k + (1 << step);
                     }
                 }
             }
             if(i>=0 && k<length && a[i] + a[k] >temp)i--;
             if(i>=0 && k<length && a[i] + a[k] <temp)k++;

         }
        return triples;
    }

    private final int[] a;
    private final int length;
}