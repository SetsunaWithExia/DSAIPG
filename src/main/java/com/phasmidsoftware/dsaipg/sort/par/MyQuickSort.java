package com.phasmidsoftware.dsaipg.sort.par;

import java.util.*;

public class MyQuickSort {
    public static void main(String[] args) {
        int arrayLength= 10000000;
        Random random = new Random();
        int[] array = new int[arrayLength];
        Collection<Long> timeList = new ArrayList<>();

        long time;
        long startTime = System.currentTimeMillis();
        for (int t = 0; t < 10; t++) {
            for (int i = 0; i < array.length; i++) array[i] = random.nextInt(2147483647);
            Arrays.sort(array);
        }
        long endTime = System.currentTimeMillis();
        time = (endTime - startTime);
        timeList.add(time);
        System.out.printf("QuickSort Average Time: %dms\n",time/10);
//            System.out.println("cutoff:" + (ParSort.cutoff/arrayLength*100) + "\t\t10times Time:" + time + "ms");

    }
}
