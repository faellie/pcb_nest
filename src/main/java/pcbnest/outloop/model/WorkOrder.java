package pcbnest.outloop.model;


import pcbnest.outloop.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;

public class WorkOrder {
    private Partern partern;
    int dups = 0;

    public WorkOrder(Partern partern, int dups) {
        this.partern = partern;
        this.dups = dups;
    }

    public Partern getPartern() {
        return partern;
    }

    public void setPartern(Partern partern) {
        this.partern = partern;
    }

    public int getDups() {
        return dups;
    }

    public void setDups(int dups) {
        this.dups = dups;
    }

    public void IncreDups() {
        this.dups ++;
    }


    public String toString() {
        String lStr =
                "Dups = " + dups + "; partern : " + partern.toString();
        return lStr;
    }
}
