package pcbnest.outloop.model;


public class WorkOrder {
    private Partern partern;
    private int dups = 0;
    private Double usagePercent = 0.0;

    public WorkOrder(Partern partern, int dups, double usagePercent) {
        this.partern = partern;
        this.dups = dups;
        this.usagePercent = usagePercent;
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
                "Dups = " + dups + "; partern : " + partern.toString() + "; Usage Percent =  " + (usagePercent + "%") ;
        return lStr;
    }

}
