package com.company.day16;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Aoc2021Day16 {

    static int versionSum = 0;
    static String bitString;

    public static void main(String[] args) {
        File file = new File("input.txt");
        String line;
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            while ((line = br.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    sb.append(convertHexadecimalCharacter(c));
                }

            }
            bitString = sb.toString();

            while (validBitString(bitString)) {
                long result = parseBitString();
                System.out.println("result " + result);
            }

            System.out.println("The sum of all version " + versionSum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static long parseBitString() {
        Packet packet = new Packet();
        packet.version = Integer.parseInt(bitString.substring(0, 3), 2);
        versionSum += packet.version;
        packet.packetType = Integer.parseInt(bitString.substring(3, 6), 2);
        bitString = bitString.substring(6);
        int counter = 0;

        if (packet.packetType == 4) {
            // literal value
            StringBuilder valueString = new StringBuilder();
            String startingBit;
            do {
                valueString.append(bitString, counter + 1, counter + 5);
                startingBit = bitString.substring(counter, counter + 1);
                counter += 5;
            } while (!startingBit.equals("0"));
            bitString = bitString.substring(counter);
            return Long.parseLong(valueString.toString(), 2);
        } else {
            packet.lengthType = Integer.parseInt(bitString.substring(0, 1));
            bitString = bitString.substring(1);
            List<Long> results = new ArrayList<>();

            if (packet.lengthType == 0) {
                packet.subPacketLength = Integer.parseInt(bitString.substring(0, 15), 2);
                bitString = bitString.substring(15);
                String remainingPartOfBitString = bitString.substring(packet.subPacketLength);
                bitString = bitString.substring(0, packet.subPacketLength);
                while (validBitString(bitString)) {
                    results.add(parseBitString());
                }
                bitString = remainingPartOfBitString;
            } else {
                if (packet.lengthType == 1) {
                    packet.subPacketLength = Integer.parseInt(bitString.substring(0, 11), 2);
                    bitString = bitString.substring(11);
                    for (int i = 0; i < packet.subPacketLength; i++) {
                        results.add(parseBitString());
                    }
                }
            }

            // operator
            switch (packet.packetType) {
                // sum
                case 0:
                    return results.stream().mapToLong(Long::longValue).sum();
                // product
                case 1:
                    return results.stream().mapToLong(Long::longValue).reduce(1, Math::multiplyExact);
                //minimum
                case 2:
                    return results.stream().mapToLong(Long::longValue).min().orElseThrow(NoSuchElementException::new);
                // maximum
                case 3:
                    return results.stream().mapToLong(Long::longValue).max().orElseThrow(NoSuchElementException::new);
                // greater than
                case 5:
                    return results.get(0) > results.get(1) ? 1L : 0L;
                // less than
                case 6:
                    return results.get(0) < results.get(1) ? 1L : 0L;
                // equal to
                case 7:
                    return results.get(0).equals(results.get(1)) ? 1L : 0L;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    private static boolean validBitString(String bitString) {
        return bitString.contains("1");
    }

    private static String convertHexadecimalCharacter(char c) {

        switch (c) {
            case '0':
                return "0000";
            case '1':
                return "0001";
            case '2':
                return "0010";
            case '3':
                return "0011";
            case '4':
                return "0100";
            case '5':
                return "0101";
            case '6':
                return "0110";
            case '7':
                return "0111";
            case '8':
                return "1000";
            case '9':
                return "1001";
            case 'A':
                return "1010";
            case 'B':
                return "1011";
            case 'C':
                return "1100";
            case 'D':
                return "1101";
            case 'E':
                return "1110";
            case 'F':
                return "1111";
            default:
                throw new IllegalArgumentException();
        }
    }
}

class Packet {
    int version;
    int packetType;
    int lengthType;
    int subPacketLength;
}

