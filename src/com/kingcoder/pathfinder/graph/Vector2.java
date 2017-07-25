package com.kingcoder.pathfinder.graph;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(){}

    public Vector2(int x, int y){
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2 vec){
        return x == vec.x && y == vec.y;
    }

    public boolean equals(int x, int y){
        return this.x == x && this.y == y;
    }

    public Vector2 add(Vector2 vec){
        Vector2 result = new Vector2();
        result.x = vec.x + x;
        result.y = vec.y + y;

        return result;
    }

    public Vector2 subtract(Vector2 vec){
        Vector2 result = new Vector2();
        result.x = vec.x - x;
        result.y = vec.y - y;

        return result;
    }

    public Vector2 multiply(Vector2 vec){
        Vector2 result = new Vector2();
        result.x = vec.x * x;
        result.y = vec.y * y;

        return result;
    }

    public Vector2 divide(Vector2 vec){
        Vector2 result = new Vector2();
        result.x = vec.x / x;
        result.y = vec.y / y;

        return result;
    }

    public float length(){
        return (float)Math.sqrt(x*x + y*y);
    }

    public void normalize(){
        float length = length();
        x /= length;
        y /= length;
    }

    public String toString(){
        return x + "-" + y;
    }
}
