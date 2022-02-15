package com.group5.game.Types.Math;

/**
 * A class used to represent points in 2D space.
 * <p>
 * From moment of creation, values are considered "constant" in order to enforce acting as a pseudo value type.
 * <p>
 * Equality is not determined by instance, but value of x and y.
 * <p>
 * HashCode uses quick and dirty shift hash that should probably work.
 */
public class Int2 {
    public final int x;
    public final int y;

    public Int2(){
        x = 0;
        y = 0;
    }
    public Int2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static Int2 Plus(Int2 a, Int2 b){
        return new Int2(a.x + b.x, a.y + b.y);
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof Int2)) return false;
        Int2 o = (Int2) other;
        return x == o.x && y == o.y;
    }
    @Override
    public int hashCode(){
        return x << 7 + y << 17;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
