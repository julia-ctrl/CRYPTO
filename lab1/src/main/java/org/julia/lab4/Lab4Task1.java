package org.julia.lab4;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.apache.commons.codec.digest.DigestUtils;
import org.julia.utils.LabConstants;
import org.julia.utils.LabUtils;

public class Lab4Task1 {

    private static final String RANDOM_PASSWORD_ALPHABET = LabConstants.ENG_ALPHABET +
            LabConstants.ENG_ALPHABET_LOVER_CASE + LabConstants.NUMBERS;
    private static List<String> top100;
    private static List<String> top100k;
    private static final Random rand = new Random();

    public static void main(String[] args) throws Exception {
        //https://github.com/danielmiessler/SecLists/blob/master/Passwords/Common-Credentials/10-million-password-list-top-100.txt
        top100 = LabUtils.resourceToLines("/lab4/top100passwords.txt");

        //https://github.com/danielmiessler/SecLists/blob/master/Passwords/Common-Credentials/10-million-password-list-top-100000.txt
        top100k = LabUtils.resourceToLines("/lab4/top100000passwords.txt");

        generateMd5Passwords();
        generateSha1WithSaltPasswords();
    }

    private static void generateMd5Passwords() throws IOException {
        int passwordsToGenerate = 100_000;
        File file = new File("build/md5.csv");
        file.getParentFile().mkdirs();
        if (file.exists()) {
            file.delete();
        }
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeNext(new String[]{"hash"}, false);
        for (int i = 0; i < passwordsToGenerate; i++) {
            String randomPassword = generateRandomPassword();
            String md5 = DigestUtils.md5Hex(randomPassword).toLowerCase(Locale.ROOT);
            writer.writeNext(new String[]{md5}, false);
        }
        writer.close();
        System.out.println("File with md5 hashes:" + file.getAbsolutePath());
    }

    private static void generateSha1WithSaltPasswords() throws IOException {
        int passwordsToGenerate = 100_000;
        File file = new File("build/sha1_with_salt.csv");
        file.getParentFile().mkdirs();
        if (file.exists()) {
            file.delete();
        }
        CSVWriter writer = new CSVWriter(new FileWriter(file));
        writer.writeNext(new String[]{"hash", "salt"}, false);
        for (int i = 0; i < passwordsToGenerate; i++) {
            String salt = randomPassword();
            String randomPassword = generateRandomPassword();
            String md5 = DigestUtils.sha1Hex(randomPassword + salt).toLowerCase(Locale.ROOT);
            writer.writeNext(new String[]{md5, salt}, false);
        }
        writer.close();
        System.out.println("File with md5 hashes:" + file.getAbsolutePath());
    }

    private static String generateRandomPassword() {
        int randomNumber = rand.nextInt(100);
        //10%
        if (randomNumber < 10) {
            return getRandomPasswordFromTop100();
            //60%
        } else if (randomNumber < 70) {
            return getRandomPasswordFromTop100k();
            //5%
        } else if (randomNumber < 75) {
            return randomPassword();
            //25%
        } else {
            return getRandomPasswordFromTop100WithSuffix();
        }
    }

    private static String getRandomPasswordFromTop100() {
        return top100.get(rand.nextInt(top100.size()));
    }

    private static String getRandomPasswordFromTop100k() {
        return top100k.get(rand.nextInt(top100k.size()));
    }

    private static String getRandomPasswordFromTop100WithSuffix() {
        return top100.get(rand.nextInt(top100.size())) + rand.nextInt(1000);
    }

    private static String randomPassword() {
        //random length from 4 till 6 chars in password
        int passwordLength = 4 + rand.nextInt(3);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordLength; i++) {
            sb.append(RANDOM_PASSWORD_ALPHABET.charAt(rand.nextInt(RANDOM_PASSWORD_ALPHABET.length())));
        }
        return sb.toString();
    }
}
