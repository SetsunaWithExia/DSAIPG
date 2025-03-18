package com.phasmidsoftware.dsaipg.sort.par;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class MyMergeSort {

    public static void sort(int[] array, int from, int to) {
        if (from >= to) return;
        int mid = from + (to - from) / 2 ;
  //      System.out.println(from + " " + to + " " + mid);
        sort(array, from, mid);
        sort(array, mid+1, to);
        int i = from,j = mid+1;
        int temp[] = new int[to-from+1];
        int idx=0;
        while(i<=mid && j<=to) {
            if(array[i] < array[j]) temp[idx++] = array[i++];
            else temp[idx++] = array[j++];
        }
        while(i<=mid) temp[idx++] = array[i++];
        while(j<=to) temp[idx++] = array[j++];
        for(int k = 0; k<to-from+1; k++) array[from+k] = temp[k];
    }
    public static void main(String args[]) {
        int arrayLength= 10000000;
        Random random = new Random();
        int[] array = new int[arrayLength];


        long time;
        long startTime = System.currentTimeMillis();
        for (int t = 0; t < 10; t++) {
            for (int i = 0; i < array.length; i++) array[i] = random.nextInt(2147483647);
            MyMergeSort.sort(array,0,array.length-1);
        }
        long endTime = System.currentTimeMillis();
        time = (endTime - startTime);
        System.out.printf("MergeSort Average Time: %dms\n",time/10);
//            System.out.println("cutoff:" + (ParSort.cutoff/arrayLength*100) + "\t\t10times Time:" + time + "ms");

    }

}
