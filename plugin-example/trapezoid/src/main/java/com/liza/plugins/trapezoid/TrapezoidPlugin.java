package com.liza.plugins.trapezoid;

import com.liza.plugins.ShapePlugin;
import com.liza.shapes.Shape;
import javafx.scene.paint.Color;

public class TrapezoidPlugin implements ShapePlugin {
    @Override
    public Shape createShape(double x1, double y1, double x2, double y2,
                             Color strokeColor, Color fillColor, double strokeWidth) {
        return new TrapezoidShape(x1, y1, x2, y2, strokeColor, fillColor, strokeWidth);
    }

    @Override
    public String getShapeTypeName() {
        return "Trapezoid";
    }
}