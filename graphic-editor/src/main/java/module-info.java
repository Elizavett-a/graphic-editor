module com.liza {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.liza.application;
    exports com.liza.controllers;
    exports com.liza.managers;
    exports com.liza.plugins;
    exports com.liza.shapes.impl;
    exports com.liza.shapes.util;
    exports com.liza.shapes;

    opens com.liza.controllers to javafx.fxml;
    opens com.liza.fxml to javafx.fxml;

    uses com.liza.plugins.ShapePlugin;
}