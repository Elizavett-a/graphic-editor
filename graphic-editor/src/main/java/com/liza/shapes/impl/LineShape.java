package com.liza.shapes.impl;

import com.liza.shapes.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serial;
import java.io.Serializable;

public class LineShape extends Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public LineShape(double x1, double y1, double x2, double y2, Color color, double strokeWidth) {
        super(x1, y1, x2, y2, color, strokeWidth);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor());
        gc.setLineWidth(getStrokeWidth());
        gc.strokeLine(getX1(), getY1(), getX2(), getY2());
    }

    public String getType() {
        return "LINE";
    }
}