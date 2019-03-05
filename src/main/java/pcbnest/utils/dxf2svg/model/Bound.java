package pcbnest.utils.dxf2svg.model;

/**
 * read multiple dxf files from a dir and get polygon from each file (assume each file contains a polygon).
 * shift them and put into one SVG file
 */


public class Bound {


    private int minX;


    private int minY;


    private int maxX;


    private int maxY;

    public Bound() {
        minX = 0;
        minY = 0;
        maxX = 0;
        maxY = 0;
    }

    public Bound(int minX, int minY, int maxX, int maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public String toString() {
        return "minX = " + minX + " minY = " + minY  + " maxX = " + maxX + " maxY = " + maxY;
    }


    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
