package pcbnest.outloop.model;


public class WorkOrder {
    private Patern patern;
    private int dups = 0;
    private Double usagePercent = 0.0;

    public WorkOrder(Patern patern, int dups, double usagePercent) {
        this.patern = patern;
        this.dups = dups;
        this.usagePercent = usagePercent;
    }

    public Patern getPatern() {
        return patern;
    }

    public void setPatern(Patern patern) {
        this.patern = patern;
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


    public String info() {
        String lStr =
                "Dups = " + dups + "; partern : " + patern.info() + "; Usage Percent =  " + (usagePercent*100) + "%" ;
        return lStr;
    }

}
