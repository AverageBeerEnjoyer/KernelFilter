module ru.katkov.signalprocessing.kernelfilter {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens ru.katkov.signalprocessing.kernelfilter to javafx.fxml;
    exports ru.katkov.signalprocessing.kernelfilter;
    exports png;
    exports png.image;
}