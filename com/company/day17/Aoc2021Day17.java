package com.company.day17;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Aoc2021Day17 {

    static int minX = 0;
    static int maxX = 0;
    static int minY = 0;
    static int maxY = 0;

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {


            Map<Integer, Set<Integer>> targetArea = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] targetAreaString = line.split(": ");
                String[] xAndY = targetAreaString[1].split(", ");
                String[] xComplete = xAndY[0].split("=");
                String[] yComplete = xAndY[1].split("=");
                String[] xNumbers = xComplete[1].split("\\.\\.");
                String[] YNumbers = yComplete[1].split("\\.\\.");
                minX = Integer.parseInt(xNumbers[0]);
                maxX = Integer.parseInt(xNumbers[1]);
                minY = Integer.parseInt(YNumbers[0]);
                maxY = Integer.parseInt(YNumbers[1]);
            }

            long maxYPosition = Long.MIN_VALUE;
            long currentXPosition;
            long currentYPosition;
            long hits = 0L;

            for (int startingXVelocity = -500; startingXVelocity < 500; startingXVelocity++) {
                for (int startingYVelocity = -500 ; startingYVelocity < 500; startingYVelocity++) {
                    int currentXVelocity = startingXVelocity;
                    int currentYVelocity = startingYVelocity;

                    currentXPosition = 0L;
                    currentYPosition = 0L;
                    long tempMaxYPosition = Long.MIN_VALUE;
                    while (!overshootingPosition(currentYPosition, currentXPosition) && !targetHit(currentYPosition, currentXPosition)) {
                        currentXPosition += currentXVelocity;
                        currentYPosition += currentYVelocity;

                        if (currentXVelocity != 0) {
                            if (currentXVelocity > 0)
                                currentXVelocity--;
                            else currentXVelocity++;
                        }
                        currentYVelocity--;

                        if (currentYPosition > tempMaxYPosition) {
                            tempMaxYPosition = currentYPosition;
                        }
                    }

                    if (targetHit(currentYPosition, currentXPosition)) {
                        hits++;
                        if (tempMaxYPosition > maxYPosition) {
                            maxYPosition = tempMaxYPosition;
                        }
                    }
                }
            }

            System.out.println("Max Y position " + maxYPosition);
            System.out.println("Total hits are " + hits);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean targetHit(long currentYPosition, long currentXPosition) {
        if (maxY > 0) {
            if (currentYPosition < minY || currentYPosition > maxY) {
                return false;
            }
        } else {
            if (currentYPosition < minY || currentYPosition > maxY) {
                return false;
            }
        }

        if (maxX > 0) {
            if (currentXPosition < minX || currentXPosition > maxX) {
                return false;
            }
        } else {
            if (currentXPosition < minX || currentXPosition > maxX) {
                return false;
            }
        }

        return true;
    }

    private static boolean overshootingPosition(long currentYPosition, long currentXPosition) {
        if (maxY > 0) {
            if (currentYPosition > maxY) {
                return true;
            }
        } else {
            if (currentYPosition < minY) {
                return true;
            }
        }

        if (maxX > 0) {
            if (currentXPosition > maxX) {
                return true;
            }
        } else {
            if (currentXPosition < minX) {
                return true;
            }
        }
        return false;
    }
}
