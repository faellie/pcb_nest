package pcbnest.dxf;



import javafx.scene.shape.Polygon;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.dxf.DXFDocument;
import org.kabeja.dxf.DXFLayer;
import org.kabeja.dxf.DXFPolyline;
import org.kabeja.parser.DXFParser;
import org.kabeja.parser.ParseException;
import org.kabeja.parser.Parser;
import org.kabeja.parser.ParserBuilder;
import org.kabeja.svg.generators.SVGPolylineGenerator;
import pcbnest.common.Config;
import pcbnest.common.PartConfig;
import pcbnest.common.PartRatio;
import pcbnest.utils.dxf2svg.PolylineUtils;
import pcbnest.utils.dxf2svg.model.Bound;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class dxfUtils {


    private  static SVGPolylineGenerator svgPolylineGenerator= new SVGPolylineGenerator();

    public static void processParts(PartConfig aInPartConfig) {
        for(int i = 0; i < aInPartConfig.getDups(); i ++) {
            processPart(aInPartConfig);
        }
    }

    public static void processPart(PartConfig aInPartConfig) {
        String lDxfFileName = aInPartConfig.getFilename();
        Parser parser = ParserBuilder.createDefaultParser();
        FileInputStream lInputStream = null;
        try {
            lInputStream = new FileInputStream(lDxfFileName);
            parser.parse(lInputStream, DXFParser.DEFAULT_ENCODING);
        } catch (FileNotFoundException e) {
            System.out.println("Failed to open file. " + e);
            return;
        } catch (ParseException e) {
            System.out.println("Failed to parse file. " + e);
            return;
        }

        //get the document and the layer
        DXFDocument doc = parser.getDocument();
        DXFLayer layer = doc.getDXFLayer(aInPartConfig.getLayer());

        //get all polylines from the layer
        List plines = layer.getDXFEntities(DXFConstants.ENTITY_TYPE_POLYLINE);
        PolylineUtils.cleanDups(plines);
        PolylineUtils.cleanOrphan(plines);
        PolylineUtils.normalizePlines(plines, new Bound());
        List lOrederedPl = PolylineUtils.reorder(plines);
        addPartToFile(lOrederedPl, aInPartConfig);
    }

    private static void addPartToFile(List<DXFPolyline> aInPlList, PartConfig aInPartConfig) {
        boolean isFirst = true;
        String lines = "<path id=\"" + aInPartConfig.getLayer() + "\" d = \"";
        for (DXFPolyline pl : aInPlList) {
            String lSVGPath = svgPolylineGenerator.getSVGPath(pl);
            if (isFirst) {
                isFirst = false;
            } else {
                lSVGPath = lSVGPath.replaceFirst("M", "L");
            }
            lines = lines + lSVGPath + "\n";
        }
        lines = lines + "z \" stroke=\"" + aInPartConfig.getColorcode() + " \" fill = \"none\" />";
        //writer.println(lines);
    }
}
