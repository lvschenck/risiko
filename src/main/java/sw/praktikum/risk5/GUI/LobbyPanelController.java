package sw.praktikum.risk5.GUI;

import java.io.IOException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sw.praktikum.risk5.App.RiskMain;
import sw.praktikum.risk5.Database.Database;
import sw.praktikum.risk5.Network.Client;
import sw.praktikum.risk5.Network.ClientInterface;
import sw.praktikum.risk5.Network.Server;
import sw.praktikum.risk5.Network.ServerInterface;

/**
 * Handles the input on the Settings Screen.
 *
 * @author lschenck
 */

public class LobbyPanelController implements LobbyPanelInterface {


  @FXML
  private Button startGame;
  @FXML
  private Text lobbyHeader;
  @FXML
  private ChoiceBox<String> kiStatus1;
  @FXML
  private ChoiceBox<String> kiStatus2;
  @FXML
  private ChoiceBox<String> kiStatus3;
  @FXML
  private ChoiceBox<String> kiStatus4;
  @FXML
  private ChoiceBox<String> kiStatus5;

  @FXML
  private BorderPane player1Pane;
  @FXML
  private BorderPane player2Pane;
  @FXML
  private BorderPane player3Pane;
  @FXML
  private BorderPane player4Pane;
  @FXML
  private BorderPane player5Pane;
  @FXML
  private BorderPane player6Pane;

  @FXML
  private AnchorPane avatarPane1;

  @FXML
  private AnchorPane avatarPane2;

  @FXML
  private StackPane playerPane;

  @FXML
  private StackPane kiPane;

  @FXML
  private GridPane kiPlayerGrid;
  @FXML
  private Button addKiButton;
  @FXML
  private Button removeKiButton;

  @FXML
  private GridPane mainGridPane;

  @FXML
  private TextField messageInput;
  @FXML
  private TextArea messageOutput;
  @FXML
  private ChoiceBox<String> recipientList;
  @FXML
  private Button sendMessageButton;

  private boolean kiSelected;
  private boolean directStart;
  private ServerInterface serverInterface;
  private ClientInterface clientPlayerInterface;
  private ClientInterface clientInterface1;
  private ClientInterface clientInterface2;
  private ClientInterface clientInterface3;
  private ClientInterface clientInterface4;
  private ClientInterface clientInterface5;
  private Database db;

  private ObservableList<String> kiStatusList1 = FXCollections
      .observableArrayList("Easy", "Medium", "Hard");
  private ObservableList<String> kiStatusList2 = kiStatusList1;
  private ObservableList<String> kiStatusList3 = kiStatusList1;
  private ObservableList<String> kiStatusList4 = kiStatusList1;
  private ObservableList<String> kiStatusList5 = kiStatusList1;

  private Integer kiPlayerSize;

  private ObservableList<String> participantList = FXCollections.observableArrayList("all");

  /**
   * Sends a message from one player to another possible to either send to one player or to all
   *
   * @author esali
   */
  @FXML
  private void sendMessage(ActionEvent actionEvent) {
    String playerName = RiskMain.getInstance().getDomain().getPlayerName();
    System.out.println(messageInput.getText());
    String text = playerName + ":     " + messageInput.getText();
    if (RiskMain.getInstance().getDomain().getIsServer()) {
      this.serverInterface = RiskMain.getInstance().getDomain().getServer();
      this.serverInterface
          .sendMessageChat(playerName, messageInput.getText(), recipientList.getValue());
    } else {
      this.clientPlayerInterface = RiskMain.getInstance().getDomain().getClient();
    }
    messageInput.setText("");
    messageOutput.appendText(text + "\n");
  }

  /**
   * gets Message and displays it in the TextArea
   *
   * @author esali
   */
  @Override
  public void receiveMessageChat(String author, String chatMessage, boolean single) {
    messageOutput.appendText(author + ":   ");
    messageOutput.appendText(chatMessage + "\n");
  }

  @Override
  public void receiveMessageLobby(String gameName, int[] amountAiWithDifficulty,
      String[] allPlayerNames, String[] pictureOfPlayers) {

    int aiCount = 0;
    for (int i = 0; i < allPlayerNames.length; i++) {
      if ((db.getPlayerData(String.valueOf(db.getUserId(allPlayerNames[i])), "avatar").equals("player-ki"))){
        switch (aiCount){
          case 1: kiStatus1.setValue(String.valueOf(amountAiWithDifficulty[aiCount]));
          break;
          case 2: kiStatus2.setValue(String.valueOf(amountAiWithDifficulty[aiCount]));
            break;
          case 3: kiStatus3.setValue(String.valueOf(amountAiWithDifficulty[aiCount]));
            break;
          case 4: kiStatus4.setValue(String.valueOf(amountAiWithDifficulty[aiCount]));
            break;
          case 5: kiStatus5.setValue(String.valueOf(amountAiWithDifficulty[aiCount]));
            break;
        }
        aiCount++;
      }
    }

  }

  @Override
  public void receiveMessageStart() {

  }

  @FXML
  private void addKiPlayer() {
    if (kiPlayerSize < 5 && kiSelected == false) {
      Platform.runLater(() -> {
        kiPlayerGrid.getChildren().get(kiPlayerSize - 1).setOpacity(1.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(kiPlayerSize - 1)).getRight()
            .setDisable(false);
      });
      kiPlayerSize += 1;
      System.out.println(kiPlayerSize + "playerss");
    }
  }

  @FXML
  private void removeKiPlayer() {
    if (kiPlayerSize > 0 && kiSelected == false) {
      Platform.runLater(() -> {
        kiPlayerGrid.getChildren().get(kiPlayerSize).setOpacity(0.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(kiPlayerSize)).getRight().setDisable(true);
      });
      kiPlayerSize -= 1;
      System.out.println(kiPlayerSize + "playersssssssss");
    }
  }

  @FXML
  private void chooseAvatarPlayer(MouseEvent event) {
    avatarPane1.setOpacity(1.0);
    double posY = event.getScreenY() - 225.0;
    if (posY > 180) {
      posY = 180;
    }
    avatarPane1.getChildren().get(0).relocate(88.0, posY);
    playerPane.getChildren().get(0).toFront();
  }

  @FXML
  private void selectAvatarPlayer(MouseEvent event) {
    avatarPane1.setOpacity(0.0);
    playerPane.getChildren().get(1).toBack();
  }

  @FXML
  private void chooseAvatarKi(MouseEvent event) {
    avatarPane2.setOpacity(1.0);
    double posY = event.getScreenY() - 225.0;
    if (posY > 180) {
      posY = 180;
    }
    avatarPane2.getChildren().get(0).relocate(88.0, posY);
    kiPane.getChildren().get(0).toFront();
  }

  @FXML
  private void selectAvatarKi(MouseEvent event) {
    avatarPane2.setOpacity(0.0);
    kiPane.getChildren().get(1).toBack();
  }

  @FXML
  private void openGame(ActionEvent actionEvent) {
    if (!(kiSelected && directStart)) {
      ServerInterface server = new Server(RiskMain.getInstance().getDomain().getPlayerName());
      RiskMain.getInstance().getDomain().setGameName("SinglePlayer");
      RiskMain.getInstance().getDomain().setServer(server);
      int countKi = 0;
      for (int i = 0; i < 5; i++) {
        System.out.println(!((BorderPane) kiPlayerGrid.getChildren().get(i)).getRight()
            .disableProperty()
            .getValue());
        if (!((BorderPane) kiPlayerGrid.getChildren().get(i)).getRight()
            .disableProperty()
            .getValue()) {
          countKi++;
          switch (i) {
            case 0:
              this.clientInterface1 = new Client("localhost", "PrimeTime",
                  (String) kiStatus1.getValue(), true);
              System.out.println("client" + countKi);
              break;
            case 1:
              this.clientInterface2 = new Client("localhost", "E.T.", (String) kiStatus2.getValue(),
                  true);
              System.out.println("client" + countKi);
              break;
            case 2:
              this.clientInterface3 = new Client("localhost", "R2D2", (String) kiStatus3.getValue(),
                  true);
              System.out.println("client" + countKi);
              break;
            case 3:
              this.clientInterface4 = new Client("localhost", "Terminator",
                  (String) kiStatus4.getValue(),
                  true);
              System.out.println("client" + countKi);
              break;
            case 4:
              this.clientInterface5 = new Client("localhost", "Ultron",
                  (String) kiStatus5.getValue(),
                  true);
              System.out.println("client" + countKi);
              break;
          }
          try{
            Thread.sleep(10);
          }catch (InterruptedException e){
            e.printStackTrace();
          }
        }
      }

      if (!directStart) {
        startGame.setText("Start Game");
      } else {
        Parent newContent = null;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));

        // GamePanelController controller = new GamePanelController(userName,
        // serverInterface);
        // loader.setController(controller);
        Parent root = null;
        try {
          root = loader.load();
        } catch (IOException e) {
          e.printStackTrace();
        }
        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        primaryStage.getScene().setRoot(root);
      }
      enableMultiPlayerCommunication();
    } else {
      // In Lobby ohne KI Spieler - nutze gameType Variable in Domain für Spiel (Sinle, Multi oder Tutorial)

      Parent newContent = null;

      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GamePanel.fxml"));

      // GamePanelController controller = new GamePanelController(userName,
      // serverInterface);
      // loader.setController(controller);
      Parent root = null;
      try {
        root = loader.load();
      } catch (IOException e) {
        e.printStackTrace();
      }
      Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
      primaryStage.getScene().setRoot(root);
    }
  }

  private void enableMultiPlayerCommunication() {
    addKiButton.setDisable(true);
    removeKiButton.setDisable(true);
    kiStatus1.setDisable(true);
    kiStatus2.setDisable(true);
    kiStatus3.setDisable(true);
    kiStatus4.setDisable(true);
    kiStatus5.setDisable(true);
    startGame.setText("start Game");
    kiSelected = true;

    sendMessageButton.setDisable(false);
    recipientList.setDisable(false);
    messageInput.setDisable(false);
    messageOutput.setDisable(false);
  }

  /**
   * opens the Menu
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

  /**
   * initialize values
   *
   * @author lvschenck
   */
  public void initialize() {
    this.db = RiskMain.getInstance().getDomain().getData();
    ((Label) player1Pane.getCenter()).setText(RiskMain.getInstance().getDomain().getPlayerName());
    String avatar = db.getPlayerData(
        String.valueOf(db.getUserId(RiskMain.getInstance().getDomain().getPlayerName())), "avatar");
    ((HBox) player1Pane.getLeft()).getStyleClass().clear();
    ((HBox) player1Pane.getLeft()).getStyleClass().add(avatar);
    kiPlayerSize = 1;

    kiStatus1.setValue("Easy");
    kiStatus1.setItems(kiStatusList1);

    kiStatus2.setValue("Easy");
    kiStatus2.setItems(kiStatusList2);
    kiStatus2.setDisable(true);

    kiStatus3.setValue("Easy");
    kiStatus3.setItems(kiStatusList3);
    kiStatus3.setDisable(true);

    kiStatus4.setValue("Easy");
    kiStatus4.setItems(kiStatusList4);
    kiStatus4.setDisable(true);

    kiStatus5.setValue("Easy");
    kiStatus5.setItems(kiStatusList5);
    kiStatus5.setDisable(true);

    recipientList.setValue("all");
    recipientList.setItems(participantList);
    switch (RiskMain.getInstance().getDomain().getGameType()) {
      default:
        kiSelected = false;
        directStart = false;
        break;
      case 1:
        directStart = true;
        kiSelected = false;
        startGame.setText("Start Game");
        lobbyHeader.setText("Gamelobby " + "Single Player");
        break;
      case 2:
        kiSelected = false;
        directStart = false;
        lobbyHeader.setText("Gamelobby " + RiskMain.getInstance().getDomain().getGameName());
        break;
      case 3:
        kiSelected = true;
        directStart = true;
        lobbyHeader.setText("Gamelobby " + RiskMain.getInstance().getDomain().getGameName());
        startGame.setDisable(true);
        startGame.setText("Start Game");
        enableMultiPlayerCommunication();
        kiPlayerGrid.getChildren().get(0).setOpacity(0.0);
        ((BorderPane) kiPlayerGrid.getChildren().get(0)).getRight().setDisable(true);
        break;
      case 4:
        startGame.setText("Start Game");
        directStart = true;
        kiSelected = false;
        break;
    }


  }
}
