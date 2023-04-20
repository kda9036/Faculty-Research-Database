import java.sql.*;                   // Load JDBC core library

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
import java.util.*;
import java.io.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/*
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
*/

/** 
 * ResearchDataLayer - data layer
 * Project: Faculty Research Database
 * Group 4:
 * @author Appleton, Kelly
 * @author Pegoli, Christopher
 * @author Zhao, Wentao
 * @version 11/28/21
 */
 
public class ResearchDataLayer extends Application implements EventHandler<ActionEvent> {

   private Connection conn;
   private Statement stmt;
   private ResultSet rs;       // result set, used after query
   public String sql;          // any sql SELECT statement
   /* JDBC Type 4 Driver */
   final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

   /* url line below at the end identifies the database name */
	/* Define Data Source */
   public static String url = "jdbc:mysql://localhost/research";  // research DB

   public static String databaseName = new String();
   public static String userName = new String();
   public static String password = new String();
   
   /**
    * Default Constructor
    */
   public ResearchDataLayer() {
   } //end constructor


   /**
    * loadDriver
    */
   public void loadDriver() {
      try {
         Class.forName(DEFAULT_DRIVER);
         alert(AlertType.INFORMATION, "Successfully loaded driver class: \n" +
            DEFAULT_DRIVER, "Driver Loaded");
      } //end try
      catch (ClassNotFoundException cnfe) {
         // Create an alert box
         alert(AlertType.ERROR, "Unable to load driver class: \n" +
            DEFAULT_DRIVER, "Driver Failed to Load");
         return;
      } //end catch
   } // end loadDriver()

    /**
     * testConnection 
     * Create a connection use the driver to create a connection
     */
   public void testConnection() {
      boolean connected = true;
      String password = new String();
      // Create the custom dialog for entering hidden password
      Dialog dialog = new Dialog();
      dialog.setTitle("Please Enter Password");
      dialog.setHeaderText("Password (Blank if 'student'): ");
      PasswordField pwField = new PasswordField();
      dialog.getDialogPane().setContent(pwField);
      ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
      dialog.showAndWait();
      password = pwField.getText();
      if(password.equals("")) {
         password = "student";
      }
   
      try {
         conn = DriverManager.getConnection(url, "root", password);
         connected = true;
      }// end try
      catch (SQLException se) {
         connected = false;
         se.printStackTrace();
      }
      catch (Exception e) {
         connected = false;
         e.printStackTrace();
      }// end catch
   
      if(connected){
         alert(AlertType.INFORMATION, "Your DB Connection works.\nAuthor's name Group 4", "Connected");
      }
      else {
         alert(AlertType.ERROR, "Unable to connect to data source:\n" +
            url, "Connection Failed");
         System.exit(1);
      } // end if/else
   } // end testConnection()

    /**
     * selectUser
     * @return user (type - faculty, student, or public)
     */
   public String selectUser() {
      String user = "";
      // Create the custom dialog for user selection
      Dialog dialog = new Dialog();
      dialog.setTitle("User Selection");
      dialog.setHeaderText("Please select user type:");
      ToggleGroup group = new ToggleGroup();
      RadioButton rb1 = new RadioButton("Faculty");
      rb1.setToggleGroup(group);
      rb1.setUserData("faculty");
      rb1.setSelected(true);
      RadioButton rb2 = new RadioButton("Student");
      rb2.setToggleGroup(group);
      rb1.setUserData("student");
      RadioButton rb3 = new RadioButton("Public/Other");
      rb3.setToggleGroup(group);
      rb1.setUserData("public");
      TilePane tilePane = new TilePane();
      tilePane.getChildren().add(rb1);
      tilePane.getChildren().add(rb2);
      tilePane.getChildren().add(rb3);
      tilePane.setHgap(20);
      dialog.getDialogPane().setContent(tilePane);
      dialog.getDialogPane().setPrefSize(500, 200);
      dialog.getDialogPane().setPadding(new Insets(10, 20, 10, 20));
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      group.setUserData(group.getSelectedToggle().toString());
      user = group.getUserData().toString();
   
      return user;
   } // end selectUser()
   
    /**
     * selectDocType
     * @return user (type - book, speech)
     */
   public String selectDocType() {
      String type = "";
      // Create the custom dialog for user selection
      Dialog dialog = new Dialog();
      dialog.setTitle("Document Type Selection");
      dialog.setHeaderText("What would you like to upload?");
      ToggleGroup group = new ToggleGroup();
      RadioButton rb1 = new RadioButton("Book Abstract");
      rb1.setToggleGroup(group);
      rb1.setUserData("book");
      rb1.setSelected(true);
      RadioButton rb2 = new RadioButton("Speaking Engagement Abstract");
      rb2.setToggleGroup(group);
      rb1.setUserData("speach");
      TilePane tilePane = new TilePane();
      tilePane.getChildren().add(rb1);
      tilePane.getChildren().add(rb2);
      dialog.getDialogPane().setContent(tilePane);
      dialog.getDialogPane().setPrefSize(500, 200);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      group.setUserData(group.getSelectedToggle().toString());
      type = group.getUserData().toString();
   
      return type;
   } // end selectDocType()

    /**
     * selectTitle
     * @return title (of book or speech)
     */
   public String selectTitle() {
      String title = "";
      // Create the custom dialog for title entry
      Dialog dialog = new Dialog();
      dialog.setTitle("Book or Speaking Engagement Abstract Title");
      dialog.setHeaderText("Please enter the title: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      title = tfTitle.getText();
   
      return title;
   } // end selectTitle()
   
    /**
     * selectFieldToUpdate
     * @return field
     */
   public String selectFieldToUpdate() {
      String field = "";
      // Create the custom dialog for user selection
      Dialog dialog = new Dialog();
      dialog.setTitle("Field to Update Selection");
      dialog.setHeaderText("Please select the field to update:");
      ToggleGroup group = new ToggleGroup();
      RadioButton rb1 = new RadioButton("Building Number");
      rb1.setToggleGroup(group);
      rb1.setUserData("Building");
      rb1.setSelected(true);
      RadioButton rb2 = new RadioButton("Office Number");
      rb2.setToggleGroup(group);
      rb1.setUserData("Office");
      RadioButton rb3 = new RadioButton("Email");
      rb3.setToggleGroup(group);
      rb1.setUserData("Email");
      TilePane tilePane = new TilePane();
      tilePane.getChildren().add(rb1);
      tilePane.getChildren().add(rb2);
      tilePane.getChildren().add(rb3);
      tilePane.setHgap(20);
      dialog.getDialogPane().setContent(tilePane);
      dialog.getDialogPane().setPadding(new Insets(10, 20, 10, 20));
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      group.setUserData(group.getSelectedToggle().toString());
      field = group.getUserData().toString();
   
      return field;
   } // end selectFieldToUpdate()

   
    /**
     * selectSecondAuthor
     * @return selection (add second author or done)
     */
   public String selectSecondAuthor() {
      String selection = "";
      // Create the custom dialog for user selection
      Dialog dialog = new Dialog();
      dialog.setTitle("Add Another Author");
      dialog.setHeaderText("Do you need to add an author for the abstract?");
      ToggleGroup group = new ToggleGroup();
      RadioButton rb1 = new RadioButton("Yes, there is a second author.");
      rb1.setToggleGroup(group);
      rb1.setUserData("Yes");
      rb1.setSelected(true);
      RadioButton rb2 = new RadioButton("No, I'm the only author.");
      rb2.setToggleGroup(group);
      rb1.setUserData("No");
      TilePane tilePane = new TilePane();
      tilePane.getChildren().add(rb1);
      tilePane.getChildren().add(rb2);
      tilePane.setHgap(10);
      dialog.getDialogPane().setContent(tilePane);
      dialog.getDialogPane().setPrefSize(500, 200);
      dialog.getDialogPane().setPadding(new Insets(10, 20, 10, 20));
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      group.setUserData(group.getSelectedToggle().toString());
      selection = group.getUserData().toString();
   
      return selection;
   } // end selectSecondAuthor()
   
    /**
     * getAuthorLastName
     * @return lName (last name of author)
     */
   public String getAuthorLastName() {
      String lName = "";
      // Create the custom dialog for title entry
      Dialog dialog = new Dialog();
      dialog.setTitle("Author's Last Name");
      dialog.setHeaderText("Please enter the author's last name: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      lName = tfTitle.getText();
   
      return lName;
   } // end getAuthorLastName()
   
    /**
     * getAuthorFirstName
     * @return fName (first name of author)
     */
   public String getAuthorFirstName() {
      String fName = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Author's First Name");
      dialog.setHeaderText("Please enter the author's first name: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      fName = tfTitle.getText();
   
      return fName;
   } // end getAuthorFirstName()
   
    /**
     * selectBuilding
     * @return building - the updated building #
     */
   public int selectBuilding() {
      int building = 0;
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Building Number");
      dialog.setHeaderText("Please enter the new building #: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      building = Integer.parseInt(tfTitle.getText());
   
      return building;
   } // end selectBuilding()
   
    /**
     * selectOffice
     * @return office - the updated office #
     */
   public int selectOffice() {
      int office = 0;
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Office Number");
      dialog.setHeaderText("Please enter the new office #: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      office = Integer.parseInt(tfTitle.getText());
   
      return office;
   } // end selectOffice()
   
    /**
     * selectFacEmail
     * @return email - the updated faculty email
     */
   public String selectFacEmail() {
      String email = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Faculty Email");
      dialog.setHeaderText("Please enter the new email: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      email = tfTitle.getText();
   
      return email;
   } // end selectFacEmail()
   
    /**
     * getYesNoSelection
     * @param scenario - determines header text
     * @return selection (yes or no)
     */
   public String getYesNoSelection(int scenario) {
      String selection = "";
      // Create the custom dialog for user selection
      Dialog dialog = new Dialog();
      dialog.setTitle("Confirm Deletion");
      if (scenario == 1) {
         dialog.setHeaderText("Are you sure you would like to delete the current faculty member (delete your account)?");
      } else if (scenario == 2) {
         dialog.setHeaderText("Are you sure you would like to delete the abstract?");
      } else if (scenario == 3) {
         dialog.setHeaderText("Are you sure you would like to delete the student record?");
      } else if (scenario == 4) {
         dialog.setHeaderText("Are you sure you would like to delete the public record?");
      }
      ToggleGroup group = new ToggleGroup();
      RadioButton rb1 = new RadioButton("Yes, delete.");
      rb1.setToggleGroup(group);
      rb1.setUserData("Yes");
      rb1.setSelected(true);
      RadioButton rb2 = new RadioButton("No, keep.");
      rb2.setToggleGroup(group);
      rb1.setUserData("No");
      TilePane tilePane = new TilePane();
      tilePane.getChildren().add(rb1);
      tilePane.getChildren().add(rb2);
      tilePane.setHgap(50);
      dialog.getDialogPane().setContent(tilePane);
      dialog.getDialogPane().setPadding(new Insets(10, 20, 10, 20));
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      group.setUserData(group.getSelectedToggle().toString());
      selection = group.getUserData().toString();
   
      return selection;
   } // end getYesNoSelection()
   
    /**
     * numSelection
     * @param - the scenario (determines header text)
     * @return num - the number selection
     */
   public int numSelection(int scenario) {
      int num = 0;
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Numeric Selection");
      if(scenario == 1) {
         dialog.setHeaderText("Please enter the number of the keyword/topic to update/delete: ");
      } else if (scenario == 2) {
         dialog.setHeaderText("Please enter the abstract ID to display related faculty/author contact information:\n" +
                              "Enter 0 to continue without displaying faculty information");
      } else if (scenario == 3) {
         dialog.setHeaderText("Please enter the abstract ID to display related student contact information:\n" +
                              "Enter 0 to continue without displaying student information");
      }
         
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      num = Integer.parseInt(tfTitle.getText());
   
      return num;
   } // end numSelection()
   
    /**
     * selectAbstractByID
     * @return ID (abstractID)
     */
   public int selectAbstractByID() {
      int ID = 0;
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Abstract to Delete Selection");
      dialog.setHeaderText("Please select the abstract ID you wish to delete:");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      ID = Integer.parseInt(tfTitle.getText());
   
      return ID;
   } // end selectAbstractByID()
   
   /*OVERRIDE TO HANDLE GUI/EVENTS*/
   public void start(Stage _stage) {}   
   public void handle(ActionEvent evt) {}
        
//*****************************************************************************************************************************
// Faculty Methods

   /**
    * getFacultyID
    * @param email - the faculty's email
    * @return facultyID - the faculty's ID
    */     
   public int getFacultyID(String email) {
      int facultyID;
      try {        
         // prepared statement
         String sql = "SELECT faculty_ID FROM faculty WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email);  
         rs = stmt.executeQuery();
         rs.next();
         facultyID = rs.getInt(1);
         return facultyID;
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET FACULTY ID FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in getFacultyID method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
   } // end getFacultyID()
   
   /**
    * getFacultyID (overloaded)
    * @param lname - the faculty's last name
    * @param fname - the faculty's first name
    * @return facultyID - the faculty's ID
    */     
   public int getFacultyID(String lname, String fname) {
      int facultyID;
      try {        
         // prepared statement
         String sql = "SELECT faculty_ID FROM faculty WHERE lastName = ? AND firstName = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, lname);  
         stmt.setString(2, fname); 
         rs = stmt.executeQuery();
         rs.next();
         facultyID = rs.getInt(1);
         return facultyID;
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET FACULTY ID FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in getFacultyID method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
   } // end getFacultyID() - overloaded
       
   /**
    * addFaculty
    * @param lName - the faculty's last name
    * @param fName - the faculty's first name
    * @param buildingNum - the faculty's building number
    * @param officeNum - the faculty's office number
    * @param email - the faculty's email
    * @param password - the faculty's desired password for the program
    * @return the number of rows affected
    */
   public int addFaculty(String lName, String fName, int buildingNum, int officeNum, String email, String password) {
      int rows = 0;
      try {        
         // prepared statement
         String sql = "INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email, user_password) VALUES (?, ?, ?, ?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, lName);
         stmt.setString(2, fName);
         stmt.setInt(3, buildingNum);  
         stmt.setInt(4, officeNum);  
         stmt.setString(5, email); 
         stmt.setString(6, password);   
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("ADD FACULTY FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in addFaculty method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end addFaculty()
   
   /**
    * addFaculty - overloaded
    * @param lName - the faculty's last name
    * @param fName - the faculty's first name
    * @return the number of rows affected
    */
   public int addFaculty(String lName, String fName) {
      int rows = 0;
      try {        
         // prepared statement
         String sql = "INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email, user_password) VALUES (?, ?, ?, ?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, lName);
         stmt.setString(2, fName);
         stmt.setNull(3, Types.NULL);  
         stmt.setNull(4, Types.NULL);  
         stmt.setNull(5, Types.NULL); 
         stmt.setNull(6, Types.NULL);   
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("ADD FACULTY FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in addFaculty method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end addFaculty() - overloaded

   
   /**
    * updateFaculty (building/office #)
    * @param facultyID - the faculty to update
    * @param updateSelect - 1 for building number and 2 for office number
    * @param number - the faculty's new building or office number
    * @return the number of rows affected
    */
   public int updateFaculty(int facultyID, int updateSelect, int number) {
      int rows = 0;
      int updateSelection = updateSelect;
      String field = "";
      if(updateSelection == 1) {
         field = "buildingNum";
      } else if (updateSelection == 2) {
         field = "officeNum";
      } else {
         System.out.println("Invalid selection");
         return 0;
      }
      try {
         // prepared statement
         String sql = "UPDATE faculty SET " + field + " = ? WHERE faculty_ID = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters  
         stmt.setInt(1, number);
         stmt.setInt(2, facultyID);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE FACULTY FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updateFaculty method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end of updateFaculty() - building/office #
   
   /**
    * updateFacultyEmail
    * @param facultyID - the faculty to update
    * @param email - the faculty's new email
    * @return the number of rows affected
    */
   public int updateFacultyEmail(int facultyID, String email) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "UPDATE faculty SET email = ? WHERE faculty_ID = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters  
         stmt.setString(1, email);
         stmt.setInt(2, facultyID);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE FACULTY EMAIL FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updateFacultyEmail method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end of updateFacultyEmail()
   
   /**
    * deleteFaculty
    * @param facultyID - the faculty to delete
    * @return the number of rows affected
    */
   public int deleteFaculty(int facultyID) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "DELETE FROM faculty WHERE faculty_ID = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind parameters
         stmt.setInt(1, facultyID);
         
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("DELETE FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in deleteFaculty method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end deleteFaculty()
   
   /**
    * getFacultyInfo
    * @param abstractID
    * @return info - the faculty's information
    */     
   public String getFacultyInfo(int abstractID) {
      String info = "";
      try {        
         // Statement
         String sql = "SELECT lastName, firstName, buildingNum, officeNum, email FROM faculty JOIN faculty_abstract USING (faculty_ID) JOIN abstract USING (abstract_ID) WHERE abstract_ID = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setInt(1, abstractID); 
         // Create a resultset  
         rs = stmt.executeQuery();
         info += String.format("%-10s %-10s %-10s %-6s %-10s\n", "Last_Name", "First_Name", "Building_#", "Office_#", "Email");
         
         while (rs.next()) {
            String lastName = rs.getString(1);
            String firstName = rs.getString(2);
            int buildingNum = rs.getInt(3);
            int officeNum = rs.getInt(4);
            String email = rs.getString(5);
            info += String.format("%-10s %-10s %10d %8d %-25s\n", lastName, firstName, buildingNum, officeNum, email);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET FACULTY INFO FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in getFacultyInfo method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return info;
   } // end getFacultyInfo()
   
   /**
    * getStoredPW
    * @param email - the faculty's email
    * @return storedPW - the faculty's stored password
    */     
   public String getStoredPW(String email) {
      String storedPW = "";
      try {        
         // prepared statement
         String sql = "SELECT user_password FROM faculty WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email);  
         rs = stmt.executeQuery();
         rs.next();
         storedPW = rs.getString(1);
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET STORED PASSWORD FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         alert(AlertType.ERROR, "Incorrect email &/or password...\nExiting program.", "Invalid Login");
         System.exit(1);
      }
      catch(Exception e) {
         System.out.println("Error occured in getStoredPW method");
         System.out.println("ERROR MESSAGE is -> " + e);
         alert(AlertType.ERROR, "Incorrect email &/or password...\nExiting program.", "Invalid Login");
         e.printStackTrace();
         System.exit(1);
      }
      return storedPW;
   } // end getStoredPW()

   
//******************************************************************************************************************************
// Book/Speech Abstract Methods   

   /**
    * getDescription
    * @param file the selected file
    * @return the abstract description
    */
   public String getDescription(File file) {
      String description = null;      
      /*
      String ext = FilenameUtils.getExtension(file.toString());
   
      // Read .docx files
      if(ext.equals("doc") || ext.equals("docx")) {
         XWPFDocument document = null;
         FileInputStream fileInputStream = null;
         try {
            fileInputStream = new FileInputStream(file);
            document = new XWPFDocument(fileInputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
         
            description = extractor.getText();
         
         } catch (Exception e) {
            System.out.println("We had an error while reading the Word Doc");
         } finally {
            try {
               if (document != null) {
                  document.close();
               }
               if (fileInputStream != null) {
                  fileInputStream.close();
               }
            } catch (Exception ex) {
            }
         }
      } else if (ext.equals("txt")) {
      */
      // Read .txt files
         try {
            Scanner in = new Scanner(file);
            while (in.hasNext()) {
               description += in.nextLine();
            }
            in.close();
         }            
         catch (FileNotFoundException fnfe) {
            Alert alert = new Alert(AlertType.ERROR, "Error - file not found.");
            alert.showAndWait();
            return "";
         }          
         catch (Exception e) {
            Alert alert = new Alert(AlertType.ERROR, "Error...Application will terminate.");
            alert.showAndWait();
            System.exit(1);
         }
         /*
      } else {
         Alert alert = new Alert(AlertType.ERROR, "Error...Incompatible file type.");
         alert.showAndWait();
         System.exit(1);
      } // end if/else
      */
      return description;
   } // end getDescription()
  
   /**
    * listAbstracts
    * @return list (of all abstracts)
    */     
   public String listAbstracts() {
      String list = "";
      try {        
         // Statement
         String sql = "SELECT abstract_ID, abstract_type, title FROM abstract";
         stmt = conn.createStatement();
         // Create a resultset  
         rs = stmt.executeQuery(sql);
         list = String.format("%-12s %-8s %-25s\n", "AbstractID", "Type", "Title");
         
         while (rs.next()) {
            int abstractID = rs.getInt("abstract_ID");
            String type = rs.getString("abstract_type");
            String title = rs.getString("title");
            list += String.format("%10d   %-8s %-25s\n", abstractID, type, title);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("LIST ABSTRACTS FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in listAbstracts method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return list;
   } // end listAbstracts()
   
   
   /**
    * getAbstractInfo
    * @param id - the abstract ID
    * @return info - the abstract info
    */     
   public String getAbstractInfo(int id) {
      String info = "";
      try {        
         // Statement
         String sql = "SELECT * FROM abstract WHERE abstract_ID = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setInt(1, id); 
         // Create a resultset  
         rs = stmt.executeQuery();
         info += String.format("%-10s %-8s %-25s %-10s\n", "AbstractID", "Type", "Title", "About");
         
         while (rs.next()) {
            int abstractID = rs.getInt("abstract_ID");
            String type = rs.getString("abstract_type");
            String title = rs.getString("title");
            String about = rs.getString("about");
            info += String.format("%10d %-8s %-25s %-10s\n", abstractID, type, title, about);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET ABSTRACT INFO FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in getAbstractInfo method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return info;
   } // end getAbstractInfo()

//    /**
//     * getNumAbstracts
//     * @return numAbstracts - the number of abstracts
//     */ 
//    public String getNumAbstracts() {
//       int rowCount = 0;
//       String numAbstracts = "";
//       try {
//          // rows: 
//          // Create a statement
//          stmt = conn.createStatement();
//          // Create a resultset - get total # rows / total # abstracts
//          sql = "SELECT COUNT(*) FROM abstract";
//          rs = stmt.executeQuery(sql);
//          rs.next(); // moving to the next row in rs
//          rowCount = rs.getInt(1);
//          numAbstracts = "Number of abstracts to browse: " + rowCount;
//          return numAbstracts;
//    } // end getNumAbstracts()

   /**
    * browseAbstracts
    * @return allAbstracts
    */ 
   public ArrayList<String> browseAbstracts() {
      int rowCount = 0;
      String abstractEntry = "";
      ArrayList<String> allAbstracts = new ArrayList<String>();
      try {      
         // Create a statement
         stmt = conn.createStatement();
         // get & display all abstract info
         sql = "SELECT * FROM abstract";
         rs = stmt.executeQuery(sql);
         while (rs.next()) {
            int abstractID = rs.getInt("abstract_ID");
            String type = rs.getString("abstract_type");
            String title = rs.getString("title");
            String about = rs.getString("about");
            abstractEntry = String.format("%10d   %-10s %-25s %-15s", abstractID, type, title, about);
            allAbstracts.add(abstractEntry);
         } // end while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("BROWSE ABSTRACTS FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in browseAbstracts method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return allAbstracts;
   } // end browseAbstracts()
   
   /**
    * matchFaculty
    * @param email - the student's email
    * @return info - the matched info
    */     
   public String matchFaculty(String email) {
      int rowCount = 0;
      String word1;
      String word2;
      String word3;
      int matchCount = 0;
      String info = "";
      ArrayList<Integer> match1 = new ArrayList<Integer>();
      ArrayList<Integer> match2 = new ArrayList<Integer>();
      ArrayList<Integer> match3 = new ArrayList<Integer>();
         
      try {    
         // prepared statement
         String sql = "SELECT word1, word2, word3 FROM student WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email);   
         // System.out.println("Command to be executed: " + stmt);
         rs = stmt.executeQuery();
         rs.next();
         word1 = rs.getString(1);
         word2 = rs.getString(2);
         word3 = rs.getString(3);
      
         sql = "SELECT abstract_ID, about FROM abstract";
         rs = stmt.executeQuery(sql);
         while (rs.next()) {
            matchCount = 0;
            int abstractID = rs.getInt(1);
            String about = rs.getString(2);
            if(about.contains(word1)) {
               matchCount++;
            }
            if(about.contains(word2)) {
               matchCount++;
            }
            if(about.contains(word3)) {
               matchCount++;
            }
            if(matchCount == 3) {
               match3.add(abstractID);
            }
            if(matchCount == 2) {
               match2.add(abstractID);
            }
            if(matchCount == 1) {
               match1.add(abstractID);
            }
         }

         // display abstracts in order of matches, if any
         if (match1.size() == 0 && match2.size() == 0 && match3.size() == 0) {
            info += "There are currently no abstracts that match your interests.";
            return info;
         }
         if (match3.size() != 0) {
            info += "The following abstract(s) match 3 of your interests:\n";
            for (int id : match3) {
               info += getAbstractInfo(id);
            }
         }
         if (match2.size() != 0) {
            info += "The following abstract(s) match 2 of your interests:\n";
            for (int id : match2) {
               info += getAbstractInfo(id);
            }
         }
         if (match1.size() != 0) {
            info += "The following abstract(s) match 1 of your interests:\n";
            for (int id : match1) {
               info += getAbstractInfo(id);
            }
         }
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("MATCH FACULTY FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in matchFaculty method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      info += "\nPress 'Continue' to enter abstract ID and retrieve faculty info.";
      return info;
   } // end matchFaculty()
   
  
  /**
   * addAbstract
   * @param document - the type of document - book or speaking engagement abstract
   * @param title - the title of the document
   * @param description - what the document contains/about
   * @return generatedKey - the abstractID added
   */
   public int addAbstract(String document, String title, String description) {
      int generatedKey = 0;  // abstractID auto-generated
      
      try {        
         // prepared statement
         String sql = "INSERT INTO abstract (abstract_type, title, about) VALUES (?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);        
         
         // bind values into the parameters
         stmt.setString(1, document);
         stmt.setString(2, title);
         stmt.setString(3, description);    
         // System.out.println("Command to be executed: " + stmt);
         stmt.execute();
      
         ResultSet rs = stmt.getGeneratedKeys();
         if (rs.next()) {
            generatedKey = rs.getInt(1);
         }
          
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("ADD ABSTRACT FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in addAbstract method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return generatedKey;
   } // end addAbstract()
  
   /**
    * linkAbstract
    * @param facultyID - the faculty's ID
    * @param abstractID - the abstract's ID
    * @return the number of rows affected
    */
   public int linkAbstract(int facultyID, int abstractID) {
      int rows = 0;
      try {        
         // prepared statement
         String sql = "INSERT INTO faculty_abstract (faculty_ID, abstract_ID) VALUES (?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setInt(1, facultyID);  
         stmt.setInt(2, abstractID);  
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("LINK ABSTRACT FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in linkAbstract method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end linkAbstract()
   
   /**
    * deleteAbstract
    * @param abstractID - the abstract to delete
    * @return the number of rows affected
    */
   public int deleteAbstract(int abstractID) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "DELETE FROM abstract WHERE abstract_ID = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind parameters
         stmt.setInt(1, abstractID);
         
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("DELETE FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in deleteAbstract method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end deleteAsbtract()
   
//****************************************************************************************************************************   
// Student Methods

    /**
     * selectStudentEmail
     * @return email
     */
   public String selectStudentEmail() {
      String email = "";
      // Create the custom dialog for title entry
      Dialog dialog = new Dialog();
      dialog.setTitle("Student Email");
      dialog.setHeaderText("Enter CURRENT email: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      email = tfTitle.getText();
   
      return email;
   } // end selectStudentEmail()
   
    /**
     * selectNewStudentEmail
     * @return email
     */
   public String selectNewStudentEmail() {
      String email = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Update Student Email");
      dialog.setHeaderText("Enter NEW email: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      email = tfTitle.getText();
   
      return email;
   } // end selectNewStudentEmail()
   
    /**
     * selectNewWord
     * @return newWord (the updated key word/topic - or blank to delete)
     */
   public String selectNewWord() {
      String newWord = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Update or Delete Keyword");
      dialog.setHeaderText("Please enter new keyword/topic (Leave blank to delete):");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      newWord = tfTitle.getText();
   
      return newWord;
   } // end selectNewWord()

  /**
    * getStudentID
    * @param email - the student's email
    */     
   public int getStudentID(String email) {
      int studentID;
      try {        
         // prepared statement
         String sql = "SELECT student_ID FROM student WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email);  
         rs = stmt.executeQuery();
         rs.next();
         studentID = rs.getInt(1);
         return studentID;
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET STUDENT ID FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in getStudentID method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
   } // end getStudentID()
   
   /**
    * addStudent
    * @param lName - the student's last name
    * @param fName - the student's first name
    * @param email - the student's email
    * @param word1 - key word/topic #1
    * @param word2 - key word/topic #1
    * @param word3 - key word/topic #1
    * @return the number of rows affected
    */
   public int addStudent(String lName, String fName, String email, String word1, String word2, String word3) {
      int rows = 0;
      try {        
         // prepared statement
         String sql = "INSERT INTO student (lastName, firstName, email, word1, word2, word3) VALUES (?, ?, ?, ?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         if (word1.equals("")) {
            word1 = null;
         }
         if (word2.equals("")) {
            word2 = null;
         }
         if (word3.equals("")) {
            word3 = null;
         }
         stmt.setString(1, lName);
         stmt.setString(2, fName); 
         stmt.setString(3, email); 
         stmt.setString(4, word1); 
         stmt.setString(5, word2);  
         stmt.setString(6, word3);    
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("ADD STUDENT FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in addStudent method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end addStudent()

   /**
    * updateStudentEmail
    * @param studentID - the student to update
    * @param email - the student's new email
    * @return the number of rows affected
    */
   public int updateStudentEmail(int studentID, String email) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "UPDATE student SET email = ? WHERE student_ID = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters  
         stmt.setString(1, email);
         stmt.setInt(2, studentID);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE STUDENT EMAIL FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updateStudentEmail method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end updateStudentEmail()

   /**
    * deleteStudent
    * @param email - the email of the student to delete
    * @return the number of rows affected
    */
   public int deleteStudent(String email) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "DELETE FROM student WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind parameters
         stmt.setString(1, email);
         
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("DELETE FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in deleteStudent method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end deleteStudent()

   /**
    * listStudentTopics
    * @param email - the student's email
    * @return list - the list of student topics
    */     
   public String listStudentTopics(String email) {
      String list = "";
      try {        
         // Statement
         String sql = "SELECT word1, word2, word3 FROM student";
         stmt = conn.createStatement();
         // Create a resultset  
         rs = stmt.executeQuery(sql);
         
         while (rs.next()) {
            String word1 = rs.getString("word1");
            String word2 = rs.getString("word2");
            String word3 = rs.getString("word3");
            list = String.format("\t1. %-20s\n\t2. %-20s\n\t3. %-20s\n", word1, word2, word3);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("LIST STUDENT TOPICS FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in listStudentTopics method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return list;
   } // end listStudentTopics()

   /**
    * updateStudentTopic
    * @param email - the student's email
    * @param wordNum - the keyword/topic to update
    * @param word - the new keyword/topic
    * @return the number of rows affected
    */
   public int updateStudentTopic(String email, int wordNum, String word) {
      int rows = 0;
      if(word.equals("")) {
         word = null;
      }
      try {
         // prepared statement
         String sql = "UPDATE student SET word" + wordNum + " = ? WHERE email = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters
         stmt.setString(1, word);
         stmt.setString(2, email);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE STUDENT TOPIC FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updateStudentTopic method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end updateStudentTopic()  
    
   /**
    * getStudentInfo
    * @param email - the student's email
    * @return info - the student's information
    */     
   public String getStudentInfo(String email) {
      String info = "";
      try {        
         // Statement
         String sql = "SELECT lastName, firstName, email, word1, word2, word3 FROM student WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email); 
         // Create a resultset  
         rs = stmt.executeQuery();
         info += String.format("%-12s %-12s %-16s %-15s\n", "Last_Name", "First_Name", "Email", "Topic(s)");
         
         while (rs.next()) {
            String lastName = rs.getString(1);
            String firstName = rs.getString(2);
            String stuEmail = rs.getString(3);
            String word1 = rs.getString(4);
            String word2 = rs.getString(5);
            String word3 = rs.getString(6);
            info += String.format("%-12s %-12s %-16s %-8s %-8s %-8s\n", lastName, firstName, stuEmail, word1, word2, word3);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("GET STUDENT INFO FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in getStudentInfo method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return info;
   } // end getStudentInfo()

//*********************************************************************************************************************************   
// Public Methods

   /**
    * addPublic
    * @param publicName - the public or company's name
    * @param email - the student's email
    * @param word1 - key word/topic #1
    * @param word2 - key word/topic #1
    * @param word3 - key word/topic #1
    * @return the number of rows affected
    */
   public int addPublic(String publicName, String email, String word1, String word2, String word3) {
      int rows = 0;
      try {        
         // prepared statement
         String sql = "INSERT INTO public (publicName, email, word1, word2, word3) VALUES (?, ?, ?, ?, ?)";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         if (word1.equals("")) {
            word1 = null;
         }
         if (word2.equals("")) {
            word2 = null;
         }
         if (word3.equals("")) {
            word3 = null;
         }
         stmt.setString(1, publicName);
         stmt.setString(2, email); 
         stmt.setString(3, word1); 
         stmt.setString(4, word2);  
         stmt.setString(5, word3);    
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("ADD PUBLIC FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in addPublic method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end addPublic()

    /**
     * selectPublicEmail
     * @return email
     */
   public String selectPublicEmail() {
      String email = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Public/Company Email");
      dialog.setHeaderText("Enter CURRENT email: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      email = tfTitle.getText();
   
      return email;
   } // end selectPublicEmail()

//   /**
//     * getPublicID
//     * @param email - the public/company's email
//     */     
//    public int getPublicID(String email) {
//       int publicID;
//       try {        
//          // prepared statement
//          String sql = "SELECT student_ID FROM student WHERE email = ?";
//          PreparedStatement stmt = conn.prepareStatement(sql);
//          // bind values into the parameters
//          stmt.setString(1, email);  
//          rs = stmt.executeQuery();
//          rs.next();
//          publicID = rs.getInt(1);
//          return publicID;
//       } // end try
//       catch(SQLException sqle) {
//          System.out.println("SQL ERROR");
//          System.out.println("GET PUBLIC ID FAILED!!!!");
//          System.out.println("ERROR MESSAGE IS -> " + sqle);
//          sqle.printStackTrace();
//          return(0);
//       }
//       catch(Exception e) {
//          System.out.println("Error occured in getPublicID method");
//          System.out.println("ERROR MESSAGE is -> " + e);
//          e.printStackTrace();
//          return(0);
//       }
//    } // end getPublicID()

    /**
     * selectNewPublicEmail
     * @return email
     */
   public String selectNewPublicEmail() {
      String email = "";
      // Create the custom dialog
      Dialog dialog = new Dialog();
      dialog.setTitle("Update Public/Company Email");
      dialog.setHeaderText("Enter NEW email: ");
      
      TextField tfTitle = new TextField();
      dialog.getDialogPane().setContent(tfTitle);
     
      ButtonType loginButtonType = new ButtonType("OK", ButtonData.OK_DONE);
      dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);
   
      dialog.showAndWait();
      
      email = tfTitle.getText();
   
      return email;
   } // end selectNewPublicEmail()

   /**
    * updatePublicEmail
    * @param currentEmail - the public's current email
    * @param newEmail - the public's new email
    * @return the number of rows affected
    */
   public int updatePublicEmail(String currentEmail, String newEmail) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "UPDATE public SET email = ? WHERE email = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters  
         stmt.setString(1, newEmail);
         stmt.setString(2, currentEmail);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE PUBLIC EMAIL FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updatePublicEmail method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end updatePublicEmail()
   
   /**
    * deletePublic
    * @param email - the email of the public record/company to delete
    * @return the number of rows affected
    */
   public int deletePublic(String email) {
      int rows = 0;
      try {
         // prepared statement
         String sql = "DELETE FROM public WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind parameters
         stmt.setString(1, email);
         
         // System.out.println("Command to be executed: " + stmt);
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("DELETE FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in deletePublic method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end deletePublic()

  /**
    * listPublicTopics
    * param email - the public/company's email
    * @return list - the list of public topics
    */     
   public String listPublicTopics(String email) {
      String list = "";
      try {        
         // Statement
         String sql = "SELECT word1, word2, word3 FROM public";
         stmt = conn.createStatement();
         // Create a resultset  
         rs = stmt.executeQuery(sql);
         
         while (rs.next()) {
            String word1 = rs.getString("word1");
            String word2 = rs.getString("word2");
            String word3 = rs.getString("word3");
            list = String.format("\t1. %-20s\n\t2. %-20s\n\t3. %-20s\n", word1, word2, word3);
         } // while
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("LIST PUBLIC TOPICS FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in listPublicTopics method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return list;
   } // end listPublicTopics()


   /**
    * updatePublicTopic
    * @param email - the public/company's email
    * @param wordNum - the keyword/topic to update
    * @param word - the new keyword/topic
    * @return the number of rows affected
    */
   public int updatePublicTopic(String email, int wordNum, String word) {
      int rows = 0;
      if(word.equals("")) {
         word = null;
      }
      try {
         // prepared statement
         String sql = "UPDATE public SET word" + wordNum + " = ? WHERE email = ?";  
         PreparedStatement stmt = conn.prepareStatement(sql);        
         // bind parameters
         stmt.setString(1, word);
         stmt.setString(2, email);
      
         rows = stmt.executeUpdate();
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("UPDATE PUBLIC TOPIC FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
         return(0);
      }
      catch(Exception e) {
         System.out.println("Error occured in updatePublic Topic method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
         return(0);
      }
      return rows;
   } // end updatePublicTopic()   

   /**
    * matchStudent
    * @param email - the public/company's email
    * @return info - the matched info
    */     
   public String matchStudent(String email) {
      int rowCount = 0;
      String word1;
      String word2;
      String word3;
      String stuWord1;
      String stuWord2;
      String stuWord3;
      String stuEmail;
      String info = "";
      int matchCount = 0;
      ArrayList<String> match1 = new ArrayList<String>();
      ArrayList<String> match2 = new ArrayList<String>();
      ArrayList<String> match3 = new ArrayList<String>();
         
      try {    
         // prepared statement
         String sql = "SELECT word1, word2, word3 FROM public WHERE email = ?";
         PreparedStatement stmt = conn.prepareStatement(sql);
         // bind values into the parameters
         stmt.setString(1, email);   
         // System.out.println("Command to be executed: " + stmt);
         rs = stmt.executeQuery();
         rs.next();
         word1 = rs.getString(1);
         word2 = rs.getString(2);
         word3 = rs.getString(3);
         
         // add words to array list
         String[] wordToMatch = {word1, word2, word3};
      
         sql = "SELECT email, word1, word2, word3 FROM student";
         rs = stmt.executeQuery(sql);
         while (rs.next()) {
            matchCount = 0;
            stuEmail = rs.getString(1);
            stuWord1 = rs.getString(2);
            stuWord2 = rs.getString(3);
            stuWord3 = rs.getString(4);
                        
            if(Arrays.asList(wordToMatch).contains(stuWord1)) {
               matchCount++;
            }
            if(Arrays.asList(wordToMatch).contains(stuWord2)) {
               matchCount++;
            }
            if(Arrays.asList(wordToMatch).contains(stuWord3)) {
               matchCount++;
            }
            if(matchCount == 3) {
               match3.add(stuEmail);
            }
            if(matchCount == 2) {
               match2.add(stuEmail);
            }
            if(matchCount == 1) {
               match1.add(stuEmail);
            }
         }

         // display students in order of matches, if any
         if (match1.size() == 0 && match2.size() == 0 && match3.size() == 0) {
            info = "There are currently no students with matching topics.";
            return info;
         }
         if (match3.size() != 0) {
            info += "The following student(s) match 3 of your topics/interests:\n";
            for (String eAddr : match3) {
               info += getStudentInfo(eAddr);
            }
         }
         if (match2.size() != 0) {
            info += "The following student(s) match 2 of your topics/interests:\n";
            for (String eAddr : match2) {
               info += getStudentInfo(eAddr);
            }
         }
         if (match1.size() != 0) {
            info += "The following student(s) match 1 of your topics/interests:\n";
            for (String eAddr : match1) {
              info += getStudentInfo(eAddr);
            }
         }
      } // end try
      catch(SQLException sqle) {
         System.out.println("SQL ERROR");
         System.out.println("MATCH STUDENT FAILED!!!!");
         System.out.println("ERROR MESSAGE IS -> " + sqle);
         sqle.printStackTrace();
      }
      catch(Exception e) {
         System.out.println("Error occured in matchStudent method");
         System.out.println("ERROR MESSAGE is -> " + e);
         e.printStackTrace();
      }
      return info;
   } // end matchStudent()

  
//*********************************************************************************************************************************
// Additional Methods

   /**
    * doExit
    */
   public void doExit() {
      // ********************************************************************************* add closing of db???
      java.util.Date now = new java.util.Date();
      alert(AlertType.INFORMATION, "Good-Bye!"+
         "\nEnd of program\nTerminated at ->" + now, "End of Program");
   
      System.out.println("\nEnd of program\nTerminated at -> " + now);
      System.exit(0);
   } // end doExit()
   
   /** alert - method to create alerts */
   private void alert(AlertType type, String message, String header) {
      Alert alert = new Alert(type, message);
      alert.setHeaderText(header);
      alert.showAndWait();
   } // end alert()
} //end class