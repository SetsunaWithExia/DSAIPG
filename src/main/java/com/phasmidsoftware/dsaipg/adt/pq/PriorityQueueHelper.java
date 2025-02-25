package com.phasmidsoftware.dsaipg.adt.pq;

import com.phasmidsoftware.dsaipg.util.Benchmark_Timer;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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
