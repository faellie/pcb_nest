package pcbnest.outloop.core;


import pcbnest.outloop.model.*;
import pcbnest.outloop.utils.Comparators;
import pcbnest.outloop.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Fit {
    private CustomOrder customOrder;
    private ArrayList<WorkOrder> workOrders = new ArrayList<>();

    //todo configuration or...


    public Fit(CustomOrder customOrder) {
        this.customOrder = customOrder;
    }


    public CustomOrder getCustomOrder() {
        return customOrder;
    }

    public ArrayList<WorkOrder> getWorkOrders() {
        return workOrders;
    }

    //try to find the best possible fit for the order
    public void doFit() {

        ArrayList<PcbBoard> targetList = customOrder.clonePcbBoards();


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

        //todo only use first
        //Patern lPatern = Patern.initPatern(customOrder.getBaseBoards().get(0), customOrder);
        Patern lPatern = initPatern(customOrder.getBaseBoards().get(0), aInTargetList);
        boolean canFit = true;
        double usedArea = lPatern.getUsedArea();
        double usedPercent = usedArea/customOrder.getBaseBoards().get(0).getArea();
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
                            lPatern.increCount(nextPcb.getId());
                            nextPcb.decreCount();
                            usedArea = usedArea + nextPcb.getArea();
                            usedPercent = usedArea/customOrder.getBaseBoards().get(0).getArea();
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
        ArrayList<FillElement> fillElements = lPatern.getPcb();
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
        return new WorkOrder(lPatern, dup + 1, usedPercent);
    }

    private Patern initPatern(BaseBoard aInBaseBoard, ArrayList<PcbBoard> aInTargetList) {
        Patern lPatern = Patern.initPatern(aInBaseBoard, customOrder);
        int lMaxCount = maxCount(aInTargetList);
        int lMinCount = minCount(aInTargetList);

        double usedArea = 0.0;
        double usedPercent = 0.0;
        double targetedArea = customOrder.getBaseBoards().get(0).getArea() * customOrder.getMaxFitRate();

        for (int index = 0; index < aInTargetList.size(); index++) {
            PcbBoard nextPcb = aInTargetList.get(index);
            //if min count 100  and this count = 330, we will try to add 3 (330/100) first
            if(nextPcb.getCount() > 0) {
                double dup = nextPcb.getCount()/lMinCount;
                usedArea = usedArea + nextPcb.getArea() * dup;
            }
        }
        // now check the area
        double ratio = targetedArea/usedArea;
        //now go through the loop again with the ratio
        usedArea = 0.0;
        usedPercent = 0.0;
        for (int index = 0; index < aInTargetList.size(); index++) {
            PcbBoard nextPcb = aInTargetList.get(index);
            //if min count 100  and this count = 330, we will try to add 3 (330/100) first
            if(nextPcb.getCount() > 0) {
                int dup = (int) ((nextPcb.getCount()/lMinCount) * ratio);
                dup = dup <= nextPcb.getCount() ? dup :  nextPcb.getCount();
                nextPcb.decreCount(dup);
                lPatern.increCount(nextPcb.getId(), dup);
                usedArea = usedArea + nextPcb.getArea() * dup;
            }
        }
        return lPatern;
    }

    /**
     * take a decision random to add the pcb or not based on the count of the pcb in the aInTargetList
     * @param aInPcb
     * @param aInTargetList
     * @return
     */
    private boolean doAdd(PcbBoard aInPcb, ArrayList<PcbBoard> aInTargetList) {
        int lTotalCount = totalCount(aInTargetList);
        Random lRand  = new Random();
        int lDice = lRand.nextInt(lTotalCount);
        return lDice <= aInPcb.getCount();
    }

    private int totalCount(ArrayList<PcbBoard> aInTargetList) {
        int lTotalCount = 0;
        for(PcbBoard lpcb : aInTargetList) {
            lTotalCount = lTotalCount + lpcb.getCount();
        }
        return lTotalCount;
    }

    private int maxCount(ArrayList<PcbBoard> aInTargetList) {
        int lMaxCount = 1;
        for(PcbBoard lpcb : aInTargetList) {
            lMaxCount = lMaxCount > lpcb.getCount() ? lMaxCount :  lpcb.getCount();
        }
        return lMaxCount;
    }

    private int minCount(ArrayList<PcbBoard> aInTargetList) {
        int lMinCount = 1000000000;
        for(PcbBoard lpcb : aInTargetList) {
            if(lpcb.getCount() != 0) {
                lMinCount = lMinCount < lpcb.getCount() ? lMinCount : lpcb.getCount();
            }
        }
        return lMinCount;
    }
}
