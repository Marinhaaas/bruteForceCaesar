package pt.rubenmarinheiro;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class Configs {

    /**
     * BruteForceCaesar method that invokes all the other methods to start the BruteForce process.
     * @param list Wordlist of plaintext passwords
     * @param startTime Application Stopwatch start time
     * @param hash Hash provided by teacher
     */
    public static void bruteForce(String[] list, long startTime, String hash) {
        String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", actualPre, actualSuf;
        StringBuilder saltBuilder = new StringBuilder();

        for (int i = 0; i < list.length; i++) {  //Wordlist
            for (int j = 0; j < alphabet.length(); j++) { //First Char of salt
                for (int k = 0; k < alphabet.length(); k++) { //Second Char of salt
                    for (int l = 0; l < alphabet.length(); l++) { //Caesar Cipher Shift
                        saltBuilder.append(alphabet.charAt(j)).append(alphabet.charAt(k));
                        actualPre = textToHash(saltBuilder.toString() + caesarCipher(list[i], l, alphabet));
                        actualSuf = textToHash(caesarCipher(list[i], l, alphabet) + saltBuilder.toString());
                        if (actualPre.equals(hash) || actualSuf.equals(hash)) {
                            System.out.println();
                            System.out.println("--------------------------------");
                            System.out.println("Stop! The secret word is: " + list[i]);
                            System.out.println("--------------------------------");
                            System.out.println();

                            long finish = System.currentTimeMillis();
                            long timeElapsed = finish - startTime; //Calculate elapsed time
                            System.out.println("Duration of the BruteForce: " + timeElapsed / 1000 + " seconds");
                            if (actualPre.equals(hash)) {
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
            System.out.println("Palavra analisada: " + list[i]);
        }
    }

    /**
     * Method to cipher a plain password using SHA512
     * @param plainText Text to cipher with SHA512
     * @return SHA512 hash of provided text
     */
    public static String textToHash(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(plainText.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 128) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method used to cipher a plain password using Caesar Cipher
     * @param plainText Text to cipher with Caesar Cipher
     * @param shift Caesar Cipher shift used to cipher
     * @param alphabet Custom Alphabet used to cipher
     * @return Caesar Cipher ciphered text of provided text
     */
    public static String caesarCipher(String plainText, int shift, String alphabet) {
        StringBuilder nomes = new StringBuilder();
        int actualIndex, count;
        String result = null, character;
        for (int i = 0; i < plainText.length(); i++) {
            character = String.valueOf(plainText.charAt(i));
            actualIndex = alphabet.indexOf(character); //Stores the current character position in the variable alphabet
            if ((actualIndex + shift) >= alphabet.length()) { //
                count = (actualIndex + shift) / alphabet.length();
                result = nomes.append(alphabet.charAt((actualIndex + shift) - (alphabet.length() * count))).toString();
            } else {
                result = nomes.append(alphabet.charAt(actualIndex + shift)).toString();
            }
        }
        return result;
    }

    /**
     * Method used to read a provided wordlist
     * @param path System path of the wordlist
     * @return Array with all words of the provided wordlist
     */
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

    /**
     * Method used to clear the console (Windows and Linux)
     */
    public static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "color a").inheritIO().start().waitFor();
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     *
     * @param args BruteForceCaesar class arguments
     */
    public static void argsValidation(String[] args) {
        if (args.length != 2) {
            clearConsole();
            System.out.println("Incorrect arguments, run again (hash + wordlist path).");
            System.exit(1);
        }
        if (!new File(args[1]).exists()) {
            clearConsole();
            System.out.print("Incorrect arguments, enter a valid path.");
            System.exit(1);
        }
        if (args[0].length() != 128) {
            clearConsole();
            System.out.println("Incorrect arguments, invalid SHA512 Hash.");
            System.exit(1);
        }
    }
}
