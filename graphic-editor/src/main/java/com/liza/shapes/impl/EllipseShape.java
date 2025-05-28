package com.liza.shapes.impl;

import com.liza.shapes.Shape;
import com.liza.shapes.util.ColorSerializationHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;

public class EllipseShape extends Shape {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient Color fillColor;

    public EllipseShape(double x1, double y1, double x2, double y2, Color mainColor, Color addColor, double strokeWidth) {
        super(x1, y1, x2, y2, mainColor, strokeWidth);
        this.fillColor = addColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(fillColor);
        gc.setStroke(getColor());
        gc.setLineWidth(getStrokeWidth());
        gc.strokeOval(getX(), getY(), getWidth(), getHeight());
        gc.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ColorSerializationHelper.serializeColor(fillColor, out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        fillColor = ColorSerializationHelper.deserializeColor(in);
    }

    public String getType() {
        return "ELLIPSE";
    }

    public void setFillColor(Color color){ this.fillColor = color; }
}