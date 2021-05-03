package com.mobiquity;

import java.util.Arrays;
import java.util.PriorityQueue;

public class KnapSack {
    private static int size;
    private static float capacity;

    // Function to calculate upper bound (includes fractional part of the items)
    static float upperBound(float tv, float tw, int idx, Item arr[]) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].weight <= capacity) {
                weight += arr[i].weight;
                value -= arr[i].value;
            } else {
                value -= (float)(capacity - weight) / arr[i].weight * arr[i].value;
                break;
            }
        }
        return value;
    }

    // Calculate lower bound (doesn't include fractional part of items)
    static float lowerBound(float tv, float tw, int idx, Item arr[]) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].weight <= capacity) {
                weight += arr[i].weight;
                value -= arr[i].value;
            } else {
                break;
            }
        }
        return value;
    }

    static void assign(Node a, float ub, float lb, int level, boolean flag, float tv, float tw) {
        a.ub = ub;
        a.lb = lb;
        a.level = level;
        a.flag = flag;
        a.tv = tv;
        a.tw = tw;
    }

    public static void solve(Item arr[]) {
        // Sort the items based on the
        // profit/weight ratio
        Arrays.sort(arr, (a, b) -> {
            boolean temp = (float)a.value / a.weight > (float)b.value / b.weight;
            return temp ? -1 : 1;
        });

        Node current, left, right;
        current = new Node();
        left = new Node();
        right = new Node();

        float minLB = 0, finalLB = Integer.MAX_VALUE;

        current.tv = current.tw = current.ub = current.lb = 0;
        current.level = 0;
        current.flag = false;

        // Priority queue to store elements based on lower bounds
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> {
            boolean temp = a.lb > b.lb || a.tw < b.tw;
            return temp ? 1 : -1;
        });

        // Insert a dummy node
        pq.add(current);

        // curr_path -> Boolean array to store
        // at every index if the element is
        // included or not

        // final_path -> Boolean array to store
        // the result of selection array when
        // it reached the last level
        boolean currPath[] = new boolean[size];
        boolean finalPath[] = new boolean[size];

        while (!pq.isEmpty()) {
            current = pq.poll();
            if (current.ub > minLB || current.ub >= finalLB) {
                // if the current node's best case
                // value is not optimal than minLB,
                // then there is no reason to
                // explore that node. Including
                // finalLB eliminates all those
                // paths whose best values is equal
                // to the finalLB
                continue;
            }

            System.out.println();
            System.out.println("Current now=" + current);

            if (current.level != 0)
                currPath[current.level - 1] = current.flag;

            if (current.level == size) {
                if (current.lb < finalLB) {
                    // Reached last level
                    for (int i = 0; i < size; i++)
                        finalPath[arr[i].idx] = currPath[i];
                    finalLB = current.lb;
                }
                continue;
            }

            int level = current.level;

            // right node -> Exludes current item
            // Hence, cp, cw will obtain the value
            // of that of parent
            assign(right, upperBound(current.tv, current.tw, level + 1, arr),
                    lowerBound(current.tv, current.tw, level + 1, arr),
                    level + 1, false, current.tv, current.tw);


            if (current.tw + arr[current.level].weight <= capacity) {

                System.out.println("in...");

                // left node -> includes current item
                // c and lb should be calculated
                // including the current item.
                left.ub = upperBound(current.tv - arr[level].value, current.tw + arr[level].weight, level + 1, arr);
                left.lb = lowerBound(current.tv - arr[level].value, current.tw + arr[level].weight, level + 1, arr);
                assign(left, left.ub, left.lb, level + 1, true, current.tv - arr[level].value, current.tw + arr[level].weight);
            }
            // If the left node cannot
            // be inserted
            else {
                // Stop the left node from
                // getting added to the
                // priority queue
                left.ub = left.lb = 1;
            }

//            System.out.println("current.tw=" + current.tw + ", arr[current.level].weight=" + arr[current.level].weight + ", sum=" + (current.tw + arr[current.level].weight));


            // Update minLB
            minLB = Math.min(minLB, left.lb);
            minLB = Math.min(minLB, right.lb);

            if (minLB >= left.ub)
                pq.add(new Node(left));
            if (minLB >= right.ub)
                pq.add(new Node(right));

            System.out.println("minLB=" + minLB + ", \nLeft=" + left.lb + ", \nRight=" + right.lb);

            System.out.println("Queue now=" + pq);
        }
        System.out.println("Items taken into the knapsack are");
        for (int i = 0; i < size; i++) {
            if (finalPath[i])
                System.out.print("1 ");
            else
                System.out.print("0 ");
        }

        System.out.println("\nMaximum profit" + " is " + (-finalLB));
    }

    // Driver code
    public static void main(String args[]) {
//        size = 4;
//        capacity = 15;


//        Item arr[] = new Item[size];
//        arr[0] = new Item(10, 2, 0);
//        arr[1] = new Item(10, 4, 1);
//        arr[2] = new Item(12, 6, 2);
//        arr[3] = new Item(18, 9, 3);
//
//        solve(arr);

        size = 1;
        capacity = 8;

//        int capacity = 81;
//        int[] V = {45, 98, 3, 76, 9, 48};
//        double[] W = {53.38, 88.62, 78.48, 72.30, 30.18, 46.34};
//        int[] W = {1, 2};

//        capacity = 75;
//        V = new int[]{29, 74, 16, 55, 52, 75, 74, 35, 78};
//        W = new double[]{85.31, 14.55, 3.98, 26.24, 63.69, 76.25, 60.02, 93.18, 89.95};

//        capacity = 56;
//        V = new int[]{13, 40, 10, 16, 36, 79, 45, 79, 64};
//        W = new double[]{90.72, 33.80, 43.15, 37.97, 46.81, 48.77, 81.80, 19.36, 6.76};

        Item arr[] = new Item[size];
        arr[0] = new Item(45, 53.38f, 0);


//        Item arr[] = new Item[size];
//        arr[0] = new Item(45, 53.38f, 0);
//        arr[1] = new Item(98, 88.62f, 1);
//        arr[2] = new Item(3, 78.48f, 2);
//        arr[3] = new Item(76, 72.30f, 3);
//        arr[4] = new Item(9, 30.18f, 4);
//        arr[5] = new Item(48, 46.34f, 5);

//        Item arr[] = new Item[size];
//        arr[0] = new Item(29, 85.31f, 0);
//        arr[1] = new Item(74, 14.55f, 1);
//        arr[2] = new Item(16, 3.98f, 2);
//        arr[3] = new Item(55, 26.24f, 3);
//        arr[4] = new Item(52, 63.69f, 4);
//        arr[5] = new Item(75, 76.25f, 5);
//        arr[6] = new Item(74, 60.02f, 6);
//        arr[7] = new Item(35, 93.18f, 7);
//        arr[8] = new Item(78, 89.95f, 8);

//        Item arr[] = new Item[size];
//        arr[0] = new Item(13, 90.72f, 0);
//        arr[1] = new Item(40, 33.80f, 1);
//        arr[2] = new Item(10, 43.15f, 2);
//        arr[3] = new Item(16, 37.97f, 3);
//        arr[4] = new Item(36, 46.81f, 4);
//        arr[5] = new Item(79, 48.77f, 5);
//        arr[6] = new Item(45, 81.80f, 6);
//        arr[7] = new Item(79, 19.36f, 7);
//        arr[8] = new Item(64, 6.76f, 8);

        solve(arr);
    }

}
