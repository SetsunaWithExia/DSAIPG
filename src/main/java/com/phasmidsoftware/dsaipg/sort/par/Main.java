package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    /**
     * The main method serves as the entry point for the program. It processes command-line arguments,
     * configures sorting parameters, performs parallel sorting on a random array, measures execution time,
     * and writes the performance results to a CSV file.
     *
     * @param args command-line arguments used for configuring program execution.
     */
    public static void main(String[] args) {
        //processArgs(args);

        int arrayLength = (int) 1E6;
//        if(configuration.containsKey("-N")){
//             arrayLength = configuration.get("-N");
//        }
//        if (configuration.containsKey("-P")){
//            ParSort.parallelism = configuration.get("-P");
//        }
//            System.out.println("ArrayLength: " + arrayLength);
//            System.out.println("Degree of parallelism: " + ParSort.parallelism);

        Random random = new Random();
        int[] array = new int[arrayLength];
        long timeList[][] = new long[10001][10001];
        int MaxcutoffPercentage=25;
        int MaxparallismDegree=11;
        System.out.println("ArrayLength: " + arrayLength);
        for(int parallismDegree =1; parallismDegree <= MaxparallismDegree; parallismDegree++){
            ParSort.parallelism=parallismDegree;
            System.out.println("\nDegree of parallelism: " + ParSort.parallelism);
            for(int j = 1; j <= MaxcutoffPercentage; j++){
                ParSort.cutoff = (arrayLength/100*j);
                System.out.println("ParSort cutoff: " + ParSort.cutoff);
                //ParSort.cutoff = arrayLength/100 * (j);
                // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                long time;
                long startTime = System.currentTimeMillis();
                for (int t = 0; t < 10; t++) {
                    for (int i = 0; i < array.length; i++) array[i] = random.nextInt(2147483647);
                    ParSort.sort(array, 0, array.length);
                    //Arrays.sort(array);
                }
                long endTime = System.currentTimeMillis();
                time = (endTime - startTime);
                timeList[parallismDegree][j]=time/10;
                double percentage = (double) ParSort.cutoff / array.length * 100;
                System.out.printf("Cutoff Percentage: %.2f%%\t\tAverage Time: %dms\n", percentage,time/10);
//            System.out.println("cutoff:" + (ParSort.cutoff/arrayLength*100) + "\t\t10times Time:" + time + "ms");
            }
        }
        try{
            FileOutputStream fis
//                            = new FileOutputStream("D:\\NortheasternUniversity\\INFO 6205 Program Structure and Algorithms\\Assignment5\\arrayLength = "+arrayLength+" parallelism = "+ParSort.parallelism+"CutoffPercentageGap=10"+"new.csv");
                    = new FileOutputStream("D:\\NortheasternUniversity\\INFO 6205 Program Structure and Algorithms\\Assignment5\\result1e6.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            String str;

            for(int i=15;i<=MaxparallismDegree;i++){
                str=String.valueOf(i);
                for(int k=1;k<=MaxcutoffPercentage;k++){
                    str+=","+timeList[i][k];
                }
                bw.write(str+"\n");
                bw.flush();
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Processes the command-line arguments by iterating through the provided array of arguments.
     * Each argument is checked for specific prefixes (e.g., "-" symbols), and arguments with such prefixes
     * are further handled using {@link #processArg(String[])}. The method continuously modifies the arguments array
     * by removing processed elements.
     *
     * @param args an array of strings representing command-line arguments to be processed.
     *             Each argument can include options, flags, or parameters that configure the program's behavior.
     */
    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    /**
     * Processes a given array of strings, extracting a subset of elements and applying a command
     * processing operation on the first two elements of the input array.
     *
     * @param xs the input array of strings where the first two elements are used for command processing
     *           and the remaining elements are returned as the result.
     * @return an array of strings containing the elements of the input array excluding the first two.
     */
    private static String[] processArg(String[] xs) {
        String[] result = new String[xs.length-2];
        for (int i = 0; i < xs.length; i++)
       //  System.out.println("xs["+i+"]: " + xs[i]);
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    /**
     * Processes a command and performs an associated action based on the given inputs.
     *
     * @param x the command identifier, which specifies the operation to perform.
     *          Supported values: "N" for setting configuration and "P" for retrieving
     *          the common pool parallelism level.
     * @param y the value associated with the command. For "N", this represents the
     *          configuration value to be set.
     */
    private static void processCommand(String x, String y) {
        setConfig(x, Integer.parseInt(y));
    }

    /**
     * Configures a key-value pair in the application's configuration.
     * This method stores the specified key and associated integer value
     * into the configuration map.
     *
     * @param x the key to be stored in the configuration
     * @param i the integer value to be associated with the specified key
     */
    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();
}