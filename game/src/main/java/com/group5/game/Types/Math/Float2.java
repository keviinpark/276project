package com.group5.game.Types.Math;

/**
 * A class used to represent points in 2D (continous) space.
 * <p>
 * From moment of creation, values are considered "constant" in order to enforce acting as a pseudo value type.
 * <p>
 * Equality is not determined by instance, but value of x and y.
 * <p>
 * HashCode uses quick and dirty shift hash that should probably work.
 */
public class Float2 {
    public final float x;
    public final float y;

    public Float2(){
        x = 0;
        y = 0;
    }
    public Float2(float x, float y){
        this.x = x;
        this.y = y;
    }

    public static Float2 Plus(Float2 a, Float2 b){
        return new Float2(a.x + b.x, a.y + b.y);
    }


    @Override
    public boolean equals(Object other){
        if(!(other instanceof Int2)) return false;
        Int2 o = (Int2) other;
        return x == o.x && y == o.y;
    }
    @Override
    public int hashCode(){
        return (int)x << 7 + (int)y << 17;
    }

    @Override
    public String toString(){
        return "(" + x + "," + y + ")";
    }
}
