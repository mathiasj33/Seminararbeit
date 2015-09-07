package net.softwarepage.facharbeit.normalgame.logic;

import java.io.Serializable;

public class Vector implements Serializable {
    
    private float first;
    private float second;

    public Vector(float first, float second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public String toString() {
        return first + ";" + second;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Float.floatToIntBits(this.first);
        hash = 19 * hash + Float.floatToIntBits(this.second);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector other = (Vector) obj;
        if (Float.floatToIntBits(this.first) != Float.floatToIntBits(other.first)) {
            return false;
        }
        if (Float.floatToIntBits(this.second) != Float.floatToIntBits(other.second)) {
            return false;
        }
        return true;
    }

    public float getFirst() {
        return first;
    }

    public float getSecond() {
        return second;
    }

    public void setFirst(float first) {
        this.first = first;
    }

    public void setSecond(float second) {
        this.second = second;
    }
}
