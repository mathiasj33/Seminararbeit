package net.softwarepage.facharbeit.normalgame.helpers;

public class MathHelper {
    public static float round(float value, int numberOfDecimals) {
        int decimalValue = (int) Math.pow(10, numberOfDecimals);
        return (float) Math.round(value * decimalValue) / decimalValue;
    }
}
