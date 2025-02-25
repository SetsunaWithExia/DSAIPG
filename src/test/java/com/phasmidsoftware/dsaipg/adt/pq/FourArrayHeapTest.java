package com.phasmidsoftware.dsaipg.adt.pq;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class FourArrayHeapTest {
    @Test
    public void MaxHeapwithFloydSortTest() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        Integer input[]=new Integer[4095];
        FourArrayHeap<Integer> fah = new FourArrayHeap<>(4095, Comparator.comparing(Integer::intValue),true,true);

        for(int i=0;i<input.length;i++){
            input[i]=rand.nextInt();
            fah.give(input[i]);
        }

        Arrays.sort(input);

        for(int i=input.length-1;i>=0;i--)
            assertEquals(input[i],fah.take());

    }
    @Test
    public void MaxHeapwithoutFloydSortTest() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        Integer input[]=new Integer[4095];
        FourArrayHeap<Integer> fah = new FourArrayHeap<>(4095, Comparator.comparing(Integer::intValue),false,true);
        for(int i=0;i<input.length;i++){
            input[i]=rand.nextInt();
            fah.give(input[i]);
        }

        Arrays.sort(input);

        for(int i=input.length-1;i>=0;i--)
            assertEquals(input[i],fah.take());

    }
    @Test
    public void MinHeapwithFloydSortTest() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());

        Integer input[]=new Integer[4095];
        FourArrayHeap<Integer> fah = new FourArrayHeap<>(4095, Comparator.comparing(Integer::intValue),true,false);

        for(int i=0;i<input.length;i++){
            input[i]=rand.nextInt();
            fah.give(input[i]);
        }

        Arrays.sort(input);
        for(int i=0;i<input.length;i++)
            assertEquals(input[i],fah.take());

    }
    @Test
    public void MinHeapwithoutFloydSortTest() {
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        Integer input[]=new Integer[4095];

        FourArrayHeap<Integer> fah = new FourArrayHeap<>(4095, Comparator.comparing(Integer::intValue),false,false);

        for(int i=0;i<input.length;i++){
            input[i]=rand.nextInt();
            fah.give(input[i]);
        }

        Arrays.sort(input);
        for(int i=0;i<input.length;i++)
            assertEquals(input[i],fah.take());

    }
}
