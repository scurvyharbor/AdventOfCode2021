package com.company.day2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;

public class Aoc2021Day2 {

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        int depthP1 = 0;
        int depthP2 = 0;
        int position = 0;
        int aim = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] splitInput = line.split(" ");
                final String command = splitInput[0];
                final int movement = Integer.parseInt(splitInput[1]);
                switch (command) {
                    case "forward":
                        position += movement;
                        depthP2 += (movement * aim);
                        break;
                    case "up":
                        depthP1 -= movement;
                        aim -= movement;
                        break;
                    case "down":
                        depthP1 += movement;
                        aim += movement;
                        break;

                }
            }
            System.out.println("result part 1 " + (depthP1 * position));
            System.out.println("result part 1 " + (depthP2 * position));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}