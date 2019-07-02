package pcbnest.outloop.test;


import pcbnest.outloop.logic.Fit;
import pcbnest.outloop.model.BaseBoard;
import pcbnest.outloop.model.CustomOrder;
import pcbnest.outloop.model.PcbBoard;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String [] args) throws IOException {
        ArrayList<PcbBoard> pcbBoards = new ArrayList<>();
        ArrayList<BaseBoard> baseBoards = new ArrayList<>();

        baseBoards.add(new BaseBoard(1000.0, 1));
        pcbBoards.add(new PcbBoard(200.0, 100, 1));
        pcbBoards.add(new PcbBoard(100.0, 200, 2));
        pcbBoards.add(new PcbBoard(50.0, 200, 3));


        CustomOrder customOrder = new CustomOrder(pcbBoards, baseBoards);
        Fit lFit = new Fit(customOrder);
        lFit.doFit();

    }
}
