package application;


import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

import javax.mail.Message;
import javax.mail.MessagingException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;


public class userAccountEntire implements Initializable{

	@FXML
	private Button newMessage;
	@FXML
	private Button mailBox;
	@FXML
	private Button settings;
	@FXML
	private Button saveSettings;
	@FXML
	private Label pass;
	@FXML
	private Button save;
	@FXML
	private Button modfLabel;
	@FXML
	private TextField dbName;
	@FXML
	private TextField dbHost;
	@FXML
	private TextField dbUser;
	@FXML
	private TextField dbPass;
	@FXML
	private TextField smtpEmail;
	@FXML
	private TextField smtpPass;
	@FXML
	private Button send;
	@FXML
	private Button loadFile;
	@FXML
	private Pane newMessagePane;
	@FXML
	private Pane mailBoxPane;
	@FXML
	private Pane settingsPane;
	@FXML
	private TextField messageTitle;
	@FXML
	private TextArea messageText;
	@FXML
	private Circle messageCountSymbol;
	@FXML
	private Label messageCountNumber;
	@FXML
	private ImageView userAvatar;
	@FXML
	private Label usernameLabel;
	@FXML
	private ListView<String> userList;
	@FXML
	private Label fileNameLabel;
	@FXML
	private ListView<String> mailGet;
	@FXML
	private ListView<String> mesageTable;
	@FXML
	private TextArea mesageContentArea;
	@FXML
	private Button downloadFile;
	
	
	final Timeline timeline = new Timeline();
	
	
	Message[] msgs;
	ArrayList<String> mailGeters = new ArrayList<String>();
	
	final FileChooser  fileChoser = new FileChooser ();
	public File file = null;
	PropertiesLoadWrite psw = new PropertiesLoadWrite();
	
	getMailBox gMB = new getMailBox();
	String userName = null;
	String PASSWORD = null;
	int seconds = 5;

	public userAccountEntire() {

	}

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Timeline gameStartTimer = new Timeline(new KeyFrame(Duration.seconds(30), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					ObservableList<String> msgList =FXCollections.observableArrayList();
					ArrayList<String> msg = gMB.getMSG();
					for(int i = 0; i < msg.size(); i+=2) {
						String row = msg.get(i)+"   "+msg.get(i+1);
						msgList.add(row);
					}
					mesageTable.setItems(msgList);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}));
		gameStartTimer.setCycleCount(Timeline.INDEFINITE);
		gameStartTimer.play();
		
		String username = Main.retUsername();
		usernameLabel.setText(username);
		String[] defaultModel  = null;
		ArrayList<String> user = new ArrayList<String>();
		try {
			user =  DBMethods.getAllusers();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ObservableList<String> userLists = FXCollections.observableArrayList(user);
		userList.setItems(userLists);
	
	}
	
	@FXML
	public void downloadFileClicked() {
		downloadFile.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				String msg = mesageTable.getSelectionModel().getSelectedItem();
				System.out.println(msg);
				String[] msgArray = msg.split("   ");
				try {
					gMB.downloadFile(msgArray[0], msgArray[1]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
	}
	
	
	
	@FXML
	public void loadMessgae() {
		mesageTable.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				String mesageName = mesageTable.getSelectionModel().getSelectedItem();
				if(mesageName !=null) {
					String[]msgs = mesageName.split("   ");
					try {
						String[] msg =  gMB.getMSGContent(msgs[0].toString(), msgs[1].toString());
						mesageContentArea.setText("FROM: "+msg[0]+"\n"+"SUBJECT "+msg[1]
								+"\n\n\n\n\n"+msg[2]+"\n\n\n Sended "+msg[3]+".\n");
						
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}
		});
	}
	
	
	
	@FXML
	public void getParams(String username, String password) {
		this.userName = username;
		this.PASSWORD = password;
		usernameLabel.setText(username);
		pass.setText(password);
	}

	

	@FXML
	public void newMessageClicked() {
		newMessage.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				newMessagePane.setVisible(true);
				mailBoxPane.setVisible(false);
				settingsPane.setVisible(false);
			}
		});
	}
	
	@FXML
	public void mailBoxClicked() {
		mailBox.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				newMessagePane.setVisible(false);
				mailBoxPane.setVisible(true);
				settingsPane.setVisible(false);
			}
		});
	}

	@FXML
	public void settingsClicked() {
		settings.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				newMessagePane.setVisible(false);
				mailBoxPane.setVisible(false);
				settingsPane.setVisible(true);
				
				dbHost.setText(psw.loadProperties("dbHost"));
				dbName.setText(psw.loadProperties("dbName"));
				dbUser.setText(psw.loadProperties("dbUser"));
				dbPass.setText(psw.loadProperties("dbPass"));
				smtpEmail.setText(psw.loadProperties("smtpEmail"));
				smtpPass.setText(psw.loadProperties("smtpPass"));
			}
		});
	}	
	@FXML
	public void sendMessageClicked() {
		send.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
			try {
				if(file != null) {
					new SMTPMethods(usernameLabel.getText(), messageTitle.getText(), messageText.getText(), mailGeters,file);
				}else if(file == null) {
					new SMTPMethods(usernameLabel.getText(), messageTitle.getText(), messageText.getText(), mailGeters);
				}
			} catch (SQLException | MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		});
	}
	
	@FXML
	public void loadFileClicked() {
		loadFile.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				file = new Main().readLocalFile();
			}
		});
	}
	

	
	
	@FXML
	public void stackUsenameForSending() {
		userList.setOnMouseClicked(new EventHandler<Event>() {

			@Override
			public void handle(Event event) {
				String username = userList.getSelectionModel().getSelectedItem();

				if(mailGeters.isEmpty()) {
					mailGeters.add(username);
					ObservableList<String> usrList = FXCollections.observableArrayList(mailGeters);
					mailGet.setItems(usrList);
				}else if(!mailGeters.contains(username)) {
					List<String> numbers = mailGet.getItems();
					mailGeters.clear();
					mailGeters.addAll(0, numbers);
					mailGeters.add(username);
					ObservableList<String> usrList = FXCollections.observableArrayList(mailGeters);
					mailGet.setItems(usrList);
				}else if (mailGeters.contains(username)) {
					List<String> numbers = mailGet.getItems();
					mailGeters.clear();
					mailGeters.addAll(0, numbers);
					mailGeters.remove(username);
					ObservableList<String> usrList = FXCollections.observableArrayList(mailGeters);
					mailGet.setItems(usrList);
				}
			}
		});
	}
	
	@FXML
	public void saveSettingsClicked() {
		saveSettings.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event arg0) {
				psw.storeProperties("dbHost", dbHost.getText());
				psw.storeProperties("dbName", dbName.getText());
				psw.storeProperties("dbUser",dbUser.getText());
				psw.storeProperties("dbPass", dbPass.getText());
			    psw.storeProperties("smtpEmail", smtpEmail.getText());
				psw.storeProperties("smtpPass", smtpPass.getText());

				newMessagePane.setVisible(true);
				mailBoxPane.setVisible(false);
				settingsPane.setVisible(false);
				
			}
		});
	}


}
