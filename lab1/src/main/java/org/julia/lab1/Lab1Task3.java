package org.julia.lab1;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import static org.julia.utils.LabConstants.ENG_ALPHABET;
import static org.julia.utils.LabConstants.ENG_ALPHABET_LOVER_CASE;
import static org.julia.utils.LabConstants.NUMBERS;
import org.julia.utils.LabUtils;

public class Lab1Task3 {
    public static void main(String[] args) throws Exception{
        final String keyAlphabet = ENG_ALPHABET + ENG_ALPHABET_LOVER_CASE + NUMBERS;
        long maxScore = Long.MIN_VALUE;
        String bestText = "";
        String bestKey = "";
        String encodedText = LabUtils.resourceToString("/lab1_task3.txt");;

        byte[] encodedArray = Base64.getDecoder().decode(encodedText.getBytes(StandardCharsets.UTF_8));
        int amountOfChars = 3;
        List<Integer> passwordIndexesList = new LinkedList<>();
        passwordIndexesList.add(0);
        while (passwordIndexesList.size() <= amountOfChars) {
            String keyStr = passwordIndexesList.stream()
                    .map(i -> new String(new char[]{keyAlphabet.charAt(i)})).collect(Collectors.joining(""));


            byte[] key = keyStr.getBytes(StandardCharsets.UTF_8);
            System.out.println(keyStr);

            byte[] decodedArray = LabUtils.decodeXor(encodedArray, key);
            String candidate = new String(decodedArray, StandardCharsets.UTF_8);
            long score = LabUtils.getStrScore(candidate);
            if (score > maxScore) {
                maxScore = score;
                bestText = candidate;
                bestKey = keyStr;
            }
            for (int position = passwordIndexesList.size() - 1; position >= 0; position--) {
                Integer val = passwordIndexesList.get(position);
                val += 1;
                if (val >= keyAlphabet.length()) {
                    val = 0;
                    passwordIndexesList.set(position, val);
                    if (position == 0) {
                        passwordIndexesList.add(0, 0);
                        break;
                    }
                } else {
                    passwordIndexesList.set(position, val);
                    break;
                }
            }

        }
        System.out.println(bestKey);
        System.out.println(bestText);
    }
}
