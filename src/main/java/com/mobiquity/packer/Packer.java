package com.mobiquity.packer;

import com.mobiquity.exception.APIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Packer {
    public static void main(String[] args) throws APIException {
        int capacity = 81;
        int[] V = {45, 98, 3, 76, 9, 48};
//        double[] W = {53.38, 88.62, 78.48, 72.30, 30.18, 46.34};
        int[] W = {1, 2};

//        capacity = 75;
//        V = new int[]{29, 74, 16, 55, 52, 75, 74, 35, 78};
//        W = new double[]{85.31, 14.55, 3.98, 26.24, 63.69, 76.25, 60.02, 93.18, 89.95};

//        capacity = 56;
//        V = new int[]{13, 40, 10, 16, 36, 79, 45, 79, 64};
//        W = new double[]{90.72, 33.80, 43.15, 37.97, 46.81, 48.77, 81.80, 19.36, 6.76};

        capacity = 10;
        V = new int[] {10, 40, 30, 60};
        W = new int[] {5, 4, 6, 3};
        knapsackA(capacity, W, V);
        // System.out.println(pack(Packer.class.getResource("/testcases.txt").getPath()));
    }

    /**
     * @param capacity - The maximum capacity of the knapsack
     * @param W - The weights of the items
     * @param V - The values of the items
     * @return The maximum achievable profit of selecting a subset of the elements such that the
     *     capacity of the knapsack is not exceeded
     */
    public static void knapsack(int capacity, double[] W, int[] V) {
        if (W == null || V == null || W.length != V.length || capacity < 0)
            throw new IllegalArgumentException("Invalid input");

        final int N = W.length;

        // Initialize a table where individual rows represent items
        // and columns represent the weight of the knapsack
        int[][] T = new int[N + 1][capacity + 1];

        // Initialize P array to keep tabs with items picked as we iterate
        boolean[][] P = new boolean[N + 1][capacity + 1];

        for (int i = 0; i < N; i++) {
            for (int w = 0; w < capacity; w++) {
                // initialize your counters
                if(i == 0 || w == 0) {
                    T[i][w] = 0;
                    P[i][w] = false;
                } else if (W[i] <= w && T[i-1][w] < (V[i] + T[i-1][w - (int) Math.round(W[i])])) {
//                } else if (W[i] <= w && T[i-1][w] < (V[i] + T[i-1][w - W[i]])) {
                    // track maximum profit
                    T[i][w] = V[i] + T[i-1][w - (int) Math.round(W[i])];
                    // keep track of items picked and not picked
                    P[i][w] = true;

                    System.out.println(i + "-" + w + " picked.");
                } else {
                    // track maximum profit
                    T[i][w] = T[i-1][w];
                    // keep track of items picked and not picked
                    P[i][w] = false;
                }
            }
        }

//        for(int i = 0; i < T.length; i++) {
//            for(int j = 0; j < T[i].length; i++) {
//                System.out.print(T[i] + ", ");
//            }
//            System.out.println();
//        }

        /**
         * Display or queue items picked from the table above
         */
        int currentCapacity = capacity;

        System.out.println("Items picked with maximum profit are:");

        for (int i = N; i > 0; i--) {
            if (P[i-1][currentCapacity-1]) {
                System.out.println("Item " + i);
                currentCapacity -= W[i-1];
            }
        }
        System.out.println();
    }

    public static void knapsackA(int capacity, int[] W, int[] V) {
        if (W == null || V == null || W.length != V.length || capacity < 0)
            throw new IllegalArgumentException("Invalid input");

        final int N = W.length + 1;
        capacity += 1;

        // Initialize a table where individual rows represent items
        // and columns represent the weight of the knapsack
        int[][] T = new int[N][capacity];

        // Initialize P array to keep tabs with items picked as we iterate
        boolean[][] P = new boolean[N + 1][capacity + 1];

        for (int i = 0; i <= N; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (w == 0 || i == 0) {
                    T[i][w] = 0;
                } else if (W[i] <= w && T[i-1][w] < (V[i] + T[i-1][w - (int) Math.round(W[i])])) {
                    // track maximum profit
                    T[i][w] = V[i] + T[i-1][w - (int) Math.round(W[i])];
                    // keep track of items picked and not picked
                    P[i][w] = true;
                } else {
                    // track maximum profit
                    T[i][w] = T[i-1][w];
                    // keep track of items picked and not picked
                    P[i][w] = false;
                }
            }
        }

//        for(int i = 0; i < T.length; i++) {
//            for(int j = 0; j < T[i].length; i++) {
//                System.out.print(T[i] + ", ");
//            }
//            System.out.println();
//        }

        /**
         * Display or queue items picked from the table above
         */
        int currentCapacity = capacity;

        System.out.println("Items picked with maximum profit are:");

        for (int i = N; i > 0; i--) {
            if (P[i][currentCapacity]) {
                System.out.println("Item " + i);
                currentCapacity -= W[i];
            }
        }
        System.out.println();
    }

    public static void knapsack2(int capacity, double[] W, int[] V) {
        if (W == null || V == null || W.length != V.length || capacity < 0)
            throw new IllegalArgumentException("Invalid input");

        final int N = W.length - 1;

        // Initialize a table where individual rows represent items
        // and columns represent the weight of the knapsack
        int[][] T = new int[N + 1][capacity + 1];

        // Initialize P array to keep tabs with items picked as we iterate
        // boolean[][] P = new boolean[N + 1][capacity + 1];

        for (int i = 0; i <= N; i++) {
            for (int w = 0; w <= capacity; w++) {
                // initialize your counters
                if(i == 0 || w == 0) {
                    T[i][w] = 0;
                    //P[i][w] = false;
                } else if (W[i] <= w) {
                    // track maximum profit
                    T[i][w] = Math.max( T[i-1][w], V[i] + T[i-1][w - (int) Math.round(W[i])]);
                    // keep track of items picked and not picked
                    //P[i][w] = true;

                    // System.out.println(i + "-" + w + " picked.");
                } else {
                    // track maximum profit
                    T[i][w] = T[i-1][w];
                    // keep track of items picked and not picked
                    // P[i][w] = false;
                }
            }
        }

        for(int i = 0; i < T.length; i++) {
            for(int j = 0; j < T[i].length; j++) {
                System.out.print(T[i][j] + ", ");
            }
            System.out.println();
        }

        /**
         * Display or queue items picked from the table above
         */
        int currentCapacity = capacity;

        System.out.println("Items picked with maximum profit are:");

//        for (int i = N; i > 0; i--) {
//            if (P[i-1][currentCapacity-1]) {
//                System.out.println("Item " + i);
//                currentCapacity -= W[i-1];
//            }
//        }
        System.out.println();
    }

    public static String pack(String filePath) throws APIException {
        String[] content = null;
        Path path = Paths.get(filePath);

        try {
            content = Files.readString(path).split("\\n"); // Default = StandardCharsets.UTF_8
            Files.readString(path).lines()
                    .map(line -> line);
        } catch (IOException e) {
            throw new APIException(e.getMessage());
        }

        System.out.println("...returning...");
        return content[1];
    }

    public static int knapsack3(int capacity, int[] W, int[] V) {

        if (W == null || V == null || W.length != V.length || capacity < 0)
            throw new IllegalArgumentException("Invalid input");

        final int N = W.length;

        // Initialize a table where individual rows represent items
        // and columns represent the weight of the knapsack
        int[][] DP = new int[N + 1][capacity + 1];

        for (int i = 1; i <= N; i++) {

            // Get the value and weight of the item
            int w = W[i - 1], v = V[i - 1];

            for (int sz = 1; sz <= capacity; sz++) {

                // Consider not picking this element
                DP[i][sz] = DP[i - 1][sz];

                // Consider including the current element and
                // see if this would be more profitable
                if (sz >= w && DP[i - 1][sz - w] + v > DP[i][sz]) DP[i][sz] = DP[i - 1][sz - w] + v;
            }
        }

        int sz = capacity;
        List<Integer> itemsSelected = new ArrayList<>();

        // Using the information inside the table we can backtrack and determine
        // which items were selected during the dynamic programming phase. The idea
        // is that if DP[i][sz] != DP[i-1][sz] then the item was selected
        for (int i = N; i > 0; i--) {
            if (DP[i][sz] != DP[i - 1][sz]) {
                int itemIndex = i - 1;
                itemsSelected.add(itemIndex);
                sz -= W[itemIndex];
            }
        }

         java.util.Collections.reverse(itemsSelected);

        itemsSelected.forEach(x -> System.out.println(x + ", "));

        // Return the items that were selected
//         return itemsSelected;

        // Return the maximum profit
        return DP[N][capacity];
    }
}
