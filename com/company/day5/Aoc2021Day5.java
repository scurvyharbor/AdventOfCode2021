package com.company.day5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc2021Day5 {


    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<Line> lines = new ArrayList<>();
        final int GRID_SIZE = 1000;
        int[][] sea = new int[GRID_SIZE][GRID_SIZE];


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] coordinates = line.split(" -> ");
                Line coordinateLine = new Line();

                String[] startArray = coordinates[0].split(",");
                coordinateLine.start = new Coordinate(Integer.parseInt(startArray[0]), Integer.parseInt(startArray[1]));
                String[] endArray = coordinates[1].split(",");
                coordinateLine.end = new Coordinate(Integer.parseInt(endArray[0]), Integer.parseInt(endArray[1]));
                lines.add(coordinateLine);

            }

            // PART 1
            List<Line> coordinatesPart1 = lines.stream()
                    .filter(coordinate -> coordinate.start.x == coordinate.end.x
                            || coordinate.start.y == coordinate.end.y).collect(Collectors.toList());

            for (Line coordinateLine : coordinatesPart1) {
                int startTempX = coordinateLine.start.x;
                int startTempY = coordinateLine.start.y;
                int endTempX = coordinateLine.end.x;
                int endTempY = coordinateLine.end.y;

                boolean subtract = startTempX > endTempX;

                sea[startTempY][startTempX] = sea[startTempY][startTempX] + 1;
                while (startTempX != endTempX) {
                    startTempX = subtract ? startTempX - 1 : startTempX + 1;
                    sea[startTempY][startTempX] = sea[startTempY][startTempX] + 1;
                }

                if (startTempY > endTempY) {
                    subtract = true;
                }
                while (startTempY != endTempY) {
                    startTempY = subtract ? startTempY - 1 : startTempY + 1;
                    sea[startTempY][startTempX] = sea[startTempY][startTempX] + 1;
                }
            }

            int dangerousPoints = 0;
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    if(sea[i][j] > 1){
                        dangerousPoints++;
                    }
                }
            }

            System.out.println("There are " + dangerousPoints + " dangerous points P1");

            // PART 2
            sea = new int[GRID_SIZE][GRID_SIZE];
            for(Line coordinateLine : lines){

                int startTempX = coordinateLine.start.x;
                int startTempY = coordinateLine.start.y;
                int endTempX = coordinateLine.end.x;
                int endTempY = coordinateLine.end.y;

                boolean subtractX = startTempX > endTempX;

                boolean subtractY = startTempY > endTempY;

                sea[startTempY][startTempX] = sea[startTempY][startTempX] + 1;
                while (startTempX != endTempX || startTempY != endTempY) {

                    if(startTempX != endTempX){
                        startTempX = subtractX ? startTempX - 1 : startTempX + 1;
                    }
                    if(startTempY != endTempY){
                        startTempY = subtractY ? startTempY - 1 : startTempY + 1;
                    }
                    sea[startTempY][startTempX] = sea[startTempY][startTempX] + 1;
                }

                dangerousPoints = 0;
                for (int i = 0; i < GRID_SIZE; i++) {
                    for (int j = 0; j < GRID_SIZE; j++) {
                        if(sea[i][j] > 1){
                            dangerousPoints++;
                        }
                    }
                }

            }
            System.out.println("There are " + dangerousPoints + " dangerous points P2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Line {
    public Coordinate start;
    public Coordinate end;
}

class Coordinate {
    int x;
    int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
}