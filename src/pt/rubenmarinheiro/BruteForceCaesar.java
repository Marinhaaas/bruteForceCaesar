package pt.rubenmarinheiro;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * BruteForce system developed to find a specific salted password after Caesar Cipher, providing one SHA512 final password and one wordlist.
 * @author Rúben Marinheiro
 * @version 1.0
 */
public class BruteForceCaesar {
    /**
     *
     * @param args To run this app, must be provided two arguments, one SHA512 password and one wordlist system path, respectively.
     */
    public static void main(String[] args) {
        Configs.argsValidation(args);
        Configs.clearConsole();
        System.out.println("Running... Exploring all wordlist passwords...");
        long start = System.currentTimeMillis(); //Start Stopwatch
        ArrayList<String> words = Configs.readWordList(args[1]);
        String[] newWords = words.toArray(new String[words.size()]);

        int listSize = newWords.length;
        String[] part1 = Arrays.copyOfRange(newWords, 0, (listSize + 1)/4);
        String[] part2 = Arrays.copyOfRange(newWords, part1.length, part1.length*2);
        String[] part3 = Arrays.copyOfRange(newWords, part1.length*2, part1.length*3);
        String[] part4 = Arrays.copyOfRange(newWords, part1.length*3, (listSize));

        Thread thread1 = new Thread(() -> {
            Configs.bruteForce(part1, start, args[0]);
        });
        Thread thread2 = new Thread(() -> {
            Configs.bruteForce(part2, start, args[0]);
        });
        Thread thread3 = new Thread(() -> {
            Configs.bruteForce(part3, start, args[0]);
        });
        Thread thread4 = new Thread(() -> {
            Configs.bruteForce(part4, start, args[0]);
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}

