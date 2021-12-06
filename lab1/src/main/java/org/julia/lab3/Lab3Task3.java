package org.julia.lab3;

import org.julia.lab3.client.CasinoClient;
import org.julia.lab3.client.CasinoMode;
import org.julia.lab3.client.dto.BetResult;
import org.julia.lab3.client.utils.Mt19937Rnd;


public class Lab3Task3 {
    public static void main(String[] args) throws Exception {
        CasinoClient client = new CasinoClient();

        int[] seedArray = new int[624];
        for (int i = 0; i < seedArray.length; i++) {
            long realNumber = client.play(CasinoMode.BETTER_MT, 1, 1L).getRealNumber();
            seedArray[i]=(int) realNumber;
        }

        Mt19937Rnd mtOnServer = new Mt19937Rnd(seedArray);


        long money;
        do {
            long next = mtOnServer.next();
            BetResult res = client.play(CasinoMode.BETTER_MT, 100, next);
            money = res.getAccount().getMoney();
        } while (money < 1_000_000);
        System.out.println("money=" + money);
    }
}
