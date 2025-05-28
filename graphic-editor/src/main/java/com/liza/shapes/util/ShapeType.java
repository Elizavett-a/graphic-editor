package com.liza.shapes.util;

import com.liza.shapes.*;
import com.liza.shapes.impl.*;

import java.util.HashMap;
import java.util.Map;

public enum ShapeType {
    LINE(LineShape.class),
    ELLIPSE(EllipseShape.class),
    RECTANGLE(RectangleShape.class),
    POLYGON(PolygonShape.class),
    POLYLINE(PolylineShape.class);

    private final Class<? extends Shape> shapeClass;
    private static final Map<String, Class<? extends Shape>> DYNAMIC_TYPES = new HashMap<>();

    ShapeType(Class<? extends Shape> shapeClass) {
        this.shapeClass = shapeClass;
    }

    public Class<? extends Shape> getShapeClass() {
        return shapeClass;
    }

    public static void forName(String name) {
        try {
            valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            if (DYNAMIC_TYPES.containsKey(name.toUpperCase())) {
                return;
            }
            throw new IllegalArgumentException("Unknown shape type: " + name);
        }
    }

    public static void registerDynamicType(String name, Class<? extends Shape> shapeClass) {
        DYNAMIC_TYPES.put(name.toUpperCase(), shapeClass);
    }

    public static boolean isDynamicType(String name) {
        return DYNAMIC_TYPES.containsKey(name.toUpperCase());
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}