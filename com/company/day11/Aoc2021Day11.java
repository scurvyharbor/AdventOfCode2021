package com.company.day11;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc2021Day11 {

    static List<List<Octopus>> octopusEnergyLevels = new ArrayList<>();
    static long flashes = 0L;
    static int currentStep;

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        final int STEPS = 1000;


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                octopusEnergyLevels.add(new ArrayList<>(Arrays.stream(line.split("")).map(l -> new Octopus(Integer.parseInt(l))).collect(Collectors.toList())));
            }

            for (currentStep = 0; currentStep < STEPS; currentStep++) {
                increaseAllLevels();
                checkForFlashes();
                resetFlashes();
                if (allOctopusFlashed()) {
                    break;
                }
            }
            System.out.println("Total flashes " + flashes);
            System.out.println("Combined flash at step " + currentStep + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean allOctopusFlashed() {
        for (List<Octopus> octopusEnergyLevel : octopusEnergyLevels) {
            for (Octopus octopus : octopusEnergyLevel) {
                if (octopus.energyLevel > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void resetFlashes() {
        for (List<Octopus> octopusEnergyLevel : octopusEnergyLevels) {
            for (Octopus octopus : octopusEnergyLevel) {
                octopus.flashed = false;
            }
        }
    }

    private static void checkForFlashes() {
        while (hasFlashes()) {
            for (int y = 0; y < octopusEnergyLevels.size(); y++) {
                for (int x = 0; x < octopusEnergyLevels.get(y).size(); x++) {
                    if (octopusEnergyLevels.get(y).get(x).energyLevel > 9) {
                        if (currentStep < 100) {
                            flashes++;
                        }
                        octopusEnergyLevels.get(y).get(x).energyLevel = 0;
                        octopusEnergyLevels.get(y).get(x).flashed = true;
                        increaseEnergyLevelAdjacent(y, x);
                    }
                }
            }
        }
    }

    private static void increaseEnergyLevelAdjacent(int y, int x) {
        int[] cells = new int[]{-1, 0, 1};

        // row above current Octopus
        int rowAbove = y - 1;
        if (rowAbove >= 0) {
            for (int cell : cells) {
                int column = x + cell;
                if (column >= 0 && column < octopusEnergyLevels.get(rowAbove).size()) {
                    Octopus octopus = octopusEnergyLevels.get(rowAbove).get(column);
                    if (!octopus.flashed) {
                        octopus.energyLevel = octopus.energyLevel + 1;
                    }
                }
            }
        }

        // row below current Octopus
        int rowBelow = y + 1;
        if (rowBelow < octopusEnergyLevels.size()) {
            for (int cell : cells) {
                int column = x + cell;
                if (column >= 0 && column < octopusEnergyLevels.get(rowBelow).size()) {
                    Octopus octopus = octopusEnergyLevels.get(rowBelow).get(column);
                    if (!octopus.flashed) {
                        octopus.energyLevel = octopus.energyLevel + 1;
                    }
                }
            }
        }

        // cells left & right from current Octopus
        int columnLeft = x - 1;
        if (columnLeft >= 0) {
            Octopus octopus = octopusEnergyLevels.get(y).get(columnLeft);
            if (!octopus.flashed) {
                octopus.energyLevel = octopus.energyLevel + 1;
            }
        }
        int columnRight = x + 1;
        if (columnRight < octopusEnergyLevels.get(y).size()) {
            Octopus octopus = octopusEnergyLevels.get(y).get(columnRight);
            if (!octopus.flashed) {
                octopus.energyLevel = octopus.energyLevel + 1;
            }
        }
    }

    private static boolean hasFlashes() {
        for (List<Octopus> octopusEnergyLevel : octopusEnergyLevels) {
            for (Octopus octopus : octopusEnergyLevel) {
                if (octopus.energyLevel > 9) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void increaseAllLevels() {
        for (List<Octopus> octopusEnergyLevel : octopusEnergyLevels) {
            for (Octopus octopus : octopusEnergyLevel) {
                octopus.energyLevel = octopus.energyLevel + 1;
            }
        }
    }
}

class Octopus {
    int energyLevel;
    boolean flashed;

    public Octopus(int energyLevel) {
        this.energyLevel = energyLevel;
        flashed = false;
    }
}
