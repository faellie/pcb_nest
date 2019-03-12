package pcbnest.common;


import javafx.scene.shape.Polygon;

public class PartRatio {

    private Polygon polygon;
    int ratio = 0;

    public PartRatio(Polygon polygon, int ratio) {
        this.polygon = polygon;
        this.ratio = ratio;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
}
