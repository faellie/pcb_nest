package pcbnest.outloop.model;


public class BaseBoard extends Shape {
    String id;

    public BaseBoard(double area, String id) {
        super(area);
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public BaseBoard() {
    }

    public void setId(String id) {
        this.id = id;
    }
}
