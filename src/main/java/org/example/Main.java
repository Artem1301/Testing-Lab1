package org.example;

import java.util.Scanner;

import static org.example.LineInputs.*;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Line line1 = input1_2(scanner);
        Line line2 = input1_2(scanner);
        Line line3 = input3(scanner);

        LineBuilder lineBuilder = new LineBuilder();
        String result = lineBuilder.analyzer(line1, line2, line3);

        System.out.println("Результат: ");
        System.out.println(result);
    }
}