package org.example;

import java.awt.geom.Point2D;
import java.util.Scanner;

public class LineBuilder {

    // Точність для порівняння чисел з плаваючою комою
    private static final double EPSILON = 1e-8;
    private static final int MIN = -145;
    private static final int MAX = 145;

    // Головний метод аналізу взаємного розміщення трьох прямих
    public String analyzer(Line line1, Line line2, Line line3) {

        // Перевірка: усі три прямі співпадають
        if (areCoincident(line1, line2) && areCoincident(line2, line3)) {
            return "Прямі співпадають";
        }

        // Перевірка: усі три прямі паралельні (але не співпадають)
        if (areParallel(line1, line2) && areParallel(line2, line3) &&
                !(areCoincident(line1, line2) && areCoincident(line2, line3))) {
            return "Прямі не мають спільних точок";
        }

        // Перевірка простих випадків (збіг точок або ортогональні прямі)
        Point2D basicIntersectionPoint = findSimpleIntersection(line1, line2, line3);
        if (basicIntersectionPoint != null) {
            return String.format(
                    "Очевидний випадок: єдина точка перетину (x0, y0) , x0=%.3f, y0=%.3f",
                    basicIntersectionPoint.getX(),
                    basicIntersectionPoint.getY()
            );
        }

        // Перевірка: всі три прямі перетинаються в одній точці
        if (isIntersectInOnePoint(line1, line2, line3)) {
            Point2D p;

            // беремо точку перетину будь-яких двох непаралельних прямих
            if (!areParallel(line1, line2)) {
                p = getIntersection(line1, line2);
            } else {
                p = getIntersection(line2, line3);
            }

            return String.format(
                    "Єдина точка перетину прямих (x0, y0), x0=%.3f, y0=%.3f",
                    p.getX(),
                    p.getY()
            );
        }

        // Випадок: 1 і 2 прямі паралельні, третя їх перетинає
        if (areParallel(line1, line2) && !areCoincident(line1, line2)) {

            Point2D p1 = getIntersection(line1, line3);
            Point2D p2 = getIntersection(line2, line3);

            return String.format(
                    "Дві точки перетину прямих (x1, y1) = (%.3f, %.3f), (x2, y2) = (%.3f, %.3f)",
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY()
            );
        }

        // Випадок: 2 і 3 прямі паралельні
        if (areParallel(line2, line3) && !areCoincident(line2, line3)) {

            Point2D p1 = getIntersection(line2, line1);
            Point2D p2 = getIntersection(line3, line1);

            return String.format(
                    "Дві точки перетину прямих (x1, y1) = (%.3f, %.3f), (x2, y2) = (%.3f, %.3f)",
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY()
            );
        }

        // Випадок: 1 і 3 прямі паралельні
        if (areParallel(line1, line3) && !areCoincident(line1, line3)) {

            Point2D p1 = getIntersection(line1, line2);
            Point2D p2 = getIntersection(line3, line2);

            return String.format(
                    "Дві точки перетину прямих (x1, y1) = (%.3f, %.3f), (x2, y2) = (%.3f, %.3f)",
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY()
            );
        }

        // Якщо всі прямі попарно не паралельні → буде три точки перетину
        if (!areParallel(line1, line2) &&
                !areParallel(line2, line3) &&
                !areParallel(line1, line3)) {

            Point2D p1 = getIntersection(line1, line2);
            Point2D p2 = getIntersection(line2, line3);
            Point2D p3 = getIntersection(line3, line1);

            return String.format(
                    "Три точки перетину прямих (x1, y1), (x2, y2) і (x3, y3),%n" +
                            "x1=%.3f, y1=%.3f, x2=%.3f, y2=%.3f, x3=%.3f, y3=%.3f",
                    p1.getX(), p1.getY(),
                    p2.getX(), p2.getY(),
                    p3.getX(), p3.getY()
            );
        }

        // Якщо жоден випадок не підійшов
        throw new IllegalArgumentException("Невідомий випадок");
    }

    ////////////////////////////////////////////////////////////
    // Перевірка чи паралельні дві прямі
    // A1*B2 - A2*B1 = 0
    private boolean areParallel(Line line1, Line line2) {

        double det = line1.getA() * line2.getB() - line2.getA() * line1.getB();
        return Math.abs(det) < EPSILON;

    }

    // Перевірка чи співпадають прямі
    private boolean areCoincident(Line line1, Line line2) {

        if (!areParallel(line1, line2)) {
            return false;
        }

        double det2 = line1.getA() * line2.getC() - line2.getA() * line1.getC();
        double det3 = line1.getB() * line2.getC() - line2.getB() * line1.getC();

        return Math.abs(det2) <= EPSILON && Math.abs(det3) <= EPSILON;

    }

    // Перевірка чи всі три прямі проходять через одну точку
    private boolean isIntersectInOnePoint(Line line1, Line line2, Line line3) {

        if (!areParallel(line1, line2)) {

            Point2D p = getIntersection(line1, line2);
            return containsPoint(line3, p);

        } else if (!areParallel(line2, line3)) {

            Point2D p = getIntersection(line2, line3);
            return containsPoint(line1, p);

        } else if (!areParallel(line1, line3)) {

            Point2D p = getIntersection(line1, line3);
            return containsPoint(line2, p);

        }

        return false;

    }

    // Знаходження точки перетину двох прямих (формула Крамера)
    private Point2D getIntersection(Line line1, Line line2) {

        double denominator = line1.getA() * line2.getB() - line2.getA() * line1.getB();

        double x = -(line1.getC() * line2.getB() - line2.getC() * line1.getB()) / denominator;

        double y = -(line1.getA() * line2.getC() - line2.getA() * line1.getC()) / denominator;

        return new Point2D.Double(x, y);

    }

    // Перевірка чи належить точка прямій
    private boolean containsPoint(Line line, Point2D point) {

        double val = line.getA() * point.getX() + line.getB() * point.getY() + line.getC();
        return Math.abs(val) < EPSILON;

    }

    // Випадок коли прямі паралельні осям координат
    private Point2D orthogonalIntersection(Line line1, Line line2) {

        if (Math.abs(line1.getB()) < EPSILON && Math.abs(line2.getA()) < EPSILON) {

            return new Point2D.Double(
                    -line1.getC() / line1.getA(),
                    -line2.getC() / line2.getB()
            );

        }

        if (Math.abs(line1.getA()) < EPSILON && Math.abs(line2.getB()) < EPSILON) {

            return new Point2D.Double(
                    -line2.getC() / line2.getA(),
                    -line1.getC() / line1.getB()
            );

        }

        return null;

    }

    // Перевірка простих випадків (збіг введених точок або ортогональні прямі)
    private Point2D findSimpleIntersection(Line line1, Line line2, Line line3) {

        // перевірка збігу введених точок
        for (Point2D p1 : line1.getInputPoints()) {

            for (Point2D p3 : line3.getInputPoints()) {

                if (Math.abs(p1.getX() - p3.getX()) < EPSILON &&
                        Math.abs(p1.getY() - p3.getY()) < EPSILON) {

                    if (containsPoint(line2, p1)) {

                        return p1;
                    }
                }
            }
        }

        Point2D point;
        point = orthogonalIntersection(line1, line2);

        if (point != null && containsPoint(line3, point)) {
            return point;
        }

        point = orthogonalIntersection(line2, line3);

        if (point != null && containsPoint(line1, point)) {
            return point;
        }

        point = orthogonalIntersection(line1, line3);

        if (point != null && containsPoint(line2, point)) {
            return point;
        }

        return null;

    }

    //Допоміжні методи
    static double[] readNumbers(Scanner scanner, int expectedCount) {
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            throw new IllegalArgumentException("Порожній рядок");
        }

        String[] parts = input.split("\\s++");

        if (parts.length != expectedCount) {
            throw new IllegalArgumentException(String.format("Неправильна кількість параметрів. Очікується: %d, введено: %d.", expectedCount, parts.length));
        }

        double[] result = new double[expectedCount];
        for (int i = 0; i < expectedCount; i++) {
            result[i] = Double.parseDouble(parts[i]);

            if (result[i] < MIN || result[i] > MAX) {
                throw new IllegalArgumentException(String.format("Значення %.1f виходить за межі допустимого діапазону [%d; %d].", result[i], MIN, MAX));
            }
        }
        return result;
    }

    static void printErrorAndSolutionMessages(String errorMessage, String solution) {
        System.out.println(errorMessage);
        System.out.println(solution + "\n");
    }
}