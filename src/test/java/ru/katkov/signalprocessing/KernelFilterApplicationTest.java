package ru.katkov.signalprocessing;

import org.junit.jupiter.api.Test;
import png.ImageDecoder;
import png.ImageEncoder;
import png.PngImage;
import png.chunk.Ihdr;
import png.image.BufferedRgbaImage;
import ru.katkov.signalprocessing.kernelfilter.filter.Filter;
import ru.katkov.signalprocessing.kernelfilter.filter.FilterMatrix;
import ru.katkov.signalprocessing.kernelfilter.filter.FilterMedian;

import java.io.File;
import java.io.IOException;

public class KernelFilterApplicationTest {


    void applyFilter(Filter filterMatrix, String filename) {
        PngImage png;
        try {
            png = PngImage.read(new File("cat.png"));
            BufferedRgbaImage img = (BufferedRgbaImage) ImageDecoder.toImage(png);
            BufferedRgbaImage processed = filterMatrix.apply(img);
            PngImage pngImage = ImageEncoder.toPng(processed, Ihdr.InterlaceMethod.NONE);
            pngImage.write(new File(filename));
            System.out.println("------ success --------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test1() {
        int[][] matrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix, 9);
        applyFilter(filterMatrix, "cat1.png");
    }

    @Test
    void test2() {
        int[][] matrix = {{1, 1, 1}, {1, 2, 1}, {1, 1, 1}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix, 10);
        applyFilter(filterMatrix, "cat2.png");
    }

    @Test
    void test3() {
        int[][] matrix = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix, 16);
        applyFilter(filterMatrix, "cat3.png");
    }

    @Test
    void test4() {
        int[][] matrix = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix);
        applyFilter(filterMatrix, "cat4.png");
    }

    @Test
    void test5() {
        int[][] matrix = {{-1, -1, -1}, {-1, 9, -1}, {-1, -1, -1}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix);
        applyFilter(filterMatrix, "cat5.png");
    }

    @Test
    void test6() {
        int[][] matrix = {{1, -2, 1}, {-2, 5, -2}, {1, -2, 1}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix);
        applyFilter(filterMatrix, "cat6.png");
    }

    @Test
    void test7() {
        int[][] matrix = {{0, 0, 0}, {0, -1, 0}, {0, 0, 0}};
        FilterMatrix filterMatrix = new FilterMatrix(matrix, 1,256);
        applyFilter(filterMatrix, "cat7.png");
    }
    @Test
    void test8() {
        int[][] matrix = {{0, 0, 0}, {0, -1, 0}, {0, 0, 0}};
        FilterMedian filter = new FilterMedian();
        applyFilter(filter, "cat8.png");
    }
}
