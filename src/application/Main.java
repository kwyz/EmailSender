package application;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.Node;


public class Main extends Application {
	

	static DBMethods dbmethods;	
	
	@FXML
	private Hyperlink regSwitchHyperlink;
	@FXML
	private TextField username;
	@FXML
	private TextField password;
	@FXML
	private TextField Email;
	@FXML
	private TextField logUsername;
	@FXML
	private TextField logpassword;
	@FXML
	private CheckBox userTermsBox;
	@FXML
	private Pane regPane;
	@FXML
	private Pane logPane;
	@FXML
	private Button goBackButton;
	@FXML
	private Button signUpButton;
	@FXML
	private Button signInButton;
	@FXML
	private Label errorLabel;
	
	static userAccountEntire uAE;
	static String UsrName = null;
	public static Stage mainStage;
	
	@FXML
	public void regSwitchHyperlink() {
		regSwitchHyperlink.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				regPane.setVisible(true);
				logPane.setVisible(false);
			}
		});
	}
	
	@FXML
	public void actionBack() {
		goBackButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				regPane.setVisible(false);
				logPane.setVisible(true);
			}
		});
	}

	
	@FXML
	public void signUpButton() {
		signUpButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
					String login = username.getText();
					String pass = password.getText();
					String email = Email.getText();
					try {
						if(username.getText()!=null || password.getText()!=null || Email.getText()!=null) {
							if(userTermsBox.isSelected()) {
								if(email.contains("@")) {
									dbmethods.insertFields(login, pass, email);
								}else {
									errorLabel.setText("Entered e-mail is incorect! Try again.");
								}
							}else {
								errorLabel.setText("Please accept user term and user data policy.");
							}
						}else {
							errorLabel.setText("Please enter corect your credentials.");
						}
					} catch (NoSuchAlgorithmException | SQLException e) {
						errorLabel.setText("E-mail already in use!");
						e.printStackTrace();
					}
			}
			});
	}
	@FXML
	public void fieldClick() {	
		Email.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Email.setText("");
				errorLabel.setText("");
			}
		});
	}
	
	@FXML
	public void logInSystem() {
		signInButton.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
			try {
				UsrName = logUsername.getText();
				if(dbmethods.checkCredentials(UsrName, logpassword.getText())) {
					 Parent home_page_parent = null;
					 try {
							home_page_parent = FXMLLoader.load(getClass().getResource("acountEntire.fxml"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					 	Scene home_page_scene = new Scene(home_page_parent);
				        Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		                app_stage.hide(); 
		                app_stage.setScene(home_page_scene);
		                app_stage.show();
				}else {
					System.out.println("Incorect credentials!");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			}
		});
	}
	
	@FXML
	public File readLocalFile() {
		FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 File selectedFile = fileChooser.showOpenDialog(mainStage);
		 return selectedFile;
		
	}
	
	
	
	public static String retUsername() {
		return UsrName;
	}

	@Override
	public void start(Stage stage) throws IOException, InterruptedException {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
	    Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    mainStage = stage;
	    stage.show();   
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	    	public void handle(WindowEvent we) {
	                stage.close();
	            }
	        });
	}  
   
	
	public static void main(String[] args) throws SQLException {
		uAE = new userAccountEntire();

		dbmethods = new DBMethods(); 
		launch(args);
	}


}
