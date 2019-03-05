package nest4J.data;

import nest4J.data.NestPath;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yisa
 */
public class ParallelData {
    public NfpKey key ;
    public List<nest4J.data.NestPath> value ;

    public ParallelData() {
        value = new ArrayList<nest4J.data.NestPath>();
    }

    public ParallelData(NfpKey key, List<nest4J.data.NestPath> value) {
        this.key = key;
        this.value = value;
    }

    public NfpKey getKey() {
        return key;
    }

    public void setKey(NfpKey key) {
        this.key = key;
    }

    public List<nest4J.data.NestPath> getValue() {
        return value;
    }

    public void setValue(List<NestPath> value) {
        this.value = value;
    }
}
