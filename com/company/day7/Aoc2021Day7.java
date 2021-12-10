package com.company.day7;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc2021Day7 {

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<Integer> positions = new ArrayList<>();
        long smallestDistance = Long.MAX_VALUE;
        long smallestDistance2 = Long.MAX_VALUE;


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(",");
                positions.addAll(Arrays.stream(lineArray).map(Integer::parseInt).collect(Collectors.toList()));
            }

            int maxValue = positions.stream().max(Comparator.comparing(Integer::valueOf)).orElse(0);

            for (int i = 0; i < maxValue; i++) {
                long temp = 0L;
                long temp2 = 0L;
                for (Integer position : positions) {
                    long fuel = Math.abs(position - i);
                    temp += fuel;
                    temp2 += (fuel + 1) * fuel / 2;
                }

                if (temp < smallestDistance) {
                    smallestDistance = temp;
                }

                if (temp2 < smallestDistance2) {
                    smallestDistance2 = temp2;
                }
            }

            System.out.println(smallestDistance);
            System.out.println(smallestDistance2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
