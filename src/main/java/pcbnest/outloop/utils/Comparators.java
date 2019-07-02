package pcbnest.outloop.utils;


import pcbnest.outloop.model.PcbBoard;
import pcbnest.outloop.model.Shape;

import java.util.Comparator;

public class Comparators {
    public static Comparator<PcbBoard> comparePcbBoard = (PcbBoard s1, PcbBoard s2) -> {
        return s1.getCount() -s2.getCount();
    };

    public static Comparator<Shape> compareShape = (Shape s1, Shape s2) -> {
        return Double.compare(s1.getArea(), s2.getArea());
    };
}
