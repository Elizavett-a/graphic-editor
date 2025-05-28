package com.liza.shapes;

import com.liza.shapes.util.ColorSerializationHelper;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;

public abstract class Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private double x1, y1, x2, y2;
    private transient Color mainColor;
    private double strokeWidth;

    public Shape(double x1, double y1, double x2, double y2, Color mainColor, double strokeWidth) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.mainColor = mainColor;
        this.strokeWidth = strokeWidth;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ColorSerializationHelper.serializeColor(mainColor, out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        mainColor = ColorSerializationHelper.deserializeColor(in);
    }

    public double getStrokeWidth() {
        return strokeWidth;
    }

    public Color getColor() {
        return mainColor;
    }

    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public double getX2() {
        return x2;
    }

    public double getY2() {
        return y2;
    }

    public double getX() {
        return Math.min(getX1(), getX2());
    }

    public double getY() {
        return Math.min(getY1(), getY2());
    }

    public double getWidth() {
        return Math.abs(getX2() - getX1());
    }

    public double getHeight() {
        return Math.abs(getY2() - getY1());
    }

    public void setX1(double x){ this.x1 = x; }

    public void setX2(double x){ this.x2 = x; }

    public void setY1(double y){ this.y1 = y; }

    public void setY2(double y){ this.y2 = y; }

    public void setMainColor(Color color){ this.mainColor = color; }

    public void setStrokeWidth(double newStrokeWidth){this.strokeWidth = newStrokeWidth;}

    public abstract void draw(GraphicsContext gc);

    public String getMainColorAsString() {
        return mainColor.toString();
    }

    public void setMainColorFromString(String colorString) {
        this.mainColor = Color.valueOf(colorString);
    }
}