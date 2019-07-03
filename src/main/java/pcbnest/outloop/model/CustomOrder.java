package pcbnest.outloop.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import pcbnest.outloop.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;

public class CustomOrder {
    private ArrayList<PcbBoard> pcbBoards = new ArrayList<>();
    private ArrayList<BaseBoard> baseBoards = new ArrayList<>();
    private Double maxFitRate = 0.95;
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

    public CustomOrder() {
    }

    /**
     * sort the internal list based on area
     */
    private void sort(){
        Collections.sort(pcbBoards, Comparators.compareArea);
        Collections.sort(baseBoards, Comparators.compareArea);
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

    public Double getMaxFitRate() {
        return maxFitRate;
    }

    public void setMaxFitRate(Double maxFitRate) {
        this.maxFitRate = maxFitRate;
    }

    @JsonIgnore
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

    public String toString() {

        String lRet = "\n\tBaseBoardID : " + getBaseBoards().get(0).getId() + "(A:" + getBaseBoards().get(0).getArea() + ")\n\t";
        for(PcbBoard pcb : getPcbBoards()) {
            lRet = lRet + "(" +pcb.getId() + " A:" + pcb.getArea() + ") X " + pcb.getCount() + "; ";
        }
        lRet = lRet + "]";
        return  lRet;

    }
}
