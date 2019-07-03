package pcbnest.outloop.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import pcbnest.outloop.core.Fit;
import pcbnest.outloop.model.BaseBoard;
import pcbnest.outloop.model.CustomOrder;
import pcbnest.outloop.model.PcbBoard;

import java.io.IOException;
import java.util.ArrayList;

public class Test {
    public static void main(String [] args) throws IOException {
        ArrayList<PcbBoard> pcbBoards = new ArrayList<>();
        ArrayList<BaseBoard> baseBoards = new ArrayList<>();

        baseBoards.add(new BaseBoard(1000.0, "mainBoard"));
        pcbBoards.add(new PcbBoard(200.0, 100, "board1"));
        pcbBoards.add(new PcbBoard(100.0, 200, "board2"));
        pcbBoards.add(new PcbBoard(50.0, 200, "board3"));


        CustomOrder customOrder = new CustomOrder(pcbBoards, baseBoards);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customOrder);
            //String json = mapper.writeValueAsString(customOrder);
            System.out.println("customOrder JSON = " + json);

            CustomOrder customOrder1 = mapper.readValue(json, CustomOrder.class);
            String json1 = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customOrder);
            System.out.println("customOrder JSON = " + json1);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Fit lFit = new Fit(customOrder);
        lFit.doFit();

    }
}
