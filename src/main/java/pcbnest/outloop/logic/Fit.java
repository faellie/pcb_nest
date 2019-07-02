package pcbnest.outloop.logic;


import pcbnest.outloop.model.*;
import pcbnest.outloop.utils.Comparators;
import pcbnest.outloop.utils.Generic;

import java.util.ArrayList;
import java.util.Collections;

public class Fit {
    private CustomOrder customOrder;

    //todo configuration or...
    private Double fitRate = 0.9;

    //this is the final results
    private ArrayList<Partern> fits = new ArrayList<>();
    public Fit(CustomOrder customOrder) {
        this.customOrder = customOrder;
    }

    //try to find the best possible fit for the order
    public void doFit() {

        ArrayList<PcbBoard> targetList = customOrder.clonePcbBoards();
        ArrayList<WorkOrder> workOrders = new ArrayList<>();
        Collections.sort(targetList, Comparators.comparePcbBoard);



        //now we add to the work order
        while (!Generic.noMorePart(targetList)) {
            WorkOrder lWorkOrder = nextWorkOrder(targetList);
            workOrders.add(lWorkOrder);
        }
        System.out.print("Final work order : " + Generic.printWorkOrders(workOrders));
    }



    private WorkOrder nextWorkOrder(ArrayList<PcbBoard> aInTargetList ) {
        Double usedArea = 0.0;

        //todo only use first
        Partern lPartern = Partern.initPartern(customOrder.getBaseBoards().get(0), customOrder);
        boolean foundFit = true;

        //try to fit as much as possible
        while(foundFit) {
            foundFit = false;
            for (int index = 0; index < aInTargetList.size(); index++) {
                PcbBoard nextPcb = aInTargetList.get(index);
                if(nextPcb.getCount() > 0) {
                    usedArea = usedArea + nextPcb.getArea();
                    //todo we only work on first out board (the largest) for now
                    if (usedArea < customOrder.getBaseBoards().get(0).getArea() * fitRate) {
                        //add this from target list to  selectedList
                        lPartern.increCount(nextPcb.getId());
                        nextPcb.decreCount();
                        foundFit = true;
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
            PcbBoard lPcb = Generic.getWithId(aInTargetList, lElement.getPcbId());
            if(lElement.getCount() != 0) {
                dup = dup > lPcb.getCount() / lElement.getCount() ? lPcb.getCount() / lElement.getCount() : dup;
            }
        }
        if( dup >= 0 ) {
            //update targetList
            for(FillElement lElement : fillElements) {
                //PcbBoard lPcb = targetList.stream().filter((a)->a.getId() == lElement.getPcbId());
                PcbBoard lPcb = Generic.getWithId(aInTargetList, lElement.getPcbId());
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
        return new WorkOrder(lPartern, dup + 1);
    }
}
