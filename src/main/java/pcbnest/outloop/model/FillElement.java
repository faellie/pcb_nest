package pcbnest.outloop.model;


import java.util.ArrayList;

public class FillElement {
    private int pcbId;
    private int count;

    public FillElement(int pcbId, int count) {
        this.pcbId = pcbId;
        this.count = count;
    }

    public int getPcbId() {
        return pcbId;
    }

    public void setPcbId(int pcbId) {
        this.pcbId = pcbId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
