package com.company.day3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aoc2021Day3 {


    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<String> fullDiagnosticsReport = new ArrayList<>();
        Map<Integer, Integer> commonBits;
        String epsilonRate = "";
        String gammaRate = "";

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                fullDiagnosticsReport.add(line);
            }
            commonBits = calculateMostCommonBit(fullDiagnosticsReport);

            // part 1 processing of commonBits
            for (Map.Entry<Integer, Integer> entry : commonBits.entrySet()) {

                if (entry.getValue() > 0) {
                    epsilonRate = epsilonRate.concat("1");
                    gammaRate = gammaRate.concat("0");
                } else {
                    epsilonRate = epsilonRate.concat("0");
                    gammaRate = gammaRate.concat("1");
                }
            }

            // part 2 processing of commonBits
            List<String> scrubber = new ArrayList<>(fullDiagnosticsReport);
            List<String> generator = new ArrayList<>(fullDiagnosticsReport);
            int scrubberIndex = 0;

            while (scrubber.size() > 1) {
                int finalScrubberIndex = scrubberIndex;
                commonBits = calculateMostCommonBit(scrubber);
                int value = commonBits.get(scrubberIndex);
                scrubberIndex++;

                if (value > 0) {
                    scrubber.removeIf(diagnostic -> scrubber.size() > 1 && diagnostic.charAt(finalScrubberIndex) == '1');
                } else {
                    if (value < 0) {
                        scrubber.removeIf(diagnostic -> scrubber.size() > 1 && diagnostic.charAt(finalScrubberIndex) == '0');
                    } else {
                        scrubber.removeIf(diagnostic -> scrubber.size() > 1 && diagnostic.charAt(finalScrubberIndex) == '1');
                    }
                }
            }

            int generatorIndex = 0;
            while (generator.size() > 1) {
                commonBits = calculateMostCommonBit(generator);
                int value = commonBits.get(generatorIndex);
                int finalGeneratorIndex = generatorIndex;
                generatorIndex++;
                if (value > 0) {
                    generator.removeIf(diagnostic -> generator.size() > 1 && diagnostic.charAt(finalGeneratorIndex) == '0');
                } else {
                    if (value < 0) {
                        generator.removeIf(diagnostic -> generator.size() > 1 && diagnostic.charAt(finalGeneratorIndex) == '1');
                    } else {
                        generator.removeIf(diagnostic -> generator.size() > 1 && diagnostic.charAt(finalGeneratorIndex) == '0');
                    }
                }
                generator.forEach(System.out::println);
            }


            int epsilon = Integer.parseInt(epsilonRate, 2);
            int gamma = Integer.parseInt(gammaRate, 2);

            System.out.println("Power consumption of the submarine is " + (epsilon * gamma));

            int generatorRating = Integer.parseInt(generator.get(0), 2);
            int scrubberRating = Integer.parseInt(scrubber.get(0), 2);
            System.out.println("Life support rating of the submarine is " + (generatorRating * scrubberRating));
        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<Integer, Integer> calculateMostCommonBit(List<String> diagnosticReport) {
        Map<Integer, Integer> commonBits = new HashMap<>();
        for (String diagnostic : diagnosticReport) {
            for (int i = 0; i < diagnostic.length(); i++) {
                char bit = diagnostic.charAt(i);
                if (bit == '1') {
                    if (commonBits.containsKey(i)) {
                        commonBits.put(i, commonBits.get(i) + 1);
                    } else {
                        commonBits.put(i, 1);
                    }
                } else {
                    if (commonBits.containsKey(i)) {
                        commonBits.put(i, commonBits.get(i) - 1);
                    } else {
                        commonBits.put(i, -1);
                    }
                }
            }
        }
        return commonBits;
    }
}
