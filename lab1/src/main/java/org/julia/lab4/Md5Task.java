package org.julia.lab4;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.codec.digest.DigestUtils;
import static org.julia.utils.LabConstants.ENG_ALPHABET;
import static org.julia.utils.LabConstants.ENG_ALPHABET_LOVER_CASE;
import static org.julia.utils.LabConstants.NUMBERS;

public class Md5Task {

    public static void main(String[] args) {
        final String alphabet = ENG_ALPHABET + ENG_ALPHABET_LOVER_CASE + NUMBERS + "-_/";
        final Map<String, String> hashes = new LinkedHashMap<>();
        hashes.put("5e056c500a1c4b6a7110b50d807bade5", null);
        hashes.put("0ea29a0d3cbfb7f98424a0a035859f60", null);
        hashes.put("0857e3b78eeb749dd6354c846960d687", null);
        hashes.put("2041810a4da800f1488b2aa507435604", null);
        hashes.put("24ef22994fb114ef16c39f7fada35ff9", null);
        hashes.put("abe0a2e79b3747ef12b70b429ba905ab", null);
        hashes.put("2104ceb418482819eb46ac3d952913a4", null);
        hashes.put("d4185990ae5d1b1f1c0cc42e49559fab", null);
        hashes.put("818a5850ffd55379b827b3099e8744b0", null);
        hashes.put("8ec7e84f89ab729805aaae71a45c5ee3", null);
        hashes.put("c81329a7e0dfd63c766fc9c565146fff", null);
        hashes.put("fa86feed504c85cbf2bd2ac970d707a4", null);
        hashes.put("1e944377523e4aae7baea648b21fdac9", null);
        hashes.put("ee647efa1529f47da13db71b662df247", null);
        hashes.put("077b1a78fae644f15d11e88d8a4d1622", null);


        List<String> knownParts = new ArrayList<>();
        //https://hashes.com/ru/decrypt/hash
        knownParts.addAll(Arrays.asList("ccKc", "862E", "https", "ocument", "cs.go", "ogle.", "zDaGq", "72fF", "nBZmJ"));
        knownParts.addAll(Arrays.asList("XrifRK", "lX5t6x"));//found by selfwritten bruteforce
        knownParts.add("mhRpprx");//found by tool that utilize GPU for find unhashed value
        //hashcat.exe -m 0 -a 3 -i --increment-min=6 --increment-max=8 -1abcdefghijklmnopqrstuvwxyz-_ABCDEFGHIJKLMNOPQRSTUVWXYZ --hwmon-disable  818a5850ffd55379b827b3099e8744b0 ?1?1?1?1?1?1?1?1


        for (String part : knownParts) {
            String md5 = DigestUtils.md5Hex(part).toLowerCase(Locale.ROOT);
            if (hashes.containsKey(md5)) {
                System.out.println(MessageFormat.format("Found for `{0}` in known parts: {1}", md5, part));
                hashes.put(md5, part);
            }
        }
        // looks like link to https://docs.google.com/document/d/ end ends on /edit so will try to find all possible substrings
        String[] partOfUrls = new String[]{"https://docs.google.com/document/d/", "/edit"};
        for (String urlPart : partOfUrls) {
            for (int i = 0; i < urlPart.length() - 1; i++) {
                for (int j = i; j < urlPart.length() + 1; j++) {
                    String part = urlPart.substring(i, j);
                    String md5 = DigestUtils.md5Hex(part).toLowerCase(Locale.ROOT);
                    if (hashes.containsKey(md5) && hashes.get(md5) == null) {
                        System.out.println(MessageFormat.format("Found for `{0}` in gdoc url : {1}", md5, part));
                        hashes.put(md5, part);
                    }
                }
            }
        }

        //link should be in format
        //https://docs.google.com/document/d/<id_of_documet>/edit
        //but was found only
        //https://docs.google.com/document
        //so need to try to find with suffix for `/d/` and prefix for `/edit`
        //brute force partial
        List<Integer> passwordIndexesList = new ArrayList<>();
        passwordIndexesList.add(0);
        int amountOfChars = 3;
        int countOfFound = 0;
        while (passwordIndexesList.size() <= amountOfChars && countOfFound < 2) {
            String keyStr = passwordIndexesList.stream()
                    .map(i -> new String(new char[]{alphabet.charAt(i)})).collect(Collectors.joining(""));
            String p1 = "/d/" + keyStr;
            String p2 = keyStr + "/edit";
            String md5p1 = DigestUtils.md5Hex(p1).toLowerCase(Locale.ROOT);
            String md5p2 = DigestUtils.md5Hex(p2).toLowerCase(Locale.ROOT);
            if (hashes.containsKey(md5p1) && hashes.get(md5p1) == null) {
                System.out.println(MessageFormat.format("Found for `{0}`: {1}", md5p1, p1));
                hashes.put(md5p1, p1);
            }

            if (hashes.containsKey(md5p2) && hashes.get(md5p2) == null) {
                System.out.println(MessageFormat.format("Found for `{0}`: {1}", md5p2, p2));
                hashes.put(md5p2, p2);
            }
            incrementPassword(passwordIndexesList, alphabet.length());
        }


        hashes.entrySet().stream().filter(kv -> kv.getValue() == null)
                .forEach(kv -> System.out.println("not decrypted: " + kv.getKey()));
        int totalLengthFound = hashes.values().stream().filter(v -> v != null).mapToInt(v -> v.length()).sum();
        System.out.println(totalLengthFound);


        //XrifRK lX5t6x
        //brute force
        amountOfChars = 7;
        passwordIndexesList = new ArrayList<>();
        passwordIndexesList.add(0);
        while (passwordIndexesList.size() <= amountOfChars &&
                !hashes.entrySet().stream().allMatch(kv -> kv.getValue() != null)) {
            String keyStr = passwordIndexesList.stream()
                    .map(i -> new String(new char[]{alphabet.charAt(i)})).collect(Collectors.joining(""));
            String md5 = DigestUtils.md5Hex(keyStr).toLowerCase(Locale.ROOT);
            if (hashes.containsKey(md5) && hashes.get(md5) == null) {
                System.out.println(MessageFormat.format("Found for `{0}`: {1}", md5, keyStr));
                hashes.put(md5, keyStr);
            }
            incrementPassword(passwordIndexesList, alphabet.length());
        }
        final StringBuilder decoded = new StringBuilder();
        for (String val : hashes.values()) {
            decoded.append(val);
        }
        System.out.println(decoded.toString());
    }

    private static void incrementPassword(List<Integer> passwordIndexesList, int alphabetLength) {
        for (int position = passwordIndexesList.size() - 1; position >= 0; position--) {
            Integer val = passwordIndexesList.get(position);
            val += 1;
            if (val >= alphabetLength) {
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
}
