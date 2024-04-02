package ru.katkov.signalprocessing.kernelfilter.filter;

import png.image.BufferedRgbaImage;

public interface Filter {
    BufferedRgbaImage apply(BufferedRgbaImage img);
}
