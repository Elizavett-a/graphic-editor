package com.liza.shapes.impl;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class PolylineShape extends LineShape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Double> allPoints;

    public PolylineShape(double x1, double y1, Color color, double strokeWidth) {
        super(x1, y1, x1, y1, color, strokeWidth);
        allPoints = new ArrayList<>();
        addPoint(x1, y1);
    }

    public double getLastX() {
        if (allPoints.isEmpty()) {
            return 0;
        }
        return allPoints.get(allPoints.size() - 2);
    }

    public double getLastY() {
        if (allPoints.isEmpty()) {
            return 0;
        }
        return allPoints.get(allPoints.size() - 1);
    }

    public void addPoint(double x, double y) {
        allPoints.add(x);
        allPoints.add(y);
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (allPoints.isEmpty()) {
            return;
        }

        gc.setStroke(getColor());
        gc.setLineWidth(getStrokeWidth());

        double[] xPoints = new double[allPoints.size() / 2];
        double[] yPoints = new double[allPoints.size() / 2];

        for (int i = 0; i < allPoints.size(); i += 2) {
            xPoints[i / 2] = allPoints.get(i);
            yPoints[i / 2] = allPoints.get(i + 1);
        }

        gc.strokePolyline(xPoints, yPoints, xPoints.length);
    }
}