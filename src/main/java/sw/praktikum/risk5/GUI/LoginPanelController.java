package sw.praktikum.risk5.GUI;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;

/**
 * Builds the initial screen for login.
 *
 * @author lschenck, esali
 */

public class LoginPanelController {

	Database db = new Database();

	@FXML
	public TextField playerName;
	@FXML
	private PasswordField passwordField;

	@FXML
	private Label errorMessage;
	@FXML
	private GridPane errorMessagePane;
	@FXML
	private GridPane errorMessagePaneParent;

	/**
	 * opens the Panel MainMenu when clicked on Button "Conquer" and changes the
	 * name of the Player on the Main Menu, shows error Message when wrong username
	 * or password is provided
	 * 
	 * @author esali
	 */
	@FXML
	private void logIn(ActionEvent actionEvent) {
		if (db.checkPassword(playerName.getText(), passwordField.getText())) {
			Parent newContent = null;
			try {
				RiskMain.getInstance().getDomain().setPlayerName(playerName.getText());
				newContent = FXMLLoader.load(RiskMain.class.getResource("/view/MainMenu.fxml"));

			} catch (IOException e) {
				e.printStackTrace();
			}
			Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
			primaryStage.getScene().setRoot(newContent);
		} else {

			errorMessagePane.setOpacity(1.0);
			errorMessagePaneParent.toFront();
			errorMessage.setText("Could not Log In \n Invalid Username or Password");
			System.out.println("Invalid Username or Password");
			playerName.setText("");
			passwordField.setText("");
		}
	}

	/**
	 * register a new Person with the database method create user
	 * returns boolen, opnes Main Menu when true else Error Message
	 * 
	 */
	@FXML
	private void register(ActionEvent actionEvent) {
		if (db.createUser(playerName.getText(), passwordField.getText())) {
			Parent newContent = null;
			try {
				RiskMain.getInstance().getDomain().setPlayerName(playerName.getText());
				newContent = FXMLLoader.load(RiskMain.class.getResource("/view/MainMenu.fxml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
			primaryStage.getScene().setRoot(newContent);
		}
		else {
			errorMessagePane.setOpacity(1.0);
			errorMessagePaneParent.toFront();
			errorMessage.setText("Could not Register \n Invalid Username or Password");
			System.out.println("Couldn't Register");
			playerName.setText("");
			passwordField.setText("");
		}
	}

	@FXML
	private void cancelErrorMessage(ActionEvent event) {
		errorMessagePane.setOpacity(0.0);
		errorMessagePaneParent.toBack();
	}

}