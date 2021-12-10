package com.company.day1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Aoc2021Day1 {

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        int value;
        int increase = 0;
        int sumIncrease = 0;
        List<Integer> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            int prev = Integer.parseInt(br.readLine());
            values.add(prev);

            // part 1
            while ((line = br.readLine()) != null) {
                value = Integer.parseInt(line);
                values.add(value);
                if (value > prev) {
                    increase++;
                }

                prev = value;
            }

            // part 2
            for (int i = 0; i < values.size() - 3; i++) {
                int firstSum = values.get(i) + values.get(i + 1) + values.get(i + 2);
                int secondSum = values.get(i + 1) + values.get(i + 2) + values.get(i + 3);

                if(secondSum > firstSum){
                    sumIncrease++;
                }
            }

            System.out.println("increases in part 1 " + increase);
            System.out.println("increases in part 2 " + sumIncrease);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}