package pcbnest.outloop.model;


import java.util.ArrayList;

public class PcbBoard extends Shape{
    //how many is required
    private int count;

    //id should be linked to the file name of the svg/dxf file
    private String id;

    public PcbBoard(double area, int count, String id) {
        super(area);
        this.count = count;
        this.id = id;
    }

    public PcbBoard() {
        super();
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void decreCount() {
        if(count <=0 ) {
            System.out.println("Error : PcbBoard.decreCount(); count can not be < 0 ");
        }
        count --;
    }

    public void decreCount(int decr) {
        if(count <=0 ) {
            System.out.println("Error : PcbBoard.decreCount(); count can not be < 0 ");
        }
        count = count - decr;
        if(count < 0 ) {
            System.out.println("Error : PcbBoard.decreCount(); count =  " + count + " after decr by " + decr );
        }
    }
}
