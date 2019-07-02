package pcbnest.outloop.utils;


import pcbnest.outloop.model.PcbBoard;
import pcbnest.outloop.model.WorkOrder;

import java.util.ArrayList;

public class Generic {

    public static PcbBoard getWithId(ArrayList<PcbBoard> aInPcbBoardList, int aInId) {
        for(PcbBoard pcb : aInPcbBoardList ) {
            if(pcb.getId() == aInId) {
                return pcb;
            }
        }
        //todo
        System.out.println("ERROR : Failed to found PCBboard with id " + aInId);
        return null;
    }

    public static boolean noMorePart(ArrayList<PcbBoard> aInTaregtList) {
        int parts = 0;
        for(PcbBoard pcb : aInTaregtList) {
            parts = parts + pcb.getCount();
        }
        return parts <= 0;
    }

    public static String printWorkOrders(ArrayList<WorkOrder> workOrders) {
        String lines = "\n";
        for(WorkOrder lWorkOrder : workOrders) {
            lines = lines + lWorkOrder.toString() + "\n";
        }
        return lines;
    }
}
