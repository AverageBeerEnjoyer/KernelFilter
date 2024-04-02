package ru.katkov.signalprocessing.kernelfilter;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import png.ImageEncoder;
import png.PngImage;
import png.chunk.Ihdr;
import png.image.BufferedRgbaImage;

import java.io.File;
import java.io.IOException;

public class FilePicker {
    private final FileChooser fileChooser;
    private final Stage stage;

    public FilePicker() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));
        fileChooser.setTitle("Chose task file");
        stage = new Stage();
    }

    public void save(BufferedRgbaImage image) {
        PngImage png = ImageEncoder.toPng(image, Ihdr.InterlaceMethod.NONE);
        File file = fileChooser.showSaveDialog(stage);
        if (file == null) return;
        try {
            png.write(file);
        } catch (IOException ignored) {}
    }

    public File open() {
        return fileChooser.showOpenDialog(stage);
    }
}