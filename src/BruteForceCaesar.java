import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class BruteForceCaesar {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String salt, actualPre, actualSuf;
        int totalCount = 0;
        ArrayList<String> words = readWordList(args[1]);

        for (int i = 0; i <= 51; i++) {
            for (int j = 0; j <= 51; j++) {
                for (int k = 0; k < words.size(); k++) {
                    for (int l = 0; l <= 51; l++) {
                        totalCount += 1;
                        salt = new StringBuilder().append(alphabet.charAt(i)).append(alphabet.charAt(j)).toString();
                        actualPre = textToHash(salt + caesar(words.get(k), l));
                        actualSuf = textToHash(caesar(words.get(k), l) + salt);
                        if (!actualPre.equals(args[0]) && !actualSuf.equals(args[0])) {
                            System.out.println(totalCount + " of 4271389824");
                        } else {
                            System.out.println();
                            System.out.println("--------------------------------");
                            System.out.println("Stop! The secret word is: " + words.get(k));
                            System.out.println("--------------------------------");
                            System.out.println();


                            long finish = System.currentTimeMillis();
                            long timeElapsed = finish - start;
                            System.out.println("Duration of the BruteForce: " + timeElapsed / 1000 + " seconds");
                            System.out.println("Analyzed hashes: " + totalCount);
                            if (actualPre.equals(args[0])) {
                                System.out.println("Salt Position: Prefix");
                            } else {
                                System.out.println("Salt Position: Suffix");
                            }
                            System.out.println("Salt: " + salt);
                            System.out.println("Caesar shift: " + l);

                            System.exit(1);
                        }
                    }
                }
            }
        }
    }

    public static String textToHash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(plainText.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String caesar(String msg, int shift) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int actualIndex, count;
        String character;
        String result = "";
        for (int i = 0; i < msg.length(); i++) {
            character = String.valueOf(msg.charAt(i));
            actualIndex = alphabet.indexOf(character);
            if ((actualIndex + shift) >= alphabet.length()) {
                count = (actualIndex + shift) / 52;
                result += String.valueOf(alphabet.charAt((actualIndex + shift) - (52 * count)));
            } else {
                result += String.valueOf(alphabet.charAt(actualIndex + shift));
            }
        }
        return result;
    }

    public static ArrayList<String> readWordList(String path) {
        ArrayList<String> words = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(path));
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }
}

