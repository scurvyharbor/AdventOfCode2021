package com.company.day13;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Aoc2021Day13 {

    static Map<Integer, Set<Integer>> thermalMap = new HashMap<>();
    static List<FoldingInstruction> foldingInstructions = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while (!Objects.equals(line = br.readLine(), "")) {
                String[] coordinates = line.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);

                if (thermalMap.containsKey(y)) {
                    thermalMap.get(y).add(x);
                } else {
                    Set<Integer> xAxis = new HashSet<>();
                    xAxis.add(x);
                    thermalMap.put(y, xAxis);
                }
            }
            while ((line = br.readLine()) != null) {
                String[] instruction = line.split("=");
                foldingInstructions.add(new FoldingInstruction(instruction[0].charAt(instruction[0].length() - 1), Integer.parseInt(instruction[1])));
            }

            // part 1
            for (int i = 0; i < foldingInstructions.size(); i++) {
                FoldingInstruction foldingInstruction = foldingInstructions.get(i);
                HashMap<Integer, Set<Integer>> newThermalMap = new HashMap<>();

                if (foldingInstruction.axis == 'y') {
                    for (Integer dot : thermalMap.keySet()) {
                        if (dot < foldingInstruction.foldingPosition) {
                            newThermalMap.put(dot, thermalMap.get(dot));
                        } else {
                            Integer newRow = foldingInstruction.foldingPosition - (dot - foldingInstruction.foldingPosition);
                            if (newThermalMap.containsKey(newRow)) {
                                newThermalMap.get(newRow).addAll(thermalMap.get(dot));
                            } else {
                                newThermalMap.put(newRow, thermalMap.get(dot));
                            }
                        }
                    }
                } else if (foldingInstruction.axis == 'x') {
                    for (Map.Entry<Integer, Set<Integer>> row : thermalMap.entrySet()) {
                        Set<Integer> existingDots = row.getValue();
                        Set<Integer> newDots = new HashSet<>();

                        for (Integer dot : existingDots) {
                            if (dot < foldingInstruction.foldingPosition) {
                                newDots.add(dot);
                            } else {
                                newDots.add(foldingInstruction.foldingPosition - (dot - foldingInstruction.foldingPosition));
                            }
                        }
                        newThermalMap.put(row.getKey(), newDots);
                    }
                }
                thermalMap = newThermalMap;
                if (i == 0) {
                    System.out.println("There are " + countDots() + " dots visible after the first folder");
                }
            }

            // part2 Print the letters
            int maxDotPos = 0;
            for (Set<Integer> row : thermalMap.values()) {
                if (maxDotPos < Collections.max(row)) {
                    maxDotPos = Collections.max(row);
                }
            }

            for (int i = 0; i < thermalMap.size(); i++) {
                if (thermalMap.containsKey(i)) {
                    Set<Integer> row = thermalMap.get(i);
                    for (int j = 0; j < maxDotPos; j++) {
                        if (row.contains(j)) {
                            System.out.print(j < 10 ? " " + j : j);
                        } else {
                            System.out.print(" .");
                        }
                    }
                    System.out.println();
                } else {
                    // print empty row
                    for (int j = 0; j < maxDotPos; j++) {
                        System.out.print(" .");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int countDots() {
        int dots = 0;

        for (Set<Integer> row : thermalMap.values()) {
            for (Integer dot : row) {
                dots++;
            }
        }
        return dots;
    }
}

class FoldingInstruction {
    char axis;
    int foldingPosition;

    public FoldingInstruction(char axis, int foldingPosition) {
        this.axis = axis;
        this.foldingPosition = foldingPosition;
    }
}
