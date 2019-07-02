package pcbnest.outloop.model;


public class BaseBoard extends Shape {
    int id;

    public BaseBoard(double area, int id) {
        super(area);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
