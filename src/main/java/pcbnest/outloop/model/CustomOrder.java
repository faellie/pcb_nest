package pcbnest.outloop.model;


import pcbnest.outloop.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;

public class CustomOrder {
    private ArrayList<PcbBoard> pcbBoards = new ArrayList<>();
    private ArrayList<BaseBoard> baseBoards = new ArrayList<>();

    /**
     * We should always try to construct the order completly in one call.
     * The two list should not be changed after construction
     * @param pcbBoards
     * @param outBoards
     */
    public CustomOrder(ArrayList<PcbBoard> pcbBoards, ArrayList<BaseBoard> outBoards) {
        this.pcbBoards = pcbBoards;
        this.baseBoards = outBoards;
        sort();
    }

    /**
     * sort the internal list based on area
     */
    private void sort(){
        Collections.sort(pcbBoards, Comparators.compareShape);
    }


    public ArrayList<PcbBoard> getPcbBoards() {
        return pcbBoards;
    }

    public void setPcbBoards(ArrayList<PcbBoard> pcbBoards) {
        this.pcbBoards = pcbBoards;
    }

    public ArrayList<BaseBoard> getBaseBoards() {
        return baseBoards;
    }

    public void setBaseBoards(ArrayList<BaseBoard> baseBoards) {
        this.baseBoards = baseBoards;
    }

    public int getToutalCount() {
        int toutalCount = 0;
        for(PcbBoard pcb : pcbBoards) {
            toutalCount = toutalCount + pcb.getCount();
        }
        return toutalCount;
    }


    public ArrayList<PcbBoard> clonePcbBoards() {
        ArrayList<PcbBoard> lRet = new ArrayList<>();
        for(PcbBoard pcb : pcbBoards ) {
            PcbBoard lClone = new PcbBoard(pcb.getArea(), pcb.getCount(), pcb.getId());
            lRet.add(lClone);
        }
        return lRet;
    }
}
