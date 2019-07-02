package pcbnest.outloop.model;


import java.util.ArrayList;

public class Partern {
    private int baseBoardId;
    private ArrayList<FillElement> pcbs;

    public Partern(int outBoardId, ArrayList<FillElement> pcbs) {
        this.baseBoardId = outBoardId;
        this.pcbs = pcbs;
    }

    public int getBaseBoardId() {
        return baseBoardId;
    }

    public void setBaseBoardId(int baseBoardId) {
        this.baseBoardId = baseBoardId;
    }

    public ArrayList<FillElement> getPcb() {
        return pcbs;
    }

    public void setPcb(ArrayList<FillElement> pcb) {
        this.pcbs = pcb;
    }

    public static Partern initPartern(BaseBoard aInOutBoard, CustomOrder aInCustomOrder) {
        ArrayList<FillElement> list = new ArrayList<>();
        for(PcbBoard pcb : aInCustomOrder.getPcbBoards()) {
            FillElement lFill = new FillElement(pcb.getId(), 0);
            list.add(lFill);
        }
        return new Partern(aInOutBoard.getId(), list);
    }

    public void increCount(int aInPcbId) {
        for(FillElement lElement : pcbs) {
            if(lElement.getPcbId() == aInPcbId) {
                int lCurrCount = lElement.getCount();
                lElement.setCount(lCurrCount + 1);
            }
        }
    }

    public String toString() {

        String lRet = "BaseBoardID : " + baseBoardId + "; pcbs : [ " ;
        for(FillElement pcb : pcbs) {
            lRet = lRet + "(id = " + pcb.getPcbId() + " and num = " + pcb.getCount() + ")";
        }
        lRet = lRet + "]";
        return  lRet;
    }
}
