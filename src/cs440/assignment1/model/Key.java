package cs440.assignment1.model;

/**
 * Created by Shahab Shekari on 10/5/14.
 */
public class Key {

    Integer k1, k2;

    public Key(int k1, int k2) {
        this.k1 = k1;
        this.k2 = k2;
    }

    public Key setK1(int k1) {
        this.k1 = k1;
        return this;
    }

    public Key setK2(int k2) {
        this.k2 = k2;
        return this;
    }

    public boolean lt(Key other) {
        if (this.k1 + 0.000001 < other.k1) return true;
        else if (this.k1 - 0.000001 > other.k1) return false;
        return this.k2 < other.k2;
    }

    public boolean gt(Key other) {
        if (this.k1-0.00001 > other.k1) return true;
        else if (this.k1 < other.k1-0.00001) return false;
        return this.k2 > other.k2;
    }

}
