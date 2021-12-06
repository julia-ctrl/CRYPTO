package org.julia.lab3;

import org.julia.lab3.client.CasinoClient;
import org.julia.lab3.client.CasinoMode;
import org.julia.lab3.client.dto.BetResult;
import org.julia.lab3.client.utils.Mt19937Rnd;


public class Lab3Task2 {
    public static void main(String[] args) throws Exception {
        CasinoClient client = new CasinoClient();

        long start = System.currentTimeMillis() / 1000;
        long realNumber = client.play(CasinoMode.MT, 1, 1L).getRealNumber();
        long end = System.currentTimeMillis() / 1000;

        final int marginError = 10;//ten seconds
        Mt19937Rnd mtOnServer = null;
        for (long timestamp = start - marginError; timestamp < end + marginError; timestamp++) {
            Mt19937Rnd mt = new Mt19937Rnd((int) timestamp);
            if (realNumber == mt.next()) {
                mtOnServer = mt;
                break;
            }
        }

        if (mtOnServer == null) {
            throw new RuntimeException("was not bale to find");
        }

        long money;
        do {
            long next = mtOnServer.next();
            BetResult res = client.play(CasinoMode.MT, 100, next);
            money = res.getAccount().getMoney();
        } while (money < 1_000_000);
        System.out.println("money=" + money);
    }
}
