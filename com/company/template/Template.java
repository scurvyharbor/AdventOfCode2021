package com.company.template;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Template {

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                System.out.println("Template " + line);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}