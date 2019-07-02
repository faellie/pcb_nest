package pcbnest.outloop.model;


import java.util.ArrayList;

public class PcbBoard extends Shape{
    //how many is required
    private int count;
    private int id;

    public PcbBoard(double area, int count, int id) {
        super(area);
        this.count = count;
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void decreCount() {
        if(count <=0 ) {
            System.out.println("Error : PcbBoard.decreCount(); count can not be < 0 ");
        }
        count --;
    }
}
