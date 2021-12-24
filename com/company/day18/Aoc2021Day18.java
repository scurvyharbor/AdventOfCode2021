package com.company.day18;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Aoc2021Day18 {

    static List<Long> explodedPair = new ArrayList<>();
    static boolean canExplode = false;
    static boolean exploded = false;
    static boolean canSplit = false;
    static boolean splitted = false;
    static JSONArray magnitude;
    static boolean magnitudet = false;
    static List<String> lines = new ArrayList<>();
    static JSONParser parser = new JSONParser();

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String temp;
            line = br.readLine();
            lines.add(line);
            temp = line;
            JSONArray parsedSnailNumber = new JSONArray();

            while ((line = br.readLine()) != null) {
                lines.add(line);
                // first add the new line to temp
                if (!temp.isBlank()) {
                    line = "[" +
                            temp +
                            "," +
                            line +
                            "]";
                }

                parsedSnailNumber = reduce((JSONArray) parser.parse(line));

                temp = parsedSnailNumber.toString();
            }

            magnitude = parsedSnailNumber;
            while (hasJsonArray(magnitude)) {
                magnitudet = false;
                calculateMagnitude(magnitude);
            }

            assert magnitude != null;
            System.out.println("Magnitude " + ((3 * (long) magnitude.get(0)) + (2 * (long) magnitude.get(1))));

            long largestMagnitude = Long.MIN_VALUE;

            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.size(); j++) {
                    if (i != j) {

                        String sb = "[" +
                                lines.get(i) +
                                "," +
                                lines.get(j) +
                                "]";
                        magnitude = reduce((JSONArray) parser.parse(sb));
                        while (hasJsonArray(magnitude)) {
                            magnitudet = false;
                            calculateMagnitude(magnitude);
                        }

                        assert magnitude != null;
                        long newMagnitude = ((3 * (long) magnitude.get(0)) + (2 * (long) magnitude.get(1)));
                        largestMagnitude = Math.max(newMagnitude, largestMagnitude);

                        sb = "[" +
                                lines.get(j) +
                                "," +
                                lines.get(i) +
                                "]";
                        magnitude = reduce((JSONArray) parser.parse(sb));
                        while (hasJsonArray(magnitude)) {
                            magnitudet = false;
                            calculateMagnitude(magnitude);
                        }

                        assert magnitude != null;
                        newMagnitude = ((3 * (long) magnitude.get(0)) + (2 * (long) magnitude.get(1)));
                        largestMagnitude = Math.max(newMagnitude, largestMagnitude);

                    }
                }
            }

            System.out.println("Largest magnitude " + largestMagnitude);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private static JSONArray reduce(JSONArray parsedSnailNumber) throws ParseException {
        canExplode = false;
        canExplode(parsedSnailNumber, 0);

        canSplit = false;
        canSplit(parsedSnailNumber);

        while (canExplode || canSplit) {

            while (canExplode) {
                parsedSnailNumber = doExplode(parser, parsedSnailNumber);
            }

            canSplit = false;
            canSplit(parsedSnailNumber);
            if (canSplit) {
                doSplit(parsedSnailNumber);
            }
            canExplode = false;
            canExplode(parsedSnailNumber, 0);
        }
        return parsedSnailNumber;
    }

    private static void calculateMagnitude(JSONArray magnitude) {
        for (int i = 0; i < magnitude.size(); i++) {
            Object o = magnitude.get(i);

            if (o instanceof JSONArray) {
                JSONArray array = (JSONArray) o;
                if (hasJsonArray(array)) {
                    calculateMagnitude(array);
                } else {
                    if (!magnitudet) {

                        long left = (long) array.get(0);
                        long right = (long) array.get(1);

                        magnitude.set(i, ((3 * left) + (2 * right)));
                    }

                }
            }
        }
    }

    private static void doSplit(JSONArray parsedSnailNumber) {
        splitted = false;
        split(parsedSnailNumber);
        canSplit = false;
        canSplit(parsedSnailNumber);
    }

    private static JSONArray doExplode(JSONParser parser, JSONArray parsedSnailNumber) throws ParseException {
        exploded = false;
        explode(parsedSnailNumber, 1);

        String snailString = parsedSnailNumber.toString();

        String[] explodedString = snailString.split("-1");
        String beforeExplosion = explodedString[0];

        // search first number starting from the back
        int numberIndex;
        for (numberIndex = beforeExplosion.length() - 1; numberIndex > 0; numberIndex--) {
            if (Character.isDigit(beforeExplosion.charAt(numberIndex))) {
                break;
            }
        }

        if (numberIndex > 0) {
            int numberStart = numberIndex;
            while (Character.isDigit(beforeExplosion.charAt(numberStart))) {
                numberStart--;
            }

            long newNumber = Long.parseLong(beforeExplosion.substring(numberStart + 1, numberIndex + 1)) + explodedPair.get(0);
            StringBuilder builder = new StringBuilder(beforeExplosion);
            builder.replace(numberStart + 1, numberIndex + 1, String.valueOf(newNumber));
            beforeExplosion = builder.toString();
        }

        String afterExplosion = explodedString[1];
        // search first number starting from the start

        for (numberIndex = 0; numberIndex < afterExplosion.length(); numberIndex++) {
            if (Character.isDigit(afterExplosion.charAt(numberIndex))) {
                break;
            }
        }

        if (numberIndex > 0 && numberIndex != afterExplosion.length()) {
            int numberEnd = numberIndex + 1;
            while (Character.isDigit(afterExplosion.charAt(numberEnd))) {
                numberEnd++;
            }

            long newNumber = Long.parseLong(afterExplosion.substring(numberIndex, numberEnd)) + explodedPair.get(1);
            StringBuilder builder = new StringBuilder(afterExplosion);
            builder.replace(numberIndex, numberEnd, String.valueOf(newNumber));
            afterExplosion = builder.toString();
        }
        snailString = beforeExplosion + "0" + afterExplosion;
        parsedSnailNumber = (JSONArray) parser.parse(snailString);
        canExplode = false;
        canExplode(parsedSnailNumber, 0);
        return parsedSnailNumber;
    }

    private static void split(JSONArray parsedSnailNumber) {
        for (int i = 0; i < parsedSnailNumber.size(); i++) {
            Object o = parsedSnailNumber.get(i);

            if (o instanceof JSONArray) {
                JSONArray temp = (JSONArray) o;
                split(temp);
            } else {
                if (o instanceof Long) {
                    if ((Long) o >= 10L && !splitted) {
                        splitted = true;
                        long currentNumber = (Long) o;
                        parsedSnailNumber.remove(i);
                        JSONArray splittedNumber = new JSONArray();

                        splittedNumber.add(Math.floorDiv(currentNumber, 2));
                        splittedNumber.add((currentNumber + 1) / 2);
                        parsedSnailNumber.add(i, splittedNumber);
                        return;
                    }
                }
            }
        }
    }

    private static void canSplit(JSONArray parsedSnailNumber) {
        for (Object o : parsedSnailNumber) {
            if (o instanceof JSONArray) {
                JSONArray temp = (JSONArray) o;
                canSplit(temp);
            } else {
                if ((Long) o >= 10L) {
                    canSplit = true;
                }
            }
        }
    }

    private static void explode(JSONArray snailNumber, int level) {

        for (int i = 0; i < snailNumber.size(); i++) {
            if (snailNumber.get(i) instanceof JSONArray) {
                JSONArray number = (JSONArray) snailNumber.get(i);

                if (level >= 4 && !hasJsonArray(number) && !exploded) {
                    exploded = true;
                    explodedPair = new ArrayList<>();
                    for (Object n : number) {
                        explodedPair.add((Long) n);
                    }

                    snailNumber.remove(i);
                    snailNumber.add(i, -1);

                    return;
                }
                explode(number, level + 1);
            }
        }
    }

    private static void canExplode(JSONArray snailNumber, int level) {
        for (Object o : snailNumber) {
            if (o instanceof JSONArray) {
                JSONArray temp = (JSONArray) o;
                canExplode(temp, level + 1);
            } else {
                if (level >= 4 && !canExplode) {
                    canExplode = true;
                    return;
                }
            }
        }
    }

    private static boolean hasJsonArray(JSONArray o) {
        if (o == null) {
            return false;
        }

        for (Object object : o) {
            if (object instanceof JSONArray) {
                return true;
            }
        }
        return false;
    }
}

