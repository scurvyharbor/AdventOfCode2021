package com.company.day8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc2021Day8 {


    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        String[] signalPatterns;
        String[] fourDigitValues;
        int easyDigits = 0;
        int sumValue = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\| ");
                signalPatterns = split[0].split(" ");
                fourDigitValues = split[1].split(" ");

                for (String value : fourDigitValues) {
                    if (validDigit(signalPatterns, value)) {
                        easyDigits++;
                    }
                }
                sumValue += decodeValue(signalPatterns, fourDigitValues);
            }

            System.out.println("part 1 " + easyDigits);
            System.out.println("part 2 " + sumValue);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int decodeValue(String[] signalPatterns, String[] fourDigitValues) {
        // 1 = 2 segments
        // 4 = 4 segments
        // 7 = 3 segments
        // 8 = 7 segments
        List<String> patterns = new ArrayList<>(Arrays.asList(signalPatterns));

        String one = Arrays.stream(signalPatterns).filter(pattern -> pattern.length() == 2).findFirst().orElse("");
        String four = Arrays.stream(signalPatterns).filter(pattern -> pattern.length() == 4).findFirst().orElse("");
        String seven = Arrays.stream(signalPatterns).filter(pattern -> pattern.length() == 3).findFirst().orElse("");
        String eight = Arrays.stream(signalPatterns).filter(pattern -> pattern.length() == 7).findFirst().orElse("");

        patterns.remove(one);
        patterns.remove(four);
        patterns.remove(seven);
        patterns.remove(eight);

        // 5, 2 & 3 = 5 segments
        String three = findThree(patterns.stream().filter(pattern -> pattern.length() == 5).collect(Collectors.toList()), seven);
        patterns.remove(three);
        String five = findFive(patterns.stream().filter(pattern -> pattern.length() == 5).collect(Collectors.toList()), four);
        patterns.remove(five);
        String two = patterns.stream().filter(pattern -> pattern.length() == 5).findFirst().orElse("");
        // 6, 9 & 0 = 6 segments
        String six = findSix(patterns.stream().filter(pattern -> pattern.length() == 6).collect(Collectors.toList()), one);
        patterns.remove(six);
        String nine = findNine(patterns.stream().filter(pattern -> pattern.length() == 6).collect(Collectors.toList()), three);
        patterns.remove(nine);
        String zero = patterns.stream().filter(pattern -> pattern.length() == 6).findFirst().orElse("");

        StringBuilder fourDigitValue = new StringBuilder();
        for (String value : fourDigitValues) {
            switch (value.length()) {
                case 2:
                    fourDigitValue.append("1");
                    break;
                case 3:
                    fourDigitValue.append("7");
                    break;
                case 4:
                    fourDigitValue.append("4");
                    break;
                case 5:
                    fourDigitValue.append(find5Value(value, three, five, two));
                    break;
                case 6:
                    fourDigitValue.append(find6Value(value, six, nine, zero));
                    break;
                case 7:
                    fourDigitValue.append("8");
                    break;
                default:
                    System.out.println("DEFAULT");
            }
            System.out.println(fourDigitValue);
        }
        System.out.println("--------------");
        return Integer.parseInt(fourDigitValue.toString());
    }

    private static String find5Value(String value, String three, String five, String two) {
        boolean ok = true;
        // three
        for (Character c : value.toCharArray()) {
            if (!three.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "3";
        }

        // five
        ok = true;
        for (Character c : value.toCharArray()) {
            if (!five.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "5";
        }

        // two
        ok = true;
        for (Character c : value.toCharArray()) {
            if (!two.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "2";
        }
        return "";
    }

    private static String find6Value(String value, String six, String nine, String zero) {
        boolean ok = true;
        // three
        for (Character c : value.toCharArray()) {
            if (!six.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "6";
        }

        // five
        ok = true;
        for (Character c : value.toCharArray()) {
            if (!nine.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "9";
        }

        // two
        ok = true;
        for (Character c : value.toCharArray()) {
            if (!zero.contains(c.toString())) {
                ok = false;
                break;
            }
        }
        if (ok) {
            return "0";
        }
        return "";
    }

    private static String findSix(List<String> patterns, String digit) {
        for (String pattern : patterns) {
            for (Character c : digit.toCharArray()) {
                if (!pattern.contains(c.toString())) {
                    return pattern;
                }
            }
        }
        return "";
    }

    private static String findNine(List<String> patterns, String digit) {
        for (String pattern : patterns) {
            boolean ok = true;
            for (Character c : digit.toCharArray()) {
                if (!pattern.contains(c.toString())) {
                    ok = false;
                    break;
                }
            }
            if(ok){
                return pattern;
            }
        }
        return "";
    }

    private static String findThree(List<String> patterns, String digit) {
        boolean ok;
        for (String pattern : patterns) {
            ok = true;
            for (Character c : digit.toCharArray()) {
                if (!pattern.contains(c.toString())) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                return pattern;
            }
        }
        return "";
    }

    private static String findFive(List<String> patterns, String digit) {
        int chars;
        for (String pattern : patterns) {
            chars = 0;
            for (Character c : digit.toCharArray()) {
                if (pattern.contains(c.toString())) {
                    chars++;
                }
            }
            if (chars == 3) {
                return pattern;
            }
        }
        return "";
    }

    private static boolean validDigit(String[] signalPatterns, String digit) {
        // 1 = 2 segments
        // 4 = 4 segments
        // 7 = 3 segments
        // 8 = 7 segments
        String signalMatch = "";
        if (digit.length() == 2 || digit.length() == 4 || digit.length() == 3 || digit.length() == 7) {

            for (String signal : signalPatterns) {
                if (signal.length() == digit.length()) {
                    signalMatch = signal;
                }
            }

            if (!signalMatch.isBlank()) {
                for (Character c : signalMatch.toCharArray()) {
                    if (!digit.contains(c.toString())) {
                        return false;
                    }
                }
                return true;
            } else return false;
        } else return false;
    }
}
