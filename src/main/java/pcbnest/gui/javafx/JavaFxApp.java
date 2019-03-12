package pcbnest.gui.javafx;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pcbnest.common.ShapeUtils;
import pcbnest.gui.javafx.event.ConfigOpenEvent;
import pcbnest.gui.javafx.event.ConfigOpenEventHandler;

import java.io.File;


public class JavaFxApp extends Application {
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

        Group svgPolygon = new Group(ShapeUtils.createPolygon());
        Text text = new Text();

        //Setting font to the text
        text.setFont(new Font(10));

        //setting the position of the text
        text.setX(10);
        text.setY(10);
        text.setText("startup.............");
        text.addEventHandler(ConfigOpenEvent.CUSTOM_EVENT_TYPE, new ConfigOpenEventHandler() {
            @Override
            public void onEvent(String param0) {
                text.setText("file opened : " + param0);
            }
        });

        //Creating a Grid Pane
        GridPane mainPanel = new GridPane();
        //Setting size for the pane
        mainPanel.setMinSize(1600, 600);
        mainPanel.setPadding(new Insets(10, 10, 10, 10));
        //Setting the Grid alignment
        mainPanel.setAlignment(Pos.TOP_LEFT);
        //Setting the vertical and horizontal gaps between the columns
        mainPanel.setVgap(5);
        mainPanel.setHgap(5);

        mainPanel.add(text, 0, 1);
        mainPanel.add(svgPolygon, 0, 2);


        //list of shapes on left side

        //Creating Buttons
        //Button button1 = new Button("Submit");
        //Button button2 = new Button("Clear");


        //mainPanel.add(button1, 0, 0);
        //mainPanel.add(button2, 1, 0);

        Scene scene = new Scene(mainPanel);
        /*scene.addEventHandler(CustomEvent.CUSTOM_EVENT_TYPE, new CustomEventHandler() {
            @Override
            public void onEvent(String param0) {
                System.out.println("scene got event file opened " + param0);
                text.fireEvent(new CustomEvent(param0));
            }
        }) ;*/
        primaryStage.setTitle("PCB_FILL");

        mainPanel.getChildren().addAll(menuBar);

        primaryStage.setScene(scene);
        primaryStage.show();


        add.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                File file  = fileChooser.showOpenDialog(primaryStage);
                if(null != file) {
                    text.fireEvent(new ConfigOpenEvent(file.getAbsolutePath()));
                }
            }
        });

    }

    public static void main(final String[] args) {
        launch(args);
    }
}
