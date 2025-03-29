package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;

public class PriorityQueueHelper<T> extends PriorityQueue<T>{
    private final int maxInput;
    private int maxOutPut;
    public int getMaxInput() {
        return maxInput;
    }

    public int getMaxOutPut() {
        return maxOutPut;
    }


        PriorityQueueHelper(int n,boolean max, Comparator<T> comparator,boolean floyd,int maxInput,int maxOutPut) {
            super(n,max, comparator,floyd);
            this.maxInput = maxInput;
            this.maxOutPut = maxOutPut;
        }


}
