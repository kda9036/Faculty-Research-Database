import java.util.*;

// security / pw hashing 
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.math.BigInteger;
import java.security.spec.InvalidKeySpecException;

import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.Font; 
import java.io.*;
import javafx.stage.FileChooser.*;

/** 
 * PresentationResearch - presentation layer
 * Project: Faculty Research Database
 * Group 4:
 * @author Appleton, Kelly
 * @author Pegoli, Christopher
 * @author Zhao, Wentao
 * @version 11/28/21
 */

public class PresentationResearch extends Application implements EventHandler<ActionEvent> {
   private ResearchDataLayer db;
   private String userType;
   private int facultyID;
   private String facultyEmail;
   private String studentEmail;
   private String publicEmail;
   private final Object PAUSE_KEY = new Object();
   private int selection;
      
   // Window attributes
   private Stage stage;
   private Scene scene;
   // Scene changes based on user type
   private Scene facultyScene1, facultyLogin, facultyCreate, facultyTasksMenu;
   private Scene studentTasks, studentCreate;
   private Scene publicTasks, publicCreate;
   // Starting scene
   private VBox root = new VBox();
   // Additonal scenes
   private GridPane layout1, layout2, layout3, layout4, layout5, layout6, layout7, layout8;
   
   // GUI Components:   
   // Buttons
      // Main Scene
   private Button btnConnect = new Button("Connect to Data Source");
   private Button btnUser = new Button("Select User Type");
   private Button btnExit = new Button("Exit");
   private Button[] btnAll = new Button[]{btnConnect, btnUser, btnExit};
   // Other components for additional scenes
   private TextField tfEmail, tfLastName, tfFirstName, tfBuilding, tfOffice, tfNewFacEmail;
   private TextField tfStuLastName, tfStuFirstName, tfStuEmail, tfWord1, tfWord2, tfWord3;
   private TextField tfPubName, tfPubEmail, tfPubWord1, tfPubWord2, tfPubWord3;
   private PasswordField pwPassword, pwNewFacPassword;
   private Button btnLogin, btnCreate, btnLoginConfirm, btnCreateConfirm;
   private Button btnContinue2, btnContinue3, btnContinue4, btnContinue5, btnContinue6, btnContinue7, btnContinue8;
   private Button btnFacTask1, btnFacTask2, btnFacTask3, btnFacTask4, btnFacTask5;
   private Button btnStuTask1, btnStuTask2, btnStuTask3, btnStuTask4, btnStuTask5, btnStuTask6, btnStuTask7;
   private Button btnPubTask1, btnPubTask2, btnPubTask3, btnPubTask4, btnPubTask5, btnPubTask6, btnPubTask7;
   private Button btnNext, btnDone, btnPubNext, btnPubDone;
   private Button btnCreateConfirmStu, btnCreateConfirmPub;
   private TextArea taLog2 = new TextArea();
   private TextArea taLog3 = new TextArea();
   private TextArea taLog4 = new TextArea();
   private TextArea taLog5 = new TextArea();
   private TextArea taLog6 = new TextArea();
   private TextArea taLog7 = new TextArea();
   private TextArea taLog8 = new TextArea();
    
   /**
    * Constructor
    */
   public PresentationResearch(){
      db = new ResearchDataLayer();    
   } // end constructor

   /** main */
   public static void main(String [] args){
      System.out.println("Faculty Research Database");
      System.out.println("\tAppleton, Kelly \n\tPegoli, Christopher \n\tZhao, Wentao");
      launch(args);
      new PresentationResearch();
                 
   } // end main()
   
   /**
    * start - draw and set up GUI
    */
   public void start(Stage _stage) {
      System.out.println("ISTE 330-02  Project   Group 4  2021-11-28\n");
   
      stage = _stage;
      stage.setTitle("Project: Faculty Research Database");
      
      // Listen for window close
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) {
               System.exit(0);
            }
         });
   
      // Style all buttons
      for(Button b : btnAll) {
         b.setPrefWidth(450);
         b.setPrefHeight(75);
         b.setStyle("-fx-text-fill: #0000ff;");
         b.setFont(Font.font("verdana", FontWeight.BOLD, 20));
      }
   
      // Row 1 (Top) - Connect to Data Source
      FlowPane fpTop = new FlowPane(8,8);
      fpTop.getChildren().add(btnConnect);
      
      // Row 2 (Mid) - Get ResultSet
      FlowPane fpMid = new FlowPane(8,8);
      fpMid.getChildren().add(btnUser);
            
      // Row 3 (Bot) - Exit
      FlowPane fpBot = new FlowPane(8,8);
      fpBot.getChildren().add(btnExit);
   
      // add FlowPanes (Buttons) to root VBox
      root.getChildren().addAll(fpTop, fpMid, fpBot);
      
      // Listen for the buttons
      btnConnect.setOnAction(this);
      btnUser.setOnAction(this);
      btnExit.setOnAction(this);
      
      // Disable 'Select User' until connected
      btnUser.setDisable(true);
   
      // Show window
      scene = new Scene(root, 450, 225);
      stage.setScene(scene);
      // set stage position
      stage.centerOnScreen();
      stage.show(); 
                     
      /* Faculty Scenes*/
      
      // facultyScene1
      layout1 = new GridPane();
      layout1.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct1 = new Label("Please choose from the following:");
      btnLogin = new Button("Login");
      btnCreate = new Button("Create New Faculty Profile");
      layout1.add(lblInstruct1, 0, 0);  // object, col, row
      layout1.add(btnLogin, 0, 1);
      btnLogin.setPrefWidth(200);
      btnLogin.setPrefHeight(75);
      layout1.add(btnCreate, 1, 1);
      btnCreate.setPrefWidth(200);
      btnCreate.setPrefHeight(75);
      layout1.setHgap(20);
      layout1.setVgap(20);
      facultyScene1 = new Scene(layout1, 450, 100);
      
      // Faculty clicks Login or Create
      btnLogin.setOnAction(
         e -> {
            stage.setScene(facultyLogin = new Scene(layout2, 450, 375));
                // Stylesheet
            facultyLogin.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
         });
      btnCreate.setOnAction(
         e -> {
            stage.setScene(facultyCreate = new Scene(layout3, 750, 500));
                // Stylesheet
            facultyCreate.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
         });
      // facultyLogin
      layout2 = new GridPane();
      layout2.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct2 = new Label("Please enter your email and password:");
      Label lblEmail = new Label("Email:");
      tfEmail = new TextField();
      Label lblPassword = new Label("Password:");
      pwPassword = new PasswordField();
      btnLoginConfirm = new Button("Login");
      btnContinue2 = new Button("Continue");
      btnContinue2.setDisable(true);
      layout2.add(lblInstruct2, 0, 0, 4, 1);
      layout2.add(lblEmail, 0, 1);
      layout2.add(tfEmail, 1, 1);
      layout2.add(lblPassword, 0, 2);
      layout2.add(pwPassword, 1, 2);
      layout2.add(btnLoginConfirm, 4, 3);
      layout2.add(btnContinue2, 4, 4);
      layout2.add(taLog2, 0, 5, 5, 5);
      layout2.setHgap(20);
      layout2.setVgap(10);
      layout2.setHalignment(lblEmail, HPos.RIGHT);
      layout2.setHalignment(lblPassword, HPos.RIGHT);
      taLog2.setWrapText(true);
       
      btnLoginConfirm.setOnAction(e -> facultyTasks(1)); // Login
      btnContinue2.setOnAction(
         e -> {
            stage.setScene(facultyTasksMenu = new Scene(layout4, 650, 500));
                // Stylesheet
            facultyTasksMenu.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
         });
      
      // facultyCreate
      layout3 = new GridPane();
      layout3.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct3 = new Label("Please enter your information:");
      Label lblLastName = new Label("Last Name:");
      layout3.setHalignment(lblLastName, HPos.RIGHT);
      tfLastName = new TextField();
      Label lblFirstName = new Label("First Name:");
      layout3.setHalignment(lblFirstName, HPos.RIGHT);
      tfFirstName = new TextField();
      Label lblBuilding = new Label("Building #:");
      layout3.setHalignment(lblBuilding, HPos.RIGHT);
      tfBuilding = new TextField();
      Label lblOffice = new Label("Office #:");
      layout3.setHalignment(lblOffice, HPos.RIGHT);
      tfOffice = new TextField();
      Label lblNewFacEmail = new Label("Email:");
      layout3.setHalignment(lblNewFacEmail, HPos.RIGHT);
      tfNewFacEmail = new TextField();
      Label lblNewFacPassword = new Label("Password:");
      layout3.setHalignment(lblNewFacPassword, HPos.RIGHT);
      pwNewFacPassword = new PasswordField();
      btnCreateConfirm = new Button("Create Faculty Profile");
      btnCreateConfirm.setPrefHeight(35);
      btnContinue3 = new Button("Continue to Menu");
      btnContinue3.setDisable(true);
      btnContinue3.setPrefHeight(35);
      layout3.add(lblInstruct3, 0, 0, 2, 1);
      layout3.add(lblLastName, 0, 1);
      layout3.add(tfLastName, 1, 1);
      layout3.add(lblFirstName, 0, 2);
      layout3.add(tfFirstName, 1, 2);
      layout3.add(lblBuilding, 0, 3);
      layout3.add(tfBuilding, 1, 3);
      layout3.add(lblOffice, 0, 4);
      layout3.add(tfOffice, 1, 4);
      layout3.add(lblNewFacEmail, 0, 5);
      layout3.add(tfNewFacEmail, 1, 5);
      layout3.add(lblNewFacPassword, 0, 6);
      layout3.add(pwNewFacPassword, 1, 6);
      layout3.add(btnCreateConfirm, 1, 7);
      layout3.add(btnContinue3, 1, 8);
      layout3.add(taLog3, 1, 9);
      taLog3.setPrefWidth(500);
      taLog3.setWrapText(true);
      layout3.setHgap(20);
      layout3.setVgap(10);
      layout3.setHalignment(btnCreateConfirm, HPos.RIGHT);
      layout3.setHalignment(btnContinue3, HPos.RIGHT);
      
      btnCreateConfirm.setOnAction(e -> facultyTasks(2)); // add new faculty
      btnContinue3.setOnAction(
         e -> {
            stage.setScene(facultyTasksMenu = new Scene(layout4, 650, 500));
                // Stylesheet
            facultyTasksMenu.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
         });
      // Faculty Tasks Menu
      layout4 = new GridPane();
      layout4.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct4 = new Label("Please choose from the following:");
      btnFacTask1 = new Button("Update faculty info (building, office, email)");
      btnFacTask2 = new Button("Delete faculty");
      btnFacTask3 = new Button("Add a new abstract");
      btnFacTask4 = new Button("Delete an abstract");
      btnFacTask5 = new Button("Done/Exit");
      btnContinue4 = new Button("Continue");
      btnContinue4.setDisable(true);
      btnContinue4.setPrefHeight(35);
      
      btnFacTask1.setMaxWidth(Double.MAX_VALUE);
      btnFacTask2.setMaxWidth(Double.MAX_VALUE);
      btnFacTask3.setMaxWidth(Double.MAX_VALUE);
      btnFacTask4.setMaxWidth(Double.MAX_VALUE);
      btnFacTask5.setMaxWidth(Double.MAX_VALUE);
      btnFacTask1.setPrefHeight(35);
      btnFacTask2.setPrefHeight(35);
      btnFacTask3.setPrefHeight(35);
      btnFacTask4.setPrefHeight(35);
      btnFacTask5.setPrefHeight(35);
   
      layout4.add(lblInstruct4, 0, 0, 4, 1);  // object, col, row, colspan, rowspan
      VBox vbButtons = new VBox();
      vbButtons.setSpacing(10);
      vbButtons.setPadding(new Insets(10, 20, 10, 20)); 
      vbButtons.getChildren().addAll(btnFacTask1, btnFacTask2, btnFacTask3, btnFacTask4, btnFacTask5);
      layout4.add(vbButtons, 0, 1);
      layout4.add(btnContinue4, 0, 2);
      layout4.setHalignment(btnContinue4, HPos.RIGHT);
      layout4.add(taLog4, 0, 3);
      taLog4.setPrefWidth(650);
      taLog4.setWrapText(true);
      taLog4.setScrollTop(Double.MAX_VALUE); // text area auto scrolls down
      layout4.setVgap(10); 
      
      btnFacTask1.setOnAction(e -> facultyTasksMenu(1)); // update faculty
      btnFacTask2.setOnAction(e -> facultyTasksMenu(2)); // delete faculty
      btnFacTask3.setOnAction(e -> facultyTasksMenu(3)); // add abstract
      btnFacTask4.setOnAction(e -> facultyTasksMenu(4)); // delete abstract
      btnFacTask5.setOnAction(e -> facultyTasksMenu(5)); // exit
      
      btnContinue4.setOnAction(e -> resume());
      
      /* Student Scenes*/
      
      // studentTasks
      layout5 = new GridPane();
      layout5.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct5 = new Label("Please choose from the following:");
      btnStuTask1 = new Button("Add student information/record");
      btnStuTask2 = new Button("Update student information/record (email)");
      btnStuTask3 = new Button("Delete student information/record");
      btnStuTask4 = new Button("Browse abstracts");
      btnStuTask5 = new Button("Update/Delete key topic to research");
      btnStuTask6 = new Button("Find faculty members who share my interests");
      btnStuTask7 = new Button("Done/Exit");
      btnContinue5 = new Button("Continue");
      btnContinue5.setDisable(true);
      btnContinue5.setPrefHeight(35);
      btnNext = new Button("Next");
      btnNext.setDisable(true);
      btnNext.setPrefHeight(35);
      btnDone = new Button("Done");
      btnDone.setDisable(true);
      btnDone.setPrefHeight(35);
      layout5.setVgap(10);
         
      btnStuTask1.setMaxWidth(Double.MAX_VALUE);
      btnStuTask2.setMaxWidth(Double.MAX_VALUE);
      btnStuTask3.setMaxWidth(Double.MAX_VALUE);
      btnStuTask4.setMaxWidth(Double.MAX_VALUE);
      btnStuTask5.setMaxWidth(Double.MAX_VALUE);
      btnStuTask6.setMaxWidth(Double.MAX_VALUE);
      btnStuTask7.setMaxWidth(Double.MAX_VALUE);
      btnStuTask1.setPrefHeight(35);
      btnStuTask2.setPrefHeight(35);
      btnStuTask3.setPrefHeight(35);
      btnStuTask4.setPrefHeight(35);
      btnStuTask5.setPrefHeight(35);
      btnStuTask6.setPrefHeight(35);
      btnStuTask7.setPrefHeight(35);
   
         
      layout5.add(lblInstruct5, 0, 0);  // object, col, row, colspan, rowspan
      VBox vbButtons5 = new VBox();
      vbButtons5.setSpacing(10);
      vbButtons5.setPadding(new Insets(10, 20, 10, 20)); 
      vbButtons5.getChildren().addAll(btnStuTask1, btnStuTask2, btnStuTask3, btnStuTask4, btnStuTask5, btnStuTask6, btnStuTask7);
      layout5.add(vbButtons5, 0, 1);
      layout5.add(btnContinue5, 0, 2);
      layout5.setHalignment(btnContinue5, HPos.LEFT);
      layout5.add(btnNext, 0, 2);
      layout5.setHalignment(btnNext, HPos.CENTER);
      layout5.add(btnDone, 0, 2);
      layout5.setHalignment(btnDone, HPos.RIGHT);
      layout5.add(taLog5, 0, 3);
      taLog5.setPrefWidth(650);
      taLog5.setWrapText(true);
      taLog5.setScrollTop(Double.MAX_VALUE);
      layout5.setVgap(10); 
      studentTasks = new Scene(layout5, 650, 600);
      // Stylesheet
      studentTasks.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
      
     
      btnStuTask1.setOnAction(e -> stage.setScene(studentCreate)); // studentCreate scene (add student)
      btnStuTask2.setOnAction(e -> studentTasks(2)); // update student
      btnStuTask3.setOnAction(e -> studentTasks(3)); // delete student
      btnStuTask4.setOnAction(e -> studentTasks(4)); // browse abstracts
      btnStuTask5.setOnAction(e -> studentTasks(5)); // update/delete topics
      btnStuTask6.setOnAction(e -> studentTasks(6)); // find faculty
      btnStuTask7.setOnAction(e -> studentTasks(7)); // exit
      
      btnContinue5.setOnAction(e -> resume());
      btnNext.setOnAction(e -> resume());
      btnDone.setOnAction(
         e -> {  // done - browse abstracts
            selection = 0;
            resume();
         });
      
      // studentCreate
      layout6 = new GridPane();
      layout6.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct6 = new Label("Please enter your information:");
      Label lblStuLastName = new Label("Last Name:");
      layout6.setHalignment(lblStuLastName, HPos.RIGHT);
      tfStuLastName = new TextField();
      Label lblStuFirstName = new Label("First Name:");
      layout6.setHalignment(lblStuFirstName, HPos.RIGHT);
      tfStuFirstName = new TextField();
      Label lblStuEmail = new Label("Email:");
      layout6.setHalignment(lblStuEmail, HPos.RIGHT);
      tfStuEmail = new TextField();
      Label lblWord1 = new Label("Key topic # 1 of 3:");
      layout6.setHalignment(lblWord1, HPos.RIGHT);
      tfWord1 = new TextField();
      Label lblWord2 = new Label("Key topic # 2 of 3 (or blank):");
      layout6.setHalignment(lblWord2, HPos.RIGHT);
      tfWord2 = new TextField();
      Label lblWord3 = new Label("Key topic # 3 of 3 (or blank):");
      layout6.setHalignment(lblWord3, HPos.RIGHT);
      tfWord3 = new TextField();
      btnCreateConfirmStu = new Button("Create Student Profile");
      btnCreateConfirmStu.setPrefHeight(35);
      layout6.setHalignment(btnCreateConfirmStu, HPos.RIGHT);
      btnContinue6 = new Button("Continue to Menu");
      btnContinue6.setDisable(true);
      btnContinue6.setPrefHeight(35);
      layout6.setHalignment(btnContinue6, HPos.RIGHT);
      layout6.add(lblInstruct6, 0, 0, 2, 1);
      layout6.add(lblStuLastName, 0, 1);
      layout6.add(tfStuLastName, 1, 1);
      layout6.add(lblStuFirstName, 0, 2);
      layout6.add(tfStuFirstName, 1, 2);
      layout6.add(lblStuEmail, 0, 3);
      layout6.add(tfStuEmail, 1, 3);
      layout6.add(lblWord1, 0, 4);
      layout6.add(tfWord1, 1, 4);
      layout6.add(lblWord2, 0, 5);
      layout6.add(tfWord2, 1, 5);
      layout6.add(lblWord3, 0, 6);
      layout6.add(tfWord3, 1, 6);
      layout6.add(btnCreateConfirmStu, 1, 7);
      layout6.add(btnContinue6, 1, 8);
      layout6.add(taLog6, 1, 9);
      taLog6.setPrefWidth(500);
      taLog6.setWrapText(true);
      taLog6.setScrollTop(Double.MAX_VALUE);
      layout6.setVgap(10);
      layout6.setHgap(10);
      studentCreate = new Scene(layout6, 750, 500);
      // Stylesheet
      studentCreate.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
      
      btnCreateConfirmStu.setOnAction(e -> studentTasks(1)); // add student
      btnContinue6.setOnAction(e -> stage.setScene(studentTasks)); // return to studentTasks
   
      /* Public Scenes*/
      
      // publicTasks
      layout7 = new GridPane();
      layout7.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct7 = new Label("Please choose from the following:");
      btnPubTask1 = new Button("Add public information/record");
      btnPubTask2 = new Button("Update public information/record (email)");
      btnPubTask3 = new Button("Delete public information/record");
      btnPubTask4 = new Button("Browse abstracts");
      btnPubTask5 = new Button("Update/Delete key topic");
      btnPubTask6 = new Button("Find students with related interests");
      btnPubTask7 = new Button("Done/Exit");
      btnContinue7 = new Button("Continue");
      btnContinue7.setDisable(true);
      btnContinue7.setPrefHeight(35);
      btnPubNext = new Button("Next");
      btnPubNext.setDisable(true);
      btnPubNext.setPrefHeight(35);
      btnPubDone = new Button("Done");
      btnPubDone.setDisable(true);
      btnPubDone.setPrefHeight(35);
      layout7.setVgap(10);
      
      btnPubTask1.setMaxWidth(Double.MAX_VALUE);
      btnPubTask2.setMaxWidth(Double.MAX_VALUE);
      btnPubTask3.setMaxWidth(Double.MAX_VALUE);
      btnPubTask4.setMaxWidth(Double.MAX_VALUE);
      btnPubTask5.setMaxWidth(Double.MAX_VALUE);
      btnPubTask6.setMaxWidth(Double.MAX_VALUE);
      btnPubTask7.setMaxWidth(Double.MAX_VALUE);
      btnPubTask1.setPrefHeight(35);
      btnPubTask2.setPrefHeight(35);
      btnPubTask3.setPrefHeight(35);
      btnPubTask4.setPrefHeight(35);
      btnPubTask5.setPrefHeight(35);
      btnPubTask6.setPrefHeight(35);
      btnPubTask7.setPrefHeight(35);
      
      layout7.add(lblInstruct7, 0, 0, 4, 1);  // object, col, row, colspan, rowspan
      VBox vbButtons7 = new VBox();
      vbButtons7.setSpacing(10);
      vbButtons7.setPadding(new Insets(10, 20, 10, 20)); 
      vbButtons7.getChildren().addAll(btnPubTask1, btnPubTask2, btnPubTask3, btnPubTask4, btnPubTask5, btnPubTask6, btnPubTask7);
      layout7.add(vbButtons7, 0, 1);      
      layout7.add(btnContinue7, 0, 2);
      layout5.setHalignment(btnContinue7, HPos.LEFT);
      layout7.add(btnPubNext, 0, 2);
      layout7.setHalignment(btnPubNext, HPos.CENTER);
      layout7.add(btnPubDone, 0, 2);
      layout7.setHalignment(btnPubDone, HPos.RIGHT);
      layout7.add(taLog7, 0, 3);
      taLog7.setPrefWidth(650);
      taLog7.setWrapText(true);
      taLog7.setScrollTop(Double.MAX_VALUE);
      layout7.setVgap(10); 
      publicTasks = new Scene(layout7, 650, 600);
      // Stylesheet
      publicTasks.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
     
      btnPubTask1.setOnAction(e -> stage.setScene(publicCreate)); // publicCreate scene (add student)
      btnPubTask2.setOnAction(e -> publicTasks(2)); // update student
      btnPubTask3.setOnAction(e -> publicTasks(3)); // delete student
      btnPubTask4.setOnAction(e -> publicTasks(4)); // browse abstracts
      btnPubTask5.setOnAction(e -> publicTasks(5)); // update/delete topics
      btnPubTask6.setOnAction(e -> publicTasks(6)); // find faculty
      btnPubTask7.setOnAction(e -> publicTasks(7)); // exit
      
      btnContinue7.setOnAction(e -> resume());
      btnPubNext.setOnAction(e -> resume());
      btnPubDone.setOnAction(
         e -> {  // done - browse abstracts
            selection = 0;
            resume();
         });
   
      // publicCreate
      layout8 = new GridPane();
      layout8.setPadding(new Insets(10, 10, 10, 10));
      Label lblInstruct8 = new Label("Please enter your information:");
      Label lblPubName = new Label("Public/company name:");
      layout8.setHalignment(lblPubName, HPos.RIGHT);
      tfPubName = new TextField();
      Label lblPubEmail = new Label("Email:");
      layout8.setHalignment(lblPubEmail, HPos.RIGHT);
      tfPubEmail = new TextField();
      Label lblPubWord1 = new Label("Key topic # 1 of 3:");
      layout8.setHalignment(lblPubWord1, HPos.RIGHT);
      tfPubWord1 = new TextField();
      Label lblPubWord2 = new Label("Key topic # 2 of 3 (or blank):");
      layout8.setHalignment(lblPubWord2, HPos.RIGHT);
      tfPubWord2 = new TextField();
      Label lblPubWord3 = new Label("Key topic # 3 of 3 (or blank):");
      layout8.setHalignment(lblPubWord3, HPos.RIGHT);
      tfPubWord3 = new TextField();
      btnCreateConfirmPub = new Button("Create Public Profile");
      btnCreateConfirmPub.setPrefHeight(35);
      layout8.setHalignment(btnCreateConfirmPub, HPos.RIGHT);
      btnContinue8 = new Button("Continue to Menu");
      btnContinue8.setDisable(true);
      btnContinue8.setPrefHeight(35);
      layout8.setHalignment(btnContinue8, HPos.RIGHT);
      layout8.add(lblInstruct8, 0, 0, 2, 1);
      layout8.add(lblPubName, 0, 1);
      layout8.add(tfPubName, 1, 1);
      layout8.add(lblPubEmail, 0, 2);
      layout8.add(tfPubEmail, 1, 2);
      layout8.add(lblPubWord1, 0, 3);
      layout8.add(tfPubWord1, 1, 3);
      layout8.add(lblPubWord2, 0, 4);
      layout8.add(tfPubWord2, 1, 4);
      layout8.add(lblPubWord3, 0, 5);
      layout8.add(tfPubWord3, 1, 5);
      layout8.add(btnCreateConfirmPub, 1, 6);
      layout8.add(btnContinue8, 1, 7);
      layout8.add(taLog8, 1, 8);
      taLog8.setPrefWidth(500);
      taLog8.setWrapText(true);
      taLog8.setScrollTop(Double.MAX_VALUE);
      layout8.setVgap(10);
      layout8.setHgap(10);
      publicCreate = new Scene(layout8, 750, 500);
      // Stylesheet
      publicCreate.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
      
      btnCreateConfirmPub.setOnAction(e -> publicTasks(1)); // add public
      btnContinue8.setOnAction(e -> stage.setScene(publicTasks)); // return to publicTasks
   
   } // end start()
   
      
   /**
    * handle for button actions
    */   
   public void handle(ActionEvent evt) {
      // Get the object of the button that was clicked
      Button btn = (Button)evt.getSource();
      
      // Switch on its name
      switch(btn.getText()) {
         case "Connect to Data Source":
            db.loadDriver();
            db.testConnection();
            btnConnect.setDisable(true);
            btnUser.setDisable(false);
            break;
         case "Select User Type":
            userType = db.selectUser();
            if(userType.contains("Faculty")) {
               userType = "faculty";
            } else if (userType.contains("Student")) {
               userType = "student";
            } else {
               userType = "public";
            }
            switch(userType) {
               case "faculty":
                  // use faculty scene(s) and perform faculty tasks
                  stage.setScene(facultyScene1);
                  break;
               case "student":
                  // use student scene(s) and perform student tasks
                  stage.setScene(studentTasks);
                  break;
               case "public":
                  stage.setScene(publicTasks);
                  break;
            }
            break;
         case "Exit":
            db.doExit();
            break;
      }
   } // end handle()
   
   /**
    * facultyTasks
    */
   public void facultyTasks(int option) {
      // faculty info
      int facultyID2 = 0;
      String lname = "";
      String fname = "";
      int buildingNum = 0;
      int officeNum = 0;
      int abstractID = 0;
      // pw & hashing
      String  originalPassword = ""; 
      String storedPW = "";  
      String generatedSecuredPasswordHash = "";
      boolean matched = false;
      
   
      taLog2.appendText("Faculty Research Database\n");
      taLog2.appendText("\tAppleton, Kelly \n\tPegoli, Christopher \n\tZhao, Wentao\n");
      taLog2.appendText("ISTE 330-02  Project   Group 4  2021-11-28\n\n");
      // LOGIN vs ADD NEW FACULTY Profile       
      switch(option) {
         case 1:  // Login
            facultyEmail = tfEmail.getText();
            storedPW = db.getStoredPW(facultyEmail);
            // hash pw
            try{
               originalPassword = pwPassword.getText();
               if(originalPassword.equals("")) {
                  if(storedPW == null) {
                     taLog2.appendText("Successful login!\n");
                     btnContinue2.setDisable(false);
                     btnLoginConfirm.setDisable(true);
                     break;
                  } else {
                     taLog2.appendText("Incorrect email &/or password... Exiting program.");
                     alert(AlertType.ERROR, "Incorrect email &/or password...\nExiting program.", "Invalid Login");
                     System.exit(0);
                  }
               }
            
               generatedSecuredPasswordHash 
                  = generateStorngPasswordHash(originalPassword);            
            
               matched = validatePassword(originalPassword, storedPW);
            }
            catch(NoSuchAlgorithmException nsae) {}
            catch(InvalidKeySpecException ikse) {}
         
            if(!matched) { // if invalid pw
               taLog2.appendText("Incorrect email &/or password... Exiting program.");
               alert(AlertType.ERROR, "Incorrect email &/or password...\nExiting program.", "Invalid Login");
               System.exit(0);
            }
            
            taLog2.appendText("Successful Login!\n");
            btnContinue2.setDisable(false);
            btnLoginConfirm.setDisable(true);
            break;
         case 2:  // Add new faculty
            lname = tfLastName.getText();
            fname = tfFirstName.getText();
            buildingNum = Integer.parseInt(tfBuilding.getText());
            officeNum = Integer.parseInt(tfOffice.getText());
            facultyEmail = tfNewFacEmail.getText();
            
            // hash pw
            try {
               originalPassword = pwNewFacPassword.getText();
               generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
            }
            catch(NoSuchAlgorithmException e) {}
            catch(InvalidKeySpecException e) {}                  
            taLog3.appendText(db.addFaculty(lname, fname, buildingNum, officeNum, facultyEmail, generatedSecuredPasswordHash) + " faculty member added.\n");
            
            // New faculty member 'needs' to insert abstract or speaking engagement...
            abstractID = addFile();
            
            // Link abstract to faculty
            facultyID = db.getFacultyID(facultyEmail);
            taLog3.appendText(db.linkAbstract(facultyID, abstractID) + " abstract linked to faculty member.\n");
            btnCreateConfirm.setDisable(true);
            btnContinue3.setDisable(false);
            // Ask if 2nd author for abstract added
               // If no second author
            if(db.selectSecondAuthor().contains("No")) {
               break;
            } else {            
               // Create faculty record for 2nd author (if necessary)
               // Can include first and last name only with remainder of record blank
                  // add 2nd author/faculty record
               lname = db.getAuthorLastName();
               fname = db.getAuthorFirstName();
            
               // Add faculty record
               taLog3.appendText(db.addFaculty(lname, fname) + " faculty member added.\n");
               // Link 2nd author/faculty to abstract
               // Get auto-generated facultyID
               facultyID2 = db.getFacultyID(lname, fname);  // use overloaded method
               taLog3.appendText(db.linkAbstract(facultyID2, abstractID) + " abstract linked to faculty member.\n");
               break;
            }
      } // end switch
   } // end FacultyTasks()

   /**
    * facultyTasksMenu
    * @param the case # to perform
    */
   public void facultyTasksMenu(int caseNum) {   
      String field = "";
      int buildingNum = 0;
      int officeNum = 0;
      facultyID = db.getFacultyID(facultyEmail);
      int facultyID2 = 0;
      String lname = "";
      String fname = "";
      int abstractID = 0;
            
      switch(caseNum) {
         case 1: // update faculty
            field = db.selectFieldToUpdate();
            if(field.contains("Building")) {
               field = "Building";
            } else if (field.contains("Office")) {
               field = "Office";
            } else {
               field = "Email";
            }
         
            switch(field) {
               case "Building":
                  buildingNum = db.selectBuilding();
                  taLog4.appendText(db.updateFaculty(facultyID, 1, buildingNum) + " update performed\n");
                  break;
               case "Office":
                  officeNum = db.selectOffice();
                  taLog4.appendText(db.updateFaculty(facultyID, 2, officeNum) + " update performed\n");
                  break;
               case "Email":
                  facultyEmail = db.selectFacEmail();
                  taLog4.appendText(db.updateFacultyEmail(facultyID, facultyEmail) + " update performed\n");
                  break;
            } // end switch   
            break;
         case 2: // delete faculty
            taLog4.appendText("If you have current abstracts in the system, they may not be deleted.\n");
            taLog4.appendText("Please consider deleting your abstracts before deleting your account.\n");
               // Confirm
            if(db.getYesNoSelection(1).contains("No")) {
               break;
            } else {  
                  // delete faculty account/info
               taLog4.appendText(db.deleteFaculty(facultyID) + " delete performed\n");
               db.doExit();
                  // ? delete faculty - added abstract stays in system... ok???????????????????????????
                  // abstract may or may not be linked to another faculty member...
            } 
            break;
         case 3: // add abstract
            abstractID = addFile();
               // Link abstract to faculty
            facultyID = db.getFacultyID(facultyEmail);
            taLog4.appendText(db.linkAbstract(facultyID, abstractID) + " abstract linked to faculty member.\n");
               // Ask if 2nd author for abstract added
                  // If no second author
            if(db.selectSecondAuthor().contains("No")) {
               break;
            } else {            
                  // Create faculty record for 2nd author (if necessary)
                  // Can include first and last name only with remainder of record blank
                     // add 2nd author/faculty record
               lname = db.getAuthorLastName();
               fname = db.getAuthorFirstName();
            
                  // Add faculty record
               taLog4.appendText(db.addFaculty(lname, fname) + " faculty member added.\n");
                  // Link 2nd author/faculty to abstract
                  // Get auto-generated facultyID
               facultyID2 = db.getFacultyID(lname, fname);  // use overloaded method
               taLog4.appendText(db.linkAbstract(facultyID2, abstractID) + " abstract linked to faculty member.\n");
            }
            break;
         case 4: // delete abstract
               // List abstracts - ID, type, and title
            taLog4.appendText(db.listAbstracts());
            btnContinue4.setDisable(false);
            taLog4.appendText("Press 'Continue'\n");
            pause();
            btnContinue4.setDisable(true);
               // Select abstract ID to delete
            abstractID = db.selectAbstractByID();
               // Confirm
            if(db.getYesNoSelection(2).contains("No")) {
               taLog4.appendText("No deletion performed.\n");
               break;
            } else { 
                  // delete selected abstract
               taLog4.appendText(db.deleteAbstract(abstractID) + " delete performed\n");
            } 
            break;
         case 5:
            db.doExit();
            break;
      } // end switch
   } // end facultyTasksMenu()   
   
   /**
    * studentTasks
    * @param the case # to perform
    */
   public void studentTasks(int caseNum) {
      // student info
      int studentID = 0;
      String lname = "";
      String fname = "";
      String word1 = "";
      String word2 = "";
      String word3 = "";
      String newWord = "";
      ArrayList<String> allAbstracts;
            
      switch(caseNum) {
         case 1: // add student
            lname = tfStuLastName.getText();
            fname = tfStuFirstName.getText();
            studentEmail = tfStuEmail.getText();
            word1 = tfWord1.getText();
            word2 = tfWord2.getText();
            word3 = tfWord3.getText();
            taLog6.appendText(db.addStudent(lname, fname, studentEmail, word1, word2, word3) + " student record added.\n"); 
            btnCreateConfirmStu.setDisable(true);
            btnContinue6.setDisable(false);
            break;
         case 2: // update student (email)
            studentEmail = db.selectStudentEmail();
            studentID = db.getStudentID(studentEmail);
            studentEmail = db.selectNewStudentEmail();
            taLog5.appendText(db.updateStudentEmail(studentID, studentEmail) + " update performed\n");
            break;
         case 3: // delete student
            studentEmail = db.selectStudentEmail();
            if(db.getYesNoSelection(3).contains("No")) {
               break;
            } else {
               taLog5.appendText(db.deleteStudent(studentEmail) + " delete performed\n");
            }
            break;
         case 4: // browse abstracts
               /* Allow user to browse abstract one at a time
                  Stop whenever done or no abstracts left */
               // Get total number of abstracts
            allAbstracts = db.browseAbstracts();
            int numOfAbstracts = allAbstracts.size();
            taLog5.appendText("Number of abstracts to browse: " + numOfAbstracts + "\n");
               // display abstracts, 1 at a time
            btnNext.setDisable(false);
            btnDone.setDisable(false);
            taLog5.appendText(String.format("%-12s %-10s %-25s %-15s %n", "AbstractID", "Type", "Title", "About\n"));
            selection = 1;
            for (int i = 0; i < numOfAbstracts; i++) {
               if(selection == 1) {
                  taLog5.appendText(allAbstracts.get(i));
                  taLog5.appendText("\n\nPress 'Next' to display the next abstract\n");
                  taLog5.appendText("Press 'Done' if you are done browsing\n\n");
                  pause();
               } else {
                  break;
               }
            } // end for
            taLog5.appendText("You have viewed all abstracts in the database or have selected 'Done'\n");
            btnNext.setDisable(true);
            btnDone.setDisable(true);
            break;
         case 5: // update/delete topic
            studentEmail = db.selectStudentEmail(); 
               // list topics/keywords currently associated with student
            taLog5.appendText(db.listStudentTopics(studentEmail));
               // prompt which topic/keyword to update
            selection = db.numSelection(1);
               // get new topic/keyword (or blank to delete)
            newWord = db.selectNewWord();
            taLog5.appendText(db.updateStudentTopic(studentEmail, selection, newWord) + " update performed\n");
            break;
         case 6: // match abstracts/faculty
               /* You must provide an intersection of the
                  facultys abstracts to a students
                  research interest. Then provide to the
                  student the Facultys name, Building
                  Number and Office number and email
                  address so the student can go make an
                  appointment with the Faculty member who
                  shares their interest.
               */
               // get student email
            studentEmail = db.selectStudentEmail(); 
               // call matchFaculty Method
            taLog5.appendText(db.matchFaculty(studentEmail) + "\n");
            btnContinue5.setDisable(false);
            pause();
            btnContinue5.setDisable(true);
               // Ask if student wants faculty info based on abstract(s) returned
               // pull faculty information based on abstract id selection
            selection = db.numSelection(2);
            if(selection != 0) {
               taLog5.appendText(db.getFacultyInfo(selection) + "\n");
            }
            break;
         case 7:
            db.doExit();
            break;
      } // end switch
   
   } // end studentTasks()
   
   
   /**
    * publicTasks
    * @param the case # to perform
    */
   public void publicTasks(int caseNum) {
      // public info
      int publicID = 0;
      String publicName = "";
      String oldEmail = "";
      String word1 = "";
      String word2 = "";
      String word3 = "";
      String newWord = "";
      ArrayList<String> allAbstracts;
            
      switch(caseNum) {
         case 1:  // Add public
            publicName = tfPubName.getText();
            publicEmail = tfPubEmail.getText();
            word1 = tfPubWord1.getText();
            word2 = tfPubWord2.getText();
            word3 = tfPubWord3.getText();
            taLog8.appendText(db.addPublic(publicName, publicEmail, word1, word2, word3) + " public record added.\n"); 
            btnCreateConfirmPub.setDisable(true);
            btnContinue8.setDisable(false);
            break;
         case 2: // update public (email)
            oldEmail = db.selectPublicEmail();
            publicEmail = db.selectNewPublicEmail();
            taLog7.appendText(db.updatePublicEmail(oldEmail, publicEmail) + " update performed\n");
            break;
         case 3: // delete public
            publicEmail = db.selectPublicEmail();
            if(db.getYesNoSelection(4).contains("No")) {
               break;
            } else {
               taLog7.appendText(db.deletePublic(publicEmail) + " delete performed\n");
            }
            break;
         case 4: // browse abstracts
               // Allow user to browse abstract one at a time
               // Stop whenever done or no abstracts left
            allAbstracts = db.browseAbstracts();
            int numOfAbstracts = allAbstracts.size();
            taLog7.appendText("Number of abstracts to browse: " + numOfAbstracts + "\n");
               // display abstracts, 1 at a time
            btnPubNext.setDisable(false);
            btnPubDone.setDisable(false);
            taLog7.appendText(String.format("%-12s %-10s %-25s %-15s %n", "AbstractID", "Type", "Title", "About"));
            selection = 1;
            for (int i = 0; i < numOfAbstracts; i++) {
               if(selection == 1) {
                  taLog7.appendText(allAbstracts.get(i));
                  taLog7.appendText("\n\nPress 'Next' to display the next abstract\n");
                  taLog7.appendText("Press 'Done' if you are done browsing\n\n");
                  pause();
               } else {
                  break;
               }
            } // end for
            taLog7.appendText("You have viewed all abstracts in the database or have selected 'Done'\n");
            btnPubNext.setDisable(true);
            btnPubDone.setDisable(true);
            break;
         case 5: // update/delete topic
            publicEmail = db.selectPublicEmail(); 
               // list topics/keywords currently associated with public
            taLog7.appendText(db.listPublicTopics(publicEmail));
               // prompt which topic/keyword to update
            selection = db.numSelection(1);
               // get new topic/keyword (or blank to delete)
            newWord = db.selectNewWord();
            taLog7.appendText(db.updatePublicTopic(publicEmail, selection, newWord) + " update performed\n");
            break;
         case 6:
               /* You must produce an intersection from
                  users outside of the COLLEGE with students
                  currently attending RIT.
               */           
               // get public email
            publicEmail = db.selectPublicEmail(); 
               // call matchStudent Method
            taLog7.appendText(db.matchStudent(publicEmail) + "\n");
            break;             
         case 7:
            db.doExit();
            break;
      } // end switch
   
   } // end publicTasks()
   
   /**
    * addFile - Faculty selects a file to be uploaded from a file chooser
    * (file = book abstract or speech abstract)
    * @return abstractID - the ID of the added abstract
    */
   private int addFile() {   
      String document = "";
      String title = "";
      String description = ""; 
      File selectedFile = null;
      int abstractID = 0;
              
      // Prompt to insert book abstract vs speaking engagement abstract: 
      document = db.selectDocType();
      if(document.contains("Book")) {
         document = "Book";
      } else {
         document = "Speech";
      }
      
      title = db.selectTitle();
      
      try {      
         // Choose a file
         FileChooser fileChooser = new FileChooser();
         // set initial directory
         fileChooser.setInitialDirectory(new File("."));
         fileChooser.setTitle("Choose text file for upload");
         fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("All Files", "*.*"));      
         // Show the chooser
         selectedFile = fileChooser.showOpenDialog(stage);  // show dialog and link it to our stage
         if (selectedFile == null) {  // if no file entered
            // Cancelled
            alert(AlertType.ERROR, "No local file chosen... cancelled!", "Cancelled");
            return abstractID;
         }
      } // end try
      catch(Exception e) {  // add more catch exceptions???
         alert(Alert.AlertType.ERROR, "Unexpected Exception (addFile):  " + e, "Error");
         return abstractID;
      }
   
      // get description of document
      description = db.getDescription(selectedFile);
      
      // add abstract to database      
      taLog3.appendText("Inserted record's ID: " + (abstractID = db.addAbstract(document, title, description)) + "\n");
   
      return abstractID;
   } // end addFile
   
   /** alert - method to create alerts */
   private void alert(AlertType type, String message, String header) {
      Alert alert = new Alert(type, message);
      alert.setHeaderText(header);
      alert.showAndWait();
   } // end alert()  
   
   /** Pause program to allow user to view log */
   private void pause() {
      Platform.enterNestedEventLoop(PAUSE_KEY);
   } // end pause()

   /** Resume program after pause / when user wishes to continue */
   private void resume() {
      Platform.exitNestedEventLoop(PAUSE_KEY, null);
   } // end resume()
   
   /** Generating Hash from String */
 //    private static String get_SHA_1_SecurePassword(String passwordToHash,
//             String salt) {
//         String generatedPassword = null;
//         try {
//             MessageDigest md = MessageDigest.getInstance("SHA-1");
//             md.update(salt.getBytes());
//             byte[] bytes = md.digest(passwordToHash.getBytes());
//             StringBuilder sb = new StringBuilder();
//             for (int i = 0; i < bytes.length; i++) {
//                 sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
//                         .substring(1));
//             }
//             generatedPassword = sb.toString();
//         } catch (NoSuchAlgorithmException e) {
//             e.printStackTrace();
//         }
//         return generatedPassword;
//     }
//     
//     // Add salt
//     private static String getSalt() throws NoSuchAlgorithmException {
//         SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//         byte[] salt = new byte[16];
//         sr.nextBytes(salt);
//         return salt.toString();
//     }
    
    
    /* Generate Hash from String */
   private static String generateStorngPasswordHash(String password) 
    throws NoSuchAlgorithmException, InvalidKeySpecException
   {
      int iterations = 1000;
      char[] chars = password.toCharArray();
      byte[] salt = getSalt();
   
      PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
   
      byte[] hash = skf.generateSecret(spec).getEncoded();
      return iterations + ":" + toHex(salt) + ":" + toHex(hash);
   }

   private static byte[] getSalt() throws NoSuchAlgorithmException
   {
      SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
      byte[] salt = new byte[16];
      sr.nextBytes(salt);
      return salt;
   }

   private static String toHex(byte[] array) throws NoSuchAlgorithmException
   {
      BigInteger bi = new BigInteger(1, array);
      String hex = bi.toString(16);
    
      int paddingLength = (array.length * 2) - hex.length();
      if(paddingLength > 0)
      {
         return String.format("%0"  +paddingLength + "d", 0) + hex;
      }else{
         return hex;
      }
   }

/* Validate Password*/
   private static boolean validatePassword(String originalPassword, String storedPassword) 
    throws NoSuchAlgorithmException, InvalidKeySpecException
   {
      String[] parts = storedPassword.split(":");
      int iterations = Integer.parseInt(parts[0]);
   
      byte[] salt = fromHex(parts[1]);
      byte[] hash = fromHex(parts[2]);
   
      PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), 
         salt, iterations, hash.length * 8);
      SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
      byte[] testHash = skf.generateSecret(spec).getEncoded();
   
      int diff = hash.length ^ testHash.length;
      for(int i = 0; i < hash.length && i < testHash.length; i++)
      {
         diff |= hash[i] ^ testHash[i];
      }
      return diff == 0;
   }
   private static byte[] fromHex(String hex) throws NoSuchAlgorithmException
   {
      byte[] bytes = new byte[hex.length() / 2];
      for(int i = 0; i < bytes.length ;i++)
      {
         bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
      }
      return bytes;
   }


} // End of Class