package ru.katkov.signalprocessing.kernelfilter;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import png.ImageDecoder;
import png.ImageEncoder;
import png.PngImage;
import javafx.fxml.FXML;
import png.chunk.Ihdr;
import png.image.BufferedRgbaImage;
import ru.katkov.signalprocessing.kernelfilter.filter.Filter;
import ru.katkov.signalprocessing.kernelfilter.filter.FilterMatrix;
import ru.katkov.signalprocessing.kernelfilter.filter.FilterMedian;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    static class DefaultFilters{
        public static final FilterMatrix negative = new FilterMatrix(new int[][]{{0,0,0},{0,-1,0},{0,0,0}}, 1, 255);
        public static final FilterMatrix h1 = new FilterMatrix(new int[][]{{1,1,1},{1,1,1},{1,1,1}}, 9);
        public static final FilterMatrix h2 = new FilterMatrix(new int[][]{{1,1,1},{1,2,1},{1,1,1}}, 10);
        public static final FilterMatrix h3 = new FilterMatrix(new int[][]{{1,2,1},{2,4,2},{1,2,1}}, 16);
        public static final FilterMatrix h4 = new FilterMatrix(new int[][]{{0,-1,0},{-1,5,-1},{0,-1,0}});
        public static final FilterMatrix h5 = new FilterMatrix(new int[][]{{-1,-1,-1},{-1,9,-1},{-1,-1,-1}});
        public static final FilterMatrix h6 = new FilterMatrix(new int[][]{{1,-2,1},{-2,5,-2},{1,-2,1}});
    }
    private FilePicker filePicker;
    private BufferedRgbaImage source;
    private BufferedRgbaImage processed;
    @FXML
    private ImageView sourceImage;
    @FXML
    private ImageView processedImage;
    @FXML
    private Spinner<Integer> weightSpinner, offsetSpinner;
    @FXML
    private GridPane matrixGrid;
    @FXML
    ComboBox<FilterMatrix> filterComboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        filePicker = new FilePicker();
        try {
            File file = new File("cat.png");
            FileInputStream stream = new FileInputStream(file);
            sourceImage.setImage(new Image(stream));
            PngImage png = PngImage.read(file);
            source = (BufferedRgbaImage) ImageDecoder.toImage(png);
        } catch (IOException ignored) {
        }
    }

    @SuppressWarnings("unchecked")
    private void setFilterToGrid(FilterMatrix filterMatrix) {
        int[][] matrix = filterMatrix.getMatrix();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Spinner<Integer> spinner = (Spinner<Integer>) matrixGrid.getChildren().get(3 * i + j);
                spinner.getValueFactory().setValue(matrix[i][j]);
            }
        }
        weightSpinner.getValueFactory().setValue(filterMatrix.getWeight());
        offsetSpinner.getValueFactory().setValue(filterMatrix.getOffset());
    }


    @FXML
    private void getImageFromFile() {
        File file = filePicker.open();
        PngImage png;
        try {
            FileInputStream stream = new FileInputStream(file);
            Image imageUI = new Image(stream);
            sourceImage.setImage(imageUI);
            png = PngImage.read(file);
            source = (BufferedRgbaImage) ImageDecoder.toImage(png);
        } catch (IOException e) {
        }
    }

    @FXML
    private void saveImage(){
        if(processed == null) return;
        filePicker.save(processed);
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void apply() {
        int[][] matrix = new int[3][3];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                Spinner<Integer> spinner = (Spinner<Integer>) matrixGrid.getChildren().get(3 * i + j);
                matrix[i][j] = spinner.getValue();
            }
        }
        FilterMatrix filterMatrix = new FilterMatrix(matrix, weightSpinner.getValue(), offsetSpinner.getValue());
        processed = filterMatrix.apply(source);
        setProcessedToView();
    }

    private void applyFilter(Filter filter){
        processed = filter.apply(source);
        setProcessedToView();
    }

    private void setProcessedToView(){
        PngImage png = ImageEncoder.toPng(processed, Ihdr.InterlaceMethod.NONE);
        try {
            File file = new File("tmp.png");
            png.write(file);
            FileInputStream stream = new FileInputStream(file);
            Image image = new Image(stream);
            processedImage.setImage(image);
        } catch (IOException ignored){}
    }

    @FXML
    private void applyMedianFilter(){
        FilterMedian filter = new FilterMedian();
        applyFilter(filter);
    }
    @FXML
    private void H1(){
        setFilterToGrid(DefaultFilters.h1);
        applyFilter(DefaultFilters.h1);
    }
    @FXML
    private void H2(){
        setFilterToGrid(DefaultFilters.h2);
        applyFilter(DefaultFilters.h2);
    }
    @FXML
    private void H3(){
        setFilterToGrid(DefaultFilters.h3);
        applyFilter(DefaultFilters.h3);
    }
    @FXML
    private void H4(){
        setFilterToGrid(DefaultFilters.h4);
        applyFilter(DefaultFilters.h4);
    }
    @FXML
    private void H5(){
        setFilterToGrid(DefaultFilters.h5);
        applyFilter(DefaultFilters.h5);
    }
    @FXML
    private void H6(){
        setFilterToGrid(DefaultFilters.h6);
        applyFilter(DefaultFilters.h6);
    }
    @FXML
    private void negative(){
        setFilterToGrid(DefaultFilters.negative);
        applyFilter(DefaultFilters.negative);
    }
}