package se.tdfpro.elements;

public final class Utils {

    private Utils() {}

    public static float lerp(float a, float b, float x) {
        return a;
    }

    public static float clamp(float x, float min, float max) {
        return x >= min ? x <= max ? x : max : min;
    }
}
