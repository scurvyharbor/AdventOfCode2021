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
            Map<String, Long> polymer = new HashMap<>();
            for (int i = 0; i < template.length() - 2; i++) {
                polymer.put(template.substring(i, i + 2), 1L);
                polymer.put(template.substring(i + 1, i + 3), 1L);
            }
            // skip empty line
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] pair = line.split(" -> ");
                pairInsertionRules.put(pair[0], pair[1]);
            }

            for (int i = 0; i < STEPS; i++) {
                Map<String, Long> newPolymer = new HashMap<>();
                for (String currentPolymer : polymer.keySet()) {
                    if (pairInsertionRules.containsKey(currentPolymer)) {
                        Long amount = polymer.get(currentPolymer);
                        String firstNewPair = currentPolymer.charAt(0) + pairInsertionRules.get(currentPolymer);
                        String secondNewPair = pairInsertionRules.get(currentPolymer) + currentPolymer.charAt(1);

                        newPolymer.put(firstNewPair, newPolymer.containsKey(firstNewPair) ? newPolymer.get(firstNewPair) + amount : amount);
                        newPolymer.put(secondNewPair, newPolymer.containsKey(secondNewPair) ? newPolymer.get(secondNewPair) + amount : amount);
                    } else {
                        newPolymer.put(currentPolymer, polymer.get(currentPolymer));
                    }
                }
                polymer = newPolymer;
            }
            Map<Character, Long> letterCount = new HashMap<>();
            for (Map.Entry<String, Long> element : polymer.entrySet()) {
                String poly = element.getKey();
                Long amount = element.getValue();

                letterCount.put(poly.charAt(0), letterCount.containsKey(poly.charAt(0)) ? letterCount.get(poly.charAt(0)) + amount : amount);
                letterCount.put(poly.charAt(1), letterCount.containsKey(poly.charAt(1)) ? letterCount.get(poly.charAt(1)) + amount : amount);

            }

            Long min = Collections.min(letterCount.values());
            Long max = Collections.max(letterCount.values());
            // divide by 2 for counting letters twice
            System.out.println((max - min) / 2 + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
