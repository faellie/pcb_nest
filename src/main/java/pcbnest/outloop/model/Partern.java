package pcbnest.outloop.model;


import java.util.ArrayList;

public class Partern {
    private String baseBoardId;
    private ArrayList<FillElement> pcbs;

    public Partern(String aInBaseBoardId, ArrayList<FillElement> pcbs) {
        this.baseBoardId = aInBaseBoardId;
        this.pcbs = pcbs;
    }

    public String getBaseBoardId() {
        return baseBoardId;
    }

    public void setBaseBoardId(String baseBoardId) {
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
            FillElement lFill = new FillElement(pcb.getId(), 0, pcb.getArea());
            list.add(lFill);
        }
        return new Partern(aInOutBoard.getId(), list);
    }

    public void increCount(String aInPcbId) {
        for(FillElement lElement : pcbs) {
            if(lElement.getPcbId().equals(aInPcbId)) {
                int lCurrCount = lElement.getCount();
                lElement.setCount(lCurrCount + 1);
            }
        }
    }

    public String toString() {

        String lRet = "BaseBoardID : " + baseBoardId + "; pcbs : [ " ;
        for(FillElement pcb : pcbs) {
            lRet = lRet + "(" +pcb.getPcbId() + " A:" + pcb.getArea() + ") X " + pcb.getCount() + "; ";
        }
        lRet = lRet + "]";
        return  lRet;
    }
}
