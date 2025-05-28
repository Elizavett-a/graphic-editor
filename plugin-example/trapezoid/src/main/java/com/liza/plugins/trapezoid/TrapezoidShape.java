package com.liza.plugins.trapezoid;

import com.liza.shapes.Shape;
import com.liza.shapes.util.ColorSerializationHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.*;

public class TrapezoidShape extends Shape implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient Color fillColor;
    private double topWidthRatio = 0.7;

    public TrapezoidShape(double x1, double y1, double x2, double y2,
                          Color strokeColor, Color fillColor, double strokeWidth) {
        super(x1, y1, x2, y2, strokeColor, strokeWidth);
        this.fillColor = fillColor;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(getColor());
        gc.setFill(fillColor);
        gc.setLineWidth(getStrokeWidth());

        double[] xPoints = calculateXPoints();
        double[] yPoints = calculateYPoints();

        gc.fillPolygon(xPoints, yPoints, 4);
        gc.strokePolygon(xPoints, yPoints, 4);
    }

    private double[] calculateXPoints() {
        double left = Math.min(getX1(), getX2());
        double right = Math.max(getX1(), getX2());
        double width = right - left;
        double topWidth = width * topWidthRatio;
        double offset = (width - topWidth) / 2;

        return new double[] {
                left + offset,
                left + offset + topWidth,
                right,
                left
        };
    }

    private double[] calculateYPoints() {
        double top = Math.min(getY1(), getY2());
        double bottom = Math.max(getY1(), getY2());

        return new double[] {
                top,
                top,
                bottom,
                bottom
        };
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeDouble(topWidthRatio);
        ColorSerializationHelper.serializeColor(fillColor, out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        topWidthRatio = in.readDouble();
        fillColor = ColorSerializationHelper.deserializeColor(in);
    }

    public String getType() {
        return "TRAPEZOID";
    }

    public void setFillColor(Color color) {
        this.fillColor = color;
    }

    public void setTopWidthRatio(double ratio) {
        this.topWidthRatio = Math.max(0.1, Math.min(ratio, 0.9));
    }
}