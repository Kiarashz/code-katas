package com.telflow;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Test class that test methods of CountWords class using JUnit.
 *
 * @version $Id$
 */
public class CountWordsTest {

    ResourceBundle bundle = ResourceBundle.getBundle("messages");

    /**
     * Tests CountWords.readCommandParameter(String[] args) method when input parameters are valid.
     */
    @Test
    public void readCommandParameters_validInputTest() {

        String[] validArgs = {"startswith=m,M", "longerThan=3", "./words.txt"};
        Map<String,String> expected = new HashMap<>();
        expected.put(CountWords.STARTS_WITH,"m,M");
        expected.put(CountWords.LONGER_THAN, "3");
        expected.put(CountWords.FILENAME, "./words.txt");

        Map<String,String> actual = CountWords.readAndValidateCommandParameters(validArgs, bundle);
        assertEquals("Method readAndValidateCommandParameters failed to return valid Map", actual, expected);
    }

    /**
     * Tests CountWords.readCommandParameter(String[] args) method when input parameters
     * are NOT valid strings.
     */
    @Test(expected=IllegalArgumentException.class)
    public void readCommandParameters_invalidInputsTest() {

        String[] invalidArgs = {"startswith m,M", "longerthen 5", "something"};

        // expecting exception
        CountWords.readAndValidateCommandParameters(invalidArgs, bundle);
    }


    /**
     * Tests CountWords.readCommandParameter(String[] args) method when NOT all
     * input parameters are provided.
     */
    @Test(expected=IllegalArgumentException.class)
    public void readCommandParameters_invalidNumberOfParametersTest() {

        String[] invalidNumberOfArgs = {"startswith=m,M"};

        // expecting exception
        CountWords.readAndValidateCommandParameters(invalidNumberOfArgs, bundle);
    }

    /**
     * Tests CountWords.readWordsIntoStreamTest(String filename) method for a valid text file
     */
    @Test
    public void readWordsIntoStreamTest() {
        StringBuilder stringBuilder = new StringBuilder(Paths.get("").toAbsolutePath().toString());
        stringBuilder.append(File.separator);
        stringBuilder.append("src");
        stringBuilder.append(File.separator);
        stringBuilder.append("test");
        stringBuilder.append(File.separator);
        stringBuilder.append("resources");
        stringBuilder.append(File.separator);
        stringBuilder.append("words.txt");
        String filename = stringBuilder.toString();

        String[] expected = {"You","may","come","from","an","object",
                "oriented","or","a","more","diverse","development","background",
                "But","you","will","have","an","agnostic","mindset","in","terms",
                "of","technology","and","development","languages","and","keen","to",
                "gain","broader","development","experience"};
        Arrays.sort(expected);

        Stream<String> actual = CountWords.readWordsIntoStream(filename);
        List<String> actualList = actual.collect(Collectors.toList());
        String[] actualArray = new String[actualList.size()];
        actualList.toArray(actualArray);
        Arrays.sort(actualArray);
        assertTrue("The readWordsIntoStreamTest methods returns invalid stream!", Arrays.equals(expected, actualArray));
    }


    /**
     * Tests CountWords.checkStartChar(String word, String startingChars) method for a
     * valid condition.
     */
    @Test
    public void checkStartChar_trueTest() {
        String word = "Melbourne";
        String startingChars = "m,M";
        assertTrue("The method checkStartChar failed to find m/M as starting character!",
                CountWords.checkStartChar(word, startingChars));
    }

    /**
     * Tests CountWords.checkStartChar(String word, String startingChars) method for an
     * invalid condition.
     */
    @Test
    public void checkStartChar_falseTest() {
        String word = "Sydney";
        String startingChars = "m,M";
        assertFalse("The method checkStartChar failed to find m/M as starting character!",
                CountWords.checkStartChar(word, startingChars));
    }

    /**
     * Tests CountWords.increment(String inputString, String validCharacters, AtomicLong atomicLong) method
     * to check the incrementation.
     */
    @Test
    public void incrementTest() {
        String inputString = "Atomic";
        String validCharacters = "a,A";
        AtomicLong atomicLong = new AtomicLong(0);

        CountWords.increment(inputString, validCharacters, atomicLong);

        assertEquals("The increment method failed to do its job!!", 1, atomicLong.intValue());
    }

    /**
     * This method is for testing the processStream which handles the whole business logic.
     *
     */
    @Test
    public void processStream() {

        Stream<String> inputStream = Stream.of("You","may","come","from","an","Object","oriented");
        String[] expectedWords = {"come", "from", "Object", "oriented"};
        Arrays.sort(expectedWords);
        Queue<String> actualQueue = new LinkedBlockingDeque<>();
        AtomicLong actualWordCounter = new AtomicLong(0);
        String chars2StartWith = "o,O";
        int lengthLimit = 3;

        CountWords.processStream(inputStream, actualQueue, actualWordCounter, lengthLimit, chars2StartWith);

        // convert actualQueue to array to be able to compare it with expected words list.
        String[] actualWords = actualQueue.toArray(new String[actualQueue.size()]);
        Arrays.sort(actualWords);
        assertTrue("Not a valid list of words returned!", Arrays.equals(actualWords, expectedWords));
        assertEquals("Not a valid number of words returned!", 2, actualWordCounter.intValue());

    }

    /**
     * Testing CountWords.add2List(String s, int lengthLimit, List<String> list)
     *
     */
    @Test
    public void add2List() {
        String inputString = "increment";
        Queue<String> queue = new LinkedBlockingDeque<>();

        CountWords.add2List(inputString, inputString.length() - 1, queue);

        assertTrue("The add2List method modified the list incorrectly!",

                queue.contains(inputString) && queue.size() == 1);
    }
}
