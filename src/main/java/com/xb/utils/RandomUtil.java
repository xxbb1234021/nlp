package com.xb.utils;

import java.util.Random;

/**
 * Created by kevin on 2017/1/6 0006.
 */
public class RandomUtil {
    private static long seed;
    private static Random random;

    static {
        seed = System.currentTimeMillis();
        random = new Random(seed);
    }

    public static double getRandom() {
        return random.nextDouble();
    }

    public static double getRandom(double a, double b) {
        if (!(a < b)) throw new IllegalArgumentException("取值范围不对");
        return a + getRandom() * (b - a);
    }
}
