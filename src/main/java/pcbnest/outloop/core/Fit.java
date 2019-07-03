package pcbnest.outloop.core;


import pcbnest.outloop.model.*;
import pcbnest.outloop.utils.Comparators;
import pcbnest.outloop.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Fit {
    private CustomOrder customOrder;

    //todo configuration or...

    //this is the final results
    private ArrayList<Partern> fits = new ArrayList<>();

    public Fit(CustomOrder customOrder) {
        this.customOrder = customOrder;
    }

    //try to find the best possible fit for the order
    public void doFit() {

        ArrayList<PcbBoard> targetList = customOrder.clonePcbBoards();
        ArrayList<WorkOrder> workOrders = new ArrayList<>();

        //todo can sort in different ways for different output
        Collections.sort(targetList, Comparators.compareCount);



        //now we add to the work order
        while (!CommonUtils.noMorePart(targetList)) {
            WorkOrder lWorkOrder = nextWorkOrder(targetList);
            workOrders.add(lWorkOrder);
        }
        System.out.print("Final work order : " + CommonUtils.printWorkOrders(workOrders));
    }



    private WorkOrder nextWorkOrder(ArrayList<PcbBoard> aInTargetList ) {
        Double usedArea = 0.0;
        Double usedPercent = 0.0;
        //todo only use first
        Partern lPartern = Partern.initPartern(customOrder.getBaseBoards().get(0), customOrder);
        boolean canFit = true;

        //try to fit as much as possible
        while(canFit) {
            canFit = false;
            for (int index = 0; index < aInTargetList.size(); index++) {
                PcbBoard nextPcb = aInTargetList.get(index);
                if(nextPcb.getCount() > 0) {
                    //todo we only work on first outboard (the largest) for now
                    if (usedArea + nextPcb.getArea() < customOrder.getBaseBoards().get(0).getArea() * customOrder.getMaxFitRate()) {
                        //take a decision based on the count to fit this one or not
                        canFit = true;
                        if(doAdd(nextPcb, aInTargetList)) {
                            //add this from target list to  selectedList
                            lPartern.increCount(nextPcb.getId());
                            nextPcb.decreCount();
                            usedArea = usedArea + nextPcb.getArea();
                            usedPercent = (usedArea * 100.00) / customOrder.getBaseBoards().get(0).getArea();
                        }
                    } else {
                        //can not fit this pcb in, move to next
                    }
                }
            }
        }

        //now try to dup above partern as much as possible
        //i.e we will see how much can we repeat lParternList.get(0) in targetList
        //use a impossible larger number and decrease it when loop thought each pcb board
        int dup = 10000000;
        ArrayList<FillElement> fillElements = lPartern.getPcb();
        for(FillElement lElement : fillElements) {
            //PcbBoard lPcb = targetList.stream().filter((a)->a.getId() == lElement.getPcbId());
            PcbBoard lPcb = CommonUtils.getWithId(aInTargetList, lElement.getPcbId());
            if(lElement.getCount() != 0) {
                dup = dup > lPcb.getCount() / lElement.getCount() ? lPcb.getCount() / lElement.getCount() : dup;
            }
        }
        if( dup >= 0 ) {
            //update targetList
            for(FillElement lElement : fillElements) {
                //PcbBoard lPcb = targetList.stream().filter((a)->a.getId() == lElement.getPcbId());
                PcbBoard lPcb = CommonUtils.getWithId(aInTargetList, lElement.getPcbId());
                if(lElement.getCount() != 0) {
                    int newCount = lPcb.getCount() - dup * lElement.getCount();
                    if(newCount < 0) {
                        //todo
                        System.out.println("Error : unexpect new count = " + newCount);
                        System.exit(0);
                    }
                    lPcb.setCount(newCount);
                }
            }
        }
        return new WorkOrder(lPartern, dup + 1, usedPercent);
    }

    /**
     * take a decisison random to add the pcb or not based on the count of the pcb in the aInTargetList
     * @param aInPcb
     * @param aInTargetList
     * @return
     */
    private boolean doAdd(PcbBoard aInPcb, ArrayList<PcbBoard> aInTargetList) {
        int lTotalCount = 0;
        for(PcbBoard lpcb : aInTargetList) {
            lTotalCount = lTotalCount + lpcb.getCount();
        }
        Random lRand  = new Random();
        int lDice = lRand.nextInt(lTotalCount);
        return lDice <= aInPcb.getCount();
    }
}
