package org.julia.lab3.client.utils;

import java.lang.reflect.Field;
import org.apache.commons.math3.random.MersenneTwister;

public class Mt19937Rnd {
    private final MersenneTwister delegate;

    public Mt19937Rnd(int seed) {
        delegate = new MersenneTwister(seed) {
            /**
             * Fix uint problem on java
             * @return
             */
            @Override
            public long nextLong() {
                return Integer.toUnsignedLong(this.next(64));
            }
        };
    }

    public Mt19937Rnd(int[] seed) {
        delegate = new MersenneTwister() {
            /**
             * Fix uint problem on java
             * @return
             */
            @Override
            public long nextLong() {
                return Integer.toUnsignedLong(this.next(64));
            }
        };

        Field f = null;
        try {
            f = MersenneTwister.class.getDeclaredField("mt");
            f.setAccessible(true);
            final int U = 11; // additional Mersenne Twister tempering bit shift/mask
            final int L = 18; // additional Mersenne Twister tempering bit shift
            final int T = 15; // TGFSR(R) tempering bit shift
            final int C = 0xEFC60000; // TGFSR(R) tempering bitmask
            final int B = 0x9D2C5680; // TGFSR(R) tempering bitmask

            //set the same state as on server
            int[] mt = (int[]) f.get(delegate);
            for (int i = 0; i < seed.length; i++) {
                //reverse of tempering
                int val = seed[i];

                val ^= val >>> L;
                val ^= (val << T) & C;

                int temp = val;
                temp = val ^ ((temp << 7) & B);
                temp = val ^ ((temp << 7) & B);
                temp = val ^ ((temp << 7) & B);
                temp = val ^ ((temp << 7) & B);
                temp = val ^ ((temp << 7) & B);

                int k = temp >>> U;
                int l = temp ^ k;
                int m = l >>> 11;

                mt[i] = temp ^ m;
            }
            //set the same index state as on server
            f = MersenneTwister.class.getDeclaredField("mti");
            f.setAccessible(true);
            f.setInt(delegate, seed.length);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    public long next() {
        return delegate.nextLong();
    }
}
