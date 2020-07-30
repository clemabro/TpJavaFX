package application;
 
import java.io.File;
import java.io.InputStream;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class Main extends Application {
	
	BorderPane root = new BorderPane();
    GridPane grid = new GridPane();
    Text scenetitle = new Text("Welcome");
    Label userName = new Label("User Name:");
    TextField userTextField = new TextField();
    Label pw = new Label("Password:");
    PasswordField pwBox = new PasswordField();
    Button btnSign = new Button("Sign in");
    HBox hbBtn = new HBox(10);
    final Text actiontarget = new Text();
    MenuBar menuBar = new MenuBar();
    
    // --- Menu Fichier
    Menu menuFile = new Menu("Fichier");

    // --- Menu Edition
    Menu menuEdit = new Menu("Edition");

    // --- Menu Aide
    Menu menuView = new Menu("Aide");
    
    // créations des parties du menu
    MenuItem newItem = new MenuItem("Nouveau");
    MenuItem openFileItem = new MenuItem("Ouvrir");
    MenuItem exitItem = new MenuItem("Quitter");
    
    MenuItem copyItem = new MenuItem("Copier");
    MenuItem pasteItem = new MenuItem("Coller");
    
    Scene scene = new Scene(root, 1200, 700);
    
    // Recup de l'image et jla met dans l'imageview
    Class<?> clazz = this.getClass();
    InputStream input = clazz.getResourceAsStream("shiba5.jpg");
    Image image = new Image(input, 200, 200, false, true);
    ImageView imageView = new ImageView(image);
    
    // Les labels pour le data binding
    Label lblRole1 = new Label("DATA BINDING : Le username est ");
    Label lblRole2 = new Label();
    Label lblRole3 = new Label(" et le password est ");
    Label lblRole4 = new Label();
    
    // Create a Task.
    CopyTask copyTask = new CopyTask();
    
    ColumnConstraints column1 = new ColumnConstraints(150, 150, Double.MAX_VALUE);
    ColumnConstraints column2 = new ColumnConstraints(50);
    ColumnConstraints column3 = new ColumnConstraints(150, 150, Double.MAX_VALUE);
    
    // composant pour les listes add remove
    GridPane gridpane = new GridPane();
    Label candidatesLbl = new Label("A choisir");
    Label selectedLbl = new Label("Selectionnée");
    final ObservableList<String> candidates = FXCollections
            .observableArrayList("Z", "A", "B", "C", "D");
    final ListView<String> candidatesListView = new ListView<>(candidates);
    final ObservableList<String> selected = FXCollections.observableArrayList();
    final ListView<String> heroListView = new ListView<>(selected);
    Button sendRightButton = new Button(" > ");
    Button sendLeftButton = new Button(" < ");
    
    // composant pour la progress bar
    final Label label = new Label("Copy files:");
    final ProgressBar progressBar = new ProgressBar(0);
    final ProgressIndicator progressIndicator = new ProgressIndicator(0);

    final Button startButton = new Button("Start");
    final Button cancelButton = new Button("Cancel");

    final Label statusLabel = new Label();
    FlowPane rootFlow = new FlowPane();
    
    @Override
    public void start(Stage primaryStage) {
    	// ajout du css
    	scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
    	
    	// definition de la grille
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // definition du border pane
        root.setCenter(grid);
        root.setTop(menuBar);
        
        // Ajout d'un id au titre pour le css
        scenetitle.setId("welcome-text");
        // ajout d'un id sur le texte pour le css
        actiontarget.setId("actiontarget");
        
        // Ajout des composants sur la grille
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(userName, 0, 1);
        grid.add(userTextField, 1, 1);
        grid.add(pw, 0, 2);
        grid.add(pwBox, 1, 2);     
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnSign);
        grid.add(hbBtn, 1, 4);
        grid.add(actiontarget, 1, 6);
        
        // Ajout des sous menus dans la bar
        menuFile.getItems().addAll(newItem, openFileItem, exitItem);
        menuEdit.getItems().addAll(copyItem, pasteItem);
        // ajout des menus dans la bar
        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);
       
        // Ajout de l'image
        imageView.setX(200); 
        imageView.setY(200);
        imageView.setEffect(new DropShadow(20, Color.BLACK));
        grid.add(imageView, 0, 8, 3, 4);
        
        // binding de l'input username et password
        lblRole2.textProperty().bind(userTextField.textProperty());
        lblRole4.textProperty().bind(pwBox.textProperty());
        
        // ajout des labels pour le data binding
        HBox paneRole = new HBox(lblRole1, lblRole2, lblRole3, lblRole4);
        paneRole.setPadding(new Insets(10));
        
        grid.add(paneRole,0,7,2,1);
        
        // Les 2 listes l'ajout et la suppression entre les 2
       
        gridpane.setPadding(new Insets(10));
        gridpane.setHgap(10);
        gridpane.setVgap(10);
        
        column1.setHgrow(Priority.ALWAYS);
        column3.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(column1, column2, column3);
        
        GridPane.setHalignment(candidatesLbl, HPos.CENTER);
        gridpane.add(candidatesLbl, 0, 0);
        
        gridpane.add(selectedLbl, 2, 0);
        GridPane.setHalignment(selectedLbl, HPos.CENTER);

        gridpane.add(candidatesListView, 0, 1);
        gridpane.add(heroListView, 2, 1);
        
        sendRightButton.setOnAction((ActionEvent event) -> {
          String potential = candidatesListView.getSelectionModel()
              .getSelectedItem();
          if (potential != null) {
            candidatesListView.getSelectionModel().clearSelection();
            candidates.remove(potential);
            selected.add(potential);
          }
        });
        
        sendLeftButton.setOnAction((ActionEvent event) -> {
          String s = heroListView.getSelectionModel().getSelectedItem();
          if (s != null) {
            heroListView.getSelectionModel().clearSelection();
            selected.remove(s);
            candidates.add(s);
          }
        });
        VBox vbox = new VBox(5);
        gridpane.add(vbox, 1, 1);
        vbox.getChildren().addAll(sendRightButton, sendLeftButton);
        
        root.setRight(gridpane);
        
        // action lors de lappuie sur le btn sign in
        btnSign.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Sign in button pressed");
            }
        });
        
        // ajout de la combinaison ctrl x sur le menu exit
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
         
        // Quand on clique sur le menu exit 
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
         
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
        
        statusLabel.setMinWidth(250);
        statusLabel.setTextFill(Color.BLUE);
  
        // Start Button.
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	copyTask = new CopyTask();
                startButton.setDisable(true);
                progressBar.setProgress(0);
                progressIndicator.setProgress(0);
                cancelButton.setDisable(false);
  
                // Unbind progress property
                progressBar.progressProperty().unbind();
  
                // Bind progress property
                progressBar.progressProperty().bind(copyTask.progressProperty());
  
                // Hủy bỏ kết nối thuộc tính progress
                progressIndicator.progressProperty().unbind();
  
                // Bind progress property.
                progressIndicator.progressProperty().bind(copyTask.progressProperty());
  
                // Unbind text property for Label.
                statusLabel.textProperty().unbind();
  
                // Bind the text property of Label
                 // with message property of Task
                statusLabel.textProperty().bind(copyTask.messageProperty());
  
                // When completed tasks
                copyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                        new EventHandler<WorkerStateEvent>() {
  
                            @Override
                            public void handle(WorkerStateEvent t) {
                                List<File> copied = copyTask.getValue();
                                statusLabel.textProperty().unbind();
                                statusLabel.setText("Copied: " + copied.size());
                            }
                        });
  
                // Start the Task.
                new Thread(copyTask).start();
  
            }
        });
  
        // Cancel
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                startButton.setDisable(false);
                cancelButton.setDisable(true);
                copyTask.cancel(true);
                progressBar.progressProperty().unbind();
                progressIndicator.progressProperty().unbind();
                statusLabel.textProperty().unbind();
                //
                progressBar.setProgress(0);
                progressIndicator.setProgress(0);
            }
        });
        
        rootFlow.setPadding(new Insets(10));
        rootFlow.setHgap(10);
  
        rootFlow.getChildren().addAll(label, progressBar, progressIndicator, //
                statusLabel, startButton, cancelButton);
        
        grid.add(rootFlow, 4, 10,3,3);
        
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        TableView<UserAccount> table = new TableView<UserAccount>();
        
        // Create column UserName (Data type of String).
        TableColumn<UserAccount, String> userNameCol //
                = new TableColumn<UserAccount, String>("User Name");
   
        // Create column Email (Data type of String).
        TableColumn<UserAccount, String> emailCol//
                = new TableColumn<UserAccount, String>("Email");
   
        // Create column FullName (Data type of String).
        TableColumn<UserAccount, String> fullNameCol//
                = new TableColumn<UserAccount, String>("Full Name");
   
        // Create 2 sub column for FullName.
        TableColumn<UserAccount, String> firstNameCol//
                = new TableColumn<UserAccount, String>("First Name");
   
        TableColumn<UserAccount, String> lastNameCol //
                = new TableColumn<UserAccount, String>("Last Name");
   
        // Add sub columns to the FullName
        fullNameCol.getColumns().addAll(firstNameCol, lastNameCol);
   
        // Active Column
        TableColumn<UserAccount, Boolean> activeCol//
                = new TableColumn<UserAccount, Boolean>("Active");
   
        // Defines how to fill data for each cell.
        // Get value from property of UserAccount. .
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
      
        // Set Sort type for userName column
        userNameCol.setSortType(TableColumn.SortType.DESCENDING);
        lastNameCol.setSortable(false);
   
        // Display row data
        ObservableList<UserAccount> list = getUserList();
        table.setItems(list);
   
        table.getColumns().addAll(userNameCol, emailCol, fullNameCol, activeCol);
   
        StackPane root = new StackPane();
        root.setPadding(new Insets(5));
        root.getChildren().add(table);
     
        Scene sceneTable = new Scene(root, 450, 300);
        Stage stageTable = new Stage();
        stageTable.setScene(sceneTable);
        stageTable.setTitle("Exemple de tableau");
        stageTable.show();

    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    private ObservableList<UserAccount> getUserList() {
    	 
        UserAccount user1 = new UserAccount(1L, "smith", "smith@gmail.com", //
                "Susan", "Smith", true);
        UserAccount user2 = new UserAccount(2L, "mcneil", "mcneil@gmail.com", //
                "Anne", "McNeil", true);
        UserAccount user3 = new UserAccount(3L, "white", "white@gmail.com", //
                "Kenvin", "White", false);
   
        ObservableList<UserAccount> list = FXCollections.observableArrayList(user1, user2, user3);
        return list;
    }
}