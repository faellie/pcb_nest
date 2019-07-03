package pcbnest.outloop.model;


import java.util.ArrayList;

public class FillElement {
    private String pcbId;
    private int count;
    private double area;

    public FillElement(String pcbId, int count, double area) {
        this.pcbId = pcbId;
        this.count = count;
        this.area = area;
    }

    public String getPcbId() {
        return pcbId;
    }

    public void setPcbId(String pcbId) {
        this.pcbId = pcbId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }
}
