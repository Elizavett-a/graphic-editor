package com.liza.shapes.util;

import javafx.scene.paint.Color;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ColorSerializationHelper {
    public static void serializeColor(Color color, ObjectOutput out) throws IOException {
        out.writeDouble(color.getRed());
        out.writeDouble(color.getGreen());
        out.writeDouble(color.getBlue());
        out.writeDouble(color.getOpacity());
    }

    public static Color deserializeColor(ObjectInput in) throws IOException {
        double red = in.readDouble();
        double green = in.readDouble();
        double blue = in.readDouble();
        double opacity = in.readDouble();
        return new Color(red, green, blue, opacity);
    }
}