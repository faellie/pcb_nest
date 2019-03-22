package pcbnest.common;


import javafx.scene.shape.Polygon;

public class PartRatio {

    private Polygon polygon;
    private String name;
    int ratio = 0;

    public PartRatio(Polygon polygon, int ratio) {
        this.polygon = polygon;
        this.ratio = ratio;
    }

    public PartRatio(Polygon polygon, String name, int ratio) {
        this.polygon = polygon;
        this.name = name;
        this.ratio = ratio;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }
}
