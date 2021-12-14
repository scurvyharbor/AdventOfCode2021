package com.company.day14;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Aoc2021Day14 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        Map<String, String> pairInsertionRules = new HashMap<>();
        String lastPolymereAdded = "";
        final int STEPS = 40;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String template = br.readLine();
            // save the first character of the polymer, we can add 1 to the lettercount as this one is not counted twice
            Character firstChar = template.charAt(0);

            Map<String, Long> polymer = new HashMap<>();
            for (int i = 0; i < template.length() - 1; i++) {
                polymer.put(template.substring(i, i + 2), polymer.containsKey(template.substring(i, i + 2)) ? polymer.get(template.substring(i, i + 2)) + 1L : 1L);
            }
            // skip empty line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] pair = line.split(" -> ");
                pairInsertionRules.put(pair[0], pair[1]);
            }

            for (int i = 0; i < STEPS; i++) {
                // we will use this to add the create the new polymer created in this step
                Map<String, Long> newPolymer = new HashMap<>();

                // loop over the current polymer
                for (String currentPolymer : polymer.keySet()) {

                    // this polymer has an insertion rule, so we can duplicate it
                    if (pairInsertionRules.containsKey(currentPolymer)) {
                        // amount of the current polymer that is going to be duplicated
                        Long amount = polymer.get(currentPolymer);

                        String firstNewPair = currentPolymer.charAt(0) + pairInsertionRules.get(currentPolymer);
                        String secondNewPair = pairInsertionRules.get(currentPolymer) + currentPolymer.charAt(1);
                        // save the last added polymer, so that we can use the last letter to add 1 in the lettercount as this is not counted twice
                        lastPolymereAdded = secondNewPair;

                        // add the new pairs to the new polymer
                        newPolymer.put(firstNewPair, newPolymer.containsKey(firstNewPair) ? newPolymer.get(firstNewPair) + amount : amount);
                        newPolymer.put(secondNewPair, newPolymer.containsKey(secondNewPair) ? newPolymer.get(secondNewPair) + amount : amount);
                    } else {
                        // no insertion rule, just add it to the new polymer
                        newPolymer.put(currentPolymer, polymer.get(currentPolymer));
                    }
                }
                polymer = newPolymer;
            }

            // keep track of how many times each letter occurs in the polymer
            Map<Character, Long> letterCount = new HashMap<>();
            for (Map.Entry<String, Long> element : polymer.entrySet()) {
                String poly = element.getKey();
                Long amount = element.getValue();

                letterCount.put(poly.charAt(0), letterCount.containsKey(poly.charAt(0)) ? letterCount.get(poly.charAt(0)) + amount : amount);
                letterCount.put(poly.charAt(1), letterCount.containsKey(poly.charAt(1)) ? letterCount.get(poly.charAt(1)) + amount : amount);
            }

            // first letter is not counted twice, add 1 here so the result can be divided by 2
            letterCount.put(firstChar, letterCount.get(firstChar) + 1);
            // last letter is not counted twice, add 1 here so the result can be divided by 2
            letterCount.put(lastPolymereAdded.charAt(1), letterCount.get(lastPolymereAdded.charAt(1)) + 1);

            Long min = Collections.min(letterCount.values());
            Long max = Collections.max(letterCount.values());
            // divide by 2 for counting letters twice
            long absValue = max - min;
            System.out.println(absValue / 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
