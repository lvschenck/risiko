package sw.praktikum.risk5.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Network.Client;

/**
 * Handles the input of the Main Menu
 *
 * @author esali, lschenk
 */

public class MenuPanelController implements Initializable, MenuPanelInterface {

	ActionEvent event;

	Client client;

	private Database db;

	@FXML
	public Label playerName;

	@FXML
	private TextField ipAdress;
	@FXML
	private GridPane manuellJoinPane;
	@FXML
	private GridPane manuellJoinPaneParent;
	@FXML
	private GridPane hostPane;
	@FXML
	private GridPane hostPaneParent;
	@FXML
	private TextField gameLobbyName;

	@FXML
	private Label connectionJoinError;

	@FXML
	private Pane avatarPane;

	/**
	 * Closes the HostPanelNotification
	 * 
	 * @author esali
	 */
	@FXML
	private void cancelHost() {
		hostPaneParent.toBack();
		hostPane.setOpacity(0.0);
	}

	/**
	 * sets the name of the game and opens the LobbyPanel
	 *
	 * @author esali
	 */
	@FXML
	private void openHostLobby(ActionEvent actionEvent) {
		RiskMain.getInstance().getDomain().setGameName(gameLobbyName.getText());
		RiskMain.getInstance().getDomain().setGameType(2);
		Parent newContent = null;

		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * opens the Host Notification Pane
	 * 
	 * @author esali
	 */
	@FXML
	private void openHost() {
		hostPaneParent.toFront();
		hostPane.setOpacity(1.0);
	}

	/**
	 * opens the Single Player Notification Pane
	 *
	 * @author lschenck
	 */
	/**
	 * cancels Connection Pane
	 * 
	 * @author esali
	 */
	@FXML
	private void cancelConnection() {
		manuellJoinPaneParent.toBack();
		manuellJoinPane.setOpacity(0.0);
	}

	/**
	 * opens Connection Pane and sets the event as the event, that is caused by
	 * button clicked
	 * 
	 * @author esali
	 * @param event
	 */
	@FXML
	private void openManuellJoin(ActionEvent event) {
		this.event = event;
		manuellJoinPaneParent.toFront();
		manuellJoinPane.setOpacity(1.0);
	}

	/**
	 * 
	 * 
	 * @author esali
	 * @param actionEvent
	 */
	@FXML
	private void connectSearch(ActionEvent actionEvent) {
		RiskMain.getInstance().getDomain().setGameType(3);
		Parent newContent = null;

		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * opens the Lobby for a SinglePlayer
	 * 
	 * @author esali
	 * @param actionEvent
	 */

	@FXML
	private void openSingleLobby(ActionEvent actionEvent) {
		RiskMain.getInstance().getDomain().setGameType(1);
		Parent newContent = null;

		try {

			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * opens the Lobby as a TutorialGame
	 * 
	 * @author esali
	 * @param actionEvent
	 */
	@FXML
	private void openTutorialLobby(ActionEvent actionEvent) {
		RiskMain.getInstance().getDomain().setGameType(4);
		Parent newContent = null;
		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * opens the Settings
	 * 
	 * @author esali
	 * @param actionEvent
	 */
	@FXML
	private void openSettings(ActionEvent actionEvent) {
		Parent newContent = null;
		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/SettingsPanel.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * opens StatisticsPanel
	 * 
	 * @author esali
	 * @param actionEvent
	 */
	@FXML
	private void openStatistics(ActionEvent actionEvent) {
		Parent newContent = null;
		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/StatisticsPanel.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	/**
	 * sets the Client when a player wants to join a game
	 * 
	 * @author esali
	 * @param actionEvent
	 */
	@FXML
	private void connectManuell(ActionEvent actionEvent) {
		this.client = new Client(ipAdress.getText(), RiskMain.getInstance().getDomain().getPlayerName(), null, false);
	}

	/**
	 * 
	 * 
	 * @author esali
	 */
	private void openJoinLobby() {
		Parent newContent = null;
		RiskMain.getInstance().getDomain().setGameType(3);
		try {
			newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LobbyPanel.fxml"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		Stage primaryStage = (Stage) ((Node) this.event.getSource()).getScene().getWindow();
		primaryStage.getScene().setRoot(newContent);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		playerName.setText(RiskMain.getInstance().getDomain().getPlayerName());
		this.db = RiskMain.getInstance().getDomain().getData();
		String avatar = db.getPlayerData(
				Integer.toString(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())), "avatar");
		switch (avatar) {
		case "player1":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player1");
			break;
		case "player2":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player2");
			break;
		case "player3":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player3");
			break;
		case "player4":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player4");
			break;
		case "player5":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player5");
			break;
		case "player6":
			avatarPane.getStyleClass().clear();
			avatarPane.getStyleClass().add("player6");
			break;
		default:
			avatarPane.getStyleClass().clear();
		}
	}

	@Override
	public void connect(boolean b) {
		System.out.println("connect");
		if (b) {
			openJoinLobby();
		} else {
			connectionJoinError.setOpacity(1.0);
		}
	}
}
