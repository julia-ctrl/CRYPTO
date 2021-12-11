package org.julia.lab3;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.julia.lab3.client.CasinoClient;
import org.julia.lab3.client.CasinoMode;
import org.julia.lab3.client.dto.BetResult;

public class Lab3Task1 {
    private static long m = BigInteger.valueOf(2).pow(32).longValue();// m is 2^32

    public static void main(String[] args) throws Exception {
        CasinoClient client = new CasinoClient();
        List<Integer> results = new ArrayList<>();
        long money = 0;
        int lstVal = 0;
        for (int i = 0; i < 4; i++) {
            BetResult res = client.play(CasinoMode.LGC, 1, 0);
            results.add((int)res.getRealNumberAsInt());
            money = res.getAccount().getMoney();
            lstVal = (int)res.getRealNumberAsInt();
        }

        Set<Long> aCandidates = getACandidates(results);

        ACValues correctValues = findCorrectValues(aCandidates, results);
        if (correctValues.a == null || correctValues.c == null) {
            throw new RuntimeException("a or c not found");
        }
        System.out.println("a=" + correctValues.a);
        System.out.println("c=" + correctValues.c);

        do {
            int next = calculateNext(lstVal, correctValues.a, correctValues.c);
            BetResult res = client.play(CasinoMode.LGC, (int) money, next);
            money = res.getAccount().getMoney();
            lstVal = res.getRealNumberAsInt();
        } while (money < 1_000_000);
        System.out.println("money=" + money);
    }

    private static Set<Long> getACandidates(List<Integer> results) {
        Set<Long> aCandidates = new HashSet<>();
        //comment to calculate
        aCandidates.addAll(Arrays.asList(17181533709L, 1664525L, -4293302771L, 4296631821L, -8588270067L, 8591599117L,
                -12883237363L, 12886566413L, -17178204659L));
        if (!aCandidates.isEmpty()) {
            return aCandidates;
        }

        final long zMinRange = Integer.MIN_VALUE;
        final long zMaXRange = Integer.MAX_VALUE;
        for (int i = 1; i < results.size() - 1; i++) {
            int prevResult = results.get(i - 1);
            int result = results.get(i);
            int nextRes = results.get(i + 1);
            Set<Long> aCandidatesForZ = new HashSet<>();
            for (long z = zMinRange; z < zMaXRange; z++) {
                long aCandidateMod = (z * m + result - nextRes) % (prevResult - result);
                if (aCandidateMod == 0) {
                    aCandidatesForZ.add((z * m + result - nextRes) / (prevResult - result));
                }
            }
            if (aCandidates.isEmpty()) {
                aCandidates = aCandidatesForZ;
            } else {
                aCandidates.retainAll(aCandidatesForZ);
            }
        }
        return aCandidates;
    }

    private static ACValues findCorrectValues(Set<Long> aCandidates, List<Integer> results) {
        final ACValues ret = new ACValues();
        //use found result to not do bruteforce
        long cFound = 1013904223L;
        final long cMin = 1013904223L - 1;//Integer.MIN_VALUE;
        final long cMax = 1013904223L + 1;//Integer.MAX_VALUE;
        for (long a : aCandidates) {
            for (long c = cMin; c < cMax; c++) {
                boolean allCorrect = true;
                for (int i = 1; i < results.size(); i++) {
                    int prevResult = results.get(i - 1);
                    int result = results.get(i);
                    if (calculateNext(prevResult, a, c) % m != result) {
                        allCorrect = false;
                        break;
                    }
                }
                if (allCorrect) {
                    ret.a = a;
                    ret.c = c;
                    break;
                }
            }
        }
        return ret;
    }

    private static int calculateNext(int last, long a, long c) {
        long ret = (a * last + c) % m; // m is 2^32
        return (int) ret;
    }

    private static class ACValues {
        Long a;
        Long c;
    }
}
