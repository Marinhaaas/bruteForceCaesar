import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class BruteForceCaesar {
    public static void main(String[] args) {
        System.out.println("Running in background exploring all the of 4271389824 combinations...");
        long start = System.currentTimeMillis();
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String actualPre, actualSuf;
        StringBuilder saltBuilder = new StringBuilder();
        int totalCount = 0;
        ArrayList<String> words = readWordList(args[1]);
        String[] newWords = words.toArray(new String[words.size()]);

        for (int i = 0; i <= 51; i++) {
            for (int j = 0; j <= 51; j++) {
                for (int k = 0; k < newWords.length; k++) {
                    for (int l = 0; l <= 51; l++) {
                        totalCount += 1;
                        saltBuilder.append(alphabet.charAt(i)).append(alphabet.charAt(j));
                        actualPre = textToHash(saltBuilder.toString() + caesar(newWords[k], l, alphabet));
                        actualSuf = textToHash(caesar(newWords[k], l, alphabet) + saltBuilder.toString());
                        if (actualPre.equals(args[0]) || actualSuf.equals(args[0])) {
                            System.out.println();
                            System.out.println("--------------------------------");
                            System.out.println("Stop! The secret word is: " + newWords[k]);
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
                            System.out.println("Salt: " + saltBuilder.toString());
                            System.out.println("Caesar shift: " + l);

                            System.exit(1);
                        } else {
                            saltBuilder.setLength(0);
                        }
                    }
                }
            }
        }
    }

    private static String textToHash(String plainText) {
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

    private static String caesar(String msg, int shift, String alphabet) {
        StringBuilder nomes = new StringBuilder();
        int actualIndex, count;
        String character;
        String result = null;
        for (int i = 0; i < msg.length(); i++) {
            character = String.valueOf(msg.charAt(i));
            actualIndex = alphabet.indexOf(character);
            if ((actualIndex + shift) >= alphabet.length()) {
                count = (actualIndex + shift) / 52;
                result = nomes.append(alphabet.charAt((actualIndex + shift) - (52 * count))).toString();
            } else {
                result = nomes.append(alphabet.charAt(actualIndex + shift)).toString();
            }
        }
        return result;
    }

    private static ArrayList<String> readWordList(String path) {
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

