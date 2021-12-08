package org.julia.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LabUtils {
    public static String resourceToString(String path) throws URISyntaxException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(LabUtils.class.getResource(path).toURI()));
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static List<String> resourceToLines(String path) throws URISyntaxException, IOException {
        return Files.readAllLines(Paths.get(LabUtils.class.getResource(path).toURI()));
    }

    public static byte[] decodeXor(byte[] encodedArray, byte[] key) {
        byte[] decodedArray = new byte[encodedArray.length];
        for (int i = 0; i < encodedArray.length; i++) {
            decodedArray[i] = (byte) (encodedArray[i] ^ key[i % key.length]);
        }
        return decodedArray;
    }

    public static long getStrScore(String candidate) {
        long score = 0;
        char[] chars = candidate.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (LabConstants.SPACE.indexOf(ch) >= 0) {
                score += 30;
            } else if (LabConstants.ENG_ALPHABET_LOVER_CASE.indexOf(ch) >= 0) {
                score += 20;
            } else if (LabConstants.ENG_ALPHABET.indexOf(ch) >= 0) {
                score += 10;
            } else if (LabConstants.USUAL_SPECIAL_CHARS.indexOf(ch) >= 0) {
                score += 5;
            } else if (LabConstants.NUMBERS.indexOf(ch) >= 0) {
                score += 3;
            }
        }
        return score;
    }
}
