package pcbnest.outloop.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pcbnest.outloop.core.Fit;
import pcbnest.outloop.model.BaseBoard;
import pcbnest.outloop.model.CustomOrder;
import pcbnest.outloop.model.PcbBoard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Test1 {
    public static void main(String [] args) throws IOException {
        if(args.length < 1) {
            System.out.println("Usage : java -jar loop1 [full_path_for_input_file]");
            System.exit(0);
        }
        File lInputFile = new File(args[0]);

        //load input from json
        CustomOrder customOrder = null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            customOrder = mapper.readValue(lInputFile, CustomOrder.class);
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(customOrder);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if(customOrder != null) {
            System.out.println("Custom Order : " + customOrder.toString());
            Fit lFit = new Fit(customOrder);
            lFit.doFit();
        }

    }
}
