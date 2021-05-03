package com.mobiquity.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Node {
    /**
     * Upper bound value (with fractional value)
     */
    private float ub;

    /**
     * Lower bound value (without fractional value)
     */
    private float lb;

    /**
     * Decision tree level
     */
    private int level;

    /**
     * Item Select flag
     */
    private boolean flag;

    /**
     * Total items cost/value
     */
    private float tv;

    /**
     * Total items weight
     */
    private float tw;

    public Node() {}

    public Node(Node cpy) {
        this.tv = cpy.tv;
        this.tw = cpy.tw;
        this.ub = cpy.ub;
        this.lb = cpy.lb;
        this.level = cpy.level;
        this.flag = cpy.flag;
    }

    public void assign(float ub, float lb, int level, boolean flag, float tv, float tw) {
        this.ub = ub;
        this.lb = lb;
        this.level = level;
        this.flag = flag;
        this.tv = tv;
        this.tw = tw;
    }

    public void reset() {
        this.tv = this.tw = this.ub = this.lb = this.level = 0;
        this.flag = false;
    }

    public void resetTo1() {
        this.ub = this.lb = 1;
    }

}
