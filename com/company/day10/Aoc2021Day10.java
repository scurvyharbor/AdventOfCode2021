package com.company.day10;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Aoc2021Day10 {
    static int syntaxCheckerBracket = 0;
    static int syntaxCheckerSquareBracket = 0;
    static int syntaxCheckerCurlyBrace = 0;
    static int syntaxCheckerArrowBrace = 0;
    static List<Long> autoCompleteScores = new ArrayList<>();

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;

        Stack<Character> stack;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                stack = new Stack<>();
                boolean incomplete = true;
                for (int i = 0; i < line.length(); i++) {
                    Character c = line.charAt(i);
                    if (isOpeningCharacter(c)) {
                        stack.push(c);
                    } else {
                        Character peek = stack.peek();
                        if (peek.equals(getOpeningCharacter(c))) {
                            stack.pop();
                        } else {
                            incomplete = false;
                            addToSyntaxCheckerScore(c);
                            break;
                        }
                    }
                }
                if (incomplete) {
                    long autoCompleteScore = 0L;
                    while (!stack.empty()) {
                        autoCompleteScore = addToAutoCompleteScore(stack.pop(), autoCompleteScore);
                    }
                    autoCompleteScores.add(autoCompleteScore);
                }
            }

            int bracketResult = syntaxCheckerBracket * 3;
            int squareResult = syntaxCheckerSquareBracket * 57;
            int curlyResult = syntaxCheckerCurlyBrace * 1197;
            int arrowResult = syntaxCheckerArrowBrace * 25137;
            System.out.println("Score for the syntax checker is " + (bracketResult + squareResult + curlyResult + arrowResult));

            int middleIndex = autoCompleteScores.size() / 2 ;
            System.out.println("Score for the autocomplete is " + autoCompleteScores.stream().sorted().collect(Collectors.toList()).get(middleIndex));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long addToAutoCompleteScore(Character c, long autoCompleteScore) {
        autoCompleteScore = autoCompleteScore * 5;
        switch (c) {
            case '(':
                autoCompleteScore++;
                break;
            case '{':
                autoCompleteScore = autoCompleteScore + 3;
                break;
            case '[':
                autoCompleteScore = autoCompleteScore + 2;
                break;
            case '<':
                autoCompleteScore = autoCompleteScore + 4;
                break;
        }

        return autoCompleteScore;
    }

    private static void addToSyntaxCheckerScore(Character c) {
        switch (c) {
            case ')':
                syntaxCheckerBracket++;
                break;
            case '}':
                syntaxCheckerCurlyBrace++;
                break;
            case ']':
                syntaxCheckerSquareBracket++;
                break;
            case '>':
                syntaxCheckerArrowBrace++;
                break;
        }
    }

    private static boolean isOpeningCharacter(Character c) {
        switch (c) {
            case '(':
            case '{':
            case '[':
            case '<':
                return true;
            default:
                return false;
        }
    }

    private static Character getOpeningCharacter(Character c) {
        switch (c) {
            case ')':
                return '(';
            case '}':
                return '{';
            case ']':
                return '[';
            case '>':
                return '<';
            default:
                return '.';
        }
    }
}
