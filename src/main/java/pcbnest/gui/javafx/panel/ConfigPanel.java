package pcbnest.gui.javafx.panel;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.dom4j.DocumentException;
import pcbnest.common.Config;
import pcbnest.common.ConfigUtils;
import pcbnest.common.PartRatio;
import pcbnest.common.ShapeUtils;
import pcbnest.gui.javafx.event.ConfigOpenEventHandler;

import java.util.ArrayList;
import java.util.List;

public class ConfigPanel {

    /***
     *
     *                  shape1              number1
     *                  shape2              number2
     *                  shape3              number3
     *                  shape4              number4
     *                  shape5              number5*
     *
     */
    private GridPane gridPane;
    private Config config;
    private Polygon mainBoard;
    private List<PartRatio> partList = new ArrayList<>();


    public ConfigPanel() {
        gridPane = new GridPane();
        //gridPane.add(mainBoard, 0, 0);

    }

    public ConfigPanel(Config config){
        gridPane = new GridPane();
        gridPane.setGridLinesVisible(true);
        this.config = config;
        mainBoard = ConfigUtils.getMainPcb(config);
        mainBoard.setFill(Color.GREEN);

        //scale down for better display
        mainBoard.setScaleX(0.4);
        mainBoard.setScaleY(0.4);
        Group svgMain = new Group(mainBoard);
        Label labelMain = new Label("Main Board");

        Label lNumber = new Label("Numbuer used");
        TextField lTextNumber = new TextField("1");
        StackPane svgMainWithTxt = new StackPane();
        svgMainWithTxt.getChildren().addAll(new Group(mainBoard), new Label("Main Board"));
        HBox lMainBox = new HBox(50, svgMainWithTxt,  lNumber, lTextNumber);
        lMainBox.setAlignment(Pos.CENTER);
        gridPane.add(lMainBox, 0, 0);

        partList = ConfigUtils.getPartList(config);
        int row = 1;
        for(PartRatio lPartRatio : partList) {
            Group svgPolygon = new Group(lPartRatio.getPolygon());
            Label label = new Label("Number");
            TextField textField = new TextField();
            textField.setText(Integer.toString(lPartRatio.getRatio()));
            StackPane svgPolygonWithTxt = new StackPane();
            svgPolygonWithTxt.getChildren().addAll(new Group(lPartRatio.getPolygon()), new Label(lPartRatio.getName()) );
            HBox lPartBox = new HBox(50, svgPolygonWithTxt, label, textField);
            lPartBox.setAlignment(Pos.CENTER);
            gridPane.add(lPartBox, 0, row ++);
        }
    }

    public void addConfig(Config config) {
        this.config = config;
        this.config = config;
        mainBoard = ConfigUtils.getMainPcb(config);
        mainBoard.setFill(Color.GREEN);
        Group svgMain = new Group(mainBoard);
        Label labelMain = new Label("Main Board");
        HBox lMainBox = new HBox(100, svgMain, labelMain);
        lMainBox.setPadding(new Insets(0, 0, 100, 100));
        lMainBox.setSpacing(500);
        gridPane.add(lMainBox, 0, 1);
        int row = 1;
        /*for(PartRatio lPartRatio : partList) {
            Group svgPolygon = new Group(lPartRatio.getPolygon());
            Label label = new Label("Number");
            TextField textField = new TextField();
            textField.setText(Integer.toString(lPartRatio.getRatio()));
            HBox lPartBox = new HBox(svgPolygon, label, textField);

            gridPane.add(lPartBox, 0, row ++);
        }*/
    }
    public GridPane getGridPane() {
        return gridPane;
    }

    public Config getConfig() {
        return config;
    }

    public Polygon getMainBoard() {
        return mainBoard;
    }

    public List<PartRatio> getPartList() {
        return partList;
    }
}
