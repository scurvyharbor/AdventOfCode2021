package com.company.day9;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Aoc2021Day9 {

    static List<List<HeightPoint>> heightmap = new ArrayList<>();
    static List<Integer> basins = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        int riskLevel = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] levelArray = line.split("");
                List<HeightPoint> level = Arrays.stream(levelArray).map(l -> new HeightPoint(Integer.parseInt(l))).collect(Collectors.toList());
                heightmap.add(level);
            }

            for (int i = 0; i < heightmap.size(); i++) {
                for (int j = 0; j < heightmap.get(i).size(); j++) {
                    boolean lowestPoint = true;
                    int currentLevel = heightmap.get(i).get(j).height;
                    int upLevel;
                    int downLevel;
                    int leftHeight;
                    int rightHeight;

                    int up = i - 1;
                    if (up >= 0) {
                        upLevel = heightmap.get(up).get(j).height;
                        if (upLevel <= currentLevel) {
                            lowestPoint = false;
                        }
                    }

                    int down = i + 1;
                    if (down < heightmap.size()) {
                        downLevel = heightmap.get(down).get(j).height;
                        if (downLevel <= currentLevel) {
                            lowestPoint = false;
                        }
                    }

                    int left = j - 1;
                    if (left >= 0) {
                        leftHeight = heightmap.get(i).get(left).height;
                        if (leftHeight <= currentLevel) {
                            lowestPoint = false;
                        }
                    }

                    int right = j + 1;
                    if (right < heightmap.get(i).size()) {
                        rightHeight = heightmap.get(i).get(right).height;
                        if (rightHeight <= currentLevel) {
                            lowestPoint = false;
                        }
                    }

                    if (lowestPoint) {
                        riskLevel += currentLevel + 1;
                        isThreeLargestBasin(calculateBasinSize(j, i));
                    }
                }
            }
            System.out.println("The risk level is " + riskLevel);
            System.out.println("The three largest basins are " + basins.stream().mapToInt(x -> x).reduce(1, Math::multiplyExact));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateBasinSize(int x, int y) {
        return exploreBasin(x, y, 0);

    }

    private static boolean checkNewInBasin(int x, int y) {
        if (!heightmap.get(y).get(x).inBasin) {
            heightmap.get(y).get(x).inBasin = true;
            return true;
        }
        return false;
    }

    private static int exploreBasin(int x, int y, int size) {
        if (checkNewInBasin(x, y)) {
            size++;
        }

        int up = y - 1;
        if (up >= 0) {
            if (heightmap.get(up).get(x).height < 9) {
                if (!heightmap.get(up).get(x).inBasin) {
                    size = exploreBasin(x, up, size);
                }
            }
        }

        int down = y + 1;
        if (down < heightmap.size()) {
            if (heightmap.get(down).get(x).height < 9) {
                if (!heightmap.get(down).get(x).inBasin) {
                    size = exploreBasin(x, down, size);
                }
            }
        }

        int left = x - 1;
        if (left >= 0) {
            if (heightmap.get(y).get(left).height < 9) {
                if (!heightmap.get(y).get(left).inBasin) {
                    size = exploreBasin(left, y, size);
                }
            }
        }

        int right = x + 1;
        if (right < heightmap.get(y).size()) {
            if (heightmap.get(y).get(right).height < 9) {
                if (!heightmap.get(y).get(right).inBasin) {
                    size = exploreBasin(right, y, size);
                }
            }
        }

        return size;
    }

    private static void isThreeLargestBasin(int basinSize) {
        if (basins.size() < 3) {
            basins.add(basinSize);
            return;
        }
        int min = Collections.min(basins);
        if (min < basinSize) {

            for (int i = 0; i < 3; i++) {
                if (basins.get(i) == min) {
                    basins.set(i, basinSize);
                    break;
                }
            }
        }
    }
}

class HeightPoint {
    public HeightPoint(int height) {
        this.height = height;
    }

    int height;
    boolean inBasin;
}
