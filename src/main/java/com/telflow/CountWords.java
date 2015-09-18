package com.telflow;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

/**
 * The main class for the console application to read a text file and count the number of words
 * that meet the given condition on command line.
 *
 * Usage: java -cp [packaged jar file] com.telflow.CountWords startsWith=m,M longerThan=5 [filename]
 *
 * The value for <strong>startsWith</strong> specifies the restriction on valid starting characters of the words
 * that need to be counted.
 *
 * The value for <strong>longerThan</strong> shows the the minimum length of all words that need to be counted.
 *
 */
public class CountWords {

    public final static String STARTS_WITH = "startsWith";
    public final static String LONGER_THAN = "longerThan";
    public final static String FILENAME = "filename";
    public final static String STARTS_WITH_REGEX = "starts[wW]ith=[a-zA-Z](,[a-zA-Z])*";
    public final static String LONGER_THAN_REGEX = "longer[tT]han=[1-9][0-9]*";


    /**
     * Entry point - the main method of application
     *
     * @param args - string array containing command line arguments
     */
    public static void main( String[] args ) {

        ResourceBundle bundle = ResourceBundle.getBundle("messages");
        Console console = System.console();
        Map<String,String> commandArguments;


        if (args.length != 3) {
            console.printf("%s %n", bundle.getString("line"));
            console.printf("%s %n", bundle.getString("input.error.message"));
            console.printf("%s %n", bundle.getString("line"));
            console.printf("%s %n %n", bundle.getString("usage.message"));
            console.printf("%s %n %n", bundle.getString("usage.example"));
            System.exit(-1);
        }
        try {
            // validate input command line arguments
            commandArguments = readAndValidateCommandParameters(args, bundle);
            int lengthLimit = Integer.parseInt(commandArguments.get(LONGER_THAN));
            String validCharacters = commandArguments.get(STARTS_WITH);

            // Convert text file into Stream
            Stream<String> wordsStream = readWordsIntoStream(commandArguments.get(FILENAME));

            // initialize list of words
            BlockingQueue lengthRestrictedList = new LinkedBlockingDeque<>();

            // initialize number of words counter
            AtomicLong atomicCounter = new AtomicLong(0);

            // Process the stream
            processStream(wordsStream, lengthRestrictedList, atomicCounter, lengthLimit, validCharacters);

            console.printf("%s %s %n", MessageFormat.format(bundle.getString("counter.message"),validCharacters), atomicCounter.toString());
            console.printf("%s %n", MessageFormat.format(bundle.getString("list.counted.message"), lengthLimit, lengthRestrictedList.size()));
            console.printf("%s %s %n", MessageFormat.format(bundle.getString("list.message"),lengthLimit),
                    lengthRestrictedList.size() > 0 ? lengthRestrictedList.toString() :
                            MessageFormat.format(bundle.getString("list.empty.message"),lengthLimit));
        }
        catch (IllegalArgumentException e) {
            console.printf("%s %n", e.getMessage());
        }
    }

    /**
     * This method calls functional methods as part of stream processing.
     * Further methods can be implemented and added here if more conditions
     * need to be checked/implemented.
     *
     * @param wordsStream
     * @param lengthRestrictedList
     * @param startCharRestrictedCounter
     * @param lengthLimit
     * @param validCharacters
     */
    protected static void processStream(Stream<String> wordsStream, Queue<String> lengthRestrictedList, AtomicLong startCharRestrictedCounter, int lengthLimit, String validCharacters) {
        wordsStream.parallel().forEach(s -> {
            add2List(s, lengthLimit, lengthRestrictedList);
            increment(s, validCharacters, startCharRestrictedCounter);
        });
    }

    /**
     * This methods will be used as a Consumer inside the Stream processing to count the
     * number of words that start with the given list of characters.
     *
     * As the Stream may be processed in parallel on different cores, atomic number used
     * to be thread safe.
     *
     * @param s - input string to check
     * @param validCharacters
     * @param atomicLong - word counter
     */
    protected static void increment(String s, String validCharacters, AtomicLong atomicLong) {
        if (checkStartChar(s,validCharacters)) {
            atomicLong.incrementAndGet();
        }
    }

    /**
     * This methods will be used as a Consumer inside the Stream processing to add the
     * words to the given list if their length are longer than given number.
     *
     * @param s
     * @param lengthLimit
     * @param lengthRestrictedList
     */
    protected static void add2List(String s, int lengthLimit, Queue<String> lengthRestrictedList) {
        if (s.length() > lengthLimit) {
            lengthRestrictedList.add(s);
        }
    }

    /**
     * Utility method to check the given string starts with valid characters.
     *
     * @param inputString
     * @param validStartingChars
     * @return boolean indicating the validity of input string.
     */
    protected static boolean checkStartChar(String inputString, String validStartingChars) {
        for (String c: validStartingChars.split(",")) {
            if (inputString.startsWith(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * This method checks the validity of command line arguments passed in as a string array and
     * throws IllegalArgumentException of not all arguments are valid.
     *
     * @param args - String array containing command line arguments.
     * @return Map<String, String> - a map containing validated arguments.
     * @throws IllegalArgumentException
     */
    protected static Map<String,String> readAndValidateCommandParameters(String[] args, ResourceBundle bundle) throws IllegalArgumentException {
        Map<String, String> cmdArgs = new HashMap<>(3);
        int validated = 0;
        for (String str: args) { // starting character constraint
            if (str.matches(STARTS_WITH_REGEX)) {
                cmdArgs.put(STARTS_WITH,str.substring(str.indexOf("=")+1));
                validated = validated | (1 << 0);
            }
            else if (str.matches(LONGER_THAN_REGEX)) { // length limit parameter
                cmdArgs.put(LONGER_THAN,str.substring(str.indexOf("=")+1));
                validated = validated | (1 << 1);
            }
            else { // input filename
                cmdArgs.put(FILENAME,str);
                validated = validated | (1 << 2);
            }
        }
        if (validated != 7) {
            throw new IllegalArgumentException(
                    bundle.getString("format.error.message"));
        }
        return cmdArgs;
    }

    /**
     * Utility method to read a text file and return the words as a Stream.
     * @param filename
     * @return Stream of words
     */
    protected static Stream<String> readWordsIntoStream(String filename) {
        List<String> words = new LinkedList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filename))) {
            lines.parallel().forEach(s -> words.addAll(Arrays.asList(s.split("\\W+"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words.stream();
    }
}
