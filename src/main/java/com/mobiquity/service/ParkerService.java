package com.mobiquity.service;

import com.mobiquity.entity.Item;

public interface ParkerService {

    float lowerBound(float tv, float tw, int idx, Item[] arr);
    float upperBound(float tv, float tw, int idx, Item[] arr);
    String pack(Item[] arr, float capacity);

}
