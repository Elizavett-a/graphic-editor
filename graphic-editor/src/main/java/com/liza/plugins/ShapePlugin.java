package com.liza.plugins;

import com.liza.shapes.Shape;
import javafx.scene.paint.Color;
import java.io.Serializable;

public interface ShapePlugin extends Serializable {
    Shape createShape(double x1, double y1, double x2, double y2,
                      Color strokeColor, Color fillColor, double strokeWidth);

    String getShapeTypeName();
}