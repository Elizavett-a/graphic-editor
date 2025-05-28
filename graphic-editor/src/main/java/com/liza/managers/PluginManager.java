package com.liza.managers;

import com.liza.plugins.ShapePlugin;
import com.liza.shapes.Shape;
import com.liza.shapes.util.ShapeFactory;
import com.liza.shapes.util.ShapeType;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class PluginManager {
    private final List<ShapePlugin> plugins = new ArrayList<>();
    private final List<URLClassLoader> classLoaders = new ArrayList<>(); // Store classloaders

    public void loadPlugin(File jarFile) throws Exception {
        URLClassLoader loader = new URLClassLoader(
                new URL[]{jarFile.toURI().toURL()},
                getClass().getClassLoader()
        );
        classLoaders.add(loader);

        ServiceLoader<ShapePlugin> serviceLoader = ServiceLoader.load(ShapePlugin.class, loader);

        for (ShapePlugin plugin : serviceLoader) {
            plugins.add(plugin);
            try {
                ShapeType.forName(plugin.getShapeTypeName());
            } catch (IllegalArgumentException e) {
                Shape shapeInstance = plugin.createShape(0, 0, 0, 0, null, null, 0);
                ShapeType.registerDynamicType(plugin.getShapeTypeName(), shapeInstance.getClass());
            }
            ShapeFactory.registerCreator(plugin.getShapeTypeName(), plugin::createShape);
        }
    }

    public List<ShapePlugin> getPlugins() {
        return plugins;
    }

    public List<URLClassLoader> getClassLoaders() {
        return classLoaders;
    }
}