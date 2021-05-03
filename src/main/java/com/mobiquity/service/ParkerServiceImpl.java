package com.mobiquity.service;

import com.mobiquity.entity.Item;
import com.mobiquity.entity.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class ParkerServiceImpl implements ParkerService {

    private int size;
    private float capacity;

    private ParkerServiceImpl() { }

    private volatile static ParkerServiceImpl singleParkerServiceImpl = new ParkerServiceImpl();

    public static ParkerServiceImpl getInstance() {
        return singleParkerServiceImpl;
    }

    /**
     * Compute possible items using Least Cost Branch and Bound algorithm
     * @param arr - The items array. Each row represent an item, columns consist of the Cost, Weight, Index
     * @return Output string (items’ index numbers, separated by comma)
     * @apiNote Cognitive complexity 30 - 35.
     */
    public String pack(Item[] arr, float capacity) {
        size = arr.length;
        this.capacity = capacity;

        // Sort the items based on the profit/weight ratio
        Arrays.sort(arr, (a, b) -> {
            boolean temp = (float)a.getValue() / a.getWeight() > (float)b.getValue() / b.getWeight();
            return temp ? -1 : 1;
        });

        Node current, left, right;
        current = new Node();
        left = new Node();
        right = new Node();

        float minLB = 0, finalLB = Integer.MAX_VALUE;

        current.reset();

        // Priority queue to store elements based on lower bounds and ensure the least weight is favoured
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> {
            boolean temp = a.getLb() > b.getLb() || a.getTw() < b.getTw();
            return temp ? 1 : -1;
        });

        // Insert a starter node
        pq.add(current);

        boolean currPath[] = new boolean[size]; // indicate element included or not
        boolean finalPath[] = new boolean[size]; // final selection array

        while (!pq.isEmpty()) {
            current = pq.poll();
            if (current.getUb() > minLB || current.getUb() >= finalLB) {
                // kill node (don't process further) if current value is above minimum least bound
                continue;
            }

            // set selected flag
            if (current.getLevel() != 0)
                currPath[current.getLevel() - 1] = current.isFlag();

            if (current.getLevel() == size) {
                if (current.getLb() < finalLB) {
                    for (int i = 0; i < size; i++)
                        finalPath[arr[i].getIdx()] = currPath[i];
                    finalLB = current.getLb();
                }
                continue;
            }

            // System.out.println("\nMaximum cost" + " is " + (-finalLB));

            int level = current.getLevel();

            // set temporary value obtained from that of parent
            right.assign(upperBound(current.getTv(), current.getTw(), level + 1, arr),
                    lowerBound(current.getTv(), current.getTw(), level + 1, arr),
                    level + 1, false, current.getTv(), current.getTw());


            if (current.getTw() + arr[current.getLevel()].getWeight() <= capacity) {
                // set computed left node if its within the capacity allowed.
                float ub = upperBound(current.getTv() - arr[level].getValue(), current.getTw() + arr[level].getWeight(), level + 1, arr);
                float lb = lowerBound(current.getTv() - arr[level].getValue(), current.getTw() + arr[level].getWeight(), level + 1, arr);
                left.assign(ub, lb, level + 1, true, current.getTv() - arr[level].getValue(), current.getTw() + arr[level].getWeight());
            } else {
                left.resetTo1();
            }

//            System.out.println("current.tw=" + current.tw + ", arr[current.level].weight=" + arr[current.level].weight + ", sum=" + (current.tw + arr[current.level].weight));

            // Update minLB
            minLB = Math.min(minLB, left.getLb());
            minLB = Math.min(minLB, right.getLb());

            if (minLB >= left.getUb())
                pq.add(new Node(left));
            if (minLB >= right.getUb())
                pq.add(new Node(right));
        }

        List<String> resultList = new ArrayList<>();

        // compute final result
        for (int i = 1; i <= size; i++) {
            if (finalPath[i-1])
                resultList.add(String.valueOf(i));
        }

        return resultList.isEmpty() ? "-" : String.join(",", resultList);
    }

    /**
     * Function to calculate upper bound cost
     * @param idx - The item index
     * @param tw - The total weights of the items
     * @param tw - The total values of the items
     * @param arr - The items array. Each row represent an item, columns consist of the Cost, Weight, Index
     * @return The upper bound value
     */
    public float upperBound(float tv, float tw, int idx, Item[] arr) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].getWeight() <= capacity) {
                weight += arr[i].getWeight();
                value -= arr[i].getValue();
            } else {
                value -= (float)(capacity - weight) / arr[i].getWeight() * arr[i].getValue();
                break;
            }
        }
        return value;
    }

    /**
     * Function to calculate lower bound cost
     * @param idx - The item index
     * @param tw - The total weights of the items
     * @param tw - The total values of the items
     * @param arr - The items array. Each row represent an item, columns consist of the Cost, Weight, Index
     * @return The lower bound value
     */
    public float lowerBound(float tv, float tw, int idx, Item[] arr) {
        float value = tv;
        float weight = tw;
        for (int i = idx; i < size; i++) {
            if (weight + arr[i].getWeight() <= capacity) {
                weight += arr[i].getWeight();
                value -= arr[i].getValue();
            } else {
                break;
            }
        }
        return value;
    }

}
