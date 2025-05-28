package com.liza.shapes.util;

import com.liza.plugins.ShapePlugin;
import com.liza.shapes.Shape;
import com.liza.shapes.impl.*;
import javafx.scene.paint.Color;
import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {
    private static final Map<String, ShapeCreator> creators = new HashMap<>();

    @FunctionalInterface
    public interface ShapeCreator {
        Shape create(double x1, double y1, double x2, double y2,
                     Color mainColor, Color additColor, double strokeWidth);
    }

    static {
        registerCreator(ShapeType.LINE.name(), (x1, y1, x2, y2, main, addit, width) ->
                new LineShape(x1, y1, x2, y2, main, width));
        registerCreator(ShapeType.ELLIPSE.name(), EllipseShape::new);
        registerCreator(ShapeType.RECTANGLE.name(), RectangleShape::new);
        registerCreator(ShapeType.POLYGON.name(), (x1, y1, x2, y2, main, addit, width) ->
                new PolygonShape(x1, y1, x2, y2, main, addit, width, 3));
    }

    public static void registerCreator(String typeName, ShapeCreator creator) {
        creators.put(typeName.toUpperCase(), creator);
    }

    public static void registerCreator(ShapeType type, ShapeCreator creator) {
        registerCreator(type.name(), creator);
    }

    public static Shape create(ShapeType type,
                               double x1, double y1, double x2, double y2,
                               Color mainColor, Color additColor, double strokeWidth) {
        return create(type.name(), x1, y1, x2, y2, mainColor, additColor, strokeWidth);
    }

    public static void registerPluginCreator(ShapePlugin plugin) {
        registerCreator(plugin.getShapeTypeName(),
                plugin::createShape);
    }

    public static Shape create(String typeName,
                               double x1, double y1, double x2, double y2,
                               Color mainColor, Color additColor, double strokeWidth) {
        ShapeCreator creator = creators.get(typeName.toUpperCase());
        if (creator == null) {
            throw new IllegalArgumentException("Unknown shape type: " + typeName);
        }
        return creator.create(x1, y1, x2, y2, mainColor, additColor, strokeWidth);
    }
}