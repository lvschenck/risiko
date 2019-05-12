package sw.praktikum.risk5.GUI;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;


public class SettingsPanelController {

  private Database db;

  @FXML
  private TextField playerName;
  @FXML
  private HBox playerPicture;
  @FXML
  private Pane avatarPane;
  @FXML
  private Pane playerPane;
  @FXML
  private GridPane avatarPanel;

  /*
   * The Player can change his shown Name
   *
   * @author esali
   */
  @FXML
  private void changeName(ActionEvent event) {
    if (playerName.getText() != null) {
      db.setPlayerData("name", playerName.getText(),
          String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
      RiskMain.getInstance().getDomain().setPlayerName(playerName.getText());
    }
  }

  /*
   * The player can choose another Avatar Player
   *
   * @author esali
   */
  @FXML
  private void changePicture(ActionEvent event) {
    avatarPanel.toFront();
    avatarPanel.setOpacity(1.0);
  }

  @FXML
  private void selectPlayer1() {
    db.setPlayerData("avatar", "player1",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player1");
    avatarPanelToBack();
  }

  @FXML
  private void selectPlayer2() {
    db.setPlayerData("avatar", "player2",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player2");
    avatarPanelToBack();
  }

  @FXML
  private void selectPlayer3() {
    db.setPlayerData("avatar", "player3",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player3");
    avatarPanelToBack();
  }

  @FXML
  private void selectPlayer4() {
    db.setPlayerData("avatar", "player4",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player4");
    avatarPanelToBack();
  }

  @FXML
  private void selectPlayer5() {
    db.setPlayerData("avatar", "player5",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player5");
    avatarPanelToBack();
  }

  @FXML
  private void selectPlayer6() {
    db.setPlayerData("avatar", "player6",
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())));
    playerPicture.getStyleClass().clear();
    playerPicture.getStyleClass().add("player6");
    avatarPanelToBack();
  }

  @FXML
  private void avatarPanelToBack() {
    avatarPanel.toBack();
    avatarPanel.setOpacity(0.0);
  }


  /*
   * When logging out the player will be redirected to the Login Panel where he can start from
   * beginning
   *
   * @author esali
   */
  @FXML
  private void logOut(ActionEvent actionEvent) {
    Parent newContent = null;
    try {
      newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LoginPanel.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    primaryStage.getScene().setRoot(newContent);
  }


  /*
   * Method deletes the Player Profile
   */
  @FXML
  private void deletePlayerProfile(ActionEvent event) {
    db.deleteUser(RiskMain.getInstance().getDomain().getPlayerName());
    Parent newContent = null;
    try {
      newContent = FXMLLoader.load(RiskMain.class.getResource("/view/LoginPanel.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    primaryStage.getScene().setRoot(newContent);
//db.setPlayerData("name", "newName",String.valueOf(db.getUserID(RiskMain.getInstance().getDomain().getPlayerName())));
//    db.setPlayerData("avatar", "icon_1.png",String.valueOf(db.getUserID(RiskMain.getInstance().getDomain().getPlayerName())));

  }

  /*
   * When clicked on the "Back"-Button the Player will be redirected back to the Main Menu Screen
   *
   * @author esali
   */
  @FXML
  private void openMenu(ActionEvent actionEvent) {
    Parent newContent = null;
    try {
      newContent = FXMLLoader.load(RiskMain.class.getResource("/view/MainMenu.fxml"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    primaryStage.getScene().setRoot(newContent);
  }

  public void initialize() {
    String playerNameNew = RiskMain.getInstance().getDomain().getPlayerName();
    playerName.setText(playerNameNew);
    this.db = RiskMain.getInstance().getDomain().getData();
    playerPicture.getStyleClass().add(RiskMain.getInstance().getDomain().getData().getPlayerData(
        String.valueOf(RiskMain.getInstance().getDomain().getData().getUserId(playerNameNew)),
        "avatar"));
  }
}
