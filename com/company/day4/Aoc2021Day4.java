package com.company.day4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Aoc2021Day4 {


    static List<BingoCard> cards = new ArrayList<>();
    static final int CARD_SIZE = 5;
    static int uncheckedNumbersTotal = 0;

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        List<Integer> numbers;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            line = br.readLine();
            List<String> numbersString = new ArrayList<>(Arrays.asList(line.split(",")));
            numbers = numbersString.stream().map(Integer::parseInt).collect(Collectors.toList());

            // empty line
            br.readLine();

            while ((line = br.readLine()) != null) {

                BingoCard card = new BingoCard();
                // ROW
                for (int i = 0; i < CARD_SIZE; i++) {
                    List<String> cardRowString = Arrays.asList(line.trim().split(" {1,2}"));

                    // COLUMN
                    for (int j = 0; j < CARD_SIZE; j++) {
                        card.card[i][j] = new Number(Integer.parseInt(cardRowString.get(j)));
                    }
                    line = br.readLine();
                }
                cards.add(card);
            }

            // input has been parsed, BINGO time part 1
            int index = 0;
            int drawnNumber = 0;
            boolean bingo = false;
            while (index < numbers.size() && !bingo) {
                drawnNumber = numbers.get(index);

                checkNumber(drawnNumber);
                bingo();
                bingo = oneBoardWon();
                index++;
            }

            System.out.println("The final score to win bingo is " + drawnNumber * uncheckedNumbersTotal);
            // BINGO time part 2
            boolean allBoardsWon = false;
            while (index < numbers.size() && !allBoardsWon) {
                drawnNumber = numbers.get(index);
                checkNumber(drawnNumber);
                bingo();
                allBoardsWon = allBoardsWon();
                index++;
            }
            System.out.println("The final score to let the squid win bingo is " + drawnNumber * uncheckedNumbersTotal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean oneBoardWon() {
        for (BingoCard card : cards) {
            if (card.hasWon) {
                return true;
            }
        }
        return false;
    }

    private static boolean allBoardsWon() {
        for (BingoCard card : cards) {
            if (!card.hasWon) {
                return false;
            }
        }
        return true;
    }

    private static void checkNumber(int drawnNumber) {
        for (BingoCard card : cards) {
            for (int i = 0; i < CARD_SIZE; i++) {
                for (int j = 0; j < CARD_SIZE; j++) {
                    if (card.card[i][j].getNumber() == drawnNumber) {
                        card.card[i][j].setChecked(true);
                    }
                }
            }
        }
    }

    private static void bingo() {
        boolean bingo;
        // check rows
        for (BingoCard card : cards) {
            if (!card.hasWon) {
                for (int i = 0; i < CARD_SIZE; i++) {
                    bingo = true;
                    for (int j = 0; j < CARD_SIZE; j++) {
                        if (card.card[i][j].isChecked()) {
                            bingo = false;
                            break;
                        }
                    }
                    if (bingo) {
                        card.hasWon = true;
                        uncheckedNumbersTotal = calculateUnmarkedTotal(card);
                        break;
                    }
                }
            }
        }
        // check columns
        for (BingoCard card : cards) {
            if(!card.hasWon){
                for (int i = 0; i < CARD_SIZE; i++) {
                    bingo = true;
                    for (int j = 0; j < CARD_SIZE; j++) {
                        if (card.card[j][i].isChecked()) {
                            bingo = false;
                            break;
                        }
                    }
                    if (bingo) {
                        card.hasWon = true;
                        uncheckedNumbersTotal = calculateUnmarkedTotal(card);
                        break;
                    }
                }
            }
        }
    }

    private static int calculateUnmarkedTotal(BingoCard card) {
        uncheckedNumbersTotal = 0;
        for (int i = 0; i < CARD_SIZE; i++) {
            for (int j = 0; j < CARD_SIZE; j++) {
                if (card.card[i][j].isChecked()) {
                    uncheckedNumbersTotal += card.card[i][j].getNumber();
                }
            }
        }

        return uncheckedNumbersTotal;
    }
}

class BingoCard {
    Number[][] card = new Number[5][5];
    boolean hasWon = false;
}

class Number {
    private final int number;
    private boolean checked;

    public Number(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isChecked() {
        return !checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}