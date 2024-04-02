package ru.katkov.signalprocessing.kernelfilter.filter;

public class ColorConverter {
    public static long convert(int r, int g, int b, int a) {
        long res = 0;
        res += ((long) r << 48);
        res += ((long) g << 32);
        res += ((long) b << 16);
        res += a;
        return res;
    }

    public static long convert(int r, int g, int b) {
        return convert(r, g, b, 0);
    }

    public static int red(long color) {
        return get16bit(color, 3);
    }

    public static int green(long color) {
        return get16bit(color, 2);
    }

    public static int blue(long color) {
        return get16bit(color, 1);
    }

    public static int alpha(long color) {
        return get16bit(color, 0);
    }

    private static int get16bit(long val, int offset) {
        if (offset > 3 || offset < 0) throw new IllegalArgumentException();
        long res = (val >> (16 * offset)) & 65535;
        return (int) res;
    }
}
