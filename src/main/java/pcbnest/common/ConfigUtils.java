package pcbnest.common;



import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import nest4J.data.NestPath;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pcbnest.svg.SvgConverter;;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {
    public static Polygon getMainPcb(Config aInConfig) {
        Polygon lRect = new Polygon();
        lRect.getPoints().addAll(new Double[]{
                0.0, 0.0,
                (double)aInConfig.getWidth(), 0.0,
                (double)aInConfig.getWidth(), (double)aInConfig.getHight(),
                0.0, (double)aInConfig.getHight()
        });

        return lRect;
    }

    public static List<PartRatio> getPartList(Config aInConfig){
        List<PartRatio>  list = new ArrayList<>();
        for(PartConfig partConfig : aInConfig.getParts()) {
            //todo for now assume we have svg files contains the path, and we converter to polygon
            Polygon lPolygon = SvgConverter.getPolygonFromSvg(partConfig.getFilename());
            lPolygon.setFill(getColor(partConfig.getColorcode()));
            File lFile = new File(partConfig.getFilename());
            String lName = lFile.getName();
            lName = lName.substring(0, lName.indexOf("."));
            PartRatio lPartRatio  = new PartRatio(lPolygon, lName, partConfig.getDups());

            list.add(lPartRatio);

        }
        return list;
    }

    private static Paint getColor(String aInCode) {
        if(aInCode.equalsIgnoreCase("green")) {
            return Color.GREEN;
        }else if(aInCode.equalsIgnoreCase("red")) {
            return Color.RED;
        } else if(aInCode.equalsIgnoreCase("yellow")) {
            return Color.YELLOW;
        } else if(aInCode.equalsIgnoreCase("blue")) {
            return Color.BLUE;
        } else if(aInCode.equalsIgnoreCase("PURPLE")) {
            return Color.PURPLE;
        } else {
            return Color.BLACK;
        }
    }


}
