package com.liza.shapes.util;

import com.liza.shapes.Shape;
import com.liza.managers.UndoRedoManager;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class ShapeList {

    private final List<Shape> shapes = new ArrayList<>();
    private final UndoRedoManager<List<Shape>> undoRedoManager = new UndoRedoManager<>();

    public void addShape(Shape shape) {
        saveState();
        shapes.add(shape);
    }

    public void addShapes(List<Shape> shapesToAdd) {
        saveState();
        shapes.addAll(shapesToAdd);
    }

    public void undo() {
        List<Shape> previousState = undoRedoManager.undo(new ArrayList<>(shapes));
        if (previousState != null) {
            shapes.clear();
            shapes.addAll(previousState);
        }
    }

    public void redo() {
        List<Shape> nextState = undoRedoManager.redo(new ArrayList<>(shapes));
        if (nextState != null) {
            shapes.clear();
            shapes.addAll(nextState);
        }
    }

    private void saveState() {
        undoRedoManager.execute(new ArrayList<>(shapes));
    }

    public void drawAll(GraphicsContext gc) {
        for (Shape shape : shapes) {
            shape.draw(gc);
        }
    }

    public void clear() {
        shapes.clear();
        undoRedoManager.clear();
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void removeShape(Shape shape) {
        if (shapes.remove(shape)) {
            saveState();
        }
    }

    public int size() {
        return shapes.size();
    }


    public boolean contains(Shape shape) {
        return shapes.contains(shape);
    }
}