package com.liza.shapes.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URLClassLoader;
import java.util.List;

public class PluginObjectInputStream extends ObjectInputStream {
    private final List<URLClassLoader> classLoaders;

    public PluginObjectInputStream(InputStream in, List<URLClassLoader> classLoaders) throws IOException {
        super(in);
        this.classLoaders = classLoaders;
    }

    @Override
    protected Class<?> resolveClass(java.io.ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String className = desc.getName();
        for (URLClassLoader loader : classLoaders) {
            try {
                return Class.forName(className, false, loader);
            } catch (ClassNotFoundException e) {
            }
        }
        return super.resolveClass(desc);
    }
}