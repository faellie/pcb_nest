package pcbnest;



import com.sun.javafx.scene.shape.PathUtils;
import nest4J.Nest;
import nest4J.data.NestPath;

import java.util.ArrayList;
import java.util.List;

import nest4J.data.*;
import nest4J.util.*;
import de.lighti.clipper.Path;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;

public class PcbNest {

    public static void main(String[] args) {
        try {
            //test1();
            //testTriangle();
            testL();
            //testSample();
            //testArea();
        } catch (Exception e) {
            System.out.println("Exception : " + e);
        }
    }

    public static void testArea() {

        NestPath binPolygon = new NestPath();
        double width = 220;
        double height = 105;
        binPolygon.add(0, 0);
        binPolygon.add(0, height);
        binPolygon.add(width, height);
        binPolygon.add(width, 0);
        NestPath L1 = new NestPath();
        L1.add(0, 1000);
        L1.add(100, 1000);
        L1.add(100, 1200);
        L1.add(200, 1200);
        L1.add(200, 1400);
        L1.add(0, 1400);

        System.out.println("L1 Area = " + GeometryUtil.polygonArea(L1));
        NestPath L2 = new NestPath();
        L2.add(1000, 0);
        L2.add(1100, 000);
        L2.add(1100, 100);
        L2.add(1200, 100);
        L2.add(1200, 200);
        L2.add(1000, 200);

        System.out.println("L2 Area = " + GeometryUtil.polygonArea(L2));

    }


    public static void test1() {

        NestPath binPolygon = new NestPath();
        double width = 220;
        double height = 105;
        binPolygon.add(0, 0);
        binPolygon.add(0, height);
        binPolygon.add(width, height);
        binPolygon.add(width, 0);
        NestPath outer = new NestPath();
        //100X100 square
        outer.add(600, 0);
        outer.add(600, 100);
        outer.add(700, 100);
        outer.add(700, 0);
        outer.setRotation(4);
        outer.bid = 1;

        //rect
        NestPath inner = new NestPath();
        inner.add(800, 0);
        inner.add(800, 100);
        inner.add(820, 100);
        inner.add(820, 0);
        inner.setRotation(4);
        inner.bid = 2;
        NestPath little = new NestPath();
        little.add(860, 0);
        little.add(860, 20);
        little.add(960, 20);
        little.add(960, 0);
        inner.setRotation(4);
        little.bid = 3;

        NestPath little1 = new NestPath();
        little1.add(360, 0);
        little1.add(360, 20);
        little1.add(460, 20);
        little1.add(460, 0);
        inner.setRotation(4);
        little1.bid = 4;

        NestPath et1 = new NestPath();
        et1.add(500, 0);
        et1.add(520, 20);
        et1.add(500, 40);
        inner.setRotation(4);
        et1.bid = 5;

        NestPath et2 = new NestPath();
        et2.add(500, 50);
        et2.add(520, 70);
        et2.add(500, 90);
        inner.setRotation(8);
        et2.bid = 6;

        NestPath et3 = new NestPath();
        et3.add(530, 0);
        et3.add(550, 20);
        et3.add(530, 40);
        inner.setRotation(8);
        et3.bid = 7;


        NestPath et4 = new NestPath();
        et4.add(565, 0);
        et4.add(575, 20);
        et4.add(565, 40);
        inner.setRotation(8);
        et4.bid = 8;

        NestPath et5 = new NestPath();
        et5.add(530, 320);
        et5.add(550, 340);
        et5.add(530, 360);
        inner.setRotation(8);
        et5.bid = 9;


        NestPath et6 = new NestPath();
        et6.add(530, 400);
        et6.add(550, 420);
        et6.add(530, 440);
        inner.setRotation(8);
        et6.bid = 10;

        List<NestPath> list = new ArrayList<NestPath>();
        list.add(inner);
        list.add(outer);
        list.add(little);
        list.add(little1);
        list.add(et1);
        list.add(et2);
        list.add(et3);
        list.add(et4);
        list.add(et5);
        list.add(et6);

        Config config = new Config();
        config.USE_HOLE = true;
        config.SPACING = 0.0;
        config.POPULATION_SIZE=4;
        config.CLIIPER_SCALE=10000;
        config.CURVE_TOLERANCE = 0.0;
        Nest nest = new Nest(binPolygon, list, config, 5000);
        List<List<Placement>> appliedPlacement = nest.startNest();
        List<String> strings = null;
        try {
            strings = SvgUtil.svgGenerator(list, appliedPlacement, width, height);
            saveSvgFile(strings);
        } catch (Exception e) {

        }
        for (String s : strings) {
            System.out.println(s);
        }

        //todo calculate usage
        double usedArea  = 0.0;
        for(NestPath path : list) {
            usedArea = GeometryUtil.polygonArea(path) + usedArea;
        }
        double rate = usedArea/GeometryUtil.polygonArea(binPolygon);
        System.out.println("Usage = " + rate);
        return;
    }


    public static void testTriangle() {
        //test some basic fucntioanlity
        NestPath binPolygon = new NestPath();
        double width = 410;
        double height = 210;

        binPolygon.add(0, 0);
        binPolygon.add(0, height);
        binPolygon.add(width, height);
        binPolygon.add(width, 0);


        NestPath triangle1 = new NestPath();
        // triangle1
        triangle1.add(500, 0);
        triangle1.add(500, 200);
        triangle1.add(700, 200);

        triangle1.setRotation(4);
        triangle1.bid = 1;

        NestPath triangle2 = new NestPath();
        triangle2.add(1000, 300);
        triangle2.add(1000, 500);
        triangle2.add(1200, 300);

        triangle2.setRotation(4);
        triangle2.bid = 2;

        NestPath triangle3 = new NestPath();
        triangle3.add(2000, 300);
        triangle3.add(2000, 500);
        triangle3.add(2200, 300);

        triangle3.setRotation(4);
        triangle3.bid = 3;


        NestPath triangle4 = new NestPath();
        triangle4.add(3000, 300);
        triangle4.add(3000, 500);
        triangle4.add(3200, 300);

        triangle4.setRotation(4);
        triangle4.bid = 4;

        List<NestPath> list = new ArrayList<NestPath>();
        list.add(triangle1);
        list.add(triangle2);
        list.add(triangle3);
        list.add(triangle4);

        Config config = new Config();
        config.USE_HOLE = true;
        config.SPACING = 0.0;
        config.POPULATION_SIZE=4;
        config.CLIIPER_SCALE=10000;
        config.CURVE_TOLERANCE = 0.0;
        Nest nest = new Nest(binPolygon, list, config, 500);
        List<List<Placement>> appliedPlacement = nest.startNest();
        List<String> strings = null;
        try {
            strings = SvgUtil.svgGenerator(list, appliedPlacement, width, height);
            saveSvgFile(strings);
        } catch (Exception e) {

        }
        for (String s : strings) {
            System.out.println(s);
        }

        //todo calculate usage
        double usedArea  = 0.0;
        for(NestPath path : list) {
            usedArea = GeometryUtil.polygonArea(path) + usedArea;
        }
        double rate = usedArea/GeometryUtil.polygonArea(binPolygon);
        System.out.println("Usage = " + rate);
        return;
    }

    public static void testL() {
        //test some basic fucntioanlity
        NestPath binPolygon = new NestPath();
        double width = 610;
        double height = 210;

        binPolygon.add(0, 0);
        binPolygon.add(0, height);
        binPolygon.add(width, height);
        binPolygon.add(width, 0);


        NestPath L1 = new NestPath();
        // add a L shape that take 3/4 of the 200X 200
        L1.add(0, 1000);
        L1.add(100, 1000);
        L1.add(100, 1100);
        L1.add(200, 1100);
        L1.add(200, 1200);
        L1.add(0, 1200);

        L1.setRotation(4);
        L1.bid = 1;

        // add another L shape that take 3/4 of the 200X 200
        NestPath L2 = new NestPath();
        L2.add(0, 2000);
        L2.add(100, 2000);
        L2.add(100, 2100);
        L2.add(200, 2100);
        L2.add(200, 2200);
        L2.add(0, 2200);

        L2.setRotation(4);
        L2.bid = 2;

        // add another L shape that take 3/4 of the 200X 200
        NestPath L3= new NestPath();
        L3.add(0, 3000);
        L3.add(100, 3000);
        L3.add(100, 3100);
        L3.add(200, 3100);
        L3.add(200, 3200);
        L3.add(0, 3200);

        L3.setRotation(4);
        L3.bid = 3;

        // add another L shape that take 3/4 of the 200X 200
        NestPath L4= new NestPath();
        L4.add(0, 4000);
        L4.add(100, 4000);
        L4.add(100, 4100);
        L4.add(200, 4100);
        L4.add(200, 4200);
        L4.add(0, 4200);

        L4.setRotation(4);
        L4.bid = 4;

        List<NestPath> list = new ArrayList<NestPath>();
        list.add(L1);
        list.add(L2);
        list.add(L3);
        list.add(L4);


        Config config = new Config();
        config.USE_HOLE = true;
        config.SPACING = 0.0;
        config.POPULATION_SIZE=10;
        config.CLIIPER_SCALE=10000;
        config.CURVE_TOLERANCE = 0.0;
        Nest nest = new Nest(binPolygon, list, config, 500);
        List<List<Placement>> appliedPlacement = null;
        appliedPlacement = nest.startNest();
        if(null == appliedPlacement || appliedPlacement.size() == 0 ) {
            System.out.println("Size = " + appliedPlacement.size());
        } else {
            while (appliedPlacement.size() > 1) {
                System.out.println("Size = " + appliedPlacement.size());
                nest = new Nest(binPolygon, list, config, 50);
                appliedPlacement = nest.startNest();
            }
        }
        List<String> strings = null;
        try {
            strings = SvgUtil.svgGenerator(list, appliedPlacement, width, height);
            saveSvgFile(strings);
        } catch (Exception e) {

        }
        for (String s : strings) {
            System.out.println(s);
        }

        //todo calculate usage
        double usedArea  = 0.0;
        for(NestPath path : list) {
            usedArea = GeometryUtil.polygonArea(path) + usedArea;
        }
        double rate = usedArea/GeometryUtil.polygonArea(binPolygon);
        System.out.println("Usage = " + rate);
        return;
    }

    public static void testSample() throws Exception {
        List<NestPath> polygons = transferSvgIntoPolygons();
        NestPath bin = new NestPath();
        double binWidth = 511.822;
        double binHeight = 339.235;
        bin.add(0, 0);
        bin.add(binWidth, 0);
        bin.add(binWidth, binHeight);
        bin.add(0, binHeight);
        bin.bid = -1;
        Config config = new Config();
        config.SPACING = 0;
        config.POPULATION_SIZE = 5;
        Nest nest = new Nest(bin, polygons, config, 2);
        List<List<Placement>> appliedPlacement = nest.startNest();
        List<String> strings = SvgUtil.svgGenerator(polygons, appliedPlacement, binWidth, binHeight);
        saveSvgFile(strings);
    }

    private static List<NestPath> transferSvgIntoPolygons() throws DocumentException {
        List<NestPath> nestPaths = new ArrayList<>();
        SAXReader reader = new SAXReader();
        //Document document = reader.read("resource/test.xml");
        Document document = reader.read("/opt/tmp/out1.svg");
        List<Element> elementList = document.getRootElement().elements();
        int count = 0;
        for (Element element : elementList) {
            count++;
            if ("polygon".equals(element.getName())) {
                String datalist = element.attribute("points").getValue();
                NestPath polygon = new NestPath();
                for (String s : datalist.split(" ")) {
                    s = s.trim();
                    if (s.indexOf(",") == -1) {
                        continue;
                    }
                    String[] value = s.split(",");
                    double x = Double.parseDouble(value[0]);
                    double y = Double.parseDouble(value[1]);
                    polygon.add(x, y);
                }
                polygon.bid = count;
                polygon.setRotation(4);
                nestPaths.add(polygon);
            } else if ("rect".equals(element.getName())) {
                double width = Double.parseDouble(element.attribute("width").getValue());
                double height = Double.parseDouble(element.attribute("height").getValue());
                double x = Double.parseDouble(element.attribute("x").getValue());
                double y = Double.parseDouble(element.attribute("y").getValue());
                NestPath rect = new NestPath();
                rect.add(x, y);
                rect.add(x + width, y);
                rect.add(x + width, y + height);
                rect.add(x, y + height);
                rect.bid = count;
                rect.setRotation(4);
                nestPaths.add(rect);
            }
        }
        return nestPaths;
    }

    private static void  saveSvgFile(List<String> strings) throws Exception {
        File f = new File("out/test.html");
        if (!f.exists()) {
            f.createNewFile();
        }
        Writer writer = new FileWriter(f, false);
        writer.write("<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "\n" +
                "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \n" +
                "\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n" +
                " \n" +
                "<svg width=\"100%\" height=\"100%\" version=\"1.1\"\n" +
                "xmlns=\"http://www.w3.org/2000/svg\">\n");
        for(String s : strings){
            writer.write(s);
        }
        writer.write("</svg>");
        writer.close();
    }

}
