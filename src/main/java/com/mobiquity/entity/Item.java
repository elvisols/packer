package com.mobiquity.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Item {
    /**
     * Package weight
     */
    private float weight;

    /**
     * Package value
     */
    private int value;

    /**
     * Item index
     */
    private int idx;

    public Item(int value, float weight, int idx) {
        this.value = value;
        this.weight = weight;
        this.idx = idx;
    }
}
