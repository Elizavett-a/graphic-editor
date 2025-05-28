package com.liza.managers;

import com.liza.shapes.Shape;
import java.io.*;
import java.util.List;

public class FileManager {
    private final PluginManager pluginManager;

    public FileManager(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void saveShapes(List<Shape> shapes, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(shapes);
            System.out.println("Shapes saved successfully to: " + filePath);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Shape> loadShapes(String filePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filePath);
             com.liza.shapes.util.PluginObjectInputStream ois = new com.liza.shapes.util.PluginObjectInputStream(fis, pluginManager.getClassLoaders())) {
            List<Shape> shapes = (List<Shape>) ois.readObject();
            System.out.println("Shapes loaded successfully from: " + filePath);
            return shapes;
        }
    }
}