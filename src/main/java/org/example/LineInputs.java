package org.example;

import java.util.Scanner;

import static org.example.LineBuilder.printErrorAndSolutionMessages;
import static org.example.LineBuilder.readNumbers;

public class LineInputs {
    static Line input1_2(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введіть a та b (довжини відрізків на Ox та Oy):");
                double[] numbers = readNumbers(scanner, 2);
                return Line.fromGeneralForm(numbers[1], numbers[0]);

            } catch (NumberFormatException e) {
                printErrorAndSolutionMessages(
                        "Введено текст або нерозпізнані символи.",
                        "Введіть 2 числа через пробіл."
                );
            } catch (IllegalArgumentException e) {
                printErrorAndSolutionMessages(
                        e.getMessage(),
                        "Перевірте введені значення."
                );
            }
        }
    }

    static Line input3(Scanner scanner) {
        while (true) {
            try {
                System.out.println("Введіть координати точки (x0; y0) та вектора нормалі (a, b):");
                double[] numbers = readNumbers(scanner, 4);

                return Line.fromPointAndNormal(numbers[0], numbers[1], numbers[2], numbers[3]);

            } catch (NumberFormatException e) {
                printErrorAndSolutionMessages("Введено текст або нерозпізнані символи замість числа.", "Будь ласка, введіть 4 числа через пробіл.");
            } catch (IllegalArgumentException e) {
                printErrorAndSolutionMessages(e.getMessage(), "Перевірте правильність введених даних і спробуйте ще раз.");
            }
        }
    }
}
