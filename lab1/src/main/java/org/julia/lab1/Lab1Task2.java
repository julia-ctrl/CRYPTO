package org.julia.lab1;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.binary.Hex;
import org.julia.utils.LabUtils;

public class Lab1Task2 {

    public static void main(String[] args) throws Exception {
        long maxScore = Long.MIN_VALUE;
        String bestText = "";
        byte bestKey = 0;
        String encodedText = LabUtils.resourceToString("/lab1_task2.txt");

        byte[] encodedArray = Hex.decodeHex(encodedText);
        byte[] key = new byte[1];
        for (int b = Byte.MIN_VALUE; b <= Byte.MAX_VALUE; b++) {
            key[0] = (byte) b;
            byte[] decodedArray = LabUtils.decodeXor(encodedArray, key);
            String candidate = new String(decodedArray, StandardCharsets.UTF_8);
            long score = LabUtils.getStrScore(candidate);
            if (score > maxScore) {
                maxScore = score;
                bestText = candidate;
                bestKey = (byte) b;
            }
        }
        System.out.println(new String(new byte[]{bestKey}, StandardCharsets.UTF_8));
        System.out.println(bestText);
    }
}
