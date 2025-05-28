package com.liza.shapes.impl;

import com.liza.shapes.Shape;
import com.liza.shapes.util.ColorSerializationHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;

public class PolygonShape extends Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int sides;
    private transient Color fillColor;

    public PolygonShape(double x1, double y1, double x2, double y2, Color mainColor, Color addColor, double strokeWidth, int sides) {
        super(x1, y1, x2, y2, mainColor, strokeWidth);
        this.sides = Math.max(3, Math.min(sides, 10));
        this.fillColor = addColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor());
        gc.setFill(fillColor);
        gc.setLineWidth(getStrokeWidth());

        double centerX = getX1();
        double centerY = getY1();

        double radius = Math.min(Math.abs(getX2() - getX1()), Math.abs(getY2() - getY1())) / 2;

        double[] polyX = new double[sides];
        double[] polyY = new double[sides];

        for (int i = 0; i < sides; i++) {
            double angle = 2 * Math.PI * i / sides;
            polyX[i] = centerX + radius * Math.cos(angle);
            polyY[i] = centerY + radius * Math.sin(angle);
        }
        gc.fillPolygon(polyX, polyY, sides);
        gc.strokePolygon(polyX, polyY, sides);
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeInt(sides);
        ColorSerializationHelper.serializeColor(fillColor, out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        sides = in.readInt();
        fillColor = ColorSerializationHelper.deserializeColor(in);
    }

    public String getType() {
        return "POLYGON";
    }

    public void setFillColor(Color color){ this.fillColor = color; }

    public void setSides(int newSize) { this.sides = newSize; }

}