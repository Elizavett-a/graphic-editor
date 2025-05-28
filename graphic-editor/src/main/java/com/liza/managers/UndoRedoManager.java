package com.liza.managers;

import java.util.Stack;

public class UndoRedoManager<Shape> {
    private final Stack<Shape> undoStack = new Stack<>();
    private final Stack<Shape> redoStack = new Stack<>();

    public void execute(Shape state) {
        undoStack.push(state);
        redoStack.clear();
    }

    public Shape undo(Shape currentState) {
        if (!undoStack.isEmpty()) {
            redoStack.push(currentState);
            return undoStack.pop();
        }
        return null;
    }

    public Shape redo(Shape currentState) {
        if (!redoStack.isEmpty()) {
            undoStack.push(currentState);
            return redoStack.pop();
        }
        return null;
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}