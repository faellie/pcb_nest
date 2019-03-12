package pcbnest.gui.javafx;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pcbnest.PcbNest;
import pcbnest.common.Config;
import pcbnest.common.ConfigUtils;
import pcbnest.common.ShapeUtils;
import pcbnest.gui.javafx.event.ConfigOpenEvent;
import pcbnest.gui.javafx.event.ConfigOpenEventHandler;
import pcbnest.gui.javafx.panel.ConfigPanel;
import pcbnest.utils.dxf2svg.CommonUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class JavaFxApp1 extends Application {
    @Override
    public void start(final Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem add = new MenuItem("Open");
        add.setAccelerator(KeyCombination.keyCombination("Alt+o"));
        menuFile.getItems().addAll(add);
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");
        // --- Menu View
        Menu menuView = new Menu("View");
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        //Creating a Grid Pane
        GridPane mainPanel = new GridPane();
        //Setting size for the pane
        mainPanel.setGridLinesVisible(true);
        mainPanel.setMinSize(1600, 600);
        mainPanel.setPadding(new Insets(10, 10, 10, 10));
        //Setting the Grid alignment
        mainPanel.setAlignment(Pos.TOP_LEFT);
        //Setting the vertical and horizontal gaps between the columns
        mainPanel.setVgap(5);
        mainPanel.setHgap(5);

        //ConfigPanel lConfigPanel = new ConfigPanel();
        Config lConfig = CommonUtils.getConfig("/opt/LI/nesting/pcb_nest/in/input2.json");
        ConfigPanel lConfigPanel = new ConfigPanel(lConfig);
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(lConfigPanel.getGridPane());
        mainPanel.add(scrollPane, 0, 1);
        //mainPanel.add(new Group(ShapeUtils.createPolygon()), 0, 2);


        //output panel

        String lSvgStr = PcbNest.nest(lConfigPanel, lConfigPanel.getPartList());


        Group svgPolygon = new Group(ShapeUtils.createPolygon());
        //todo we suppose to do somehting with e str, but...later
        //Group svgPolygon = new Group(PcbNest.nest(lConfigPanel.getMainBoard(), lConfigPanel.getPartList()));
        mainPanel.add(svgPolygon, 1, 1);

        Scene scene = new Scene(mainPanel);

        primaryStage.setTitle("PCB_FILL");

        //mainPanel.getChildren().addAll(menuBar);
        ((GridPane)scene.getRoot()).getChildren().addAll(menuBar);
        primaryStage.setScene(scene);
        primaryStage.show();


        add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file  = fileChooser.showOpenDialog(primaryStage);
                if(null != file) {
                    Config lConfig = CommonUtils.getConfig(file.getAbsolutePath());
                    lConfigPanel.addConfig(lConfig);
                }
            }
        });

    }

    public static void main(final String[] args) {
        launch(args);
    }
}
