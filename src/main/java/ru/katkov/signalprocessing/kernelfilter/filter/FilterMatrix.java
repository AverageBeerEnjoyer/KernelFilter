package ru.katkov.signalprocessing.kernelfilter.filter;

import png.image.BufferedRgbaImage;

public class FilterMatrix implements Filter {
    private final int[][] matrix;
    private final int border;
    private final int weight;
    private final int offset;
    private int lowClampCount;
    private int highClampCount;

    public FilterMatrix(int[][] matrix, int weight, int offset) {
        if (matrix.length == 0 || matrix[0].length != matrix.length || (matrix.length & 1) != 1)
            throw new IllegalArgumentException("bag kernel");
        this.matrix = matrix;
        this.border = matrix.length / 2;
        this.weight = weight;
        this.offset = offset;
    }

    public FilterMatrix(int[][] matrix, int weight){
        this(matrix, weight, 0);
    }

    public FilterMatrix(int[][] matrix) {
        this(matrix, 1, 0);
    }

    private long processPixel(BufferedRgbaImage source, int row, int col) {
        int red = 0, green = 0, blue = 0, alpha = 0;
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
                red += ColorConverter.red(source.getPixel(colIndex, rowIndex)) * matrix[kernelRow][kernelCol];
                green += ColorConverter.green(source.getPixel(colIndex, rowIndex)) * matrix[kernelRow][kernelCol];
                blue += ColorConverter.blue(source.getPixel(colIndex, rowIndex)) * matrix[kernelRow][kernelCol];
                alpha += ColorConverter.alpha(source.getPixel(colIndex, rowIndex)) * matrix[kernelRow][kernelCol];
            }
        }
        red = clamp(red/weight + offset, source.getBitDepths()[0]);
        green = clamp(green/weight + offset, source.getBitDepths()[0]);
        blue = clamp(blue/weight + offset, source.getBitDepths()[0]);
        alpha = clamp(alpha/weight, source.getBitDepths()[0]);
        return ColorConverter.convert(red, green, blue, alpha);
    }

    private int clamp(int val, int bit) {
        if (val < 0) {
            ++lowClampCount;
            return 0;
        }
        if (val>(1 << bit) - 1){
            ++highClampCount;
            return (1 << bit) - 1;
        }
        return val;
    }

    public BufferedRgbaImage apply(BufferedRgbaImage img) {
        BufferedRgbaImage copy = img.clone();
        for (int row = 0; row < img.getHeight(); ++row) {
            for (int col = 0; col < img.getWidth(); ++col) {
                copy.setPixel(col, row, processPixel(img, row, col));
            }
        }
        System.out.println(lowClampCount);
        System.out.println(highClampCount);
        return copy;
    }

    public int[][] getMatrix(){
        return matrix.clone();
    }
    public int getWeight(){
        return weight;
    }

    public int getOffset() {
        return offset;
    }
}
