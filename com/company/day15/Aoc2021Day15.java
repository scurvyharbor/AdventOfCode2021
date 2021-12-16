package com.company.day15;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Aoc2021Day15 {
    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<List<Integer>> grid = new ArrayList<>();
        List<List<Integer>> gridPart2;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                List<Integer> row = Arrays.stream(line.split("")).map(Integer::parseInt).collect(Collectors.toList());
                grid.add(row);
            }

            Graph graph = dijkstra(grid);


            int ySize = graph.graph.size() - 1;
            int xSize = graph.graph.get(0).size() - 1;
            System.out.println("For part 1 the shortest path is " + graph.graph.get(ySize).get(xSize).distance);

            gridPart2 = new ArrayList<>(grid);
            // expand columns
            for (int i = 1; i < 5; i++) {
                for (List<Integer> row : gridPart2) {
                    List<Integer> newRow = new ArrayList<>();
                    // look at the original grid for the original row size
                    for (int j = 0; j < xSize + 1; j++) {
                        int newWeight = row.get(j) + i;
                        if (newWeight >= 10) {
                            newWeight = newWeight - 9;
                        }
                        newRow.add(newWeight);
                    }
                    row.addAll(newRow);
                }
            }

            // expand rows
            for (int i = 1; i < 5; i++) {
                List<List<Integer>> newPartGrid = new ArrayList<>();
                for (int j = 0; j <= xSize; j++) {
                    List<Integer> row = gridPart2.get(j);
                    List<Integer> newRow = new ArrayList<>();

                    for (Integer weight : row) {
                        int newWeight = weight + i;
                        if (newWeight >= 10) {
                            newWeight = newWeight - 9;
                        }
                        newRow.add(newWeight);
                    }
                    newPartGrid.add(newRow);
                }
                gridPart2.addAll(newPartGrid);
            }

            graph = dijkstra(gridPart2);

            ySize = graph.graph.size() - 1;
            xSize = graph.graph.get(0).size() - 1;
            System.out.println("For part 2 the shortest path is " + graph.graph.get(ySize).get(xSize).distance);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Graph dijkstra(List<List<Integer>> grid) {
        Graph graph = new Graph();

        for (int y = 0; y < grid.size(); y++) {
            List<Node> nodeRow = new ArrayList<>();
            for (int x = 0; x < grid.get(y).size(); x++) {
                Node node = new Node(grid.get(y).get(x), x, y);
                nodeRow.add(node);

                // up
                int up = y - 1;
                if (up >= 0) {
                    node.neighbours.add(new Node(grid.get(up).get(x), x, up));
                }
                // down
                int down = y + 1;
                if (down < grid.size()) {
                    node.neighbours.add(new Node(grid.get(down).get(x), x, down));
                }
                // left
                int left = x - 1;
                if (left >= 0) {
                    node.neighbours.add(new Node(grid.get(y).get(left), left, y));
                }
                // right
                int right = x + 1;
                if (right < grid.get(y).size()) {
                    node.neighbours.add(new Node(grid.get(y).get(right), right, y));
                }
            }
            graph.graph.add(nodeRow);
        }

        Node currentNode = graph.graph.get(0).get(0);
        currentNode.distance = 0;
        currentNode.weight = 0;

        PriorityQueue<Node> toBeVisitedNodes = new PriorityQueue<>(Comparator.comparingInt(o -> o.distance));

        while (currentNode != null) {
            // update distance of all neighbour nodes
            for (Node neighbour : currentNode.neighbours) {
                int newDistance = neighbour.weight + currentNode.distance;
                Node graphNeighbour = graph.graph.get(neighbour.y).get(neighbour.x);

                if (!graphNeighbour.visited) {
                    if (graphNeighbour.distance > newDistance) {
                        graphNeighbour.distance = newDistance;
                    }
                    if (!toBeVisitedNodes.contains(graphNeighbour)) {
                        toBeVisitedNodes.add(graphNeighbour);
                    }
                }
            }
            currentNode.visited = true;
            currentNode = toBeVisitedNodes.poll();
        }
        return graph;
    }
}

class Graph {
    List<List<Node>> graph;

    public Graph() {
        this.graph = new ArrayList<>();
    }
}

class Node {
    int weight;
    boolean visited;
    List<Node> neighbours;
    int distance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return x == node.x && y == node.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    int x;
    int y;

    public Node(Integer weight, int x, int y) {
        this.weight = weight;
        this.x = x;
        this.y = y;
        this.visited = false;
        this.neighbours = new ArrayList<>();
        this.distance = Integer.MAX_VALUE;
    }
}
