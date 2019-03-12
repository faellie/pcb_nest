package pcbnest.common;



import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import nest4J.data.NestPath;

import java.util.List;

public class ShapeUtils {
    public static SVGPath createPath() {
        final SVGPath arc = new SVGPath();
        arc.setContent("M50,50 L30,50 A20,20 0 0,1 50,30 z");
        arc.setFill(Color.BLACK);
        return arc;
    }

    public static Polygon createPolygon() {
        Polygon polygon = new Polygon();
        polygon.getPoints().addAll(new Double[]{
                200.0, 50.0,
                400.0, 50.0,
                450.0, 150.0,
                400.0, 250.0,
                200.0, 250.0,
                150.0, 150.0,
        });
        polygon.setFill(Color.GREEN);
        return polygon;
    }

    public static NestPath createNestPath(Polygon aInPolygon, double offsetY) {
        NestPath lPath = new NestPath();
        List<Double> lList = aInPolygon.getPoints();
        int size = lList.size();
        if(size % 2 != 0) {
            System.out.println("createNestPath: Unexpected error: Polygon have number of points =  " + size);
        }

        for(int i = 0; i < size/2; i++) {
            lPath.add(lList.get(i*2), lList.get(i*2 + 1) + offsetY);
        }
        return lPath;
    }
}
