package org.julia.lab2;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.julia.utils.LabUtils;

public class Lab2Task1 {
    private static final Pattern ENG_TEXT_PATTERN = Pattern.compile("[\\sa-zA-Z,.\\-'?!]+");
    
    public static void main(String[] args) throws Exception {
        List<String> encodedText = LabUtils.resourceToLines("/lab2_task1.txt");
        List<byte[]> encodedByteArrays = encodedText.stream().map(str -> {
            try {
                return Hex.decodeHex(str);
            } catch (DecoderException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        //" the " -> found `actio` so next try ' action'
        //' action' -> found `Who wou` so next try 'Who would '
        //'Who would ' ->  `hat we kno` so next try 'hat we know '
        //'hat we know ' -> `untry, from `so next try ' country, from '
        //' country, from ' -> `office, and the` so next try ` office, and the `
        // ` office, and the ` ->`might his quietus` so next try ` might his quietus `
        //` might his quietus ` -> `wrong, the proud ma` so next try ` wrong, the proud ma`
        //` wrong, the proud ma` -> `ative hue of resolut` so next try `ative hue of resolution`
        //`ative hue of resolution` ->  ' wrong, the proud man's' so next try ` wrong, the proud man's `
        //found `f might his quietus make` google `might his quietus make` found `when he himself might his quietus make`
        //found http://www.poetarium.info/sheakspear/tobe_nabokov.htm `To be or not to be`
        //----FILNAL STRING ---- `th'oppressor's wrong, the proud man's `
        //and from site above `The oppressor's wrong, the proud man's contumely,` (longest string)
        //so try `th'oppressor's wrong, the proud man's contumely,`
        byte[] encodingCribWord = " action".getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < encodedByteArrays.size() - 1; i++) {
            for (int j = i + 1; j < encodedByteArrays.size(); j++) {
                byte[] firstCiphertext = encodedByteArrays.get(i);
                byte[] secondCiphertext = encodedByteArrays.get(j);
                byte[] q1q2XOR = xor(firstCiphertext, secondCiphertext);

                byte[] subarray = new byte[encodingCribWord.length];
                if (encodingCribWord.length < q1q2XOR.length) {
                    for (int z = 0; z < 1 + q1q2XOR.length - encodingCribWord.length; z++) {
                        System.arraycopy(q1q2XOR, z, subarray, 0, subarray.length);
                        String candidate = new String(xor(encodingCribWord, subarray), StandardCharsets.UTF_8);
                        if (!ENG_TEXT_PATTERN.matcher(candidate).matches()) {
                            continue;
                        }
                        System.out.println(i + "_" + j + " " + z + "    `" + candidate + "`");
                    }
                } else {
                    String candidate = new String(xor(encodingCribWord, q1q2XOR), StandardCharsets.UTF_8);
                    if (!ENG_TEXT_PATTERN.matcher(candidate).matches()) {
                        continue;
                    }
                    System.out.println(candidate);
                }
            }
        }


    }

    static byte[] xor(byte[] first, byte[] second) {
        int minLength = Math.min(first.length, second.length);

        byte[] result = new byte[minLength];
        for (int i = 0; i < minLength; i++) {
            result[i] = (byte) (first[i] ^ second[i]);
        }
        return result;

    }
}
