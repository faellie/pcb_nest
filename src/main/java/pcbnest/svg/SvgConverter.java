package pcbnest.svg;


import javafx.scene.shape.Polygon;
import nest4J.Nest;
import nest4J.data.NestPath;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGOMPathElement;
import org.apache.batik.anim.dom.SVGPathSupport;
import org.apache.batik.bridge.*;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.dom.svg.SVGOMPoint;
import org.apache.batik.dom.svg.SVGPathSegItem;
import org.apache.batik.gvt.GraphicsNode;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.svg.SVGDocument;
import org.w3c.dom.svg.SVGPathSegList;
import org.w3c.dom.svg.SVGPoint;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class SvgConverter {

    /**
     * Conveter curve
     *  <path d="M10 10 C 20 20, 40 20, 50 10" stroke="black" fill="transparent"/>  to
      * <path d="M10 10 L 20 20 L40 20 L50 10" stroke="black" fill="transparent"/>
     *
     *
     * Converter Q T
     * <svg width="190" height="160" xmlns="http://www.w3.org/2000/svg">
     <path d="M10 80 Q 52.5 10, 95 80 T 180 80" stroke="black" fill="transparent"/>

     <path d="M10 80 L52.5 10 L95 80 L137.5 150 L180 80" stroke="red" fill="transparent"/>
     //Note: 137.5 = 95 -82.5 + 95
     //      150 = 80 -10 + 80


     Convert C S

     <svg  xmlns="http://www.w3.org/2000/svg">
     <path d="M10 80 C 40 10, 65 10, 95 80 S 150 150, 180 80" stroke="black" fill="transparent"/>

     <path d="M10 80 L40 10  L65 10 L95 80 L125 150 L150 150 L180 80" stroke="red" fill="transparent"/>
          //note  125 = 95 -65 +95
                   150 = 80 -10 + 70
     </svg>
     </svg>

     */

    ArrayList<ArrayList<SVGPoint>> pathPoints;
    int separate_points;

    /**
     * Constructor
     */
    SvgConverter(int in_separate_points){
        pathPoints = new ArrayList<ArrayList<SVGPoint>>();
        separate_points = in_separate_points;
    }

    private void readPathSvg(String f){

        try{
            SVGDocument svgDoc;
            UserAgent userAgent;
            DocumentLoader loader;
            BridgeContext ctx;
            GVTBuilder builder;
            GraphicsNode rootGN;

            userAgent = new UserAgentAdapter();
            loader = new DocumentLoader(userAgent);
            ctx = new BridgeContext(userAgent, loader);
            ctx.setDynamicState(BridgeContext.DYNAMIC);
            builder = new GVTBuilder();


            URI fileURI = new File(f).toURI();
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory svgf = new SAXSVGDocumentFactory(parser);
            svgDoc = (SVGDocument)svgf.createDocument(fileURI.toString());

            rootGN    = builder.build(ctx, svgDoc);

            System.out.println(rootGN.STROKE);

            NodeList paths = svgDoc.getElementsByTagName("path");
            for(int i=0; i < paths.getLength(); i++){

                SVGOMPathElement path = (SVGOMPathElement)paths.item(i);
                //System.out.println(i +":" + path.getAttribute("d"));
                pathPoints.add(new ArrayList<SVGPoint>());

                //System.out.println( path.getTotalLength() );

                float total_path_length = path.getTotalLength();
                float unit_length = total_path_length/(float)separate_points;
                SVGPathSegList lSegList = path.getNormalizedPathSegList();
                int lNumOfSegs= lSegList.getNumberOfItems();
                float step = total_path_length/(float)(separate_points * lNumOfSegs);
                float lCurrentLength = 0.0f;
                int lCurrSeg = 0;
                boolean finished = false;
                while(!finished) {
                    //SVGPoint tmp_point = path.getPointAtLength(unit_lenght*separate_points);
                    int lSegIndex = SVGPathSupport.getPathSegAtLength(path, lCurrentLength);
                    SVGPathSegItem lNextSeg = (SVGPathSegItem) lSegList.getItem(lSegIndex);

                    //Note circles will be handle seperatly
                    if (lNextSeg.getPathSegTypeAsLetter().equalsIgnoreCase("M")) {
                        SVGPoint lNextPoint = new SVGOMPoint(lNextSeg.getX(), lNextSeg.getY());
                        pathPoints.get(i).add(lNextPoint);
                        System.out.println("M adding " + lNextPoint.getX() + ", " + lNextPoint.getY());
                        //move till next seg
                        while (lSegIndex == lCurrSeg &&!finished) {
                            lCurrentLength = lCurrentLength + step;
                            if(lCurrentLength > total_path_length) {
                                finished = true;
                                lCurrentLength = total_path_length;
                            }
                            lSegIndex = SVGPathSupport.getPathSegAtLength(path, lCurrentLength);
                        }
                        lCurrSeg = lSegIndex;
                    } else if (lNextSeg.getPathSegTypeAsLetter().equalsIgnoreCase("L")) {
                        //its a line move all the way to the next point of the line
                        SVGPoint lNextPoint = new SVGOMPoint(lNextSeg.getX(), lNextSeg.getY());
                        pathPoints.get(i).add(lNextPoint);
                        System.out.println("L adding " + lNextPoint.getX() + ", " + lNextPoint.getY());
                        //move till next seg
                        while (lSegIndex == lCurrSeg &&!finished) {
                            lCurrentLength = lCurrentLength + step;
                            if(lCurrentLength > total_path_length) {
                                finished = true;
                                lCurrentLength = total_path_length;
                            }
                            lSegIndex = SVGPathSupport.getPathSegAtLength(path, lCurrentLength);
                        }
                        lCurrSeg = lSegIndex;
                    } else {
                        //all kinds of curves, we simply step to next mini point and add to the polygon
                        while (lSegIndex == lCurrSeg &&!finished) {
                            SVGPoint lNextPoint = SVGPathSupport.getPointAtLength(path, lCurrentLength);
                            pathPoints.get(i).add(new SVGOMPoint(lNextPoint.getX(), lNextPoint.getY()));
                            System.out.println("Else : adding " + lNextPoint.getX() + ", " + lNextPoint.getY());
                            lCurrentLength = lCurrentLength + step;
                            if(lCurrentLength > total_path_length) {
                                finished = true;
                                lCurrentLength = total_path_length;
                            }
                            lSegIndex = SVGPathSupport.getPathSegAtLength(path, lCurrentLength);
                            lNextPoint = SVGPathSupport.getPointAtLength(path, lCurrentLength);
                            pathPoints.get(i).add(new SVGOMPoint(lNextPoint.getX(), lNextPoint.getY()));
                        }
                        lCurrSeg = lSegIndex;
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        normalize();

    }

    private void writePolygonSvg(String filename){

        try{
            // Get a DOMImplementation
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            String svgNamespaceURI = "http://www.w3.org/2000/svg";

            // Create an instance of org.w3c.dom.Document
            Document document = domImpl.createDocument(svgNamespaceURI, "svg", null);

            // create root
            Element root = document.getDocumentElement();
            this.setSvgHeader(root, svgNamespaceURI);


            // Add polygon tag
            addPolygonPoints(document, root, svgNamespaceURI);

            DOMSource source = new DOMSource(document);
            File writeXML = new File(filename);
            FileOutputStream fos = new FileOutputStream(writeXML);
            StreamResult sr = new StreamResult(fos);

            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.transform(source, sr);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void setSvgHeader(Element root, String ns){
        root.setAttributeNS(ns, "width", "800px");
        root.setAttributeNS(ns, "height", "600px");
        root.setAttributeNS(ns, "x", "0px");
        root.setAttributeNS(ns, "y", "0px");
        root.setAttributeNS(ns, "enable-background", "new 0 0 800 600");
    }

    private void addPolygonPoints(Document document, Element root, String ns){
        float minX = 0.0f;
        float minY = 0.0f;
        //find min
        for(int i =0; i < pathPoints.size(); i++){
            for(int j = 0; j < pathPoints.get(i).size(); j++ ) {
                SVGPoint lPoint = (SVGPoint) (pathPoints.get(i).get(j));
                minX = minX < lPoint.getX() ? minX : lPoint.getX();
                minY = minY < lPoint.getY() ? minY : lPoint.getY();
            }
        }

        //shift so all are positive
        for(int i=0; i < pathPoints.size(); i++){
            for(int j = 0; j < pathPoints.get(i).size(); j++ ) {
                SVGPoint lPoint = (SVGPoint) (pathPoints.get(i).get(j));
                if (minX < 0.0F) {
                    lPoint.setX(lPoint.getX() - minX);
                }
                if (minY < 0.0F) {
                    lPoint.setY(lPoint.getY() - minY);
                }
            }
        }

        for(int i=0; i < pathPoints.size(); i++){
            Element polygon_element = document.createElement("polygon");
            root.appendChild(polygon_element);

            String points_value = "";

            for(int j=0; j< pathPoints.get(i).size(); j++){

                points_value = points_value + pathPoints.get(i).get(j).getX() + ","
                        + pathPoints.get(i).get(j).getY() + " ";
            }

            polygon_element.setAttributeNS(ns, "points", points_value);
            polygon_element.setAttributeNS(ns, "stroke", "#000000");
            polygon_element.setAttributeNS(ns, "fill", "#FFFFFF");
        }
    }


    public void normalize() {
        float minX = 0.0f;
        float minY = 0.0f;
        //find min
        for(int i =0; i < pathPoints.size(); i++){
            for(int j = 0; j < pathPoints.get(i).size(); j++ ) {
                SVGPoint lPoint = (SVGPoint) (pathPoints.get(i).get(j));
                minX = minX < lPoint.getX() ? minX : lPoint.getX();
                minY = minY < lPoint.getY() ? minY : lPoint.getY();
            }
        }

        //shift so all are positive
        for(int i=0; i < pathPoints.size(); i++){
            for(int j = 0; j < pathPoints.get(i).size(); j++ ) {
                SVGPoint lPoint = (SVGPoint) (pathPoints.get(i).get(j));
                if (minX < 0.0F) {
                    lPoint.setX(lPoint.getX() - minX);
                }
                if (minY < 0.0F) {
                    lPoint.setY(lPoint.getY() - minY);
                }
            }
        }
    }
    public static NestPath getNestPathFromSvg(String aInSvgFileName) {
        SvgConverter cv =  new SvgConverter(5);
        cv.readPathSvg(aInSvgFileName);
        NestPath lPath = new NestPath();
        ArrayList<ArrayList<SVGPoint>> lPoints = cv.pathPoints;
        for(int i =0; i < lPoints.size(); i++) {
            for (int j = 0; j < lPoints.get(i).size(); j++) {
                lPath.add(lPoints.get(i).get(j).getX(), lPoints.get(i).get(j).getY());
            }
        }
        return lPath;
    }

    public static Polygon getPolygonFromSvg(String aInSvgFileName) {
        SvgConverter cv =  new SvgConverter(5);
        cv.readPathSvg(aInSvgFileName);

        Polygon lPolygon = new Polygon();
        ArrayList<ArrayList<SVGPoint>> lPoints = cv.pathPoints;
        for(int i =0; i < lPoints.size(); i++) {
            for (int j = 0; j < lPoints.get(i).size(); j++) {
                lPolygon.getPoints().addAll((double) (lPoints.get(i).get(j).getX()), (double) (lPoints.get(i).get(j).getY()));
            }
        }
        return lPolygon;
    }
    public static void main(String [] args) throws IOException {

        /*if(args.length != 3){
            System.out.println("Please give [1]Input file name [2]Output file name [3]Separation points.");
            System.exit(-1);
        }*/

        //String in_filename = "/opt/LI/nesting/pcb_nest/in/028124-5.svg";
        String in_filename = "/opt/LI/nesting/pcb_nest/in/020671.svg";
        String out_filename = "/opt/LI/nesting/pcb_nest/out/test1.svg";
        int separation_points = 5;

        System.out.println("In:"+in_filename+", Out:"+ out_filename);

        SvgConverter cv =  new SvgConverter(separation_points);
        /*String lOutFile = PolylineUtils.convertFileToSvgFile(in_filename,
                new Bound(CommonUtils.vew_port_startX, CommonUtils.vew_port_startY, CommonUtils.vew_port_width, CommonUtils.vew_port_hight ));*/
        //cv.readPathSvg(lOutFile);

        cv.readPathSvg(in_filename);
        cv.writePolygonSvg(out_filename);

    }
}
