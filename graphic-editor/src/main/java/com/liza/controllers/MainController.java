package com.liza.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import com.liza.managers.FileManager;
import com.liza.managers.PluginManager;
import com.liza.plugins.ShapePlugin;
import com.liza.shapes.impl.*;
import com.liza.shapes.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import com.liza.shapes.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class MainController {
    @FXML
    private Canvas myCanvas;
    @FXML
    private ColorPicker mainColor;
    @FXML
    private ColorPicker additColor;
    @FXML
    private Slider sizeSlider;
    @FXML
    private Label sizeInfoLabel;
    @FXML
    private GridPane toolsGrid;

    private double startX, startY;
    private final ShapeList shapeList = new ShapeList();

    private PolylineShape currentPolyline;

    private String drawingMode = ShapeType.LINE.name();
    private int polygonSides = 3;

    private final PluginManager pluginManager = new PluginManager();
    private final FileManager fileManager = new FileManager(pluginManager); // Pass PluginManager to FileManager

    @FXML
    public void handleButtonLine(MouseEvent event) { drawingMode = ShapeType.LINE.name(); }

    @FXML
    public void handleButtonEllipse(MouseEvent event) { drawingMode = ShapeType.ELLIPSE.name(); }

    @FXML
    public void handleButtonRectangle(MouseEvent event) { drawingMode = ShapeType.RECTANGLE.name(); }

    @FXML
    public void handleButtonPolyline(MouseEvent event) {
        drawingMode = ShapeType.POLYLINE.name();
        currentPolyline = null;
    }

    @FXML
    public void handleButtonTriangle(MouseEvent event) {
        drawingMode = ShapeType.POLYGON.name();
        polygonSides = 3;
    }

    @FXML
    public void handleButtonPentagon(MouseEvent event) {
        drawingMode = ShapeType.POLYGON.name();
        polygonSides = 5;
    }

    private void updateSizeInfoLabel() {
        sizeInfoLabel.setText(String.format("%.0f", sizeSlider.getValue()));
    }

    @FXML
    public void initialize() {
        updateSizeInfoLabel();
        sizeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateSizeInfoLabel();
        });
    }

    @FXML
    public void handleMousePressed(MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
        if (Objects.equals(drawingMode, ShapeType.POLYLINE.name())) {
            if (currentPolyline == null) {
                currentPolyline = new PolylineShape(startX, startY,
                        mainColor.getValue(), sizeSlider.getValue());
            }
            if (!shapeList.contains(currentPolyline)) {
                shapeList.addShape(currentPolyline);
            }
        }
    }

    @FXML
    public void handleMouseDragged(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        GraphicsContext gc = myCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        shapeList.drawAll(gc);

        if (!(Objects.equals(drawingMode, ShapeType.POLYLINE.name()))) {
            Shape tempShape = createShape(startX, startY, endX, endY);
            if (tempShape != null) {
                tempShape.draw(gc);
            }
        }

        if (Objects.equals(drawingMode, ShapeType.POLYLINE.name()) && currentPolyline != null) {
            currentPolyline.draw(gc);
            gc.setStroke(mainColor.getValue());
            gc.setLineWidth(sizeSlider.getValue());
            gc.strokeLine(currentPolyline.getLastX(), currentPolyline.getLastY(), endX, endY);
        }
    }

    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateShapeButtons() {
        int initialButtonsCount = 6;
        if (toolsGrid.getChildren().size() > initialButtonsCount) {
            toolsGrid.getChildren().remove(initialButtonsCount, toolsGrid.getChildren().size());
        }

        int row = initialButtonsCount;
        for (ShapePlugin plugin : pluginManager.getPlugins()) {
            Button btn = createPluginButton(plugin);
            toolsGrid.add(btn, 0, row);
            GridPane.setColumnSpan(btn, 1);

            if (row >= toolsGrid.getRowConstraints().size()) {
                RowConstraints newRow = new RowConstraints();
                newRow.setPrefHeight(30);
                newRow.setVgrow(Priority.SOMETIMES);
                toolsGrid.getRowConstraints().add(newRow);
            }
            row++;
        }
    }

    private Button createPluginButton(ShapePlugin plugin) {
        Button btn = new Button(plugin.getShapeTypeName());
        btn.setStyle("-fx-background-color: white; -fx-border-color: gray;");
        btn.setMinHeight(30.0);
        btn.setPrefHeight(30.0);
        btn.setPrefWidth(120.0);
        btn.setAlignment(Pos.CENTER);

        btn.setOnMouseClicked(e -> {
            drawingMode = plugin.getShapeTypeName();
            currentPolyline = null;
            btn.setStyle("-fx-background-color: white; -fx-border-color: gray;");
        });

        return btn;
    }

    @FXML
    public void handleLoadPlugin(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Plugin");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage(event));

        if (selectedFile != null) {
            try {
                System.out.println("Попытка загрузить плагин из: " + selectedFile.getPath());
                pluginManager.loadPlugin(selectedFile);
                System.out.println("Загружены плагины: " + pluginManager.getPlugins().toString());
                updateShapeButtons();
            } catch (Exception e) {
                System.err.println("Ошибка загрузки плагина: " + e.getMessage());
                showErrorDialog("Failed to load plugin: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleMouseReleased(MouseEvent event) {
        double endX = event.getX();
        double endY = event.getY();

        if (!(Objects.equals(drawingMode, ShapeType.POLYLINE.name()))) {
            Shape shape = createShape(startX, startY, endX, endY);
            if (shape != null) {
                shapeList.addShape(shape);
                System.out.println("Фигура добавлена. Текущий размер списка: " + shapeList.getShapes().size());
            }
        } else if (currentPolyline != null) {
            currentPolyline.addPoint(endX, endY);
        }

        redrawCanvas();
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (drawingMode.equalsIgnoreCase(ShapeType.POLYLINE.name()) && event.getCode().toString().equals("ENTER")) {
            if (currentPolyline != null) {
                shapeList.addShape(currentPolyline);
                currentPolyline = null;
                redrawCanvas();
            }
        }
    }

    @FXML
    public void handleClearCanvas(ActionEvent event) {
        GraphicsContext gc = myCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        shapeList.clear();
    }

    @FXML
    public void handleUndo(ActionEvent event) {
        shapeList.undo();
        if (currentPolyline != null) {
            currentPolyline = null;
        }
        redrawCanvas();
    }

    @FXML
    public void handleRedo(ActionEvent event) {
        shapeList.redo();
        redrawCanvas();
    }

    private void redrawCanvas() {
        GraphicsContext gc = myCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        shapeList.drawAll(gc);
    }

    private Shape createShape(double x1, double y1, double x2, double y2) {
        if (drawingMode == null) {
            System.err.println("Drawing mode is not set!");
            return null;
        }

        if (drawingMode.equalsIgnoreCase(ShapeType.POLYLINE.name())) {
            return null;
        }

        Shape shape = ShapeFactory.create(
                drawingMode,
                x1, y1, x2, y2,
                mainColor.getValue(),
                additColor.getValue(),
                sizeSlider.getValue()
        );

        if (drawingMode.equalsIgnoreCase(ShapeType.POLYGON.name()) && shape instanceof PolygonShape) {
            ((PolygonShape) shape).setSides(polygonSides);
        }

        return shape;
    }

    @FXML
    public void handleSaveShapes(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Shapes");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary Files", "*.bin"));
        File selectedFile = fileChooser.showSaveDialog(getCurrentStage(event));
        if (selectedFile != null) {
            try {
                fileManager.saveShapes(shapeList.getShapes(), selectedFile.getAbsolutePath());
            } catch (IOException e) {
                showErrorDialog("Failed to save file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLoadShapes(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Binary Files", "*.bin"));
        File selectedFile = fileChooser.showOpenDialog(getCurrentStage(event));
        if (selectedFile != null) {
            try {
                List<Shape> shapes = fileManager.loadShapes(selectedFile.getAbsolutePath());
                if (shapes == null || shapes.isEmpty()) {
                    showErrorDialog("File is empty or corrupted");
                    return;
                }
                shapeList.clear();
                shapeList.addShapes(shapes);
                redrawCanvas();
            } catch (IOException | ClassNotFoundException e) {
                showErrorDialog("Failed to load file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}