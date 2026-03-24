package org.example;


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class Line {

    private final double a;
    private final double b;
    private final double c;

    public Line(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    private final List<Point2D> inputPoints = new ArrayList<>();

    public static Line fromGeneralForm(double a, double b) {
        if (a == 0 && b == 0) {
            throw new IllegalArgumentException("Некоректне рівняння прямої.");
        }
        double c = -a * b;
        return new Line(a, b, c);
    }

    public static Line fromPointAndNormal(double x0, double y0, double normA, double normB){
        if(Math.pow(normA, 2) + Math.pow(normB,2) == 0){
            throw new IllegalArgumentException("Помилка: нормальний вектор не може бути нульовим");
        }

        double a = normA;
        double b = normB;
        double c = -normA*x0 - normB*y0;

        Line line = new Line(a, b, c);
        line.inputPoints.add(new Point2D.Double(x0, y0));

        return line;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public List<Point2D> getInputPoints() {
        return inputPoints;
    }
}
