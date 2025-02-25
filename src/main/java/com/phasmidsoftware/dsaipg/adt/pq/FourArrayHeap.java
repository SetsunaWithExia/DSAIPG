package com.phasmidsoftware.dsaipg.adt.pq;

import java.util.Comparator;
import java.util.Random;

public class FourArrayHeap<K> {
    private int tail;
    private final K[] binHeap;
    private boolean max;
    private boolean floyd;
    private final Comparator<K> comparator;
    public FourArrayHeap(boolean max, Object[] binHeap, Comparator<K> comparator,int tail,boolean floyd) {
        this.binHeap = (K[]) binHeap;
        this.max=max;
        this.floyd=floyd;
        this.comparator=comparator;
        this.tail=tail;
    }
    boolean unordered(int i,int j) {
        return (comparator.compare(binHeap[i],binHeap[j])>0)^max;
    }
    public FourArrayHeap(int n,Comparator<K> comparator,boolean floyd,boolean max) {
        this(max,new Object[n],comparator,-1,floyd);
    }
    private void swap(int i,int j){
        K temp=binHeap[i];
        binHeap[i]=binHeap[j];
        binHeap[j]=temp;
    }
    // Parent of p : (p-1)/4 equals (p-1)>>2
    // Descendent s1 p*4+1 s2 p*4+2 s3 p*4+3 s4 p*4+4 equals p<<2+i
    public void up(int p){
        while(p>0){
            if(unordered((p-1)>>2,p)){
                swap(p,(p-1)>>2);
                p=(p-1)>>2;
            }else break;
        }
    }
    private int no_floyd_down(int x){

        int p=x,d;
        while((p<<2)+1<=tail){
            d=(p<<2)+1;
            int maxx=0;
     //       System.out.printf("Parent = %d Child = %d\n",binHeap[p],binHeap[d]);
            for(int i=1;i<=3;i++){  //find the element in four children with the highest priority
                if(d+i>tail)break;
                if(unordered(d+maxx,d+i)) maxx=i;
            }
            d+=maxx;
   //         System.out.printf("swap(%d,%d)\n",binHeap[p],binHeap[d]);
            if(unordered(p,d)){
                swap(p,d);
                p=d;
            }else break;
        }
        return p;
    }
    private int floyd_down(int x){
        int p=x,d;
        while((p<<2)+1<=tail){
            d=(p<<2)+1;
            int maxx=0;
      //      System.out.printf("Parent = %d Desc= %d\n",p,d);
            for(int i=1;i<=3;i++){  //find the element in four children with the highest priority
                if(d+i>tail)break;
                if(unordered(d+maxx,d+i)) maxx=i;
            }

            d+=maxx;
  //          System.out.printf("Max Desc= %d\n",d);
            swap(p,d);
            p=d;
        }
        return p;
    }
    public int size(){
        return tail + 1;
    }
    public boolean isEmpty(){
        return tail == -1;
    }
    public boolean give(K element){
        if(binHeap.length == this.size())tail--;
        binHeap[++tail]=element;
        up(tail);
        return true;
    }
    public K peek(int k){
        return binHeap[k];
    }
    public K take(){
        if(tail==-1)return null;
//        System.out.printf("take () %d \n",(Integer)binHeap[0]);
        K ret = binHeap[0];
        binHeap[0]=binHeap[tail--];
        if(floyd) up(floyd_down(0));
        else no_floyd_down(0);
        return ret;
    }
    public static void main(String[] args) {
        Random random = new Random();
        random.setSeed(1);
        FourArrayHeap<Integer> fourArrayHeap_with_floyd = new FourArrayHeap<>(4095,Comparator.comparing(Integer::intValue),true,true);
        for(int i=0;i<=9;i++){
            fourArrayHeap_with_floyd.give(random.nextInt(0,100));
            System.out.printf("bi[%d] = %d\n",i,fourArrayHeap_with_floyd.peek(i));
        }
//        fourArrayHeap_with_floyd.take();
//        for(int i=0;i<=8;i++){
//            System.out.printf("bi[%d] = %d\n",i,fourArrayHeap_with_floyd.peek(i));
//        }
        while(!fourArrayHeap_with_floyd.isEmpty()){
            int num = fourArrayHeap_with_floyd.take();
            System.out.printf("%d ",num);
        }
    }
}
