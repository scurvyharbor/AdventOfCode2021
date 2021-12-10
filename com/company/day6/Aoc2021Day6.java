package com.company.day6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Aoc2021Day6 {


    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        final int DAYS = 256;
        Map<Long, Long> fish = new HashMap<>();


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                String[] lineArray = line.split(",");
                //fish.addAll(Arrays.stream(lineArray).map(Long::parseInt).collect(Collectors.toList()));
                for (String l : lineArray) {
                    if (fish.containsKey(Long.parseLong(l))) {
                        long currentFish = fish.get(Long.parseLong(l));
                        fish.put(Long.parseLong(l), currentFish + 1);
                    } else {
                        fish.put(Long.parseLong(l), 1L);
                    }
                }
            }
            for (int i = 1; i <= DAYS; i++) {
                Map<Long, Long> temp = new HashMap<>();

                for (var entry : fish.entrySet()) {
                    long newLifeStage = entry.getKey() -1;
                    if(newLifeStage < 0){
                        temp.put(8L, entry.getValue());

                        if(temp.containsKey(6L)){
                            long current6 = temp.get(6L);
                            temp.put(6L, current6 + entry.getValue());
                        }else{
                            temp.put(6L, entry.getValue());
                        }
                    }else{
                        if(newLifeStage == 6){
                            if(temp.containsKey(6L)){
                                long current6 = temp.get(6L);
                                temp.put(6L, current6 + entry.getValue());
                            }else{
                                temp.put(6L, entry.getValue());
                            }
                        }else{
                            temp.put(newLifeStage, entry.getValue());
                        }
                    }
                }

                fish = new HashMap<>(temp);
                if (i == 80) {
                    System.out.println("There are " + countFish(fish) + " fish in the sea after 80 days");
                }
            }

            System.out.println("There are " + countFish(fish)+ " fish in the sea after 256 days");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long countFish(Map<Long, Long> fish){
        long numberOfFish = 0;
        for (var entry : fish.entrySet()) {
            numberOfFish += entry.getValue();
        }

        return numberOfFish;
    }

}
