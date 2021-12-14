package com.company.day12;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Aoc2021Day12 {

    static List<List<Cave>> paths = new ArrayList<>();
    static Map<String, List<Cave>> accessibleCaves = new HashMap<>();
    static Set<Cave> caveInformation = new HashSet<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<Connection> connections = new ArrayList<>();


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            // read input and objectify them for easy of processing
            while ((line = br.readLine()) != null) {
                String[] caves = line.split("-");
                Connection connection = new Connection();

                Cave from = new Cave(caves[0], Character.isLowerCase(caves[0].charAt(0)));
                Cave to = new Cave(caves[1], Character.isLowerCase(caves[1].charAt(0)));

                connection.from = from;
                connection.to = to;

                connections.add(connection);
            }

            // construct a map that keeps track what caves are accessible from a certain cave
            for (Connection connection : connections) {

                caveInformation.add(connection.to);
                caveInformation.add(connection.from);

                if (accessibleCaves.containsKey(connection.from.name)) {
                    accessibleCaves.get(connection.from.name).add(connection.to);
                } else {
                    List<Cave> path = new ArrayList<>();
                    path.add(connection.to);
                    accessibleCaves.put(connection.from.name, path);
                }

                // TODO why check here for start?
                if (!connection.from.name.equals("start")) {
                    if (accessibleCaves.containsKey(connection.to.name)) {
                        accessibleCaves.get(connection.to.name).add(connection.from);
                    } else {
                        List<Cave> path = new ArrayList<>();
                        path.add(connection.from);
                        accessibleCaves.put(connection.to.name, path);
                    }
                }
            }


            List<Cave> cavesConnectedToStart = accessibleCaves.get("start");

            for (Cave startCave : cavesConnectedToStart) {
                List<Cave> path = new ArrayList<>();
                path.add(new Cave("start", true));
                path.add(startCave);

                nextCave(startCave, path);
            }

            // Part 1
            System.out.println("There are " + paths.size() + " paths that visit at most one small cave");

            // Part 2
            // TODO brute forcing is a bad idea but works though, DFS is a better approach
            paths = new ArrayList<>();
            // get a list of all small caves
            List<String> smallCaves = accessibleCaves.keySet().stream().filter(s -> Character.isLowerCase(s.charAt(0))).collect(Collectors.toList());

            for (String smallCave : smallCaves) {
                for (Cave startCave : cavesConnectedToStart) {
                    List<Cave> path = new ArrayList<>();
                    path.add(new Cave("start", true));
                    path.add(startCave);

                    nextCavePart2(startCave, path, smallCave);
                }
            }
            System.out.println("There are " + paths.size() + " paths that visit one small cave possibly 2 times");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void nextCave(Cave cave, List<Cave> path) {
        for (Cave nextCave : accessibleCaves.get(cave.name)) {
            if (nextCave.name.equals("end")) {
                path.add(nextCave);
                paths.add(path);
            } else {
                Cave caveInfo = caveInformation.stream().filter(c -> c.equals(nextCave)).findFirst().orElse(null);
                if (caveInfo != null) {
                    if (!(caveInfo.smallCave && path.contains(nextCave))) {
                        List<Cave> newPath = new ArrayList<>(path);
                        newPath.add(nextCave);
                        nextCave(nextCave, newPath);
                    }
                }
            }
        }
    }

    public static void nextCavePart2(Cave cave, List<Cave> path, String smallCave) {
        for (Cave nextCave : accessibleCaves.get(cave.name)) {
            if (nextCave.name.equals("end")) {
                path.add(nextCave);
                //printPath(path);
                addPath(path);
            } else {
                Cave caveInfo = caveInformation.stream().filter(c -> c.equals(nextCave)).findFirst().orElse(null);
                if (caveInfo != null) {
                    if (validCave(caveInfo, path, smallCave)) {
                        List<Cave> newPath = new ArrayList<>(path);
                        newPath.add(nextCave);
                        nextCavePart2(nextCave, newPath, smallCave);
                    }
                }
            }
        }
    }

    public static void addPath(List<Cave> path) {
        boolean valid = true;
        for (List<Cave> p : paths) {
            if (p.equals(path)) {
                valid = false;
                break;
            }
        }

        if (valid) {
            paths.add(path);
        }
    }

    public static boolean validCave(Cave caveInfo, List<Cave> path, String smallCave) {
        if (caveInfo.name.equals("start")) {
            return false;
        }

        if (caveInfo.smallCave) {
            if (caveInfo.name.equals(smallCave)) {
                List<Cave> visitedSmall = path.stream().filter(c -> c.name.equals(smallCave)).collect(Collectors.toList());
                return visitedSmall.size() < 2;
            }

            return !path.contains(caveInfo);
        } else {
            return true;
        }
    }
}

class Connection {
    Cave from;
    Cave to;
}

class Cave {
    public Cave(String name, boolean smallCave) {
        this.name = name;
        this.smallCave = smallCave;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cave cave = (Cave) o;
        return name.equals(cave.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    String name;
    boolean smallCave;

}
