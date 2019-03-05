package pcbnest.utils.dxf2svg;

/**
 * read multiple dxf files from a dir and get polygon from each file (assume each file contains a polygon).
 * shift them and put into one SVG file
 */

import java.io.File;

public class CommonUtils {

    //todo hared coded for now
    public static int vew_port_startX = 0;
    public static int vew_port_startY = 0;
    public static int vew_port_width = 500;
    public static int vew_port_hight = 500;

    public  static String fileNameToLayer(String aInFileFullPath) {
        //038689-1.dxf  ==> 038689-1ROU1_GBR
        File lFile = new File(aInFileFullPath);
        String lFileName = lFile.getName();
        if(lFileName.equalsIgnoreCase("ro1.dxf")) {
            return "RO1_GBR";
        }
        return lFileName.replace(".dxf", "ROU1_GBR");
    }
}
