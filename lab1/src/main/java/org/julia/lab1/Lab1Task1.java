package org.julia.lab1;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.julia.utils.LabUtils;

public class Lab1Task1 {
    private static final int BITS_IN_BYTE = 8;

    public static void main(String[] args) throws Exception {
        String encodedText = LabUtils.resourceToString("/lab1_task1.txt");
        byte[] byteArray = new byte[encodedText.length() / BITS_IN_BYTE];
        for (int i = 0; i < byteArray.length; i++) {
            String byteInBinary = encodedText.substring(i * BITS_IN_BYTE, (i + 1) * BITS_IN_BYTE);
            byteArray[i] = Byte.parseByte(byteInBinary, 2);
        }

        byte[] decodedByteArray = Base64.getDecoder().decode(byteArray);
        String decoded = new String(decodedByteArray, StandardCharsets.UTF_8);
        System.out.println(decoded);
    }
}
