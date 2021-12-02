package org.julia.lab3;

import org.apache.commons.math3.random.MersenneTwister;
import org.julia.lab3.client.CasinoClient;
import org.julia.lab3.client.CasinoMode;
import org.julia.lab3.client.dto.BetResult;


public class Lab3Task2 {
    public static void main(String[] args) throws Exception {
        CasinoClient client = new CasinoClient();

        long start = System.currentTimeMillis() / 1000;
        long realNumber = client.play(CasinoMode.MT, 1, 1L).getRealNumber();
        long end = System.currentTimeMillis() / 1000;

        final int marginError = 10;//ten seconds
        MersenneTwister mtOnServer = null;
        for (long timestamp = start - marginError; timestamp < end + marginError; timestamp++) {
            MersenneTwister mt = new MersenneTwister((int) timestamp) {
                @Override
                public long nextLong() {
                    return Integer.toUnsignedLong(this.next(64));
                }
            };

            if (realNumber == mt.nextLong()) {
                mtOnServer = mt;
                break;
            }
        }

        if (mtOnServer == null) {
            throw new RuntimeException("was not bale to find");
        }

        long money;
        do {
            long next = mtOnServer.nextLong();
            BetResult res = client.play(CasinoMode.MT, 100, next);
            money = res.getAccount().getMoney();
        } while (money < 1_000_000);
        System.out.println("money=" + money);
    }
}
