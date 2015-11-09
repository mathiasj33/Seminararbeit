package net.softwarepage.facharbeit.normalgame.helpers;

public class MathHelper {
    public static float round(float value, int numberOfDecimals) {  //rundet eine Kommazahl
        int decimalValue = (int) Math.pow(10, numberOfDecimals);
        return (float) Math.round(value * decimalValue) / decimalValue;
    }
}
