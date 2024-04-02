package ru.katkov.signalprocessing.kernelfilter.filter;

import png.image.BufferedRgbaImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterMedian implements Filter {

    private long processPixel(BufferedRgbaImage source, int row, int col) {
        int border = 1;
        List<Integer> red = new ArrayList<>();
        List<Integer> green = new ArrayList<>();
        List<Integer> blue = new ArrayList<>();
        List<Integer> alpha = new ArrayList<>();
        for (int rowOffset = -border; rowOffset <= border; ++rowOffset) {
            int rowIndex = row + rowOffset;
            rowIndex = Math.max(rowIndex, 0);
            rowIndex = Math.min(rowIndex, source.getHeight() - 1);
            int kernelRow = rowOffset + border;
            for (int colOffset = -border; colOffset <= border; ++colOffset) {
                int colIndex = col + colOffset;
                colIndex = Math.max(colIndex, 0);
                colIndex = Math.min(colIndex, source.getWidth() - 1);
                int kernelCol = colOffset + border;
                red.add(ColorConverter.red(source.getPixel(colIndex, rowIndex)));
                green.add(ColorConverter.green(source.getPixel(colIndex, rowIndex)));
                blue.add(ColorConverter.blue(source.getPixel(colIndex, rowIndex)));
                alpha.add(ColorConverter.alpha(source.getPixel(colIndex, rowIndex)));
            }
        }
        red.sort(Integer::compare);
        green.sort(Integer::compare);
        blue.sort(Integer::compare);
        alpha.sort(Integer::compare);
        return ColorConverter.convert(red.get(4), green.get(4), blue.get(4), alpha.get(4));
    }

    @Override
    public BufferedRgbaImage apply(BufferedRgbaImage img) {
        BufferedRgbaImage copy = img.clone();
        for (int row = 0; row < img.getHeight(); ++row) {
            for (int col = 0; col < img.getWidth(); ++col) {
                copy.setPixel(col, row, processPixel(img, row, col));
            }
        }
        return copy;
    }
}
